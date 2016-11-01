package com.etcomm.dcare.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.etcomm.dcare.R;

/**
 * @类名: ProgressSeekBar
 * @描述: TODO(带有数字的水平拖动条)
 * @作�??: LYY
 * @日期: 2014-11-19 下午14:43:14
 * @修改�?:
 * @修改时间: 2014-8-11 下午2:01:14
 * @修改内容:
 * @版本: V1.0
 * @版权:卓佳Droid 馨宇
 */
public class ProgressSeekBar extends SeekBar {

	private int oldPaddingTop;

	private int oldPaddingLeft;

	private int oldPaddingRight;

	private int oldPaddingBottom;

	private boolean isMysetPadding = true;

	private String mText;

	private float mTextWidth;

	private float mImgWidth;

	private float mImgHei;

	private Paint mPaint;
	private Paint mPaintStep;

	private Resources res;

	private Bitmap bm;

	private int textsize = 10;

	private int textpaddingleft;

	private int textpaddingtop;

	private int imagepaddingleft;

	private int imagepaddingtop;

	private boolean ishide =true;

	private String mTextStep;

	public ProgressSeekBar(Context context) {
		super(context);
		init();
	}

	public ProgressSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// 屏蔽滑动
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return false;
	// }
	/**
	 * (�? Javadoc)
	 * 
	 * @方法�?: onTouchEvent
	 * @描述: 不屏蔽屏蔽滑�?
	 * @日期: 2014-8-11 下午2:03:15
	 * @param event
	 * @return
	 * @see android.widget.AbsSeekBar#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	// 修改setpadding 使其在外部调用的时�?�无�?
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if (isMysetPadding) {
			super.setPadding(left, top, right, bottom);
		}
	}

	// 初始�?
	private void init() {
		res = getResources();
		initBitmap();
		initDraw();
		setPadding();
		setOnSeekBarChangeListener(new onSeekBarChangeListener());
	}

	private void initDraw() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTypeface(Typeface.DEFAULT);
		mPaint.setTextSize(textsize);
//		mPaint.setColor(0xff23fc4f);
		mPaint.setColor(Color.parseColor("#f37f32"));
		mPaintStep = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintStep.setTypeface(Typeface.DEFAULT);
		mPaintStep.setTextSize(textsize);
		mPaintStep.setColor(Color.parseColor("#5a5a5a"));
	}

	private void initBitmap() {
		bm = BitmapFactory.decodeResource(res, R.drawable.popwindow_bg1);
		if (bm != null) {
			mImgWidth = bm.getWidth();
			mImgHei = bm.getHeight();
		} else {
			mImgWidth = 0;
			mImgHei = 0;
		}
	}

	private class onSeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			setIshide(true);
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			setIshide(true);
		}

	};

	protected synchronized void onDraw(Canvas canvas) {

		try {
			super.onDraw(canvas);
			if (ishide == true) {
				mText = (getProgress() * 1000+1000)+"";
				mTextStep = "步";
				mTextWidth = mPaint.measureText(mText);
				Rect bounds = this.getProgressDrawable().getBounds();
				float xImg = bounds.width() * getProgress() / getMax()
						+ imagepaddingleft + oldPaddingLeft;
				float yImg = imagepaddingtop + oldPaddingTop;

				float xText = bounds.width() * getProgress() / getMax()
						+ mImgWidth / 2 - mTextWidth / 2 + textpaddingleft
						+ oldPaddingLeft;
				float yText = yImg + textpaddingtop + mImgHei / 2
						+ getTextHei() / 4;
//				canvas.drawBitmap(bm, xImg, yImg, mPaint);
				mPaint.setTextSize(30);
				mPaintStep.setTextSize(25);
				canvas.drawText(mText, xText-10, yText, mPaint);
				canvas.drawText(mTextStep,xText+mPaint.measureText(mText)-10, yText, mPaintStep);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化padding 使其左右�? 留下位置用于展示进度图片
	private void setPadding() {
		int top = getBitmapHeigh() + oldPaddingTop;
		int left = getBitmapWidth() / 2 + oldPaddingLeft+10;
		int right = getBitmapWidth() / 2 + oldPaddingRight+10;
		int bottom = oldPaddingBottom;
		isMysetPadding = true;
		setPadding(left, top, right, bottom);
		isMysetPadding = false;
	}

	/**
	 * 设置展示进度背景图片
	 * 
	 * @param resid
	 */
	public void setBitmap(int resid) {
		bm = BitmapFactory.decodeResource(res, resid);
		if (bm != null) {
			mImgWidth = bm.getWidth();
			mImgHei = bm.getHeight();
		} else {
			mImgWidth = 0;
			mImgHei = 0;
		}
		setPadding();
	}

	/**
	 * 替代setpadding
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setMyPadding(int left, int top, int right, int bottom) {
		oldPaddingTop = top;
		oldPaddingLeft = left;
		oldPaddingRight = right;
		oldPaddingBottom = bottom;
		isMysetPadding = true;
		setPadding(left + getBitmapWidth() / 2, top + getBitmapHeigh(), right
				+ getBitmapWidth() / 2, bottom);
		isMysetPadding = false;
	}

	/**
	 * 设置进度字体大小
	 * 
	 * @param textsize
	 */
	public void setTextSize(int textsize) {
		this.textsize = textsize;
		mPaint.setTextSize(textsize);
	}

	/**
	 * 设置进度字体颜色
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		mPaint.setColor(color);
	}

	/**
	 * 调整进度字体的位�? 初始位置为图片的正中�?
	 * 
	 * @param top
	 * @param left
	 */
	public void setTextPadding(int top, int left) {
		this.textpaddingleft = left;
		this.textpaddingtop = top;
	}

	/**
	 * 调整进图背景图的位置 初始位置为进度条正上方�?�偏左一�?
	 * 
	 * @param top
	 * @param left
	 */
	public void setImagePadding(int top, int left) {
		this.imagepaddingleft = left;
		this.imagepaddingtop = top;
	}

	private int getBitmapWidth() {
		return (int) Math.ceil(mImgWidth);
	}

	private int getBitmapHeigh() {
		return (int) Math.ceil(mImgHei);
	}

	private float getTextHei() {
		FontMetrics fm = mPaint.getFontMetrics();
		return (float) Math.ceil(fm.descent - fm.top) + 2;
	}

	public int getTextpaddingleft() {
		return textpaddingleft;
	}

	public int getTextpaddingtop() {
		return textpaddingtop;
	}

	public int getImagepaddingleft() {
		return imagepaddingleft;
	}

	public int getImagepaddingtop() {
		return imagepaddingtop;
	}

	public int getTextsize() {
		return textsize;
	}

	public void setIshide(boolean ishide) {
		this.ishide = ishide;
	}

	public boolean isIshide() {
		return ishide;
	}

}

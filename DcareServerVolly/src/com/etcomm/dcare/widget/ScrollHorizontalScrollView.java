package com.etcomm.dcare.widget;


import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ScrollHorizontalScrollView extends HorizontalScrollView {

	/**
	 * 条目点击时的回调
	 *
	 * @author zhy
	 *
	 */
	public interface OnItemClickListener
	{
		void onClick(int index);
	}
	private int mScreenWitdh;
	private LinearLayout mContainer;
	private final String tag = getClass().getSimpleName();
	private ScrollHorizontalScrollViewAdapter adapter;
	private int itemWidth = 1;
	private OnItemClickListener mOnClickListener;
	private Scroller scroller;
	protected Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Log.i(tag, "handler odScroll");
			doScroll();
		};
	};
	private Timer t;
	public ScrollHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获得屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWitdh = outMetrics.widthPixels;
		t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
		}, 5000	, 100);
//		scroller =new  Scroller(context,new AccelerateDecelerateInterpolator());
//		setscr
//		getHandler().postDelayed(new Runnable() {
//			
//			@Override

//			public void run() {
//				// TODO Auto-generated method stub
//				doScroll();
//			}
//		}, 100);
//		scroller.
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
		if (mContainer.getChildCount() > 0) {
			itemWidth = mContainer.getChildAt(0).getWidth();
			if (itemWidth != 0) {
				mContainer.setPadding(itemWidth, 0, itemWidth, 0);
			}
		}else{
//			refreshDrawableState();
//			requestLayout();
		}
		Log.i(tag, "onMeasure");
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				Log.i(tag, "Action_Move");
				doScroll();
				break;
			case MotionEvent.ACTION_UP:
				if(android.os.Build.VERSION.SDK_INT>=21){
					stopNestedScroll();
				}else{

				}
				int scroll2 = getScrollX()+itemWidth*3/5;
				if(itemWidth>1){
					doScroll();
				}
				break;
			default:
				break;
		}
		return super.onTouchEvent(ev);
	}


	@Override
	public void setFillViewport(boolean fillViewport) {
		// TODO Auto-generated method stub
		Log.i(tag, "setFillViewport"+fillViewport);
		super.setFillViewport(fillViewport);
	}

	@Override
	public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
		Log.i(tag, "setFillViewport"+smoothScrollingEnabled);
		super.setSmoothScrollingEnabled(smoothScrollingEnabled);
	}

	@Override
	public boolean fullScroll(int fullScroll) {
		Log.i(tag, "fullScroll"+fullScroll);
		return super.fullScroll(fullScroll);
	}

//	@Override
//	public void fling(int velocityX) {
////		Log.i(tag, "fling"+velocityX+" scroller.getfinalx: "+scroller.getFinalX());
//		Log.i(tag, "fling"+velocityX);
//		doScroll();
//		super.fling(velocityX);
//	}

	@Override
	public void setOverScrollMode(int mode) {
		Log.i(tag, "setOverScrollMode"+mode);
		super.setOverScrollMode(mode);
	}

	private void doScroll() {
		// TODO Auto-generated method stub
		if(itemWidth > 1){
		}else{
			refreshDrawableState();
			requestLayout();
			return ;
//			itemWidth = 480;
		}
		int scrollx = getScrollX() + itemWidth  / 3;
		if (itemWidth > 1) {
			int index = scrollx / itemWidth;
			if(adapter!=null&&index>adapter.getCount()-1){
				index = adapter.getCount()-1;
			}
			int indexoffset = scrollx % itemWidth;
			float scale = getScaleByOffset(indexoffset);
			Log.i(tag, "scrollx: " + scrollx + " index: " + index + " indexoffset " + indexoffset + " itemWidth: "
					+ itemWidth + " scale : " + scale);
			scaleImage(index, scale);
		}else{
//			invalidate();
		}
	}

	private void scaleImage(int index, float scale) {
		// TODO Auto-generated method stub
		if(mContainer!=null){
			for (int i = 0; i < mContainer.getChildCount(); i++) {
				if(index!=i){
					mContainer.getChildAt(i).setScaleX(1.0f);
					mContainer.getChildAt(i).setScaleY(1.0f);
				}else{
					if(mOnClickListener!=null){
						mOnClickListener.onClick(index);
					}
					mContainer.getChildAt(index).setScaleX(scale);
					mContainer.getChildAt(index).setScaleY(scale);
				}
			}
		}
		invalidate();
	}
	public void setOnItemClickListener(OnItemClickListener mOnClickListener)
	{
		this.mOnClickListener = mOnClickListener;
	}
	private float getScaleByOffset(int indexoffset) {
		// TODO Auto-generated method stub
		return (float) (1.5-(Math.abs(indexoffset-itemWidth/2)/itemWidth));
//		return 0;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
	}



	@Override
	public boolean isFillViewport() {
		// TODO Auto-generated method stub
		return super.isFillViewport();
	}

	public void initData(ScrollHorizontalScrollViewAdapter adapter){
		this.adapter = adapter;
//		if(mContainer==null){
		mContainer = (LinearLayout) getChildAt(0);
//		}
		// 获得适配器中第一个View
//		final View view = adapter.getView(0, null, mContainer);
//		mContainer.addView(view);
		mContainer.removeAllViews();
		for (int i = 0; i < adapter.getCount(); i++)
		{
			View view = adapter.getView(i, null, mContainer);
//			view.setOnClickListener(this);
			if(i == 0){
				view.setScaleX(1.5f);
				view.setScaleY(1.5f);
			}
			mContainer.addView(view);
//			mViewPos.put(view, i);
//			mCurrentIndex = i;
		}
//		invalidate();
	}

	public void destroy() {
		// TODO Auto-generated method stub
		if(t != null){
			t.cancel();
			t = null;
		}
	}
}

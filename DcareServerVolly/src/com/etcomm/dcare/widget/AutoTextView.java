package com.etcomm.dcare.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.etcomm.dcare.R;
import com.etcomm.dcare.util.DensityUtil;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class AutoTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
	private static final String tag = "AutoTextView";
	private float mHeight;
	private Context mContext;
	// mInUp,mOutUp分离构成向下翻页的进出动画
	private Rotate3dAnimation mInUp;
	private Rotate3dAnimation mOutUp;

	// mInDown,mOutDown分离构成向下翻页的进出动画
	private Rotate3dAnimation mInDown;
	private Rotate3dAnimation mOutDown;
	protected int sCount;
	private String upText="";
	private String downText="";
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			setText(sCount%2==0 ?
					upText :downText);
//				    sCount+"BBBBBBB"+"\nThreeBBB");
		};
	};
	public AutoTextView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public AutoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.auto3d);
		WindowManager wm = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);
//		int width = wm.getDefaultDisplay().getWidth();
//		int height = wm.getDefaultDisplay().getHeight();
//		WindowManager manager = this.getWindowManager();
//		DisplayMetrics outMetrics = new DisplayMetrics();
//		wm.getDefaultDisplay().getMetrics(outMetrics);
//		int width2 = outMetrics.widthPixels;
//		int height2 = outMetrics.heightPixels;
		Resources resources = this.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		float density1 = dm.density;
		int width3 = dm.widthPixels;
		int height3 = dm.heightPixels;
		Log.i("AutoTextView", "density: "+density1+" width3: "+width3+" height3: "+height3+" dm.densityDpi "+dm.densityDpi+" dm.scaledDensity "+dm.scaledDensity);
		/**-- 4.5sp--9   1080*1920   6sp--12 720*1280 -*/
		mHeight = a.getDimension(R.styleable.auto3d_textSize, 12);
		Log.i(tag, "mHeight textSize: "+mHeight);
//		mHeight = 	DensityUtil.sp2px(context, 4.5f);
		mHeight = 13.5f;
		Log.i(tag, "mHeight: "+mHeight);
		a.recycle();
		mContext = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setFactory(this);
		mInUp = createAnim(-90, 0, true, true);
		mOutUp = createAnim(0, 90, false, true);
		mInDown = createAnim(90, 0, true, false);
		mOutDown = createAnim(0, -90, false, false);
		// TextSwitcher重要用于文件切换，比如 从文字A 切换到 文字 B，
		// setInAnimation()后，A将执行inAnimation，
		// setOutAnimation()后，B将执行OutAnimation
		setInAnimation(mInUp);
		setOutAnimation(mOutUp);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
				sCount++;
			}
		}, 3*100,35*100);
	}


	private Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp) {
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
		rotation.setDuration(800);
		rotation.setFillAfter(false);
		rotation.setInterpolator(new AccelerateInterpolator());
		return rotation;
	}

	// 这里返回的TextView，就是我们看到的View
	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		TextView t = new TextView(mContext);
		t.setGravity(Gravity.CENTER);
		t.setTextSize(mHeight);
		t.setLineSpacing(1, (float) 1.1);
		t.setMaxLines(2);
		return t;
	}

	// 定义动作，向下滚动翻页
	public void previous() {
		if (getInAnimation() != mInDown) {
			setInAnimation(mInDown);
		}
		if (getOutAnimation() != mOutDown) {
			setOutAnimation(mOutDown);
		}
	}

	// 定义动作，向上滚动翻页
	public void next() {
		if (getInAnimation() != mInUp) {
			setInAnimation(mInUp);
		}
		if (getOutAnimation() != mOutUp) {
			setOutAnimation(mOutUp);
		}
	}

	class Rotate3dAnimation extends Animation {
		private final float mFromDegrees;
		private final float mToDegrees;
		private float mCenterX;
		private float mCenterY;
		private final boolean mTurnIn;
		private final boolean mTurnUp;
		private Camera mCamera;

		public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;
			mTurnIn = turnIn;
			mTurnUp = turnUp;
		}

		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			mCamera = new Camera();
			mCenterY = getHeight() / 2;
			mCenterX = getWidth() / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			final float fromDegrees = mFromDegrees;
			float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
			final float centerX = mCenterX;
			final float centerY = mCenterY;
			final Camera camera = mCamera;
			final int derection = mTurnUp ? 1 : -1;
			final Matrix matrix = t.getMatrix();
			camera.save();
			if (mTurnIn) {
				camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
			} else {
				camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
			}
			camera.rotateX(degrees);
			camera.getMatrix(matrix);
			camera.restore();
			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}

	public String getUpText() {
		return upText;
	}

	public void setUpText(String upText) {
		this.upText = upText;
	}

	public String getDownText() {
		return downText;
	}

	public void setDownText(String downText) {
		this.downText = downText;
	}


}

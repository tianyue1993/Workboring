package com.etcomm.dcare.widget;
import com.etcomm.dcare.R;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnWindowAttachListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created by gaopengfei on 15/11/15.
 */
public class CircleSeekBar extends View {

    private static final double RADIAN = 180 / Math.PI;

    private static final String INATANCE_STATE = "state";
    private static final String INSTANCE_MAX_PROCESS = "max_process";
    private static final String INSTANCE_CUR_PROCESS = "cur_process";
    private static final String INSTANCE_REACHED_COLOR = "reached_color";
    private static final String INSTANCE_REACHED_WIDTH = "reached_width";
    private static final String INSTANCE_REACHED_CORNER_ROUND = "reached_corner_round";
    private static final String INSTANCE_UNREACHED_COLOR = "unreached_color";
    private static final String INSTANCE_UNREACHED_WIDTH = "unreached_width";
    private static final String INSTANCE_POINTER_COLOR = "pointer_color";
    private static final String INSTANCE_POINTER_RADIUS = "pointer_radius";
    private static final String INSTANCE_POINTER_SHADOW = "pointer_shadow";
    private static final String INSTANCE_POINTER_SHADOW_RADIUS = "pointer_shadow_radius";
    private static final String INSTANCE_WHEEL_SHADOW = "wheel_shadow";
    private static final String INSTANCE_WHEEL_SHADOW_RADIUS = "wheel_shadow_radius";
    private static final String INSTANCE_WHEEL_HAS_CACHE = "wheel_has_cache";
    private static final String INSTANCE_WHEEL_CAN_TOUCH = "wheel_can_touch";

	private static final String tag = "CircleSeekBar";

    private Paint mWheelPaint;

    private Paint mReachedPaint;

    private Paint mReachedEdgePaint;

    private Paint mPointerPaint;

    private int mMaxProcess;
    private int mCurProcess =0;
    private int mTargetProcess =0;
    private int mAniProcess =0;
    private int mReachedColor, mUnreachedColor;
    private float mReachedWidth, mUnreachedWidth;
    private boolean isHasReachedCornerRound;
    private int mPointerColor;
    private float mPointerRadius;

    private double mCurAngle;
    private float mWheelCurX, mWheelCurY;

    private boolean isHasWheelShadow, isHasPointerShadow;
    private float mWheelShadowRadius, mPointerShadowRadius;

    private boolean isHasCache;
    private Canvas mCacheCanvas;
    private Bitmap mCacheBitmap;

    private boolean isCanTouch;

    private float mDefShadowOffset;

    private OnSeekBarChangeListener mChangListener;
    /**
     * 梯度渲染  
     */
	private SweepGradient mSweepGradient;

	private int curwidth;
	private boolean isAni = false;
    public CircleSeekBar(Context context) {
        this(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
	private Handler mHandler = new Handler(Looper.getMainLooper()){
    	public void handleMessage(android.os.Message msg) {
    		Log.i(tag, "msg.what"+msg.what);
//    		float delta = mAniProcess/100;
    		setCurProcess( (int) (msg.what));
    	};
    };

	private ValueAnimator animation;  
	public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(attrs, defStyleAttr);
        initPadding();
        initPaints();
        initAnimation();
//        initSeekBarChangeListener();
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
//				Log.i(tag, "OnGlobalLayoutListener onGlobalLayout");
//				startAnimation();
			}
		});
//        getViewTreeObserver().addOnWindowAttachListener(new OnWindowAttachListener() {
//			
//			@Override
//			public void onWindowDetached() {
//				// TODO Auto-generated method stub
////				Log.i(tag, " onWindowDetached");
//			}
//			
//			@Override
//			public void onWindowAttached() {
//				// TODO Auto-generated method stub
////				Log.i(tag, " onWindowAttached");
//			}
//		});

    }
    private void initAnimation() {
		// TODO Auto-generated method stub
		animation = new ValueAnimator(); //ValueAnimator.ofInt(0,mCurProcess) ; //ValueAnimator.ofObject(new MyTypeEvaluator(), 0, curValue);
		animation.setEvaluator(new TypeEvaluator<Integer>() {
			@Override
			public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
				// TODO Auto-generated method stub
				Log.i("CircleSeekBar", "evaluate" +fraction);
				return Integer.valueOf((int)( fraction * mCurProcess ));
			};
		});
		animation.setObjectValues(Integer.valueOf(1),Integer.valueOf(mCurProcess));//Integer.valueOf(0),//Integer.valueOf(0),Integer.valueOf(mCurProcess)
		animation.setDuration(3000);
		animation.setRepeatCount(1);//Animation.INFINITE
		animation.setRepeatMode(Animation.REVERSE);
		animation.setInterpolator(new LinearInterpolator());
		animation.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				int curprocess = (Integer) animation.getAnimatedValue();
				Log.i("CircleSeekBar", "AnimatorUpdateListener: "+curprocess);
				setCurProcess(curprocess);
			}
		});
//		animation.
//		animation.start();
	}

    public void startValueAnimation(){
    	animation.start();
    }
    public void stopValueAnimation(){
    	
    }
    
	private void initSeekBarChangeListener() {
		// TODO Auto-generated method stub
    	mChangListener = new OnSeekBarChangeListener() {
			@Override
			public void onChanged(final CircleSeekBar seekbar, final int maxValue, final int curValue) {
				// TODO Auto-generated method stub
				ValueAnimator animation = new ValueAnimator(); //ValueAnimator.ofObject(new MyTypeEvaluator(), 0, curValue);
				animation.setEvaluator(new TypeEvaluator<Integer>() {
					@Override
					public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
						// TODO Auto-generated method stub
						return Integer.valueOf((int) fraction * curValue / maxValue);
//						return new Integer((int)fraction*curValue/maxValue);
					};
				});
//				return Integer.valueOf((int) (fraction*curValue/maxValue));
//				animation.set
				animation.setObjectValues(Integer.valueOf((int)(curValue*100/maxValue)),Integer.valueOf((int)(curValue*100/maxValue)));
				animation.setDuration(2000);
				animation.setRepeatCount(Animation.INFINITE);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setInterpolator(new LinearInterpolator());
				animation.start();
				animation.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// TODO Auto-generated method stub
						int curprocess = (Integer) animation.getAnimatedValue();
						Log.i(tag, "AnimatorUpdateListener: "+curprocess);
//						seekbar.setCurProcess(curprocess);
						setCurProcess(curprocess);
					}
				});
//				seekbar.setani
			}
		};
	}
    @SuppressWarnings("static-access")
	public void startvalueAnimation(){
    	ValueAnimator animation = new ValueAnimator(); //ValueAnimator.ofObject(new MyTypeEvaluator(), 0, curValue);
		animation.setEvaluator(new TypeEvaluator<Integer>() {
			@Override
			public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
				// TODO Auto-generated method stub
				Log.i(tag, "evaluate" +fraction);
				return Integer.valueOf((int) fraction * mCurProcess );
//				return new Integer((int)fraction*curValue/maxValue);
			};
		});
//		return Integer.valueOf((int) (fraction*curValue/maxValue));
//		animation.set
		animation.setObjectValues(Integer.valueOf(mCurProcess),Integer.valueOf(mCurProcess));//Integer.valueOf(0),Integer.valueOf(mCurProcess)
		animation.setDuration(2000);
		animation.setFrameDelay(100);
		animation.setRepeatCount(Animation.INFINITE);
		animation.setRepeatMode(Animation.REVERSE);
		animation.setInterpolator(new LinearInterpolator());
		animation.start();
		animation.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				int curprocess = (Integer) animation.getAnimatedValue();
				Log.i(tag, "AnimatorUpdateListener: "+curprocess);
				setCurProcess(curprocess);
//				setCurProcess(curprocess);
			}
		});
    }
	@Override
    protected void onAttachedToWindow() {
    	// TODO Auto-generated method stub
    	super.onAttachedToWindow();
//    	Log.i(tag, " onAttachedToWindow");
//    	startAnimation();
    }
    private void initPaints() {
        mDefShadowOffset = getDimen(R.dimen.def_shadow_offset);
        /**
         * 圆环画笔
         */
        mWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWheelPaint.setColor(mUnreachedColor);
        mWheelPaint.setStyle(Paint.Style.STROKE);
        mWheelPaint.setStrokeWidth(mUnreachedWidth);
        if (isHasWheelShadow) {
            mWheelPaint.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 选中区域画笔
         */
        mReachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mReachedPaint.setColor(mReachedColor);
        mReachedPaint.setStyle(Paint.Style.STROKE);
        mReachedPaint.setStrokeWidth(mReachedWidth);
        if (isHasReachedCornerRound) {
            mReachedPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        /**
         * 锚点画笔
         */
        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setColor(mPointerColor);
        mPointerPaint.setStyle(Paint.Style.FILL);
        if (isHasPointerShadow) {
            mPointerPaint.setShadowLayer(mPointerShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
        }
        /**
         * 选中区域两头的圆角画笔
         */
        mReachedEdgePaint = new Paint(mReachedPaint);
        mReachedEdgePaint.setStyle(Paint.Style.FILL);
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleSeekBar, defStyle, 0);
        mMaxProcess = a.getInt(R.styleable.CircleSeekBar_wheel_max_process, 100);
        mCurProcess = a.getInt(R.styleable.CircleSeekBar_wheel_cur_process, 0);
        if (mCurProcess > mMaxProcess) mCurProcess = mMaxProcess;
        mReachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_reached_color, getColor(R.color.def_reached_color));
        mUnreachedColor = a.getColor(R.styleable.CircleSeekBar_wheel_unreached_color,
                getColor(R.color.def_wheel_color));
        mUnreachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_unreached_width,
                getDimen(R.dimen.def_wheel_width));
        isHasReachedCornerRound = a.getBoolean(R.styleable.CircleSeekBar_wheel_reached_has_corner_round, true);
        mReachedWidth = a.getDimension(R.styleable.CircleSeekBar_wheel_reached_width, mUnreachedWidth);
        mPointerColor = a.getColor(R.styleable.CircleSeekBar_wheel_pointer_color, getColor(R.color.def_pointer_color));
        mPointerRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_radius, mReachedWidth / 2);
        isHasWheelShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_wheel_shadow, false);
        if (isHasWheelShadow) {
            mWheelShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasPointerShadow = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_pointer_shadow, false);
        if (isHasPointerShadow) {
            mPointerShadowRadius = a.getDimension(R.styleable.CircleSeekBar_wheel_pointer_shadow_radius,
                    getDimen(R.dimen.def_shadow_radius));
        }
        isHasCache = a.getBoolean(R.styleable.CircleSeekBar_wheel_has_cache, isHasWheelShadow);
        isCanTouch = a.getBoolean(R.styleable.CircleSeekBar_wheel_can_touch, true);

        if (isHasPointerShadow | isHasWheelShadow) {
            setSoftwareLayer();
        }
        a.recycle();
    }


	private boolean isFirstDraw = true;
	private boolean stop = false;
    
   public void startAnimation(final int mTProcess){
	   stop = true;
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stop = false;
    	Log.i(tag, "startAnimation "+mCurProcess);
//    	final float delta = mCurProcess/100;
    	mAniProcess = mCurProcess;
    	this.mTargetProcess = mTProcess;
    	new Thread(new Runnable() {

			//    		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				isAni = true;
//				int delt= 1;
//				if(mTProcess>80){
//					delt=2;
//				}else if(mTProcess>50){
//					delt=3;
//				}else if(mTProcess>30){
//					delt=4;
//				}else if(mTProcess>10){
//					delt=5;
//				}else{
//					delt=10;
//				}
				for (int i = 0; i <= mTargetProcess; i++) {
					Log.i(tag, "sendEmptyMessage "+i);
					try {
						Thread.sleep(18);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(stop){
						break;
					}
//					Message msg = mHandler.obtainMessage();
//					msg.obj = i * delta;
//					msg.setTarget(mHandler);
//					mHandler.sendEmptyMessage(mTargetProcess*i/100);
					mHandler.sendEmptyMessage(i);
				}
				isAni = false; 
			}
		}).start();;
//    	new Thread(new Runnable() {
//    		float delta = mCurProcess/100;
//			@Override
//			public void run() {
//				Log.i(tag, "startAnimation thread run ");
//				// TODO Auto-generated method stub
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				for( int i = 0;i<100;i++){
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					Message msg = mHandle.obtainMessage();
//					msg.obj=i*delta;
//					msg.setTarget(mHandle);
//				}
//			}
//		}).start();;
    }
    
    @SuppressLint("NewApi")
	private void initPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingStart = 0, paddingEnd = 0;
        if (Build.VERSION.SDK_INT >= 17) {
            paddingStart = getPaddingStart();
            paddingEnd = getPaddingEnd();
        }
        int maxPadding = Math.max(paddingLeft, Math.max(paddingTop,
                Math.max(paddingRight, Math.max(paddingBottom, Math.max(paddingStart, paddingEnd)))));
        setPadding(maxPadding, maxPadding, maxPadding, maxPadding);
    }

//    @TargetApi(Build.VERSION_CODES.M)
    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getContext().getColor(colorId);
        } else {
            return getResources().getColor(colorId);
        }
    }

    private float getDimen(int dimenId) {
        return getResources().getDimension(dimenId);
    }

    private void setSoftwareLayer() {
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        curwidth = min;
//        Log.i(tag, "width:"+width+" height: "+height+" min: "+min);
        setMeasuredDimension(min, min);
//        mCurAngle = (double) mCurProcess / mMaxProcess * 360.0;
//        double cos = -Math.cos(Math.toRadians(mCurAngle));
//        Log.i(tag, "mCurAngle: "+mCurAngle+" cos: "+cos);
//        float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
//        mWheelCurX = calcXLocationInWheel(mCurAngle > 180 ? 0 : min, (float) cos, radius);
//        mWheelCurY = calcYLocationInWheel((float) cos, radius);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    	// TODO Auto-generated method stub
    	super.onLayout(changed, left, top, right, bottom);
    	 mCurAngle = (double) mCurProcess / mMaxProcess * 360.0;
//    	 if(mCurAngle>350){
////    		 mCurAngle+=10;
//    	 }else{
//    		 mCurAngle+=10;
//    	 }
    	 double mAngle = mCurAngle+2;
//		if(mCurAngle > 162&&mCurAngle <=182){
//			mAngle = mCurAngle+(182-mCurAngle)/10;
//		}else if(mCurAngle>182&&mCurAngle<202){
//			mAngle = mCurAngle+((mCurAngle-182)/10);
//		}else if(mCurAngle > 340) {
//			mAngle = mCurAngle+(2-(mCurAngle-340)/10);
//		}else{
//			mAngle=mCurAngle+2;
//		}
//		else 
    	 if(mAngle> 358){
			mAngle = 358;
		}else if(mAngle<2){
//			mAngle =0;
		}
//    		 else{
//			mAngle=mCurAngle+2;
//		}
//    	 
		Log.i(tag, "mAngle: "+mAngle);
         double cos = -Math.cos(Math.toRadians(mAngle));
//         Log.i(tag, "mCurAngle: "+mCurAngle+" cos: "+cos);
         float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
         int min = getWidth();
		mWheelCurX = calcXLocationInWheel(mAngle > 180 ? 0 : min , (float) cos, radius);
         mWheelCurY = calcYLocationInWheel((float) cos, radius);
    }
    @SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
//    	Log.i(tag, "ondraw "+mCurProcess);
        float left = getPaddingLeft() + mUnreachedWidth / 2;
        float top = getPaddingTop() + mUnreachedWidth / 2;
        float right = canvas.getWidth() - getPaddingRight() - mUnreachedWidth / 2;
        float bottom = canvas.getHeight() - getPaddingBottom() - mUnreachedWidth / 2;
        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2;

        float wheelRadius = (canvas.getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - mUnreachedWidth / 2;

        if (isHasCache) {
            if (mCacheCanvas == null) {
                buildCache(centerX, centerY, wheelRadius);
            }
            canvas.drawBitmap(mCacheBitmap, 0, 0, null);
        } else {
//        	 Log.i(tag, "centerX: "+centerX+" centerY: "+centerY+" wheelRadius: "+wheelRadius);
            canvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
        }
        ///
        float cx = (left+right)/2;
        float cy = (top+bottom)/2;
//        mSweepGradient = new SweepGradient(cx, cy, Color.parseColor("#f7ef97"),Color.parseColor("#fe841b"));
//        mSweepGradient = new SweepGradient(cx, cy, new int[] {Color.BLACK,Color.BLUE,Color.GREEN,Color.YELLOW,Color.WHITE},
//        		new float[]{0.0f,0.25f,0.5f,0.75f,1.0f});
//        mSweepGradient = new SweepGradient(cx, cy, new int[] {Color.parseColor("#fac262"),Color.parseColor("#ff4709"),Color.parseColor("#fe7a0f"),Color.parseColor("#f7ef97"),Color.parseColor("#fac262")},
//        		new float[]{0.0f,0.25f,0.5f,0.75f,1.0f});
        mSweepGradient = new SweepGradient(cx, cy, new int[] {Color.parseColor("#fac262"),Color.parseColor("#fbac4a"),Color.parseColor("#fe861d"),Color.parseColor("#fe841b"),Color.parseColor("#f7ef97"),Color.parseColor("#fac262")},
        		new float[]{0.0f,0.2f,0.4f,0.7358f,0.736f,1.0f});
//        mSweepGradient = new SweepGradient(cx, cy, new int[] {Color.parseColor("#fbb655"),Color.parseColor("#ff7105")
//        		,Color.parseColor("#ff5d05"),Color.parseColor("#fc7700"),Color.parseColor("#f7f097"),Color.parseColor("#fbb655")},null);    // new float[]{0.75f,0f,0.25f,0.5f,0.75f}
//        mSweepGradient = new SweepGradient(cx, cy, new int[] {Color.CYAN,Color.DKGRAY,Color.GRAY,Color.LTGRAY,Color.MAGENTA,    
//        		Color.GREEN,Color.TRANSPARENT, Color.BLUE }, null);    
        mReachedPaint.setShader(mSweepGradient);
        //画选中区域
        canvas.drawArc(new RectF(left, top, right, bottom), -88, (float) mCurAngle, false, mReachedPaint);

        //画锚点
        Log.i(tag, "mWheelCurX: "+mWheelCurX+" mWheelCurY: "+mWheelCurY+" mPointerRadius: "+mPointerRadius);
        canvas.drawCircle(mWheelCurX, mWheelCurY, mPointerRadius, mPointerPaint);
        if(isFirstDraw ){
//        	Log.i(tag, "FirstDraw "+isFirstDraw);
        	isFirstDraw = false;
        }
    }

    private void buildCache(float centerX, float centerY, float wheelRadius) {
        mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas(mCacheBitmap);
        //画环
        Log.i(tag, "buildCache centerX: "+centerX+" centerY: "+centerY+" wheelRadius: "+wheelRadius);
        mCacheCanvas.drawCircle(centerX, centerY, wheelRadius, mWheelPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        if (isCanTouch && (event.getAction() == MotionEvent.ACTION_MOVE || isTouch(x, y))) {
//            // 通过当前触摸点搞到cos角度值
//            float cos = computeCos(x, y);
//            // 通过反三角函数获得角度值
//            if (x < getWidth() / 2) { // 滑动超过180度
//                mCurAngle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
//            } else { // 没有超过180度
//                mCurAngle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
//            }
//            mCurProcess = getSelectedValue();
//
//            float radius = (getWidth() - getPaddingLeft() - getPaddingRight() - mUnreachedWidth) / 2;
//            mWheelCurX = calcXLocationInWheel(x, cos, radius);
//            mWheelCurY = calcYLocationInWheel(cos, radius);
//            if (mChangListener != null) {
//                mChangListener.onChanged(this, mMaxProcess, mCurProcess);
//            }
//            invalidate();
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
            return super.onTouchEvent(event);
    }

    private boolean isTouch(float x, float y) {
        double radius = (getWidth() - getPaddingLeft() - getPaddingRight() + getCircleWidth()) / 2;
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        return Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2) < radius * radius;
    }

    private float getCircleWidth() {
        return Math.max(mUnreachedWidth, Math.max(mReachedWidth, mPointerRadius));
    }

    private float calcXLocationInWheel(float x, float cos, float radius) {
//    	Log.i(tag, "getWidth():"+getWidth());
//    	if(getWidth()==0){
//    		curwidth
//    	}
    	int width = getWidth();
    	if(width==0){
    		width = curwidth;
    	}
        if (x > (width / 2)) {
            return (float) (width / 2 + Math.sqrt(1 - cos * cos) * radius);
        } else {
            return (float) (width / 2 - Math.sqrt(1 - cos * cos) * radius);
        }
    }

    private float calcYLocationInWheel(float cos, float radius) {
    	int width = getWidth();
    	if(width==0){
    		width = curwidth;
    	}
        return width / 2 + radius * cos;
    }

    /**
     * 拿到倾斜的cos值
     */
    private float computeCos(float x, float y) {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INATANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_MAX_PROCESS, mMaxProcess);
        bundle.putInt(INSTANCE_CUR_PROCESS, mCurProcess);
        bundle.putInt(INSTANCE_REACHED_COLOR, mReachedColor);
        bundle.putFloat(INSTANCE_REACHED_WIDTH, mReachedWidth);
        bundle.putBoolean(INSTANCE_REACHED_CORNER_ROUND, isHasReachedCornerRound);
        bundle.putInt(INSTANCE_UNREACHED_COLOR, mUnreachedColor);
        bundle.putFloat(INSTANCE_UNREACHED_WIDTH, mUnreachedWidth);
        bundle.putInt(INSTANCE_POINTER_COLOR, mPointerColor);
        bundle.putFloat(INSTANCE_POINTER_RADIUS, mPointerRadius);
        bundle.putBoolean(INSTANCE_POINTER_SHADOW, isHasPointerShadow);
        bundle.putFloat(INSTANCE_POINTER_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_SHADOW, isHasWheelShadow);
        bundle.putFloat(INSTANCE_WHEEL_SHADOW_RADIUS, mPointerShadowRadius);
        bundle.putBoolean(INSTANCE_WHEEL_HAS_CACHE, isHasCache);
        bundle.putBoolean(INSTANCE_WHEEL_CAN_TOUCH, isCanTouch);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INATANCE_STATE));
            mMaxProcess = bundle.getInt(INSTANCE_MAX_PROCESS);
            mCurProcess = bundle.getInt(INSTANCE_CUR_PROCESS);
            mReachedColor = bundle.getInt(INSTANCE_REACHED_COLOR);
            mReachedWidth = bundle.getFloat(INSTANCE_REACHED_WIDTH);
            isHasReachedCornerRound = bundle.getBoolean(INSTANCE_REACHED_CORNER_ROUND);
            mUnreachedColor = bundle.getInt(INSTANCE_UNREACHED_COLOR);
            mUnreachedWidth = bundle.getFloat(INSTANCE_UNREACHED_WIDTH);
            mPointerColor = bundle.getInt(INSTANCE_POINTER_COLOR);
            mPointerRadius = bundle.getFloat(INSTANCE_POINTER_RADIUS);
            isHasPointerShadow = bundle.getBoolean(INSTANCE_POINTER_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_POINTER_SHADOW_RADIUS);
            isHasWheelShadow = bundle.getBoolean(INSTANCE_WHEEL_SHADOW);
            mPointerShadowRadius = bundle.getFloat(INSTANCE_WHEEL_SHADOW_RADIUS);
            isHasCache = bundle.getBoolean(INSTANCE_WHEEL_HAS_CACHE);
            isCanTouch = bundle.getBoolean(INSTANCE_WHEEL_CAN_TOUCH);
            initPaints();
        } else {
            super.onRestoreInstanceState(state);
        }

        if (mChangListener != null) {
            mChangListener.onChanged(this, mMaxProcess, mCurProcess);
        }
//        startvalueAnimation();
    }

    private int getSelectedValue() {
        return Math.round(mMaxProcess * ((float) mCurAngle / 360));
    }

    public int getCurProcess() {
        return mCurProcess;
    }

    public void setCurProcess(int curProcess) {
        this.mCurProcess = curProcess > mMaxProcess ? mMaxProcess : curProcess;
        if (mChangListener != null) {
            mChangListener.onChanged(this, mMaxProcess, curProcess);
        }
        Log.i(tag, "setCurProcess "+curProcess);
        requestLayout();
//        postInvalidate();
//        requestLayout();
        invalidate();
//        Rect dirty = new Rect() ;
//        getGlobalVisibleRect(dirty);
////        getLocalVisibleRect(dirty);
//		invalidate(dirty );
    }

    public int getReachedColor() {
        return mReachedColor;
    }

    public void setReachedColor(int reachedColor) {
        this.mReachedColor = reachedColor;
        mReachedPaint.setColor(reachedColor);
        mReachedEdgePaint.setColor(reachedColor);
        invalidate();
    }

    public int getUnreachedColor() {
        return mUnreachedColor;
    }

    public void setUnreachedColor(int unreachedColor) {
        this.mUnreachedColor = unreachedColor;
        mWheelPaint.setColor(unreachedColor);
        invalidate();
    }

    public float getReachedWidth() {
        return mReachedWidth;
    }

    public void setReachedWidth(float reachedWidth) {
        this.mReachedWidth = reachedWidth;
        mReachedPaint.setStrokeWidth(reachedWidth);
        mReachedEdgePaint.setStrokeWidth(reachedWidth);
        invalidate();
    }

    public boolean isHasReachedCornerRound() {
        return isHasReachedCornerRound;
    }

    public void setHasReachedCornerRound(boolean hasReachedCornerRound) {
        isHasReachedCornerRound = hasReachedCornerRound;
        mReachedPaint.setStrokeCap(hasReachedCornerRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        invalidate();
    }

    public float getUnreachedWidth() {
        return mUnreachedWidth;
    }

    public void setUnreachedWidth(float unreachedWidth) {
        this.mUnreachedWidth = unreachedWidth;
        mWheelPaint.setStrokeWidth(unreachedWidth);
        invalidate();
    }

    public int getPointerColor() {
        return mPointerColor;
    }

    public void setPointerColor(int pointerColor) {
        this.mPointerColor = pointerColor;
        mPointerPaint.setColor(pointerColor);
    }

    public float getPointerRadius() {
        return mPointerRadius;
    }

    public void setPointerRadius(float pointerRadius) {
        this.mPointerRadius = pointerRadius;
        mPointerPaint.setStrokeWidth(pointerRadius);
        invalidate();
    }

    public boolean isHasWheelShadow() {
        return isHasWheelShadow;
    }

    public void setWheelShadow(float wheelShadow) {
        this.mWheelShadowRadius = wheelShadow;
        if (wheelShadow == 0) {
            isHasWheelShadow = false;
            mWheelPaint.clearShadowLayer();
            mCacheCanvas = null;
            mCacheBitmap.recycle();
            mCacheBitmap = null;
        } else {
            mWheelPaint.setShadowLayer(mWheelShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public float getWheelShadowRadius() {
        return mWheelShadowRadius;
    }

    public boolean isHasPointerShadow() {
        return isHasPointerShadow;
    }

    public float getPointerShadowRadius() {
        return mPointerShadowRadius;
    }

    public void setPointerShadowRadius(float pointerShadowRadius) {
        this.mPointerShadowRadius = pointerShadowRadius;
        if (mPointerShadowRadius == 0) {
            isHasPointerShadow = false;
            mPointerPaint.clearShadowLayer();
        } else {
            mPointerPaint.setShadowLayer(pointerShadowRadius, mDefShadowOffset, mDefShadowOffset, Color.DKGRAY);
            setSoftwareLayer();
        }
        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener listener) {
        mChangListener = listener;
    }

    public interface OnSeekBarChangeListener {
        void onChanged(CircleSeekBar seekbar, int maxValue, int curValue);
    }

	@Override
	public void onFinishTemporaryDetach() {
		// TODO Auto-generated method stub
		Log.i(tag, "onFinishTemporaryDetach");
		super.onFinishTemporaryDetach();
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
//		Log.i(tag, "onFinishInflate");
		super.onFinishInflate();
	}

	@Override
	public ViewPropertyAnimator animate() {
		// TODO Auto-generated method stub
		Log.i(tag, "ViewPropertyAnimator  animate");
		return super.animate();
	}

	public boolean isAni() {
		return isAni;
	}

	public void setAni(boolean isAni) {
		this.isAni = isAni;
	}
    
    
}
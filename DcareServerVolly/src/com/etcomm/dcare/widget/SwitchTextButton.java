package com.etcomm.dcare.widget;

import com.etcomm.dcare.R;
import com.polites.MathUtils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;

public class SwitchTextButton extends CompoundButton {
	
	 private static final int TOUCH_MODE_IDLE = 0;  
	    private static final int TOUCH_MODE_DOWN = 1;  
	    private static final int TOUCH_MODE_DRAGGING = 2;  
	  
	    // Enum for the "typeface" XML parameter.  
	    private static final int SANS = 1;  
	    private static final int SERIF = 2;  
	    private static final int MONOSPACE = 3;  
	  
	    private Drawable mThumbDrawable;  
	    private Drawable mTrackDrawable;  
	    private int mThumbTextPadding;  
	    private int mSwitchMinWidth;  
	    private int mSwitchPadding;  
	    private CharSequence mTextOn;  
	    private CharSequence mTextOff;  
	  
	    private int mTouchMode;  
	    private int mTouchSlop;  
	    private float mTouchX;  
	    private float mTouchY;  
	    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();  
	    private int mMinFlingVelocity;  
	  
	    private float mThumbPosition;  
	    private int mSwitchWidth;  
	    private int mSwitchHeight;  
	    private int mThumbWidth; // Does not include padding  
	  
	    private int mSwitchLeft;  
	    private int mSwitchTop;  
	    private int mSwitchRight;  
	    private int mSwitchBottom;  
	  
	    private TextPaint mTextPaint;  
	    private ColorStateList mTextColors;  
	    private Layout mOnLayout;  
	    private Layout mOffLayout;  
	  
	    private Context mContext;  
	  
	    @SuppressWarnings("hiding")  
	    private final Rect mTempRect = new Rect();  
	  
	    private static final int[] CHECKED_STATE_SET = {  
	        android.R.attr.state_checked  
	    };  
	  
	    /** 
	     * Construct a new Switch with default styling. 
	     * 
	     * @param context The Context that will determine this widget's theming. 
	     */  
	    public SwitchTextButton(Context context) {  
	        this(context, null);  
	  
	        mContext = context;  
	    }  
	  
	    /** 
	     * Construct a new Switch with default styling, overriding specific style 
	     * attributes as requested. 
	     * 
	     * @param context The Context that will determine this widget's theming. 
	     * @param attrs Specification of attributes that should deviate from default styling. 
	     */  
	    public SwitchTextButton(Context context, AttributeSet attrs) {  
	        this(context, attrs, R.attr.switchStyle);  
	  
	        mContext = context;  
	    }  
	  
	    /** 
	     * Construct a new Switch with a default style determined by the given theme attribute, 
	     * overriding specific style attributes as requested. 
	     * 
	     * @param context The Context that will determine this widget's theming. 
	     * @param attrs Specification of attributes that should deviate from the default styling. 
	     * @param defStyle An attribute ID within the active theme containing a reference to the 
	     *                 default style for this widget. e.g. android.R.attr.switchStyle. 
	     */  
	    public SwitchTextButton(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle);  
	  
	        mContext = context;  
	  
	        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);  
	        Resources res = getResources();  
	        mTextPaint.density = res.getDisplayMetrics().density;  
	        //float scaledDensity = res.getDisplayMetrics().scaledDensity;  
	        //mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);  
	  
	        TypedArray a = context.obtainStyledAttributes(attrs,  
	                R.styleable.Switch, defStyle, 0);  
	  
	        mThumbDrawable = a.getDrawable(R.styleable.Switch_thumb);  
	        mTrackDrawable = a.getDrawable(R.styleable.Switch_track2);  
	        mTextOn = a.getText(R.styleable.Switch_textOn);  
	        mTextOff = a.getText(R.styleable.Switch_textOff);  
	        mThumbTextPadding = a.getDimensionPixelSize(  
	                R.styleable.Switch_thumbTextPadding2, 0);  
	        mSwitchMinWidth = a.getDimensionPixelSize(  
	                R.styleable.Switch_switchMinWidth2, 0);  
	        mSwitchPadding = a.getDimensionPixelSize(  
	                R.styleable.Switch_switchPadding2, 0);  
	  
	        Log.d("SvenDebug", "mTextOn:" + mTextOn);  
	        Log.d("SvenDebug", "mTextOff:" + mTextOff);  
	        Log.d("SvenDebug", "mThumbTextPadding:" + mThumbTextPadding);  
	        Log.d("SvenDebug", "mSwitchMinWidth:" + mSwitchMinWidth);  
	        Log.d("SvenDebug", "mSwitchPadding:" + mSwitchPadding);  
	  
	        int appearance = a.getResourceId(  
	                R.styleable.Switch_switchTextAppearance2, 0);  
	        if (appearance != 0) {  
	            setSwitchTextAppearance(context, appearance);  
	        }  
	        a.recycle();  
	  
	        ViewConfiguration config = ViewConfiguration.get(context);  
	        mTouchSlop = config.getScaledTouchSlop();  
	        mMinFlingVelocity = config.getScaledMinimumFlingVelocity();  
	  
	        // Refresh display with current params  
	        refreshDrawableState();  
	        setChecked(isChecked());  
	    }  
	  
	    /** 
	     * Sets the switch text color, size, style, hint color, and highlight color 
	     * from the specified TextAppearance resource. 
	     */  
	    public void setSwitchTextAppearance(Context context, int resid) {  
	        mContext = context;  
	  
	        TypedArray appearance =  
	                context.obtainStyledAttributes(resid,  
	                        R.styleable.TextAppearance);  
	  
	        ColorStateList colors = null;  
	        int ts = 0;  
	  
//	        colors = appearance.getColorStateList(R.styleable.  
//	                TextAppearance_textColor);  
	        if (colors != null) {  
	            mTextColors = colors;  
	        } else {  
	            // If no color set in TextAppearance, default to the view's textColor  
	            mTextColors = getTextColors();  
	        }  
	  
//	        ts = appearance.getDimensionPixelSize(R.styleable.  
//	                TextAppearance_textSize, 0);  
	        if (ts != 0) {  
	            if (ts != mTextPaint.getTextSize()) {  
	                mTextPaint.setTextSize(ts);  
	                requestLayout();  
	            }  
	        }  
	  
	        int typefaceIndex, styleIndex;  
	  
//	        typefaceIndex = appearance.getInt(R.styleable.  
//	                TextAppearance_typeface, -1);  
//	        styleIndex = appearance.getInt(R.styleable.  
//	                TextAppearance_textStyle, -1);  
	  
//	        setSwitchTypefaceByIndex(typefaceIndex, styleIndex);  
	  
	        appearance.recycle();  
	    }  
	  
	    private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {  
	        Typeface tf = null;  
	        switch (typefaceIndex) {  
	            case SANS:  
	                tf = Typeface.SANS_SERIF;  
	                break;  
	  
	            case SERIF:  
	                tf = Typeface.SERIF;  
	                break;  
	  
	            case MONOSPACE:  
	                tf = Typeface.MONOSPACE;  
	                break;  
	        }  
	  
	        setSwitchTypeface(tf, styleIndex);  
	    }  
	  
	    /** 
	     * Sets the typeface and style in which the text should be displayed on the 
	     * switch, and turns on the fake bold and italic bits in the Paint if the 
	     * Typeface that you provided does not have all the bits in the 
	     * style that you specified. 
	     */  
	    public void setSwitchTypeface(Typeface tf, int style) {  
	        if (style > 0) {  
	            if (tf == null) {  
	                tf = Typeface.defaultFromStyle(style);  
	            } else {  
	                tf = Typeface.create(tf, style);  
	            }  
	  
	            setSwitchTypeface(tf);  
	            // now compute what (if any) algorithmic styling is needed  
	            int typefaceStyle = tf != null ? tf.getStyle() : 0;  
	            int need = style & ~typefaceStyle;  
	            mTextPaint.setFakeBoldText((need & Typeface.BOLD) != 0);  
	            mTextPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);  
	        } else {  
	            mTextPaint.setFakeBoldText(false);  
	            mTextPaint.setTextSkewX(0);  
	            setSwitchTypeface(tf);  
	        }  
	    }  
	  
	    /** 
	     * Sets the typeface in which the text should be displayed on the switch. 
	     * Note that not all Typeface families actually have bold and italic 
	     * variants, so you may need to use 
	     * {@link #setSwitchTypeface(Typeface, int)} to get the appearance 
	     * that you actually want. 
	     * 
	     * @attr ref android.R.styleable#TextView_typeface 
	     * @attr ref android.R.styleable#TextView_textStyle 
	     */  
	    public void setSwitchTypeface(Typeface tf) {  
	        if (mTextPaint.getTypeface() != tf) {  
	            mTextPaint.setTypeface(tf);  
	  
	            requestLayout();  
	            invalidate();  
	        }  
	    }  
	  
	    /** 
	     * Returns the text displayed when the button is in the checked state. 
	     */  
	    public CharSequence getTextOn() {  
	        return mTextOn;  
	    }  
	  
	    /** 
	     * Sets the text displayed when the button is in the checked state. 
	     */  
	    public void setTextOn(CharSequence textOn) {  
	        mTextOn = textOn;  
	        requestLayout();  
	    }  
	  
	    /** 
	     * Returns the text displayed when the button is not in the checked state. 
	     */  
	    public CharSequence getTextOff() {  
	        return mTextOff;  
	    }  
	  
	    /** 
	     * Sets the text displayed when the button is not in the checked state. 
	     */  
	    public void setTextOff(CharSequence textOff) {  
	        mTextOff = textOff;  
	        requestLayout();  
	    }  
	  
	    @Override  
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
	        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
	        int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
	  
	  
	        if (mOnLayout == null) {  
	            mOnLayout = makeLayout(mTextOn);  
	        }  
	        if (mOffLayout == null) {  
	            mOffLayout = makeLayout(mTextOff);  
	        }  
	  
	        mTrackDrawable.getPadding(mTempRect);  
	        final int maxTextWidth = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth());  
	        final int switchWidth = Math.max(mSwitchMinWidth,  
	                maxTextWidth * 2 + mThumbTextPadding * 4 + mTempRect.left + mTempRect.right);  
	        final int switchHeight = mTrackDrawable.getIntrinsicHeight();  
	  
	        mThumbWidth = maxTextWidth + mThumbTextPadding * 2;  
	  
	        switch (widthMode) {  
	            case MeasureSpec.AT_MOST:  
	                widthSize = Math.min(widthSize, switchWidth);  
	                break;  
	  
	            case MeasureSpec.UNSPECIFIED:  
	                widthSize = switchWidth;  
	                break;  
	  
	            case MeasureSpec.EXACTLY:  
	                // Just use what we were given  
	                break;  
	        }  
	  
	        switch (heightMode) {  
	            case MeasureSpec.AT_MOST:  
	                heightSize = Math.min(heightSize, switchHeight);  
	                break;  
	  
	            case MeasureSpec.UNSPECIFIED:  
	                heightSize = switchHeight;  
	                break;  
	  
	            case MeasureSpec.EXACTLY:  
	                // Just use what we were given  
	                break;  
	        }  
	  
	        mSwitchWidth = switchWidth;  
	        mSwitchHeight = switchHeight;  
	  
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
	        final int measuredHeight = getMeasuredHeight();  
	        if (measuredHeight < switchHeight) {  
	            setMeasuredDimension(getMeasuredWidth(), switchHeight);  
	        }  
	    }  
	  
	   /* @Override 
	    public boolean dispatchTouchEvent(MotionEvent event) { 
	        Log.d("SvenDebug", "dispatchTouchEvent:action=" + event.getAction()); 
	        return false; 
	    }*/  
	  
	    @Override  
	    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {  
	        Log.d("SvenDebug", "dispatchPopulateAccessibilityEvent");  
	        populateAccessibilityEvent(event);  
	  
	        return false;  
	    }  
	  
	    public void populateAccessibilityEvent(AccessibilityEvent event) {  
	        if (isChecked()) {  
	            CharSequence text = mOnLayout.getText();  
	            if (TextUtils.isEmpty(text)) {  
	                text = mContext.getString(R.string.switch_on);  
	            }  
	            event.getText().add(text);  
	        } else {  
	            CharSequence text = mOffLayout.getText();  
	            if (TextUtils.isEmpty(text)) {  
	                text = mContext.getString(R.string.switch_off);  
	            }  
	            event.getText().add(text);  
	        }  
	    }  
	  
	    private Layout makeLayout(CharSequence text) {  
	        return new StaticLayout(text, mTextPaint,  
	                (int) Math.ceil(Layout.getDesiredWidth(text, mTextPaint)),  
	                Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);  
	    }  
	  
	    /** 
	     * @return true if (x, y) is within the target area of the switch thumb 
	     */  
	    private boolean hitThumb(float x, float y) {  
	        mThumbDrawable.getPadding(mTempRect);  
	        final int thumbTop = mSwitchTop - mTouchSlop;  
	        final int thumbLeft = mSwitchLeft + (int) (mThumbPosition + 0.5f) - mTouchSlop;  
	        final int thumbRight = thumbLeft + mThumbWidth +  
	                mTempRect.left + mTempRect.right + mTouchSlop;  
	        final int thumbBottom = mSwitchBottom + mTouchSlop;  
	        return x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom;  
	    }  
	  
	    @Override  
	    public boolean onTouchEvent(MotionEvent ev) {  
	        mVelocityTracker.addMovement(ev);  
	        final int action = ev.getActionMasked();  
	        Log.d("SvenDebug", "MotionEvent : " + action);  
	        switch (action) {  
	            case MotionEvent.ACTION_DOWN: {  
	                Log.d("SvenDebug", "MotionEvent.ACTION_DOWN");  
	                final float x = ev.getX();  
	                final float y = ev.getY();  
	                if (isEnabled() && hitThumb(x, y)) {  
	                    Log.d("SvenDebug", "Enable in widget rect");  
	                    mTouchMode = TOUCH_MODE_DOWN;  
	                    mTouchX = x;  
	                    mTouchY = y;  
	                }  
	                break;  
	            }  
	  
	            case MotionEvent.ACTION_MOVE: {  
	                Log.d("SvenDebug", "MotionEvent.ACTION_MOVE");  
	                switch (mTouchMode) {  
	                    case TOUCH_MODE_IDLE:  
	                        // Didn't target the thumb, treat normally.  
	                        break;  
	  
	                    case TOUCH_MODE_DOWN: {  
	                        Log.d("SvenDebug", "TOUCH_MODE_DOWN:mTouchSlop = " + mTouchSlop);  
	                        final float x = ev.getX();  
	                        final float y = ev.getY();  
	                        if (Math.abs(x - mTouchX) > mTouchSlop ||  
	                                Math.abs(y - mTouchY) > mTouchSlop) {  
	                            mTouchMode = TOUCH_MODE_DRAGGING;  
	                            getParent().requestDisallowInterceptTouchEvent(true);  
	                            mTouchX = x;  
	                            mTouchY = y;  
	                            return true;  
	                        }  
	                        break;  
	                    }  
	  
	                    case TOUCH_MODE_DRAGGING: {  
	                        Log.d("SvenDebug", "TOUCH_MODE_DRAGGING");  
	                        final float x = ev.getX();  
	                        final float dx = x - mTouchX;  
	                        float newPos = Math.max(0,  
	                                Math.min(mThumbPosition + dx, getThumbScrollRange()));  
	                        if (newPos != mThumbPosition) {  
	                            mThumbPosition = newPos;  
	                            mTouchX = x;  
	                            invalidate();  
	                        }  
	                        return true;  
	                    }  
	                }  
	                break;  
	            }  
	  
	            case MotionEvent.ACTION_UP:  
	            case MotionEvent.ACTION_CANCEL: {  
	                Log.d("SvenDebug", "MotionEvent.ACTION_UP|ACTION_CANCEL");  
	                if (mTouchMode == TOUCH_MODE_DRAGGING) {  
	                    stopDrag(ev);  
	                    return true;  
	                }  
	                mTouchMode = TOUCH_MODE_IDLE;  
	                mVelocityTracker.clear();  
	                break;  
	            }  
	        }  
	  
	        return super.onTouchEvent(ev);  
	    }  
	  
	    private void cancelSuperTouch(MotionEvent ev) {  
	        MotionEvent cancel = MotionEvent.obtain(ev);  
	        cancel.setAction(MotionEvent.ACTION_CANCEL);  
	        super.onTouchEvent(cancel);  
	        cancel.recycle();  
	    }  
	  
	    /** 
	     * Called from onTouchEvent to end a drag operation. 
	     * 
	     * @param ev Event that triggered the end of drag mode - ACTION_UP or ACTION_CANCEL 
	     */  
	    private void stopDrag(MotionEvent ev) {  
	        mTouchMode = TOUCH_MODE_IDLE;  
	        // Up and not canceled, also checks the switch has not been disabled during the drag  
	        boolean commitChange = ev.getAction() == MotionEvent.ACTION_UP && isEnabled();  
	  
	        cancelSuperTouch(ev);  
	  
	        if (commitChange) {  
	            boolean newState;  
	            mVelocityTracker.computeCurrentVelocity(1000);  
	            float xvel = mVelocityTracker.getXVelocity();  
	            if (Math.abs(xvel) > mMinFlingVelocity) {  
	                newState = xvel > 0;  
	            } else {  
	                newState = getTargetCheckedState();  
	            }  
	            animateThumbToCheckedState(newState);  
	        } else {  
	            animateThumbToCheckedState(isChecked());  
	        }  
	    }  
	  
	    private void animateThumbToCheckedState(boolean newCheckedState) {  
	        // TODO animate!  
	        //float targetPos = newCheckedState ? 0 : getThumbScrollRange();  
	        //mThumbPosition = targetPos;  
	        setChecked(newCheckedState);  
	    }  
	  
	    private boolean getTargetCheckedState() {  
	        return mThumbPosition >= getThumbScrollRange() / 2;  
	    }  
	  
	    @Override  
	    public void setChecked(boolean checked) {  
	        super.setChecked(checked);  
	        mThumbPosition = checked ? getThumbScrollRange() : 0;  
	        invalidate();  
	    }  
	  
	    @Override  
	    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {  
	        super.onLayout(changed, left, top, right, bottom);  
	  
	        mThumbPosition = isChecked() ? getThumbScrollRange() : 0;  
	  
	        int switchRight = getWidth() - getPaddingRight();  
	        int switchLeft = switchRight - mSwitchWidth;  
	        int switchTop = 0;  
	        int switchBottom = 0;  
	        switch (getGravity() & Gravity.VERTICAL_GRAVITY_MASK) {  
	            default:  
	            case Gravity.TOP:  
	                switchTop = getPaddingTop();  
	                switchBottom = switchTop + mSwitchHeight;  
	                break;  
	  
	            case Gravity.CENTER_VERTICAL:  
	                switchTop = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 -  
	                        mSwitchHeight / 2;  
	                switchBottom = switchTop + mSwitchHeight;  
	                break;  
	  
	            case Gravity.BOTTOM:  
	                switchBottom = getHeight() - getPaddingBottom();  
	                switchTop = switchBottom - mSwitchHeight;  
	                break;  
	        }  
	  
	        mSwitchLeft = switchLeft;  
	        mSwitchTop = switchTop;  
	        mSwitchBottom = switchBottom;  
	        mSwitchRight = switchRight;  
	    }  
	  
	    @Override  
	    protected void onDraw(Canvas canvas) {  
	        super.onDraw(canvas);  
	  
	        // Draw the switch  
	        int switchLeft = mSwitchLeft;  
	        int switchTop = mSwitchTop;  
	        int switchRight = mSwitchRight;  
	        int switchBottom = mSwitchBottom;  
	  
	        mTrackDrawable.setBounds(switchLeft, switchTop, switchRight, switchBottom);  
	        mTrackDrawable.draw(canvas);  
	  
	        canvas.save();  
	  
	        mTrackDrawable.getPadding(mTempRect);  
	        int switchInnerLeft = switchLeft + mTempRect.left;  
	        int switchInnerTop = switchTop + mTempRect.top;  
	        int switchInnerRight = switchRight - mTempRect.right;  
	        int switchInnerBottom = switchBottom - mTempRect.bottom;  
	        canvas.clipRect(switchInnerLeft, switchTop, switchInnerRight, switchBottom);  
	  
//	         FIXME:  
	        //Drawable offDrawable = mContext.getResources().getDrawable(R.drawable.switch_thumb_mz);  
	        //Drawable onDrawable = mContext.getResources().getDrawable(R.drawable.switch_thumb_activated_mz);  
	        //mThumbDrawable = getTargetCheckedState() ? onDrawable : offDrawable;  
	  
	        mThumbDrawable.getPadding(mTempRect);  
	        final int thumbPos = (int) (mThumbPosition + 0.5f);  
	        int thumbLeft = switchInnerLeft - mTempRect.left + thumbPos;  
	        int thumbRight = switchInnerLeft + thumbPos + mThumbWidth + mTempRect.right;  
	  
	        mThumbDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);  
	        mThumbDrawable.draw(canvas);  
	  
	        // mTextColors should not be null, but just in case  
	        if (mTextColors != null) {  
	            mTextPaint.setColor(mTextColors.getColorForState(getDrawableState(),  
	                    mTextColors.getDefaultColor()));  
	        }  
	        mTextPaint.drawableState = getDrawableState();  
	  
	        Layout switchText = getTargetCheckedState() ? mOnLayout : mOffLayout;  
	  
	        canvas.translate((thumbLeft + thumbRight) / 2 - switchText.getWidth() / 2,  
	                (switchInnerTop + switchInnerBottom) / 2 - switchText.getHeight() / 2);  
	        switchText.draw(canvas);  
	  
	        canvas.restore();  
	    }  
	  
	    @Override  
	    public int getCompoundPaddingRight() {  
	        int padding = super.getCompoundPaddingRight() + mSwitchWidth;  
	        if (!TextUtils.isEmpty(getText())) {  
	            padding += mSwitchPadding;  
	        }  
	        return padding;  
	    }  
	  
	    private int getThumbScrollRange() {  
	        if (mTrackDrawable == null) {  
	            return 0;  
	        }  
	        mTrackDrawable.getPadding(mTempRect);  
	        return mSwitchWidth - mThumbWidth - mTempRect.left - mTempRect.right;  
	    }  
	  
	    @Override  
	    protected int[] onCreateDrawableState(int extraSpace) {  
	        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);  
	        if (isChecked()) {  
	            mergeDrawableStates(drawableState, CHECKED_STATE_SET);  
	        }  
	        return drawableState;  
	    }  
	  
	    @Override  
	    protected void drawableStateChanged() {  
	        super.drawableStateChanged();  
	  
	        int[] myDrawableState = getDrawableState();  
	  
	        // Set the state of the Drawable  
	        // Drawable may be null when checked state is set from XML, from super constructor  
	        if (mThumbDrawable != null) mThumbDrawable.setState(myDrawableState);  
	        if (mTrackDrawable != null) mTrackDrawable.setState(myDrawableState);  
	  
	        invalidate();  
	    }  
	  
	    @Override  
	    protected boolean verifyDrawable(Drawable who) {  
	        return super.verifyDrawable(who) || who == mThumbDrawable || who == mTrackDrawable;  
	    }  
	  
	    /*@Override 
	    public void jumpDrawablesToCurrentState() { 
	        super.jumpDrawablesToCurrentState(); 
	        mThumbDrawable.jumpToCurrentState(); 
	        mTrackDrawable.jumpToCurrentState(); 
	    }*/  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	 private static final int THUMB_ANIMATION_DURATION = 250;
//
//	    private static final int TOUCH_MODE_IDLE = 0;
//	    private static final int TOUCH_MODE_DOWN = 1;
//	    private static final int TOUCH_MODE_DRAGGING = 2;
//
//	    // Enum for the "typeface" XML parameter.
//	    private static final int SANS = 1;
//	    private static final int SERIF = 2;
//	    private static final int MONOSPACE = 3;
//
//	    private Drawable mThumbDrawable;
//	    private ColorStateList mThumbTintList = null;
//	    private PorterDuff.Mode mThumbTintMode = null;
//	    private boolean mHasThumbTint = false;
//	    private boolean mHasThumbTintMode = false;
//
//	    private Drawable mTrackDrawable;
//	    private ColorStateList mTrackTintList = null;
//	    private PorterDuff.Mode mTrackTintMode = null;
//	    private boolean mHasTrackTint = false;
//	    private boolean mHasTrackTintMode = false;
//
//	    private int mThumbTextPadding;
//	    private int mSwitchTextButtonTextButtonMinWidth;
//	    private int mSwitchTextButtonTextButtonPadding;
//	    private boolean mSplitTrack;
//	    private CharSequence mTextOn;
//	    private CharSequence mTextOff;
//	    private boolean mShowText;
//
//	    private int mTouchMode;
//	    private int mTouchSlop;
//	    private float mTouchX;
//	    private float mTouchY;
//	    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
//	    private int mMinFlingVelocity;
//
//	    private float mThumbPosition;
//
//	    /**
//	     * Width required to draw the SwitchTextButtonTextButton track and thumb. Includes padding and
//	     * optical bounds for both the track and thumb.
//	     */
//	    private int mSwitchTextButtonTextButtonWidth;
//
//	    /**
//	     * Height required to draw the SwitchTextButtonTextButton track and thumb. Includes padding and
//	     * optical bounds for both the track and thumb.
//	     */
//	    private int mSwitchTextButtonTextButtonHeight;
//
//	    /**
//	     * Width of the thumb's content region. Does not include padding or
//	     * optical bounds.
//	     */
//	    private int mThumbWidth;
//
//	    /** Left bound for drawing the SwitchTextButtonTextButton track and thumb. */
//	    private int mSwitchTextButtonTextButtonLeft;
//
//	    /** Top bound for drawing the SwitchTextButtonTextButton track and thumb. */
//	    private int mSwitchTextButtonTextButtonTop;
//
//	    /** Right bound for drawing the SwitchTextButtonTextButton track and thumb. */
//	    private int mSwitchTextButtonTextButtonRight;
//
//	    /** Bottom bound for drawing the SwitchTextButtonTextButton track and thumb. */
//	    private int mSwitchTextButtonTextButtonBottom;
//
//	    private TextPaint mTextPaint;
//	    private ColorStateList mTextColors;
//	    private Layout mOnLayout;
//	    private Layout mOffLayout;
//	    private TransformationMethod mSwitchTextButtonTextButtonTransformationMethod;
//	    private ObjectAnimator mPositionAnimator;
//
//	    @SuppressWarnings("hiding")
//	    private final Rect mTempRect = new Rect();
//
//	    private static final int[] CHECKED_STATE_SET = {
//	        android.R.attr.state_checked
//	    };

	    /**
	     * Construct a new SwitchTextButtonTextButton with default styling.
	     *
	     * @param context The Context that will determine this widget's theming.
	     */
//	    public SwitchTextButton(Context context) {
//	        this(context, null);
//	    }

	    /**
	     * Construct a new SwitchTextButtonTextButton with default styling, overriding specific style
	     * attributes as requested.
	     *
	     * @param context The Context that will determine this widget's theming.
	     * @param attrs Specification of attributes that should deviate from default styling.
	     */
//	    public SwitchTextButton(Context context, AttributeSet attrs) {
//	        this(context, attrs, 0);//com.android.internal.R.attr.SwitchTextButtonTextButtonStyle
//	    }

	    /**
	     * Construct a new SwitchTextButtonTextButton with a default style determined by the given theme attribute,
	     * overriding specific style attributes as requested.
	     *
	     * @param context The Context that will determine this widget's theming.
	     * @param attrs Specification of attributes that should deviate from the default styling.
	     * @param defStyleAttr An attribute in the current theme that contains a
	     *        reference to a style resource that supplies default values for
	     *        the view. Can be 0 to not look for defaults.
	     */
//	    public SwitchTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
//	        this(context, attrs, defStyleAttr, 0);
//	    }


	    /**
	     * Construct a new SwitchTextButtonTextButton with a default style determined by the given theme
	     * attribute or style resource, overriding specific style attributes as
	     * requested.
	     *
	     * @param context The Context that will determine this widget's theming.
	     * @param attrs Specification of attributes that should deviate from the
	     *        default styling.
	     * @param defStyleAttr An attribute in the current theme that contains a
	     *        reference to a style resource that supplies default values for
	     *        the view. Can be 0 to not look for defaults.
	     * @param defStyleRes A resource identifier of a style resource that
	     *        supplies default values for the view, used only if
	     *        defStyleAttr is 0 or can not be found in the theme. Can be 0
	     *        to not look for defaults.
	     */
	    @SuppressLint("NewApi")
		public SwitchTextButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	        super(context, attrs, defStyleAttr, defStyleRes);

//	        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//
//	        final Resources res = getResources();
//	        mTextPaint.density = res.getDisplayMetrics().density;
////	        mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);
//	        final TypedArray a = context.obtainStyledAttributes(
//	                attrs, android.R.style.SwitchTextButton, defStyleAttr, defStyleRes);
//	        mThumbDrawable = a.getDrawable(android.R.style.SwitchTextButtonTextButton_thumb);
//	        if (mThumbDrawable != null) {
//	            mThumbDrawable.setCallback(this);
//	        }
//	        mTrackDrawable = a.getDrawable(android.R.style.SwitchTextButtonTextButton_track);
//	        if (mTrackDrawable != null) {
//	            mTrackDrawable.setCallback(this);
//	        }
//	        mTextOn = a.getText(com.android.internal.R.styleable.SwitchTextButtonTextButton_textOn);
//	        mTextOff = a.getText(com.android.internal.R.styleable.SwitchTextButtonTextButton_textOff);
//	        mShowText = a.getBoolean(com.android.internal.R.styleable.SwitchTextButtonTextButton_showText, true);
//	        mThumbTextPadding = a.getDimensionPixelSize(
//	                com.android.internal.R.styleable.SwitchTextButtonTextButton_thumbTextPadding, 0);
//	        mSwitchTextButtonTextButtonMinWidth = a.getDimensionPixelSize(
//	                com.android.internal.R.styleable.SwitchTextButtonTextButton_SwitchTextButtonTextButtonMinWidth, 0);
//	        mSwitchTextButtonTextButtonPadding = a.getDimensionPixelSize(
//	                com.android.internal.R.styleable.SwitchTextButtonTextButton_SwitchTextButtonTextButtonPadding, 0);
//	        mSplitTrack = a.getBoolean(com.android.internal.R.styleable.SwitchTextButtonTextButton_splitTrack, false);
//
//	        ColorStateList thumbTintList = a.getColorStateList(
//	                com.android.internal.R.styleable.SwitchTextButtonTextButton_thumbTint);
//	        if (thumbTintList != null) {
//	            mThumbTintList = thumbTintList;
//	            mHasThumbTint = true;
//	        }
//	        PorterDuff.Mode thumbTintMode = Drawable.parseTintMode(
//	                a.getInt(com.android.internal.R.styleable.SwitchTextButtonTextButton_thumbTintMode, -1), null);
//	        if (mThumbTintMode != thumbTintMode) {
//	            mThumbTintMode = thumbTintMode;
//	            mHasThumbTintMode = true;
//	        }
//	        if (mHasThumbTint || mHasThumbTintMode) {
//	            applyThumbTint();
//	        }
//
//	        ColorStateList trackTintList = a.getColorStateList(
//	                com.android.internal.R.styleable.SwitchTextButtonTextButton_trackTint);
//	        if (trackTintList != null) {
//	            mTrackTintList = trackTintList;
//	            mHasTrackTint = true;
//	        }
//	        PorterDuff.Mode trackTintMode = Drawable.parseTintMode(
//	                a.getInt(com.android.internal.R.styleable.SwitchTextButtonTextButton_trackTintMode, -1), null);
//	        if (mTrackTintMode != trackTintMode) {
//	            mTrackTintMode = trackTintMode;
//	            mHasTrackTintMode = true;
//	        }
//	        if (mHasTrackTint || mHasTrackTintMode) {
//	            applyTrackTint();
//	        }
//
//	        final int appearance = a.getResourceId(
//	                com.android.internal.R.styleable.SwitchTextButtonTextButton_SwitchTextButtonTextButtonTextAppearance, 0);
//	        if (appearance != 0) {
//	            setSwitchTextButtonTextButtonTextAppearance(context, appearance);
//	        }
//	        a.recycle();
//
//	        final ViewConfiguration config = ViewConfiguration.get(context);
//	        mTouchSlop = config.getScaledTouchSlop();
//	        mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
//
//	        // Refresh display with current params
//	        refreshDrawableState();
//	        setChecked(isChecked());
	    }

	    /**
	     * Sets the SwitchTextButtonTextButton text color, size, style, hint color, and highlight color
	     * from the specified TextAppearance resource.
	     *
	     * @attr ref android.R.styleable#SwitchTextButtonTextButton_SwitchTextButtonTextButtonTextAppearance
	     */
//	    public void setSwitchTextButtonTextButtonTextAppearance(Context context, @StyleRes int resid) {
//	        TypedArray appearance =
//	                context.obtainStyledAttributes(resid,
//	                        com.android.internal.R.styleable.TextAppearance);
//
//	        ColorStateList colors;
//	        int ts;
//
//	        colors = appearance.getColorStateList(com.android.internal.R.styleable.
//	                TextAppearance_textColor);
//	        if (colors != null) {
//	            mTextColors = colors;
//	        } else {
//	            // If no color set in TextAppearance, default to the view's textColor
//	            mTextColors = getTextColors();
//	        }
//
//	        ts = appearance.getDimensionPixelSize(com.android.internal.R.styleable.
//	                TextAppearance_textSize, 0);
//	        if (ts != 0) {
//	            if (ts != mTextPaint.getTextSize()) {
//	                mTextPaint.setTextSize(ts);
//	                requestLayout();
//	            }
//	        }
//
//	        int typefaceIndex, styleIndex;
//
//	        typefaceIndex = appearance.getInt(com.android.internal.R.styleable.
//	                TextAppearance_typeface, -1);
//	        styleIndex = appearance.getInt(com.android.internal.R.styleable.
//	                TextAppearance_textStyle, -1);
//
//	        setSwitchTextButtonTextButtonTypefaceByIndex(typefaceIndex, styleIndex);
//
//	        boolean allCaps = appearance.getBoolean(com.android.internal.R.styleable.
//	                TextAppearance_textAllCaps, false);
//	        if (allCaps) {
//	            mSwitchTextButtonTextButtonTransformationMethod = new AllCapsTransformationMethod(getContext());
//	            mSwitchTextButtonTextButtonTransformationMethod.setLengthChangesAllowed(true);
//	        } else {
//	            mSwitchTextButtonTextButtonTransformationMethod = null;
//	        }
//
//	        appearance.recycle();
//	    }
//
//	    private void setSwitchTextButtonTextButtonTypefaceByIndex(int typefaceIndex, int styleIndex) {
//	        Typeface tf = null;
//	        SwitchTextButtonTextButton (typefaceIndex) {
//	            case SANS:
//	                tf = Typeface.SANS_SERIF;
//	                break;
//
//	            case SERIF:
//	                tf = Typeface.SERIF;
//	                break;
//
//	            case MONOSPACE:
//	                tf = Typeface.MONOSPACE;
//	                break;
//	        }
//
//	        setSwitchTextButtonTextButtonTypeface(tf, styleIndex);
//	    }
//
//	    /**
//	     * Sets the typeface and style in which the text should be displayed on the
//	     * SwitchTextButtonTextButton, and turns on the fake bold and italic bits in the Paint if the
//	     * Typeface that you provided does not have all the bits in the
//	     * style that you specified.
//	     */
//	    public void setSwitchTextButtonTextButtonTypeface(Typeface tf, int style) {
//	        if (style > 0) {
//	            if (tf == null) {
//	                tf = Typeface.defaultFromStyle(style);
//	            } else {
//	                tf = Typeface.create(tf, style);
//	            }
//
//	            setSwitchTextButtonTextButtonTypeface(tf);
//	            // now compute what (if any) algorithmic styling is needed
//	            int typefaceStyle = tf != null ? tf.getStyle() : 0;
//	            int need = style & ~typefaceStyle;
//	            mTextPaint.setFakeBoldText((need & Typeface.BOLD) != 0);
//	            mTextPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
//	        } else {
//	            mTextPaint.setFakeBoldText(false);
//	            mTextPaint.setTextSkewX(0);
//	            setSwitchTextButtonTextButtonTypeface(tf);
//	        }
//	    }
//
//	    /**
//	     * Sets the typeface in which the text should be displayed on the SwitchTextButtonTextButton.
//	     * Note that not all Typeface families actually have bold and italic
//	     * variants, so you may need to use
//	     * {@link #setSwitchTextButtonTextButtonTypeface(Typeface, int)} to get the appearance
//	     * that you actually want.
//	     *
//	     * @attr ref android.R.styleable#TextView_typeface
//	     * @attr ref android.R.styleable#TextView_textStyle
//	     */
//	    public void setSwitchTextButtonTextButtonTypeface(Typeface tf) {
//	        if (mTextPaint.getTypeface() != tf) {
//	            mTextPaint.setTypeface(tf);
//
//	            requestLayout();
//	            invalidate();
//	        }
//	    }
//
//	    /**
//	     * Set the amount of horizontal padding between the SwitchTextButtonTextButton and the associated text.
//	     *
//	     * @param pixels Amount of padding in pixels
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButtonTextButton_SwitchTextButtonTextButtonPadding
//	     */
//	    public void setSwitchTextButtonTextButtonPadding(int pixels) {
//	        mSwitchTextButtonTextButtonPadding = pixels;
//	        requestLayout();
//	    }
//
//	    /**
//	     * Get the amount of horizontal padding between the SwitchTextButtonTextButton and the associated text.
//	     *
//	     * @return Amount of padding in pixels
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButtonTextButton_SwitchTextButtonTextButtonPadding
//	     */
//	    public int getSwitchTextButtonTextButtonPadding() {
//	        return mSwitchTextButtonTextButtonPadding;
//	    }
//
//	    /**
//	     * Set the minimum width of the SwitchTextButtonTextButton in pixels. The SwitchTextButtonTextButton's width will be the maximum
//	     * of this value and its measured width as determined by the SwitchTextButtonTextButton drawables and text used.
//	     *
//	     * @param pixels Minimum width of the SwitchTextButtonTextButton in pixels
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButtonTextButton_SwitchTextButtonTextButtonMinWidth
//	     */
//	    public void setSwitchTextButtonTextButtonMinWidth(int pixels) {
//	        mSwitchTextButtonTextButtonMinWidth = pixels;
//	        requestLayout();
//	    }
//
//	    /**
//	     * Get the minimum width of the SwitchTextButtonTextButton in pixels. The SwitchTextButtonTextButton's width will be the maximum
//	     * of this value and its measured width as determined by the SwitchTextButtonTextButton drawables and text used.
//	     *
//	     * @return Minimum width of the SwitchTextButtonTextButton in pixels
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButtonTextButton_SwitchTextButtonTextButtonMinWidth
//	     */
//	    public int getSwitchTextButtonTextButtonMinWidth() {
//	        return mSwitchTextButtonTextButtonMinWidth;
//	    }
//
//	    /**
//	     * Set the horizontal padding around the text drawn on the SwitchTextButtonTextButton itself.
//	     *
//	     * @param pixels Horizontal padding for SwitchTextButtonTextButton thumb text in pixels
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButtonTextButton_thumbTextPadding
//	     */
//	    public void setThumbTextPadding(int pixels) {
//	        mThumbTextPadding = pixels;
//	        requestLayout();
//	    }
//
//	    /**
//	     * Get the horizontal padding around the text drawn on the SwitchTextButtonTextButton itself.
//	     *
//	     * @return Horizontal padding for SwitchTextButtonTextButton thumb text in pixels
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButtonTextButton_thumbTextPadding
//	     */
//	    public int getThumbTextPadding() {
//	        return mThumbTextPadding;
//	    }
//
//	    /**
//	     * Set the drawable used for the track that the SwitchTextButton slides within.
//	     *
//	     * @param track Track drawable
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_track
//	     */
//	    public void setTrackDrawable(Drawable track) {
//	        if (mTrackDrawable != null) {
//	            mTrackDrawable.setCallback(null);
//	        }
//	        mTrackDrawable = track;
//	        if (track != null) {
//	            track.setCallback(this);
//	        }
//	        requestLayout();
//	    }
//
//	    /**
//	     * Set the drawable used for the track that the SwitchTextButton slides within.
//	     *
//	     * @param resId Resource ID of a track drawable
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_track
//	     */
//	    public void setTrackResource(@DrawableRes int resId) {
//	        setTrackDrawable(getContext().getDrawable(resId));
//	    }
//
//	    /**
//	     * Get the drawable used for the track that the SwitchTextButton slides within.
//	     *
//	     * @return Track drawable
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_track
//	     */
//	    public Drawable getTrackDrawable() {
//	        return mTrackDrawable;
//	    }
//
//	    /**
//	     * Applies a tint to the track drawable. Does not modify the current
//	     * tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
//	     * <p>
//	     * Subsequent calls to {@link #setTrackDrawable(Drawable)} will
//	     * automatically mutate the drawable and apply the specified tint and tint
//	     * mode using {@link Drawable#setTintList(ColorStateList)}.
//	     *
//	     * @param tint the tint to apply, may be {@code null} to clear tint
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_trackTint
//	     * @see #getTrackTintList()
//	     * @see Drawable#setTintList(ColorStateList)
//	     */
//	    public void setTrackTintList(@Nullable ColorStateList tint) {
//	        mTrackTintList = tint;
//	        mHasTrackTint = true;
//
//	        applyTrackTint();
//	    }
//
//	    /**
//	     * @return the tint applied to the track drawable
//	     * @attr ref android.R.styleable#SwitchTextButton_trackTint
//	     * @see #setTrackTintList(ColorStateList)
//	     */
//	    @Nullable
//	    public ColorStateList getTrackTintList() {
//	        return mTrackTintList;
//	    }
//
//	    /**
//	     * Specifies the blending mode used to apply the tint specified by
//	     * {@link #setTrackTintList(ColorStateList)}} to the track drawable.
//	     * The default mode is {@link PorterDuff.Mode#SRC_IN}.
//	     *
//	     * @param tintMode the blending mode used to apply the tint, may be
//	     *                 {@code null} to clear tint
//	     * @attr ref android.R.styleable#SwitchTextButton_trackTintMode
//	     * @see #getTrackTintMode()
//	     * @see Drawable#setTintMode(PorterDuff.Mode)
//	     */
//	    public void setTrackTintMode(@Nullable PorterDuff.Mode tintMode) {
//	        mTrackTintMode = tintMode;
//	        mHasTrackTintMode = true;
//
//	        applyTrackTint();
//	    }
//
//	    /**
//	     * @return the blending mode used to apply the tint to the track
//	     *         drawable
//	     * @attr ref android.R.styleable#SwitchTextButton_trackTintMode
//	     * @see #setTrackTintMode(PorterDuff.Mode)
//	     */
//	    @Nullable
//	    public PorterDuff.Mode getTrackTintMode() {
//	        return mTrackTintMode;
//	    }
//
//	    private void applyTrackTint() {
//	        if (mTrackDrawable != null && (mHasTrackTint || mHasTrackTintMode)) {
//	            mTrackDrawable = mTrackDrawable.mutate();
//
//	            if (mHasTrackTint) {
//	                mTrackDrawable.setTintList(mTrackTintList);
//	            }
//
//	            if (mHasTrackTintMode) {
//	                mTrackDrawable.setTintMode(mTrackTintMode);
//	            }
//
//	            // The drawable (or one of its children) may not have been
//	            // stateful before applying the tint, so let's try again.
//	            if (mTrackDrawable.isStateful()) {
//	                mTrackDrawable.setState(getDrawableState());
//	            }
//	        }
//	    }
//
//	    /**
//	     * Set the drawable used for the SwitchTextButton "thumb" - the piece that the user
//	     * can physically touch and drag along the track.
//	     *
//	     * @param thumb Thumb drawable
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_thumb
//	     */
//	    public void setThumbDrawable(Drawable thumb) {
//	        if (mThumbDrawable != null) {
//	            mThumbDrawable.setCallback(null);
//	        }
//	        mThumbDrawable = thumb;
//	        if (thumb != null) {
//	            thumb.setCallback(this);
//	        }
//	        requestLayout();
//	    }
//
//	    /**
//	     * Set the drawable used for the SwitchTextButton "thumb" - the piece that the user
//	     * can physically touch and drag along the track.
//	     *
//	     * @param resId Resource ID of a thumb drawable
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_thumb
//	     */
//	    public void setThumbResource(@DrawableRes int resId) {
//	        setThumbDrawable(getContext().getDrawable(resId));
//	    }
//
//	    /**
//	     * Get the drawable used for the SwitchTextButton "thumb" - the piece that the user
//	     * can physically touch and drag along the track.
//	     *
//	     * @return Thumb drawable
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_thumb
//	     */
//	    public Drawable getThumbDrawable() {
//	        return mThumbDrawable;
//	    }
//
//	    /**
//	     * Applies a tint to the thumb drawable. Does not modify the current
//	     * tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
//	     * <p>
//	     * Subsequent calls to {@link #setThumbDrawable(Drawable)} will
//	     * automatically mutate the drawable and apply the specified tint and tint
//	     * mode using {@link Drawable#setTintList(ColorStateList)}.
//	     *
//	     * @param tint the tint to apply, may be {@code null} to clear tint
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_thumbTint
//	     * @see #getThumbTintList()
//	     * @see Drawable#setTintList(ColorStateList)
//	     */
//	    public void setThumbTintList(@Nullable ColorStateList tint) {
//	        mThumbTintList = tint;
//	        mHasThumbTint = true;
//
//	        applyThumbTint();
//	    }
//
//	    /**
//	     * @return the tint applied to the thumb drawable
//	     * @attr ref android.R.styleable#SwitchTextButton_thumbTint
//	     * @see #setThumbTintList(ColorStateList)
//	     */
//	    @Nullable
//	    public ColorStateList getThumbTintList() {
//	        return mThumbTintList;
//	    }
//
//	    /**
//	     * Specifies the blending mode used to apply the tint specified by
//	     * {@link #setThumbTintList(ColorStateList)}} to the thumb drawable.
//	     * The default mode is {@link PorterDuff.Mode#SRC_IN}.
//	     *
//	     * @param tintMode the blending mode used to apply the tint, may be
//	     *                 {@code null} to clear tint
//	     * @attr ref android.R.styleable#SwitchTextButton_thumbTintMode
//	     * @see #getThumbTintMode()
//	     * @see Drawable#setTintMode(PorterDuff.Mode)
//	     */
//	    public void setThumbTintMode(@Nullable PorterDuff.Mode tintMode) {
//	        mThumbTintMode = tintMode;
//	        mHasThumbTintMode = true;
//
//	        applyThumbTint();
//	    }
//
//	    /**
//	     * @return the blending mode used to apply the tint to the thumb
//	     *         drawable
//	     * @attr ref android.R.styleable#SwitchTextButton_thumbTintMode
//	     * @see #setThumbTintMode(PorterDuff.Mode)
//	     */
//	    @Nullable
//	    public PorterDuff.Mode getThumbTintMode() {
//	        return mThumbTintMode;
//	    }
//
//	    private void applyThumbTint() {
//	        if (mThumbDrawable != null && (mHasThumbTint || mHasThumbTintMode)) {
//	            mThumbDrawable = mThumbDrawable.mutate();
//
//	            if (mHasThumbTint) {
//	                mThumbDrawable.setTintList(mThumbTintList);
//	            }
//
//	            if (mHasThumbTintMode) {
//	                mThumbDrawable.setTintMode(mThumbTintMode);
//	            }
//
//	            // The drawable (or one of its children) may not have been
//	            // stateful before applying the tint, so let's try again.
//	            if (mThumbDrawable.isStateful()) {
//	                mThumbDrawable.setState(getDrawableState());
//	            }
//	        }
//	    }
//
//	    /**
//	     * Specifies whether the track should be split by the thumb. When true,
//	     * the thumb's optical bounds will be clipped out of the track drawable,
//	     * then the thumb will be drawn into the resulting gap.
//	     *
//	     * @param splitTrack Whether the track should be split by the thumb
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_splitTrack
//	     */
//	    public void setSplitTrack(boolean splitTrack) {
//	        mSplitTrack = splitTrack;
//	        invalidate();
//	    }
//
//	    /**
//	     * Returns whether the track should be split by the thumb.
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_splitTrack
//	     */
//	    public boolean getSplitTrack() {
//	        return mSplitTrack;
//	    }
//
//	    /**
//	     * Returns the text displayed when the button is in the checked state.
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_textOn
//	     */
//	    public CharSequence getTextOn() {
//	        return mTextOn;
//	    }
//
//	    /**
//	     * Sets the text displayed when the button is in the checked state.
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_textOn
//	     */
//	    public void setTextOn(CharSequence textOn) {
//	        mTextOn = textOn;
//	        requestLayout();
//	    }
//
//	    /**
//	     * Returns the text displayed when the button is not in the checked state.
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_textOff
//	     */
//	    public CharSequence getTextOff() {
//	        return mTextOff;
//	    }
//
//	    /**
//	     * Sets the text displayed when the button is not in the checked state.
//	     *
//	     * @attr ref android.R.styleable#SwitchTextButton_textOff
//	     */
//	    public void setTextOff(CharSequence textOff) {
//	        mTextOff = textOff;
//	        requestLayout();
//	    }
//
//	    /**
//	     * Sets whether the on/off text should be displayed.
//	     *
//	     * @param showText {@code true} to display on/off text
//	     * @attr ref android.R.styleable#SwitchTextButton_showText
//	     */
//	    public void setShowText(boolean showText) {
//	        if (mShowText != showText) {
//	            mShowText = showText;
//	            requestLayout();
//	        }
//	    }
//
//	    /**
//	     * @return whether the on/off text should be displayed
//	     * @attr ref android.R.styleable#SwitchTextButton_showText
//	     */
//	    public boolean getShowText() {
//	        return mShowText;
//	    }
//
//	    @Override
//	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//	        if (mShowText) {
//	            if (mOnLayout == null) {
//	                mOnLayout = makeLayout(mTextOn);
//	            }
//
//	            if (mOffLayout == null) {
//	                mOffLayout = makeLayout(mTextOff);
//	            }
//	        }
//
//	        final Rect padding = mTempRect;
//	        final int thumbWidth;
//	        final int thumbHeight;
//	        if (mThumbDrawable != null) {
//	            // Cached thumb width does not include padding.
//	            mThumbDrawable.getPadding(padding);
//	            thumbWidth = mThumbDrawable.getIntrinsicWidth() - padding.left - padding.right;
//	            thumbHeight = mThumbDrawable.getIntrinsicHeight();
//	        } else {
//	            thumbWidth = 0;
//	            thumbHeight = 0;
//	        }
//
//	        final int maxTextWidth;
//	        if (mShowText) {
//	            maxTextWidth = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth())
//	                    + mThumbTextPadding * 2;
//	        } else {
//	            maxTextWidth = 0;
//	        }
//
//	        mThumbWidth = Math.max(maxTextWidth, thumbWidth);
//
//	        final int trackHeight;
//	        if (mTrackDrawable != null) {
//	            mTrackDrawable.getPadding(padding);
//	            trackHeight = mTrackDrawable.getIntrinsicHeight();
//	        } else {
//	            padding.setEmpty();
//	            trackHeight = 0;
//	        }
//
//	        // Adjust left and right padding to ensure there's enough room for the
//	        // thumb's padding (when present).
//	        int paddingLeft = padding.left;
//	        int paddingRight = padding.right;
//	        if (mThumbDrawable != null) {
//	            final Insets inset = mThumbDrawable.getOpticalInsets();
//	            paddingLeft = Math.max(paddingLeft, inset.left);
//	            paddingRight = Math.max(paddingRight, inset.right);
//	        }
//
//	        final int SwitchTextButtonWidth = Math.max(mSwitchTextButtonMinWidth,
//	                2 * mThumbWidth + paddingLeft + paddingRight);
//	        final int SwitchTextButtonHeight = Math.max(trackHeight, thumbHeight);
//	        mSwitchTextButtonWidth = SwitchTextButtonWidth;
//	        mSwitchTextButtonHeight = SwitchTextButtonHeight;
//
//	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//	        final int measuredHeight = getMeasuredHeight();
//	        if (measuredHeight < SwitchTextButtonHeight) {
//	            setMeasuredDimension(getMeasuredWidthAndState(), SwitchTextButtonHeight);
//	        }
//	    }
//
//	    /** @hide */
//	    @Override
//	    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
//	        super.onPopulateAccessibilityEventInternal(event);
//
//	        final CharSequence text = isChecked() ? mTextOn : mTextOff;
//	        if (text != null) {
//	            event.getText().add(text);
//	        }
//	    }
//
//	    private Layout makeLayout(CharSequence text) {
//	        final CharSequence transformed = (mSwitchTextButtonTransformationMethod != null)
//	                    ? mSwitchTextButtonTransformationMethod.getTransformation(text, this)
//	                    : text;
//
//	        return new StaticLayout(transformed, mTextPaint,
//	                (int) Math.ceil(Layout.getDesiredWidth(transformed, mTextPaint)),
//	                Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
//	    }
//
//	    /**
//	     * @return true if (x, y) is within the target area of the SwitchTextButton thumb
//	     */
//	    private boolean hitThumb(float x, float y) {
//	        if (mThumbDrawable == null) {
//	            return false;
//	        }
//
//	        // Relies on mTempRect, MUST be called first!
//	        final int thumbOffset = getThumbOffset();
//
//	        mThumbDrawable.getPadding(mTempRect);
//	        final int thumbTop = mSwitchTextButtonTop - mTouchSlop;
//	        final int thumbLeft = mSwitchTextButtonLeft + thumbOffset - mTouchSlop;
//	        final int thumbRight = thumbLeft + mThumbWidth +
//	                mTempRect.left + mTempRect.right + mTouchSlop;
//	        final int thumbBottom = mSwitchTextButtonBottom + mTouchSlop;
//	        return x > thumbLeft && x < thumbRight && y > thumbTop && y < thumbBottom;
//	    }
//
//	    @Override
//	    public boolean onTouchEvent(MotionEvent ev) {
//	        mVelocityTracker.addMovement(ev);
//	        final int action = ev.getActionMasked();
//	        switch (action) {
//	            case MotionEvent.ACTION_DOWN: {
//	                final float x = ev.getX();
//	                final float y = ev.getY();
//	                if (isEnabled() && hitThumb(x, y)) {
//	                    mTouchMode = TOUCH_MODE_DOWN;
//	                    mTouchX = x;
//	                    mTouchY = y;
//	                }
//	                break;
//	            }
//
//	            case MotionEvent.ACTION_MOVE: {
//	                switch (mTouchMode) {
//	                    case TOUCH_MODE_IDLE:
//	                        // Didn't target the thumb, treat normally.
//	                        break;
//
//	                    case TOUCH_MODE_DOWN: {
//	                        final float x = ev.getX();
//	                        final float y = ev.getY();
//	                        if (Math.abs(x - mTouchX) > mTouchSlop ||
//	                                Math.abs(y - mTouchY) > mTouchSlop) {
//	                            mTouchMode = TOUCH_MODE_DRAGGING;
//	                            getParent().requestDisallowInterceptTouchEvent(true);
//	                            mTouchX = x;
//	                            mTouchY = y;
//	                            return true;
//	                        }
//	                        break;
//	                    }
//
//	                    case TOUCH_MODE_DRAGGING: {
//	                        final float x = ev.getX();
//	                        final int thumbScrollRange = getThumbScrollRange();
//	                        final float thumbScrollOffset = x - mTouchX;
//	                        float dPos;
//	                        if (thumbScrollRange != 0) {
//	                            dPos = thumbScrollOffset / thumbScrollRange;
//	                        } else {
//	                            // If the thumb scroll range is empty, just use the
//	                            // movement direction to snap on or off.
//	                            dPos = thumbScrollOffset > 0 ? 1 : -1;
//	                        }
//	                        if (isLayoutRtl()) {
//	                            dPos = -dPos;
//	                        }
//	                        final float newPos = MathUtils.constrain(mThumbPosition + dPos, 0, 1);
//	                        if (newPos != mThumbPosition) {
//	                            mTouchX = x;
//	                            setThumbPosition(newPos);
//	                        }
//	                        return true;
//	                    }
//	                }
//	                break;
//	            }
//
//	            case MotionEvent.ACTION_UP:
//	            case MotionEvent.ACTION_CANCEL: {
//	                if (mTouchMode == TOUCH_MODE_DRAGGING) {
//	                    stopDrag(ev);
//	                    // Allow super class to handle pressed state, etc.
//	                    super.onTouchEvent(ev);
//	                    return true;
//	                }
//	                mTouchMode = TOUCH_MODE_IDLE;
//	                mVelocityTracker.clear();
//	                break;
//	            }
//	        }
//
//	        return super.onTouchEvent(ev);
//	    }
//
//	    private void cancelSuperTouch(MotionEvent ev) {
//	        MotionEvent cancel = MotionEvent.obtain(ev);
//	        cancel.setAction(MotionEvent.ACTION_CANCEL);
//	        super.onTouchEvent(cancel);
//	        cancel.recycle();
//	    }
//
//	    /**
//	     * Called from onTouchEvent to end a drag operation.
//	     *
//	     * @param ev Event that triggered the end of drag mode - ACTION_UP or ACTION_CANCEL
//	     */
//	    private void stopDrag(MotionEvent ev) {
//	        mTouchMode = TOUCH_MODE_IDLE;
//
//	        // Commit the change if the event is up and not canceled and the SwitchTextButton
//	        // has not been disabled during the drag.
//	        final boolean commitChange = ev.getAction() == MotionEvent.ACTION_UP && isEnabled();
//	        final boolean oldState = isChecked();
//	        final boolean newState;
//	        if (commitChange) {
//	            mVelocityTracker.computeCurrentVelocity(1000);
//	            final float xvel = mVelocityTracker.getXVelocity();
//	            if (Math.abs(xvel) > mMinFlingVelocity) {
//	                newState = isLayoutRtl() ? (xvel < 0) : (xvel > 0);
//	            } else {
//	                newState = getTargetCheckedState();
//	            }
//	        } else {
//	            newState = oldState;
//	        }
//
//	        if (newState != oldState) {
//	            playSoundEffect(SoundEffectConstants.CLICK);
//	            setChecked(newState);
//	        }
//
//	        cancelSuperTouch(ev);
//	    }
//
//	    private void animateThumbToCheckedState(boolean newCheckedState) {
//	        final float targetPosition = newCheckedState ? 1 : 0;
//	        mPositionAnimator = ObjectAnimator.ofFloat(this, THUMB_POS, targetPosition);
//	        mPositionAnimator.setDuration(THUMB_ANIMATION_DURATION);
//	        mPositionAnimator.setAutoCancel(true);
//	        mPositionAnimator.start();
//	    }
//
//	    private void cancelPositionAnimator() {
//	        if (mPositionAnimator != null) {
//	            mPositionAnimator.cancel();
//	        }
//	    }
//
//	    private boolean getTargetCheckedState() {
//	        return mThumbPosition > 0.5f;
//	    }
//
//	    /**
//	     * Sets the thumb position as a decimal value between 0 (off) and 1 (on).
//	     *
//	     * @param position new position between [0,1]
//	     */
//	    private void setThumbPosition(float position) {
//	        mThumbPosition = position;
//	        invalidate();
//	    }
//
//	    @Override
//	    public void toggle() {
//	        setChecked(!isChecked());
//	    }
//
//	    @Override
//	    public void setChecked(boolean checked) {
//	        super.setChecked(checked);
//
//	        // Calling the super method may result in setChecked() getting called
//	        // recursively with a different value, so load the REAL value...
//	        checked = isChecked();
//
//	        if (isAttachedToWindow() && isLaidOut()) {
//	            animateThumbToCheckedState(checked);
//	        } else {
//	            // Immediately move the thumb to the new position.
//	            cancelPositionAnimator();
//	            setThumbPosition(checked ? 1 : 0);
//	        }
//	    }
//
//	    @Override
//	    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//	        super.onLayout(changed, left, top, right, bottom);
//
//	        int opticalInsetLeft = 0;
//	        int opticalInsetRight = 0;
//	        if (mThumbDrawable != null) {
//	            final Rect trackPadding = mTempRect;
//	            if (mTrackDrawable != null) {
//	                mTrackDrawable.getPadding(trackPadding);
//	            } else {
//	                trackPadding.setEmpty();
//	            }
//
//	            final Insets insets = mThumbDrawable.getOpticalInsets();
//	            opticalInsetLeft = Math.max(0, insets.left - trackPadding.left);
//	            opticalInsetRight = Math.max(0, insets.right - trackPadding.right);
//	        }
//
//	        final int SwitchTextButtonRight;
//	        final int SwitchTextButtonLeft;
//	        if (isLayoutRtl()) {
//	            SwitchTextButtonLeft = getPaddingLeft() + opticalInsetLeft;
//	            SwitchTextButtonRight = SwitchTextButtonLeft + mSwitchTextButtonWidth - opticalInsetLeft - opticalInsetRight;
//	        } else {
//	            SwitchTextButtonRight = getWidth() - getPaddingRight() - opticalInsetRight;
//	            SwitchTextButtonLeft = SwitchTextButtonRight - mSwitchTextButtonWidth + opticalInsetLeft + opticalInsetRight;
//	        }
//
//	        final int SwitchTextButtonTop;
//	        final int SwitchTextButtonBottom;
//	        SwitchTextButton (getGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
//	            default:
//	            case Gravity.TOP:
//	                SwitchTextButtonTop = getPaddingTop();
//	                SwitchTextButtonBottom = SwitchTextButtonTop + mSwitchTextButtonHeight;
//	                break;
//
//	            case Gravity.CENTER_VERTICAL:
//	                SwitchTextButtonTop = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 -
//	                        mSwitchTextButtonHeight / 2;
//	                SwitchTextButtonBottom = SwitchTextButtonTop + mSwitchTextButtonHeight;
//	                break;
//
//	            case Gravity.BOTTOM:
//	                SwitchTextButtonBottom = getHeight() - getPaddingBottom();
//	                SwitchTextButtonTop = SwitchTextButtonBottom - mSwitchTextButtonHeight;
//	                break;
//	        }
//
//	        mSwitchTextButtonLeft = SwitchTextButtonLeft;
//	        mSwitchTextButtonTop = SwitchTextButtonTop;
//	        mSwitchTextButtonBottom = SwitchTextButtonBottom;
//	        mSwitchTextButtonRight = SwitchTextButtonRight;
//	    }
//
//	    @Override
//	    public void draw(Canvas c) {
//	        final Rect padding = mTempRect;
//	        final int SwitchTextButtonLeft = mSwitchTextButtonLeft;
//	        final int SwitchTextButtonTop = mSwitchTextButtonTop;
//	        final int SwitchTextButtonRight = mSwitchTextButtonRight;
//	        final int SwitchTextButtonBottom = mSwitchTextButtonBottom;
//
//	        int thumbInitialLeft = SwitchTextButtonLeft + getThumbOffset();
//
//	        final Insets thumbInsets;
//	        if (mThumbDrawable != null) {
//	            thumbInsets = mThumbDrawable.getOpticalInsets();
//	        } else {
//	            thumbInsets = Insets.NONE;
//	        }
//
//	        // Layout the track.
//	        if (mTrackDrawable != null) {
//	            mTrackDrawable.getPadding(padding);
//
//	            // Adjust thumb position for track padding.
//	            thumbInitialLeft += padding.left;
//
//	            // If necessary, offset by the optical insets of the thumb asset.
//	            int trackLeft = SwitchTextButtonLeft;
//	            int trackTop = SwitchTextButtonTop;
//	            int trackRight = SwitchTextButtonRight;
//	            int trackBottom = SwitchTextButtonBottom;
//	            if (thumbInsets != Insets.NONE) {
//	                if (thumbInsets.left > padding.left) {
//	                    trackLeft += thumbInsets.left - padding.left;
//	                }
//	                if (thumbInsets.top > padding.top) {
//	                    trackTop += thumbInsets.top - padding.top;
//	                }
//	                if (thumbInsets.right > padding.right) {
//	                    trackRight -= thumbInsets.right - padding.right;
//	                }
//	                if (thumbInsets.bottom > padding.bottom) {
//	                    trackBottom -= thumbInsets.bottom - padding.bottom;
//	                }
//	            }
//	            mTrackDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
//	        }
//
//	        // Layout the thumb.
//	        if (mThumbDrawable != null) {
//	            mThumbDrawable.getPadding(padding);
//
//	            final int thumbLeft = thumbInitialLeft - padding.left;
//	            final int thumbRight = thumbInitialLeft + mThumbWidth + padding.right;
//	            mThumbDrawable.setBounds(thumbLeft, SwitchTextButtonTop, thumbRight, SwitchTextButtonBottom);
//
//	            final Drawable background = getBackground();
//	            if (background != null) {
//	                background.setHotspotBounds(thumbLeft, SwitchTextButtonTop, thumbRight, SwitchTextButtonBottom);
//	            }
//	        }
//
//	        // Draw the background.
//	        super.draw(c);
//	    }
//
//	    @Override
//	    protected void onDraw(Canvas canvas) {
//	        super.onDraw(canvas);
//
//	        final Rect padding = mTempRect;
//	        final Drawable trackDrawable = mTrackDrawable;
//	        if (trackDrawable != null) {
//	            trackDrawable.getPadding(padding);
//	        } else {
//	            padding.setEmpty();
//	        }
//
//	        final int SwitchTextButtonTop = mSwitchTextButtonTop;
//	        final int SwitchTextButtonBottom = mSwitchTextButtonBottom;
//	        final int SwitchTextButtonInnerTop = SwitchTextButtonTop + padding.top;
//	        final int SwitchTextButtonInnerBottom = SwitchTextButtonBottom - padding.bottom;
//
//	        final Drawable thumbDrawable = mThumbDrawable;
//	        if (trackDrawable != null) {
//	            if (mSplitTrack && thumbDrawable != null) {
//	                final Insets insets = thumbDrawable.getOpticalInsets();
//	                thumbDrawable.copyBounds(padding);
//	                padding.left += insets.left;
//	                padding.right -= insets.right;
//
//	                final int saveCount = canvas.save();
//	                canvas.clipRect(padding, Op.DIFFERENCE);
//	                trackDrawable.draw(canvas);
//	                canvas.restoreToCount(saveCount);
//	            } else {
//	                trackDrawable.draw(canvas);
//	            }
//	        }
//
//	        final int saveCount = canvas.save();
//
//	        if (thumbDrawable != null) {
//	            thumbDrawable.draw(canvas);
//	        }
//
//	        final Layout SwitchTextButtonText = getTargetCheckedState() ? mOnLayout : mOffLayout;
//	        if (SwitchTextButtonText != null) {
//	            final int drawableState[] = getDrawableState();
//	            if (mTextColors != null) {
//	                mTextPaint.setColor(mTextColors.getColorForState(drawableState, 0));
//	            }
//	            mTextPaint.drawableState = drawableState;
//
//	            final int cX;
//	            if (thumbDrawable != null) {
//	                final Rect bounds = thumbDrawable.getBounds();
//	                cX = bounds.left + bounds.right;
//	            } else {
//	                cX = getWidth();
//	            }
//
//	            final int left = cX / 2 - SwitchTextButtonText.getWidth() / 2;
//	            final int top = (SwitchTextButtonInnerTop + SwitchTextButtonInnerBottom) / 2 - SwitchTextButtonText.getHeight() / 2;
//	            canvas.translate(left, top);
//	            SwitchTextButtonText.draw(canvas);
//	        }
//
//	        canvas.restoreToCount(saveCount);
//	    }
//
//	    @Override
//	    public int getCompoundPaddingLeft() {
//	        if (!isLayoutRtl()) {
//	            return super.getCompoundPaddingLeft();
//	        }
//	        int padding = super.getCompoundPaddingLeft() + mSwitchTextButtonWidth;
//	        if (!TextUtils.isEmpty(getText())) {
//	            padding += mSwitchTextButtonPadding;
//	        }
//	        return padding;
//	    }
//
//	    @Override
//	    public int getCompoundPaddingRight() {
//	        if (isLayoutRtl()) {
//	            return super.getCompoundPaddingRight();
//	        }
//	        int padding = super.getCompoundPaddingRight() + mSwitchTextButtonWidth;
//	        if (!TextUtils.isEmpty(getText())) {
//	            padding += mSwitchTextButtonPadding;
//	        }
//	        return padding;
//	    }
//
//	    /**
//	     * Translates thumb position to offset according to current RTL setting and
//	     * thumb scroll range. Accounts for both track and thumb padding.
//	     *
//	     * @return thumb offset
//	     */
//	    private int getThumbOffset() {
//	        final float thumbPosition;
//	        if (isLayoutRtl()) {
//	            thumbPosition = 1 - mThumbPosition;
//	        } else {
//	            thumbPosition = mThumbPosition;
//	        }
//	        return (int) (thumbPosition * getThumbScrollRange() + 0.5f);
//	    }
//
//	    private int getThumbScrollRange() {
//	        if (mTrackDrawable != null) {
//	            final Rect padding = mTempRect;
//	            mTrackDrawable.getPadding(padding);
//
//	            final Insets insets;
//	            if (mThumbDrawable != null) {
//	                insets = mThumbDrawable.getOpticalInsets();
//	            } else {
//	                insets = Insets.NONE;
//	            }
//
//	            return mSwitchTextButtonWidth - mThumbWidth - padding.left - padding.right
//	                    - insets.left - insets.right;
//	        } else {
//	            return 0;
//	        }
//	    }
//
//	    @Override
//	    protected int[] onCreateDrawableState(int extraSpace) {
//	        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
//	        if (isChecked()) {
//	            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
//	        }
//	        return drawableState;
//	    }
//
//	    @Override
//	    protected void drawableStateChanged() {
//	        super.drawableStateChanged();
//
//	        final int[] myDrawableState = getDrawableState();
//
//	        if (mThumbDrawable != null) {
//	            mThumbDrawable.setState(myDrawableState);
//	        }
//
//	        if (mTrackDrawable != null) {
//	            mTrackDrawable.setState(myDrawableState);
//	        }
//
//	        invalidate();
//	    }
//
//	    @Override
//	    public void drawableHotspotChanged(float x, float y) {
//	        super.drawableHotspotChanged(x, y);
//
//	        if (mThumbDrawable != null) {
//	            mThumbDrawable.setHotspot(x, y);
//	        }
//
//	        if (mTrackDrawable != null) {
//	            mTrackDrawable.setHotspot(x, y);
//	        }
//	    }
//
//	    @Override
//	    protected boolean verifyDrawable(Drawable who) {
//	        return super.verifyDrawable(who) || who == mThumbDrawable || who == mTrackDrawable;
//	    }
//
//	    @Override
//	    public void jumpDrawablesToCurrentState() {
//	        super.jumpDrawablesToCurrentState();
//
//	        if (mThumbDrawable != null) {
//	            mThumbDrawable.jumpToCurrentState();
//	        }
//
//	        if (mTrackDrawable != null) {
//	            mTrackDrawable.jumpToCurrentState();
//	        }
//
//	        if (mPositionAnimator != null && mPositionAnimator.isRunning()) {
//	            mPositionAnimator.end();
//	            mPositionAnimator = null;
//	        }
//	    }
//
//	    @Override
//	    public CharSequence getAccessibilityClassName() {
//	        return SwitchTextButton.class.getName();
//	    }
//
//	    @Override
//	    public void onProvideStructure(ViewStructure structure) {
//	        super.onProvideStructure(structure);
//	        CharSequence SwitchTextButtonText = isChecked() ? mTextOn : mTextOff;
//	        if (!TextUtils.isEmpty(SwitchTextButtonText)) {
//	            CharSequence oldText = structure.getText();
//	            if (TextUtils.isEmpty(oldText)) {
//	                structure.setText(SwitchTextButtonText);
//	            } else {
//	                StringBuilder newText = new StringBuilder();
//	                newText.append(oldText).append(' ').append(SwitchTextButtonText);
//	                structure.setText(newText);
//	            }
//	            // The style of the label text is provided via the base TextView class. This is more
//	            // relevant than the style of the (optional) on/off text on the SwitchTextButton button itself,
//	            // so ignore the size/color/style stored this.mTextPaint.
//	        }
//	    }
//
//	    /** @hide */
//	    @Override
//	    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
//	        super.onInitializeAccessibilityNodeInfoInternal(info);
//	        CharSequence SwitchTextButtonText = isChecked() ? mTextOn : mTextOff;
//	        if (!TextUtils.isEmpty(SwitchTextButtonText)) {
//	            CharSequence oldText = info.getText();
//	            if (TextUtils.isEmpty(oldText)) {
//	                info.setText(SwitchTextButtonText);
//	            } else {
//	                StringBuilder newText = new StringBuilder();
//	                newText.append(oldText).append(' ').append(SwitchTextButtonText);
//	                info.setText(newText);
//	            }
//	        }
//	    }
//
//	    private static final FloatProperty<SwitchTextButton> THUMB_POS = new FloatProperty<SwitchTextButton>("thumbPos") {
//	        @Override
//	        public Float get(SwitchTextButton object) {
//	            return object.mThumbPosition;
//	        }
//
//	        @Override
//	        public void setValue(SwitchTextButton object, float value) {
//	            object.setThumbPosition(value);
//	        }
//	    };
}

package com.etcomm.dcare.widget;

import com.etcomm.dcare.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.widget.CompoundButton;

public class TextSwitch extends CompoundButton {
	private CharSequence mTextOn;
	private CharSequence mTextOff;
	private Drawable mThumb;
	private Drawable mThumbDrawable;
	private int mTextOnColor;
	private int mTextOffColor;
	private TextPaint mTextOnPaint;
	private TextPaint mTextOffPaint;
	private int mTouchSlop;
	private int mMinFlingVelocity;

	/**
	 * Construct a new SwitchTextButtonTextButton with default styling.
	 *
	 * @param context
	 *            The Context that will determine this widget's theming.
	 */
	public TextSwitch(Context context) {
		this(context, null);
	}

	/**
	 * Construct a new SwitchTextButtonTextButton with default styling,
	 * overriding specific style attributes as requested.
	 *
	 * @param context
	 *            The Context that will determine this widget's theming.
	 * @param attrs
	 *            Specification of attributes that should deviate from default
	 *            styling.
	 */
	public TextSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);// com.android.internal.R.attr.SwitchTextButtonTextButtonStyle
	}

	/**
	 * Construct a new SwitchTextButtonTextButton with a default style
	 * determined by the given theme attribute, overriding specific style
	 * attributes as requested.
	 *
	 * @param context
	 *            The Context that will determine this widget's theming.
	 * @param attrs
	 *            Specification of attributes that should deviate from the
	 *            default styling.
	 * @param defStyleAttr
	 *            An attribute in the current theme that contains a reference to
	 *            a style resource that supplies default values for the view.
	 *            Can be 0 to not look for defaults.
	 */
	public TextSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	/**
	 * Construct a new SwitchTextButtonTextButton with a default style
	 * determined by the given theme attribute or style resource, overriding
	 * specific style attributes as requested.
	 *
	 * @param context
	 *            The Context that will determine this widget's theming.
	 * @param attrs
	 *            Specification of attributes that should deviate from the
	 *            default styling.
	 * @param defStyleAttr
	 *            An attribute in the current theme that contains a reference to
	 *            a style resource that supplies default values for the view.
	 *            Can be 0 to not look for defaults.
	 * @param defStyleRes
	 *            A resource identifier of a style resource that supplies
	 *            default values for the view, used only if defStyleAttr is 0 or
	 *            can not be found in the theme. Can be 0 to not look for
	 *            defaults.
	 */
	@SuppressLint("NewApi")
	public TextSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextSwitch, defStyleAttr, defStyleRes);
		mTextOn = a.getText(R.styleable.TextSwitch_textswitch_textOn);
		mTextOff = a.getText(R.styleable.TextSwitch_textswitch_textOff);
		mThumbDrawable = a.getDrawable(R.styleable.TextSwitch_textswitch_thumb);
		mTextOnColor = a.getColor(R.styleable.TextSwitch_textswitch_textOnColor, R.color.commen_text_color);
		mTextOffColor = a.getColor(R.styleable.TextSwitch_textswitch_textOffColor, R.color.commen_text_color);
		a.recycle();
		mTextOnPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		mTextOnPaint.setColor(mTextOnColor);
		mTextOffPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		mTextOnPaint.setColor(mTextOffColor);
		final ViewConfiguration config = ViewConfiguration.get(context);
		mTouchSlop = config.getScaledTouchSlop();
		mMinFlingVelocity = config.getScaledMinimumFlingVelocity();

		// Refresh display with current params
		refreshDrawableState();
		setChecked(isChecked());
	}

	private Layout makeONLayout(CharSequence text) {
		return new StaticLayout(text, mTextOnPaint, (int) Math.ceil(Layout.getDesiredWidth(text, mTextOnPaint)),
				Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
	}

	private Layout makeOFFLayout(CharSequence text) {
		return new StaticLayout(text, mTextOffPaint, (int) Math.ceil(Layout.getDesiredWidth(text, mTextOffPaint)),
				Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
}

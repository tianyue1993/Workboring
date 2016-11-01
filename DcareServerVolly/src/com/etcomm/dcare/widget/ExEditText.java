package com.etcomm.dcare.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.EditText;

public class ExEditText extends EditText
{

	private OnRightDrawableClickListener onRightDrawableClickListener;
	private Context context;

	public interface OnRightDrawableClickListener
	{
		public void onRightDrawableClick();
	}

	public ExEditText(Context a_context)
	{
		super(a_context);
		context = a_context;
	}

	public ExEditText(Context a_context, AttributeSet a_attributeSet)
	{
		super(a_context, a_attributeSet);
		context = a_context;
		if (!this.isInEditMode())
		{
			DisplayMetrics displayMetrics = new DisplayMetrics();
			((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			SCALE = displayMetrics.density;
		}
	}

	private Drawable dRight;
	private Drawable dLeft;
	private int touchableDRightWidth = 0;
	private float SCALE;
	private int rightCount;
	private int leftCount;

	public void setRightDrawable(Drawable left, Drawable right)
	{
		if (right != null)
		{
			dRight = right;

			// rightCount = rCount;
			// leftCount = lCount;
			// dRight.setBounds((int) (-5 * SCALE), 0, 30, 30);// ??? i don't
			// know why? but it can't display correctly.
//			dRight.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
			dRight.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
			// touchableDRightWidth = (int) ((20 + right.getMinimumWidth()) *
			// SCALE);
			touchableDRightWidth = (int) (right.getMinimumWidth());
		} else
		{
			dRight = right;
		}
		if (left != null)
		{
			dLeft = left;
			dLeft.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
		} else
		{
			dLeft = left;
		}
		super.setCompoundDrawables(left, null, right, null);
	}

	public void setRightDrawableOnClickListener(OnRightDrawableClickListener onClickListener)
	{
		this.onRightDrawableClickListener = onClickListener;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP && dRight != null)
		{
			final int x = (int) event.getX();
			int width = getWidth() - touchableDRightWidth;
			if (x+20 > (width))
			{
				onRightDrawableClickListener.onRightDrawableClick();
				event.setAction(MotionEvent.ACTION_CANCEL);// use this to
															// prevent the
															// keyboard from
															// coming up
			}
		}
		return super.onTouchEvent(event);
	}

	public void setMaxLength(int max)
	{
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(max);
		setFilters(FilterArray);
	}
}
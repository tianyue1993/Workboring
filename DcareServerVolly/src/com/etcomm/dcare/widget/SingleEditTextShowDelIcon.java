package com.etcomm.dcare.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class SingleEditTextShowDelIcon extends EditText {

//	@SuppressLint("NewApi")
//	public SingleEditTextShowDelIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		// TODO Auto-generated constructor stub
//	}
	public SingleEditTextShowDelIcon(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	public SingleEditTextShowDelIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public SingleEditTextShowDelIcon(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		this.setLines(1);
	
	}

}

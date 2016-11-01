package com.etcomm.dcare.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class ObservableHorizontalScrollView extends HorizontalScrollView {
	protected String tag = getClass().getSimpleName();
	private Runnable scrollerTask;
	private int intitPosition;
	private int newCheck = 50;
	private OnScrollStopListner onScrollstopListner;

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ObservableHorizontalScrollView(Context context) {
		super(context);
		init();

	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int newPosition = getScrollY();
			Log.i(tag, "newPosition: " + newPosition);
			if (intitPosition - newPosition == 0) {
				if (onScrollstopListner == null) {
					return;
				}
//				onScrollstopListner.onScrollChange(getScrollY());
			} else {
				intitPosition = getScrollY();
				postDelayed(scrollerTask, newCheck);
			}
		};
	};

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
//			mHandler.sendEmptyMessage(0);
			break;
		case MotionEvent.ACTION_MOVE:
//			mHandler.sendEmptyMessage(0);
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		Log.i(tag, "onScrollChanged: l:"+l+" t: "+t+" oldl: "+oldl +" oldt "+oldt);
		onScrollstopListner.onScrollChange(l);
//		if(l<=0){
//			Log.i(tag, "l<=0");
//			onScrollstopListner.onScrollChange(0);
////			postDelayed(scrollerTask, newCheck);
//		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	private void init() {
		// TODO Auto-generated method stub
//		scrollerTask = new Runnable() {
//			@Override
//			public void run() {
//				int newPosition = getScrollX();
//				Log.i(tag, "newPosition: " + newPosition);
//				if (intitPosition - newPosition == 0) {
//					if (onScrollstopListner == null) {
//						return;
//					}
//					onScrollstopListner.onScrollChange(getScrollX());
//				} else {
//					intitPosition = getScrollX();
//					postDelayed(scrollerTask, newCheck);
//				}
//			}
//		};
	}

	public interface OnScrollStopListner {
		void onScrollChange(int index);
	}

	public void setOnScrollStopListner(OnScrollStopListner listner) {
		onScrollstopListner = listner;
	}

	public void startScrollerTask() {
		intitPosition = getScrollX();
//		postDelayed(scrollerTask, newCheck);
	}
}

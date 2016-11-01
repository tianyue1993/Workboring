package com.etcomm.dcare.widget;


import java.util.logging.MemoryHandler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
	private Runnable scrollerTask;
	private int intitPosition;
	private int newCheck = 50;
	private OnScrollStopListner onScrollstopListner;
	protected String tag = getClass().getSimpleName();

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int newPosition = getScrollY();
			Log.i(tag , "newPosition: "+newPosition);
			if (intitPosition - newPosition == 0) {
				if (onScrollstopListner == null) {
					return;
				}
				onScrollstopListner.onScrollChange(getScrollY());
			} else {
				intitPosition = getScrollY();
				postDelayed(scrollerTask, newCheck);
			}
		};
	};
	/**
	 * 在事件分发其中处理触摸事件
	 * 根据android中事件分发的机制判断，个人觉得把事件处理逻辑写在分发器中比写在onTouchEvent中好些，
	 * 因为在其子View没有接收到该触摸事件之前自己就处理了触摸事件。
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				mHandler.sendEmptyMessage(0);
				break;
			case MotionEvent.ACTION_MOVE:
				mHandler.sendEmptyMessage(0);
				break;
			default:
				break;
		}

		return super.dispatchTouchEvent(ev);
	}
	private void init() {
		// TODO Auto-generated method stub
		scrollerTask = new Runnable() {
			@Override
			public void run() {
				int newPosition = getScrollY();
				Log.i(tag , "newPosition: "+newPosition);
				if (intitPosition - newPosition == 0) {
					if (onScrollstopListner == null) {
						return;
					}
					onScrollstopListner.onScrollChange(getScrollY());
				} else {
					intitPosition = getScrollY();
					postDelayed(scrollerTask, newCheck);
				}
			}
		};
	}

	public ObservableScrollView(Context context, AttributeSet attrs,
								int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ObservableScrollView(Context context) {
		super(context);
		init();
	}

	public interface OnScrollStopListner {
		void onScrollChange(int index);
	}

	public void setOnScrollStopListner(OnScrollStopListner listner) {
		onScrollstopListner = listner;
	}

	public void startScrollerTask() {
		intitPosition = getScrollY();
		postDelayed(scrollerTask, newCheck);
	}
}

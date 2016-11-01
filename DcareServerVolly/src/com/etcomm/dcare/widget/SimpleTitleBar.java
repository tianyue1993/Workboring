package com.etcomm.dcare.widget;

import com.etcomm.dcare.R;
import com.etcomm.dcare.util.DensityUtil;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SimpleTitleBar extends RelativeLayout {

	/**
	 * 基类右边导航
	 */
	private TextView rightTextView;
	private TextView title;
	private ImageView leftImage;
	public SimpleTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public SimpleTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public SimpleTitleBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				DensityUtil.dip2px(context, getResources().getDimension(R.dimen.height_alert_title)));
		setLayoutParams(lp);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View v = mInflater.inflate(R.layout.simple_system_title, null);
		title = (TextView) v.findViewById(R.id.tv_title);
		leftImage = (ImageView) v.findViewById(R.id.leftimage);
		rightTextView = (TextView) v.findViewById(R.id.rightTextView);
		rightTextView.setVisibility(View.GONE);
		addView(v, lp);
		
	}
	public void setLeftLl(OnClickListener click) {
		if (leftImage != null) {
			leftImage.setVisibility(View.VISIBLE);
			if (click != null) {
				leftImage.setOnClickListener(click);
			}
		}
	}

	public void setRightTextView(OnClickListener clickListener,String content) {
		if (rightTextView != null) {
			rightTextView.setVisibility(View.VISIBLE);
			rightTextView.setText(content);
			if (clickListener != null) {
				rightTextView.setOnClickListener(clickListener);
			}
		}
	}

	public void setRightTextContent(String string){
		if (rightTextView != null) {
			rightTextView.setVisibility(View.VISIBLE);
			rightTextView.setText(string);
		}
	}

	public void setTitle(String message) {
		if (title != null) {
			title.setText(message);

		}
	}
	public void setTitle(int message) {
		if (title != null) {
			title.setText(message);

		}
	}
	public void setLeftINVisable() {
		// TODO Auto-generated method stub
		if (leftImage != null) {
			leftImage.setVisibility(View.GONE);
			
		}
	}
}

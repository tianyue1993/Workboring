package com.etcomm.dcare.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.util.DensityUtil;

public class SimpleTitleBarWithRightText extends RelativeLayout {

	private TextView title;
	private TextView rightopt;
	private ImageView leftImage, rightimage;

	public SimpleTitleBarWithRightText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SimpleTitleBarWithRightText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SimpleTitleBarWithRightText(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, getResources().getDimension(R.dimen.height_alert_title)));
		setLayoutParams(lp);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View v = mInflater.inflate(R.layout.simple_system_title_withright, null);
		title = (TextView) v.findViewById(R.id.tv_title);
		rightopt = (TextView) v.findViewById(R.id.title_right_tv);
		leftImage = (ImageView) v.findViewById(R.id.leftimage);
		rightimage = (ImageView) v.findViewById(R.id.rightimage);
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

	public void setRightClick(OnClickListener click) {
		if (rightopt != null) {
			rightopt.setVisibility(View.VISIBLE);
			if (click != null) {
				rightopt.setOnClickListener(click);
			}
		}
	}

	public void setTitle(String message) {
		if (title != null) {
			title.setText(message);

		}
	}

	public void setRightText(String message) {
		if (rightopt != null) {
			rightopt.setText(message);

		}
	}

	public void setRightImage(int id) {
		if (rightimage != null) {
			rightimage.setImageResource(id);
			rightimage.setVisibility(View.VISIBLE);
		}
	}

	public void setLeftINVisable() {
		// TODO Auto-generated method stub
		if (leftImage != null) {
			leftImage.setVisibility(View.GONE);

		}
	}

	public void setRightINVisable() {
		// TODO Auto-generated method stub
		if (rightopt != null) {
			rightopt.setVisibility(View.GONE);

		}
	}

	public TextView getRightText() {
		if (rightopt != null) {
			return rightopt;
		}
		return null;
	}
}

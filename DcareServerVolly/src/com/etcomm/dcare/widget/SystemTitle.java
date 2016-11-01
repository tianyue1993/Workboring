package com.etcomm.dcare.widget;

import com.etcomm.dcare.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 
 * @author iexpressbox
 *
 */
public class SystemTitle extends LinearLayout {



//	@SuppressLint("NewApi")
//	public SystemTitle(Context context, AttributeSet attrs, int defStyleAttr,
//			int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		init(context);
//	}
	@SuppressLint("NewApi")
	public SystemTitle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	public SystemTitle(Context context) {
		super(context);
		init(context);
	}

	public SystemTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private TextView title = null;
	private ImageView leftImage = null;
	private View im_right_icon = null;
	private ImageView iv_title_right = null;
	private View left_ll = null;
	private TextView rightText = null;
	private OnClickListener tClick = null;

	private ProgressBar pbConnecting;


	/**
	 * @brief 
	 * 
	 * @param context
	 */
	private void init(Context context) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View v = mInflater.inflate(R.layout.system_title, null);
		title = (TextView) v.findViewById(R.id.tv_title);
		leftImage = (ImageView) v.findViewById(R.id.title_logo);
		im_right_icon = (View) v.findViewById(R.id.im_right_icon);
		iv_title_right = (ImageView) v.findViewById(R.id.iv_title_right);
		left_ll = v.findViewById(R.id.title_left_ll);
		if (title != null) {
			title.setOnClickListener(titleClick);
		}
		
		rightText = (TextView) v.findViewById(R.id.tv_right_action);

		addView(v);

	}

	/**
	 * 鑾峰彇title淇℃�?
	 * 
	 * @return
	 */
	public String getTitleMessage() {
		if (title != null)
			return title.getText().toString();
		else
			return "";

	}

	/**
	 * 璁剧疆title
	 * 
	 * @param title
	 */
	public void setTitle(String message) {
		if (title != null) {
			title.setText(message);

		}
	}

	/**
	 * @brief 璁剧疆title鐨勭偣鍑讳簨浠�
	 * 
	 * @param click
	 */
	public void setTitleClick(OnClickListener click) {
		tClick = click;
	}

	private OnClickListener titleClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (tClick != null) {
				tClick.onClick(v);
			}
		}
	};

	public void showPBConnecting(boolean isShow) {
		if (isShow) {
			if (pbConnecting != null) {
				pbConnecting.setVisibility(View.VISIBLE);
			}
		} else {
			if (pbConnecting != null) {
				pbConnecting.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * @brief 璁剧疆鍙宠竟鍥剧墖鍜�?偣鍑讳簨浠�
	 * 
	 * @param res
	 * @param click
	 */
//	public void setLeftImage(int res, OnClickListener click) {
//		if (leftImage != null) {
//			leftImage.setVisibility(View.VISIBLE);
//			leftImage.setImageResource(res);
//			if (click != null) {
//				leftImage.setOnClickListener(click);
//			}
//		}
//	}

	/**
	 * @brief 璁剧疆宸﹁竟鍖哄煙鐨勭偣鍑讳簨浠�?
	 * 
	 * @param click
	 */
	public void setLeftLl(OnClickListener click) {
		if (left_ll != null) {
			left_ll.setVisibility(View.VISIBLE);
			if (click != null) {
				left_ll.setOnClickListener(click);
			}
		}
	}

	/**
	 * @brief 璁剧疆宸﹁竟鍖哄煙鐨勭偣鍑讳簨浠�?
	 * 
	 * @param res
	 * @param click
	 */
	public void setLeftLl(int res, OnClickListener click) {
		if (leftImage != null) {
			leftImage.setVisibility(View.VISIBLE);
			leftImage.setImageResource(res);
			if (click != null) {
				left_ll.setOnClickListener(click);
			}
		}
	}
	
	/**
	 * 璁剧疆鍙充晶鎸夐挳鐐瑰嚮浜嬩�?
	 * @param res
	 * @param click
	 */
	public void setRightText(int res, OnClickListener click)
	{
		if(rightText != null)
		{
			rightText.setVisibility(View.VISIBLE);
			rightText.setText(res);
			rightText.setOnClickListener(click);
		}
	}
	public void setRightDrawable(int res, OnClickListener click)
	{
		if(iv_title_right != null)
		{
			iv_title_right.setVisibility(View.VISIBLE);
			iv_title_right.setImageResource(res);
			iv_title_right.setOnClickListener(click);
		}
	}
	public void hideRightImage(){
		if(iv_title_right!= null){
			iv_title_right.setVisibility(View.INVISIBLE);
		}
	}
	public void hideRightText(){
		if(rightText!= null){
			rightText.setVisibility(View.INVISIBLE);
		}
	}
}

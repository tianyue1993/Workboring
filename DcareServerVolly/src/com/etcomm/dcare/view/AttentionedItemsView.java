package com.etcomm.dcare.view;

import com.etcomm.dcare.R;
import com.etcomm.dcare.netresponse.AttentionedItems;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class AttentionedItemsView extends DcareItemView {

	TextView attention_count;
	TextView attention_topic;
	private AttentionedItems mInfo;
	public AttentionedItemsView(Context context) {
		super(context);
	}
	public AttentionedItemsView(Context context, AttentionedItems attentionitem) {
		super(context);
		this.mInfo = attentionitem;
		init();
	}

	public void setInfo(AttentionedItems mInfo) {
		this.mInfo = mInfo;
		setUI();
	}
	public AttentionedItems getInfo() {
		return mInfo;
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		Log.i(tag, "init: ");
		removeAllViews();
		LayoutInflater factory = LayoutInflater.from(mContext);

		View convertView = factory.inflate(R.layout.around_item_attentioned, null);
		convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		addView(convertView);
		attention_count = (TextView) convertView.findViewById(R.id.attention_count);
		attention_topic = (TextView) convertView.findViewById(R.id.attention_topic);
		setUI();
	}
	private void setUI() {
		if (mInfo != null) {
			Log.i(tag, "mInfo: "+mInfo.toString());
			attention_topic.setText(mInfo.getName()+"");
			attention_count.setText(mInfo.getDiscussion_number()+"人关注");
		}
	}

}

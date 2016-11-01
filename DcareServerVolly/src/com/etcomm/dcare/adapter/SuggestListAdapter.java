package com.etcomm.dcare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.netresponse.SuggestItems;

import java.util.ArrayList;

public class SuggestListAdapter extends DcareBaseAdapter<SuggestItems> {
	private static final String tag = "SuggestListAdapter";
	private int height;

	public SuggestListAdapter(Context context, ArrayList<SuggestItems> mSuggestItems, int height) {
		super(context);
		this.mList = mSuggestItems;
		this.height = height;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SuggestItems mInfo = getItem(position);
		// if (mInfo != null) {
		Log.i(tag, "mInfo : " + mInfo.toString());
		int type = Integer.parseInt(mInfo.getType());
		switch (type) {// // 类型 （1:健康资讯 2:企业福利
			// // 3：活动）
			case 1:
				convertView = mInflater.inflate(R.layout.suggest_item_health, null);
				ImageView suggest_image = (ImageView) convertView.findViewById(R.id.suggest_image);
//				RelativeLayout.LayoutParams linearParams1 = (RelativeLayout.LayoutParams) suggest_image.getLayoutParams();
//				linearParams1.height = height / 3;
				imageLoader.displayImage(mInfo.getImage(), suggest_image);
				TextView healthtopic = (TextView) convertView.findViewById(R.id.healthtopic);
				healthtopic.setText(mInfo.getTitle());
				TextView readingamount = (TextView) convertView.findViewById(R.id.readingamount);
				readingamount.setText(mInfo.getPv());
				break;
			case 2:
				convertView = mInflater.inflate(R.layout.suggest_item_welfare, null);
				ImageView welfareimage = (ImageView) convertView.findViewById(R.id.suggest_image);
//				RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) welfareimage.getLayoutParams();
//				linearParams2.height = height / 3;
				imageLoader.displayImage(mInfo.getImage(), welfareimage);
				TextView welfaretopic = (TextView) convertView.findViewById(R.id.welfaretopic);
				welfaretopic.setText(mInfo.getTitle());
				break;
			case 3:
				convertView = mInflater.inflate(R.layout.suggest_item_activity, null);
				ImageView atctivityImage = (ImageView) convertView.findViewById(R.id.suggest_image);
//				RelativeLayout.LayoutParams linearParams3 = (RelativeLayout.LayoutParams) atctivityImage.getLayoutParams();
//				linearParams3.height = height / 3;
				imageLoader.displayImage(mInfo.getImage(), atctivityImage);
				TextView suggest_activity_topic = (TextView) convertView.findViewById(R.id.suggest_activity_topic);
				suggest_activity_topic.setText(mInfo.getTitle());
				TextView participation = (TextView) convertView.findViewById(R.id.participation);
				participation.setText(mInfo.getNumber() + " 人参与");
				TextView activity_status = (TextView) convertView.findViewById(R.id.activity_status);
				activity_status.setText(mInfo.getStatus());
				TextView daterange = (TextView) convertView.findViewById(R.id.daterange);
				daterange.setText(mInfo.getStart_at() + "-" + mInfo.getEnd_at());
				break;
		}

		return convertView;
	}

}

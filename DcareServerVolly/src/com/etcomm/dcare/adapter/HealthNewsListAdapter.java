package com.etcomm.dcare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.netresponse.HealthNewsItems;

public class HealthNewsListAdapter extends DcareBaseAdapter<HealthNewsItems> {

	public HealthNewsListAdapter(Context context, ArrayList<HealthNewsItems> mSuggestItems) {
		// TODO Auto-generated constructor stub
		super(context);
		this.mList = mSuggestItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_healht_news, null);
			viewHolder.item_healthnews_image = (ImageView) convertView.findViewById(R.id.item_healthnews_image);
			viewHolder.item_healthnews_title = (TextView) convertView.findViewById(R.id.item_healthnews_title);
			viewHolder.item_healthnews_sumary = (TextView) convertView.findViewById(R.id.item_healthnews_sumary);
			viewHolder.item_healthnews_alreadyread = (TextView) convertView.findViewById(R.id.item_healthnews_alreadyread);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i(tag, "getview");
		final HealthNewsItems mInfo = getItem(position);
		if (null != mInfo) {
			viewHolder.item_healthnews_title.setText(mInfo.getTitle());
			viewHolder.item_healthnews_sumary.setText(mInfo.getDesc());
			viewHolder.item_healthnews_alreadyread.setText(mInfo.getPv() + "");
			Log.i(tag, "Image: "+mInfo.getImage());
			imageLoader.displayImage(mInfo.getImage(), viewHolder.item_healthnews_image);
		}
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView item_healthnews_image;
		TextView item_healthnews_title;
		TextView item_healthnews_sumary;
		TextView item_healthnews_alreadyread;
	}
}

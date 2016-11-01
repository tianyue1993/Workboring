package com.etcomm.dcare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.netresponse.MineSportsItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MineSportsListAdapter<T> extends DcareBaseAdapter<MineSportsItem> {

	private DisplayImageOptions options;

	public MineSportsListAdapter(Context context, ArrayList<MineSportsItem> mList) {
		super(context);
		this.mList = mList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.suggest_item_activity, null);
			viewHolder.atctivityImage = (ImageView) convertView.findViewById(R.id.suggest_image);
			viewHolder.atctivityImage = (ImageView) convertView.findViewById(R.id.suggest_image);
			RelativeLayout.LayoutParams linearParams1 = (RelativeLayout.LayoutParams) viewHolder.atctivityImage.getLayoutParams();
			linearParams1.height = SharePreferencesUtil.getHeight(mContext) / 3;
			viewHolder.suggest_activity_topic = (TextView) convertView.findViewById(R.id.suggest_activity_topic);
			viewHolder.daterange = (TextView) convertView.findViewById(R.id.daterange);
			viewHolder.participation = (TextView) convertView.findViewById(R.id.participation);
			viewHolder.activity_status = (TextView) convertView.findViewById(R.id.activity_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final MineSportsItem mInfo = getItem(position);
		viewHolder.participation.setText(mInfo.getNumber() + " 人参与");
		viewHolder.suggest_activity_topic.setText(mInfo.getTitle());
		viewHolder.activity_status.setText(mInfo.getStatus());
		imageLoader.displayImage(mInfo.getImage(), viewHolder.atctivityImage,options);
		viewHolder.daterange.setText(mInfo.getStart_at() + "-" + mInfo.getEnd_at());
		return convertView;
	}

	private static class ViewHolder {
		ImageView atctivityImage;
		TextView suggest_activity_topic;
		TextView daterange;
		TextView participation;
		TextView activity_status;
	}
}

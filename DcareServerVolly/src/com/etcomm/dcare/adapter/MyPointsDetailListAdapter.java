package com.etcomm.dcare.adapter;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.netresponse.MyPointsDetailItems;

public class MyPointsDetailListAdapter extends DcareBaseAdapter<MyPointsDetailItems> {

//	private LoadingDialog dialog;

	public MyPointsDetailListAdapter(Context context, ArrayList<MyPointsDetailItems> list) {
		super(context);
		this.mList = list;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_pointsdetail, null);
			viewHolder.points_image = (ImageView) convertView.findViewById(R.id.points_image);
			viewHolder.points_name = (TextView) convertView.findViewById(R.id.points_name);
			viewHolder.points_time = (TextView) convertView.findViewById(R.id.points_time);
			viewHolder.points_dec = (TextView) convertView.findViewById(R.id.points_dec);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i(tag, "getview");
		final MyPointsDetailItems mInfo = getItem(position);
		if (null != mInfo) {
			viewHolder.points_name.setText(mInfo.getType());
			DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss");
			DateTime createat = DateTime.parse(mInfo.getCreated_at(),format);
			viewHolder.points_time.setText(""+createat.toString("yyyy.MM.dd"));//库存改成兑换时间
			viewHolder.points_dec.setText(mInfo.getDescription()+" 分");
		}
		return convertView;
	}


	private static class ViewHolder {
		ImageView points_image;
		TextView points_name;
		TextView points_time;
		TextView points_dec;
	}
}

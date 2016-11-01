package com.etcomm.dcare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.netresponse.CompanyWelfareItems;

public class CompanyWelfareListAdapter extends DcareBaseAdapter<CompanyWelfareItems> {

	public CompanyWelfareListAdapter(Context context, ArrayList<CompanyWelfareItems> mSuggestItems) {
		// TODO Auto-generated constructor stub
		super(context);
		this.mList = mSuggestItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.suggest_item_welfare, null);
			viewHolder.suggest_image = (ImageView) convertView.findViewById(R.id.suggest_image);
			RelativeLayout.LayoutParams linearParams1 = (RelativeLayout.LayoutParams) viewHolder.suggest_image.getLayoutParams();
			linearParams1.height = SharePreferencesUtil.getHeight(mContext) / 3;
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.welfaretopic = (TextView) convertView.findViewById(R.id.welfaretopic);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i(tag, "getview");
		final CompanyWelfareItems mInfo = getItem(position);
		if (null != mInfo) {
			imageLoader.displayImage(mInfo.getImage(), viewHolder.suggest_image);
			viewHolder.welfaretopic.setText(mInfo.getTitle());
			viewHolder.tv_time.setText(mInfo.getStart_at() + "——" + mInfo.getEnd_at());
			imageLoader.displayImage(mInfo.getImage(), viewHolder.suggest_image);
			imageLoader.displayImage(mInfo.getImage(), viewHolder.suggest_image);
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView suggest_image;
		TextView welfaretopic;
		TextView tv_time;// 福利创建时间
	}
}

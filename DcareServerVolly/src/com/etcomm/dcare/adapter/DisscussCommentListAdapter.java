package com.etcomm.dcare.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.netresponse.DisscussCommentItems;
import com.etcomm.dcare.widget.CircleImageView;

public class DisscussCommentListAdapter extends DcareBaseAdapter<DisscussCommentItems> {

	public interface DisscussCommentResponseClickListener{
		public void onClick(DisscussCommentItems mInfo);
	}
	public  DisscussCommentResponseClickListener mListener;

	public DisscussCommentListAdapter(Context mContext, List<DisscussCommentItems> list, DisscussCommentResponseClickListener disscussCommentResponseClickListener) {
		// TODO Auto-generated constructor stub
		super(mContext);
		this.mList = list;
		this.mListener = disscussCommentResponseClickListener;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.disscusscomment_item, null);
			viewHolder.disscuss_useravator = (CircleImageView) convertView.findViewById(R.id.disscuss_useravator);
			viewHolder.disscuss_user_depart_tv = (TextView) convertView.findViewById(R.id.disscuss_user_depart_tv);
			viewHolder.disscuss_comment_user_tv = (TextView) convertView.findViewById(R.id.disscuss_comment_user_tv);
			viewHolder.disscuss_time_tv = (TextView) convertView.findViewById(R.id.disscuss_time_tv);
			viewHolder.disscuss_response_tv = (TextView) convertView.findViewById(R.id.disscuss_response_tv);
			viewHolder.disscuss_user_name_tv = (TextView) convertView.findViewById(R.id.disscuss_user_name_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i(tag, "getview");
		final DisscussCommentItems mInfo = getItem(position);
		if (null != mInfo) {
			viewHolder.disscuss_user_depart_tv.setText(mInfo.getComment());
			viewHolder.disscuss_time_tv.setText(mInfo.getCreated_at() + "");
			viewHolder.disscuss_user_name_tv.setText(mInfo.getNick_name() + "");
			if(!StringUtils.isEmpty(mInfo.getBe_reply())){
				viewHolder.disscuss_comment_user_tv.setText("回复"+mInfo.getBe_reply()+":");
			}
			viewHolder.disscuss_response_tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mListener!=null){
						mListener.onClick(mInfo);
					}
				}
			});
			imageLoader.displayImage(mInfo.getAvatar(), viewHolder.disscuss_useravator);
		}
		return convertView;
	}

	private static class ViewHolder {
		CircleImageView disscuss_useravator;
		TextView disscuss_user_depart_tv;
		TextView disscuss_comment_user_tv;
		TextView disscuss_time_tv;
		TextView disscuss_response_tv;
		TextView disscuss_user_name_tv;
	}
}

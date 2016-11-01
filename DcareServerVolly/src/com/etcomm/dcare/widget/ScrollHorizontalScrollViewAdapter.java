package com.etcomm.dcare.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.etcomm.dcare.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ScrollHorizontalScrollViewAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	// private List<Integer> mDatas;
	private ArrayList<String> mDatas;
	ImageLoader imageloader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

	// public ScrollHorizontalScrollViewAdapter(Context context, List<Integer>
	// mDatas)
	// {
	// this.mContext = context;
	// mInflater = LayoutInflater.from(context);
	// this.mDatas = mDatas;
	// }

	public ScrollHorizontalScrollViewAdapter(Context mContext2, ArrayList<String> mMan) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext2;
		mInflater = LayoutInflater.from(mContext2);
		this.mDatas = mMan;
	}

	public int getCount() {
		return mDatas.size();
	}

	public Object getItem(int position) {
		return mDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_index_gallery_item, parent, false);
			viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_index_gallery_item_image);
			// viewHolder.mText = (TextView) convertView
			// .findViewById(R.id.id_index_gallery_item_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// viewHolder.mImg.setImageResource(mDatas.get(position));
		imageloader.displayImage(mDatas.get(position), viewHolder.mImg);
		// viewHolder.mText.setText("some info ");
		return convertView;
	}

	private class ViewHolder {
		ImageView mImg;
		// TextView mText;
	}

}

package com.etcomm.dcare.adapter;

import java.util.ArrayList;

import com.etcomm.dcare.R;
import com.etcomm.dcare.util.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AddTopicDisscussPhotoGridAdapter extends BaseAdapter {
	public interface DeleteOnClickListener {
		public void delete(int position,String str);
	}
	private DeleteOnClickListener deleteOnClickListener;
	private Context mContext;
	private ArrayList<String> medilist;
	private int width;
	private LayoutInflater infalter;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public AddTopicDisscussPhotoGridAdapter(Context mContext, ArrayList<String> medilist,int scwidth,DeleteOnClickListener deleteOnClickListener) {

		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		infalter = LayoutInflater.from(mContext);
		this.medilist = medilist;
		this.width = (scwidth-2*DensityUtil.dip2px(mContext, 35))/3;
		this.deleteOnClickListener = deleteOnClickListener;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_picture_loading)
		.showImageForEmptyUri(R.drawable.ic_picture_loading)
		.showImageOnFail(R.drawable.ic_picture_loading)
		.cacheOnDisc(true)
		.cacheInMemory(true)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
//		imageLoader.
		imageLoader.clearMemoryCache();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return medilist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return medilist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		String pic = medilist.get(position);
		if(pic.equals("0")){
			return 1;
		}else{
			return 0;
		}
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewholder;
		String pic = medilist.get(position);
		final String picstr = pic;
		if(convertView== null){
			viewholder = new ViewHolder();
			View view = infalter.inflate(R.layout.layout_photo_griditem, null);
			view.setLayoutParams(new AbsListView.LayoutParams(width,width));
			convertView = view;
			viewholder.imageview = (ImageView) view.findViewById(R.id.iv_photo_lpsi);
			viewholder.imageview_delete = (ImageView) view.findViewById(R.id.iv_photo_delete_lpsi);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		if(picstr.equals("0")){
//			viewholder.imageview.setBackgroundResource(R.drawable.add_picture);
			viewholder.imageview.setImageDrawable(null);
			viewholder.imageview.setImageResource(R.drawable.add_picture_mid_s);
			viewholder.imageview_delete.setVisibility(View.GONE);
//			imageLoader.displayImage("file://"+picstr, viewholder.imageview);
		}else{
			imageLoader.displayImage("file://"+picstr, viewholder.imageview,options);
			viewholder.imageview_delete.setVisibility(View.VISIBLE);
			viewholder.imageview_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (deleteOnClickListener != null) {
						deleteOnClickListener.delete(position, picstr);
						notifyDataSetChanged();
					}
				}
			});
		}
		
		return convertView;
	}
	public class ViewHolder{
		ImageView imageview;
		ImageView imageview_delete;
	}
}

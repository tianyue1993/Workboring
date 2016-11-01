package com.etcomm.dcare.adapter;

import java.util.List;

import com.etcomm.dcare.R;
import com.etcomm.dcare.netresponse.DisscussPhotosItems;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class DisscussPhotoGridAdapter extends DcareBaseAdapter<DisscussPhotosItems> {

	private int picwidth;

	public DisscussPhotoGridAdapter(Context mContext, List<DisscussPhotosItems> photos, int mWidth) {
		super(mContext);
		this.mList = photos;
		this.picwidth = mWidth;
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
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DisscussPhotosItems mInfo = getItem(position);
		ViewHolder holder;
		if(convertView == null){
			View view = mInflater.inflate(R.layout.disscussphoto_item, null);
//			view.setLayoutParams(new LayoutParams(picwidth, picwidth));
			view.setLayoutParams(new AbsListView.LayoutParams(picwidth,picwidth));
//			ImageView view = new ImageView(mContext);
			convertView =view;
			holder = new ViewHolder();
			holder.photo_image = (ImageView) convertView.findViewById(R.id.photo_image);//view;
			holder.photo_image.setScaleType(ScaleType.CENTER_CROP);
			holder.photo_image.setLayoutParams(new AbsListView.LayoutParams(picwidth, picwidth));
//			holder.photo_image.setMaxWidth(picwidth);
//			holder.photo_image.setMinimumWidth(picwidth);
//			holder.photo_image.setMaxHeight(picwidth);
//			holder.photo_image.setMinimumHeight(picwidth);
//			convertView.setMinimumHeight(picwidth);
//			convertView.setMinimumWidth(picwidth);
//			convertView.setLayoutParams(new LayoutParams(picwidth, picwidth));
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(mInfo.getImage(), holder.photo_image,options);
		return convertView;
	}

	private static class ViewHolder {
		ImageView photo_image;
	}
}

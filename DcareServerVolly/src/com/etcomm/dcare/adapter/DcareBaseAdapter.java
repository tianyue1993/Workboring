package com.etcomm.dcare.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.config.Preferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DcareBaseAdapter<T> extends BaseAdapter {
	protected final String tag = getClass().getSimpleName();
	protected List<T> mList = null;
	protected LayoutInflater mInflater = null;
	protected Context mContext = null;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public DcareBaseAdapter(Context context)
	{
		super();
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mList = new ArrayList<T>();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.login_hedaer).showImageForEmptyUri(R.drawable.login_hedaer)
				.showImageOnFail(R.drawable.login_hedaer).cacheInMemory(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(Preferences.DEFAULT_ROUND_PIX)).build();
	}

	public MemberListAdapter setDatas(List<T> list)
	{
		if (list != null)
		{
			mList.clear();
			mList.addAll(list);
		}
		return null;
	}

	@Override
	public int getCount()
	{
		if (mList != null)
		{
			return mList.size();
		}
		return 0;
	}

	@Override
	public T getItem(int position)
	{
		if (mList != null && position < mList.size()) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return null;
	}

	/**
	 * @brief 图片加载第一次显示监听器
	 *
	 *
	 */
	static class AnimateFirstDisplayListener extends SimpleImageLoadingListener
	{

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
		{
			if (loadedImage != null)
			{
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay)
				{
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}

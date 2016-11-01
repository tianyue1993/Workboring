package com.etcomm.dcare.view;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.config.Preferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DcareItemView extends LinearLayout {
	protected final String tag = getClass().getSimpleName();
	protected Context mContext;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public DcareItemView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		mContext = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new RoundedBitmapDisplayer(Preferences.DEFAULT_ROUND_PIX))
		.build();
	}
	public DcareItemView(Context ctx, AttributeSet attrs) 
	{
		super(ctx, attrs);
		init(ctx);
	}
	
	public DcareItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
		// TODO Auto-generated constructor stub
	}

}

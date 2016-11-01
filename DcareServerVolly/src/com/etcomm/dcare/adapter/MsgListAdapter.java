package com.etcomm.dcare.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.netresponse.MsgListItems;
import com.etcomm.dcare.util.DensityUtil;

public class MsgListAdapter extends DcareBaseAdapter<MsgListItems> {

	private int mScreenWidth;

	public MsgListAdapter(Context context, List<MsgListItems> list, int width) {
		super(context);
		this.mList = list;
		this.mScreenWidth = width;
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_msglist, null);
			viewHolder.msg_avatar = (ImageView) convertView.findViewById(R.id.msg_avatar);
			viewHolder.msg_title = (TextView) convertView.findViewById(R.id.msg_title);
			viewHolder.msg_detail = (TextView) convertView.findViewById(R.id.msg_detail);
			viewHolder.msg_time = (TextView) convertView.findViewById(R.id.msg_time);
			viewHolder.disscuss_pics_gridview = (GridView) convertView.findViewById(R.id.disscuss_pics_gridview);
			// viewHolder.image_li = (LinearLayout)
			// convertView.findViewById(R.id.image_li);
			// viewHolder.image1 = (ImageView)
			// convertView.findViewById(R.id.image1);
			// viewHolder.image2 = (ImageView)
			// convertView.findViewById(R.id.image2);
			// viewHolder.image3 = (ImageView)
			// convertView.findViewById(R.id.image3);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final MsgListItems mInfo = getItem(position);
		if (null != mInfo) {
			Log.i(tag, "MsgListItems mInfo: " + mInfo.toString());
			imageLoader.displayImage(mInfo.getAvatar(), viewHolder.msg_avatar);
			viewHolder.msg_title.setText(mInfo.getTitle());
			if (StringUtils.isEmpty(mInfo.getDetail())) {
				viewHolder.msg_detail.setVisibility(View.GONE);
			} else {
				viewHolder.msg_detail.setVisibility(View.VISIBLE);
				viewHolder.msg_detail.setText(mInfo.getDetail());
			}
			viewHolder.msg_time.setText(mInfo.getTime());
			// List<String> pics = mInfo.getPicNamesArray();
			Log.i(tag, "mInfo.getPicNamesArray()  " + mInfo.getPicNamesArray());
			int piccount = mInfo.getPicNamesArray().size();
			int column = (int) (piccount / 3) + piccount % 3 == 0 ? 0 : 1 + 1;
			String model = android.os.Build.MODEL;
			String brand = android.os.Build.BRAND;
			// if(model.contains("1SW")){
			// Log.i(tag, "contains 1sw");
			// android.view.ViewGroup.MarginLayoutParams source =
			// (MarginLayoutParams)
			// viewHolder.disscuss_pics_gridview.getLayoutParams();
			// // android.view.ViewGroup.LayoutParams layoutparams = new
			// android.view.ViewGroup.LayoutParams(source);
			// source.width=mScreenWidth*2/3;
			// source.height = mScreenWidth*2/9*column;
			// source.bottomMargin = DensityUtil.dip2px(mContext, 10);
			// source.topMargin = DensityUtil.dip2px(mContext, 10);
			// viewHolder.disscuss_pics_gridview.setLayoutParams(source);
			// Log.i(tag, "setLayoutParams");
			// }else{
			// RelativeLayout.LayoutParams source = (LayoutParams)
			// viewHolder.disscuss_pics_gridview.getLayoutParams();
			// RelativeLayout.LayoutParams layoutparams = new
			// LayoutParams(source);
			// layoutparams.width=mScreenWidth*2/3;
			// layoutparams.height = mScreenWidth*2/9*column;
			// // layoutparams.bottomMargin = DensityUtil.dip2px(mContext, 10);
			// // layoutparams.topMargin = DensityUtil.dip2px(mContext, 10);
			// viewHolder.disscuss_pics_gridview.setLayoutParams(layoutparams);
			// }
			final MsgPhotoGridAdapter adapter = new MsgPhotoGridAdapter(mContext, mInfo.getPicNamesArray(), mScreenWidth * 2 / 9 - DensityUtil.dip2px(mContext, 10));
			// holder.disscuss_pics_gridview.setLayoutParams(new
			// LayoutParams(mScreenWidth*3/5,LayoutParams.WRAP_CONTENT));
			// viewHolder.disscuss_pics_gridview.setAdapter(adapter);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("photos", (Serializable) photos);
			// CommonUtils.launchActivity(this, PhotoPreviewActivity.class,
			// bundle);
			// viewHolder.disscuss_pics_gridview.setOnItemClickListener(new
			// OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> parent, View view, int
			// position, long id) {
			// // TODO Auto-generated method stub
			// Bundle bundle = new Bundle();
			// List<PhotoModel> photos= new ArrayList<PhotoModel>();
			// List<String> list = mInfo.getPicNamesArray();
			// for (int i = 0; i < list.size(); i++) {
			// photos.add(new PhotoModel(list.get(i)));
			// }
			// bundle.putSerializable("photos", (Serializable) photos);
			// bundle.putBoolean("ispicfromnet", true);
			// bundle.putInt("position", position);
			// CommonUtils.launchActivity(mContext, PhotoPreviewActivity.class,
			// bundle);
			// }
			// });
		}
		// if(pics!=null&&pics.size()>0){
		// viewHolder.image_li.setVisibility(View.VISIBLE);
		// for(int i = 0 ;i<3;i++){
		// if(i+1<=pics.size()){
		// if(i ==0 ){
		// viewHolder.image1.setVisibility(View.VISIBLE);
		// imageLoader.displayImage(pics.get(i),viewHolder.image1);
		// }else if(i == 1){
		// viewHolder.image2.setVisibility(View.VISIBLE);
		// imageLoader.displayImage(pics.get(i),viewHolder.image2);
		// }else if(i == 2){
		// viewHolder.image3.setVisibility(View.VISIBLE);
		// imageLoader.displayImage(pics.get(i),viewHolder.image3);
		// }
		// }else{
		// if(i ==0 ){
		// viewHolder.image1.setVisibility(View.INVISIBLE);
		// }else if(i == 1){
		// viewHolder.image2.setVisibility(View.INVISIBLE);
		// }else if(i == 2){
		// viewHolder.image3.setVisibility(View.INVISIBLE);
		// }
		// }
		//
		// }
		// }else{
		// viewHolder.image_li.setVisibility(View.GONE);
		// }
		// }
		return convertView;
	}

	private static class ViewHolder {
		ImageView msg_avatar;
		TextView msg_title;
		TextView msg_detail;
		TextView msg_time;
		GridView disscuss_pics_gridview;
		// LinearLayout image_li;
		// ImageView image1;
		// ImageView image2;
		// ImageView image3;
		// TextView gift_detail;
		// Button gift_exchange;
	}
}

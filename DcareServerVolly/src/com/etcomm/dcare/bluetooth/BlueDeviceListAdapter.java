package com.etcomm.dcare.bluetooth;

import java.util.List;

import com.etcomm.dcare.R;
import com.etcomm.dcare.adapter.DcareBaseAdapter;
import com.etcomm.dcare.netresponse.CompanyWelfareItems;
import com.iwown.android_iwown_ble.model.WristBand;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
//import uk.co.alt236.bluetoothlelib.device.BluetoothLeDevice;

public class BlueDeviceListAdapter extends DcareBaseAdapter<WristBand> {

	public BlueDeviceListAdapter(Context context,List<WristBand> devs) {
		super(context);
		this.mList = devs;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_device_list, null);
			viewHolder.devicename = (TextView) convertView.findViewById(R.id.device_code);
			viewHolder.devicestatus = (TextView) convertView.findViewById(R.id.device_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i(tag, "getview");
		final WristBand mInfo = getItem(position);
		if (null != mInfo) {
			/**
			 * 判断是否是敏狐手环 F4T
			 */
			if (StringUtils.startsWithIgnoreCase(mInfo.getAddress(), "F4:06:A5")) {
				viewHolder.devicename.setText(mInfo.getAddress());
			}else {
				viewHolder.devicename.setText(mInfo.getName());
			}
//			viewHolder.devicestatus.setText(mInfo.getTitle());
		}
		return convertView;
	}
	
	private static class ViewHolder {
		TextView devicename;
		TextView devicestatus;
	}
	
	
}

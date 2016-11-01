package com.etcomm.dcare.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.netresponse.MyExchangeItems;

public class MyExchangeListAdapter extends DcareBaseAdapter<MyExchangeItems> {

	// private LoadingDialog dialog;

	public MyExchangeListAdapter(Context context, ArrayList<MyExchangeItems> list) {
		super(context);
		this.mList = list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_pointsexchange, null);
			viewHolder.gift_image = (ImageView) convertView.findViewById(R.id.gift_image);
			viewHolder.gift_name = (TextView) convertView.findViewById(R.id.gift_name);
			viewHolder.gift_price = (TextView) convertView.findViewById(R.id.gift_price);
			viewHolder.gift_stock = (TextView) convertView.findViewById(R.id.gift_stock);
			viewHolder.gift_detail = (TextView) convertView.findViewById(R.id.gift_detail);
			viewHolder.gift_exchange = (Button) convertView.findViewById(R.id.gift_exchange);
			viewHolder.ll_count = (LinearLayout) convertView.findViewById(R.id.ll_count);
			viewHolder.gift_realprice = (TextView) convertView.findViewById(R.id.gift_realprice);
			viewHolder.draw_address = (TextView) convertView.findViewById(R.id.draw_address);
			viewHolder.rl_realprice = (RelativeLayout) convertView.findViewById(R.id.rl_realprice);
			viewHolder.describetext = (TextView) convertView.findViewById(R.id.describetext);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Log.i(tag, "getview");
		final MyExchangeItems mInfo = getItem(position);
		if (null != mInfo) {
			imageLoader.displayImage(mInfo.getImage(), viewHolder.gift_image);
			viewHolder.gift_realprice.setText(mInfo.getShow_money());
			viewHolder.draw_address.setText("领取地址：" + mInfo.getDraw_address());
			viewHolder.gift_name.setText(mInfo.getName());
			if (!mInfo.getShow_money().equals("")) {
				viewHolder.gift_realprice.setText(mInfo.getShow_money());
				viewHolder.rl_realprice.setVisibility(View.VISIBLE);

			} else {
				viewHolder.rl_realprice.setVisibility(View.GONE);
			}

			viewHolder.gift_price.setText(mInfo.getScore() + "积分");
			viewHolder.gift_stock.setText("数   量：" + mInfo.getNumber());// 库存改成兑换时间
			if (Integer.valueOf(StringUtils.isEmpty(mInfo.getInventory()) ? "0" : mInfo.getInventory()) < 1) {
				viewHolder.gift_exchange.setEnabled(false);
				viewHolder.gift_exchange.setClickable(false);
			} else {
				viewHolder.gift_exchange.setEnabled(true);
				viewHolder.gift_exchange.setClickable(true);
			}
			// viewHolder.gift_detail.setText(mInfo.getDescription());
			viewHolder.gift_detail.setText("详    情：" + mInfo.getDetail());
			viewHolder.gift_detail.setOnClickListener(new OnClickListener() {
				Boolean flag = true;

				@Override
				public void onClick(View v) {
					if (flag) {
						flag = false;
						viewHolder.gift_detail.setSingleLine(false);
						viewHolder.gift_detail.setEllipsize(null); // 展开
						// tv.setSingleLine(flag);
					} else {
						flag = true;
						viewHolder.gift_detail.setSingleLine(true);
						viewHolder.gift_detail.setEllipsize(TruncateAt.END); // 收缩
						// tv.setSingleLine(flag);
					}
				}
			});
			if (mInfo.getStatus().equals("1")) {
				viewHolder.gift_exchange.setEnabled(false);
				viewHolder.gift_exchange.setClickable(false);
				viewHolder.gift_exchange.setText("已领取");
				viewHolder.describetext.setVisibility(View.GONE);
			} else {
				viewHolder.gift_exchange.setEnabled(true);
				viewHolder.gift_exchange.setClickable(true);
				viewHolder.gift_exchange.setText("已申请");
				viewHolder.describetext.setVisibility(View.VISIBLE);

			}
			// viewHolder.gift_exchange.setOnClickListener(new OnClickListener()
			// {
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// }
			// });
		}

		viewHolder.ll_count.setVisibility(View.GONE);
		return convertView;
	}

	private static class ViewHolder {
		ImageView gift_image;
		TextView gift_name, draw_address;
		TextView gift_price, gift_realprice;
		TextView gift_stock, describetext;
		TextView gift_detail;
		Button gift_exchange;
		LinearLayout ll_count;
		RelativeLayout rl_realprice;
	}
}

package com.etcomm.dcare.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.widget.ProgressDialog;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.PointsExchangeItems;
import com.etcomm.dcare.widget.DialogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class PointsExchangeListAdapter extends DcareBaseAdapter<PointsExchangeItems> {

    private int byeCount = 1;
    protected ProgressDialog mProgress;

    public PointsExchangeListAdapter(Context context, ArrayList<PointsExchangeItems> list) {
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
            viewHolder.add_count = (TextView) convertView.findViewById(R.id.add_count);
            viewHolder.bye_count = (TextView) convertView.findViewById(R.id.bye_count);
            viewHolder.reduce_count = (TextView) convertView.findViewById(R.id.reduce_count);
            viewHolder.gift_realprice = (TextView) convertView.findViewById(R.id.gift_realprice);
            viewHolder.draw_address = (TextView) convertView.findViewById(R.id.draw_address);
            viewHolder.rl_realprice = (RelativeLayout) convertView.findViewById(R.id.rl_realprice);
            viewHolder.describetext = (TextView) convertView.findViewById(R.id.describetext);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.i(tag, "getview");
        final PointsExchangeItems mInfo = getItem(position);
        viewHolder.describetext.setVisibility(View.GONE);

        if (null != mInfo) {
            //库存为0的时候
            if (mInfo.getInventory() == 0) {
                byeCount = 0;
                //库存为0的时候
                viewHolder.bye_count.setText("0");
                viewHolder.add_count.setEnabled(false);
                viewHolder.reduce_count.setEnabled(false);
            } else {
                byeCount = 1;
                viewHolder.bye_count.setText("1");
                viewHolder.add_count.setEnabled(true);
                viewHolder.reduce_count.setEnabled(true);
            }
            imageLoader.displayImage(mInfo.getImage(), viewHolder.gift_image);
            if (!mInfo.getShow_money().equals("")) {
                viewHolder.gift_realprice.setText(mInfo.getShow_money());
                viewHolder.rl_realprice.setVisibility(View.VISIBLE);

            } else {
                viewHolder.rl_realprice.setVisibility(View.GONE);
            }
            viewHolder.gift_realprice.setText(mInfo.getShow_money());
            viewHolder.gift_name.setText(mInfo.getName());
            viewHolder.gift_price.setText(mInfo.getScore() + "积分");
            viewHolder.gift_stock.setText("库    存：" + mInfo.getInventory() + "");
            if (mInfo.getInventory() < 1) {
                viewHolder.gift_exchange.setEnabled(false);
                viewHolder.gift_exchange.setClickable(false);
            } else {
                viewHolder.gift_exchange.setEnabled(true);
                viewHolder.gift_exchange.setClickable(true);
            }

            viewHolder.gift_detail.setText("详    情：" + mInfo.getDetail());
            viewHolder.gift_detail.setOnClickListener(new OnClickListener() {
                Boolean flag = true;

                @Override
                public void onClick(View v) {
                    if (flag) {
                        flag = false;
                        viewHolder.gift_detail.setSingleLine(false);
                        viewHolder.gift_detail.setEllipsize(null); // 展开
                    } else {
                        flag = true;
                        viewHolder.gift_detail.setSingleLine(true);
                        viewHolder.gift_detail.setEllipsize(TruncateAt.END); // 收缩
                    }
                }
            });
            viewHolder.gift_exchange.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    byeCount = Integer.parseInt(viewHolder.bye_count.getText().toString());
                    if (byeCount > 0) {
                        exchangegift(viewHolder.gift_exchange, mInfo);
                    } else {
                        showToast("请添加兑换数量");
                    }

                }
            });
            viewHolder.add_count.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(viewHolder.bye_count.getText().toString());
                    if (mInfo.getScore() == 0) {
                        viewHolder.add_count.setClickable(false);
                        viewHolder.reduce_count.setEnabled(false);
                    } else {
                        viewHolder.add_count.setClickable(true);
                        viewHolder.reduce_count.setEnabled(true);
                    }
                    if (count < mInfo.getInventory() && mInfo.getScore() != 0) {
                        count++;
                        byeCount = count;
                    } else {
                        viewHolder.add_count.setEnabled(false);
                    }

                    viewHolder.bye_count.setText(count + "");
                }
            });
            viewHolder.reduce_count.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mInfo.getScore() == 0) {
                        viewHolder.add_count.setClickable(false);
                    } else {
                        viewHolder.add_count.setClickable(true);
                    }
                    int count = Integer.parseInt(viewHolder.bye_count.getText().toString());
                    if (count > 1) {
                        count--;
                        byeCount = count;
                        viewHolder.add_count.setEnabled(true);
                    } else {
                    }
                    viewHolder.bye_count.setText(count + "");

                }
            });
        }
        viewHolder.draw_address.setVisibility(View.GONE);
        return convertView;
    }

    public interface ExchangeGift {
        void exchangegift(int score);
    }

    private ExchangeGift mPointExchanged;
    protected Dialog tips;

    public ExchangeGift getmPointExchanged() {
        return mPointExchanged;
    }

    public void setmPointExchanged(ExchangeGift mPointExchanged) {
        this.mPointExchanged = mPointExchanged;
    }

    protected void exchangegift(final TextView gift_exchange, final PointsExchangeItems mInfo) {
        // TODO Auto-generated method stub
        if (Integer.valueOf(AppSharedPreferencesHelper.getScore()) < mInfo.getScore()) {
            Toast.makeText(mContext, "积分不足，无法兑换", Toast.LENGTH_SHORT).show();
            return;
        }

        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("gift_id", String.valueOf(mInfo.getGift_id()));
        params.put("count", String.valueOf(byeCount));
        showProgress(0, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.PointsExchange, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    // + response.getString("content").toString());
                    if (code == 0) {
                        String gift_address = response.getJSONObject("content").getString("gift_address");
                        mInfo.setStatus("1");
                        mInfo.setInventory(mInfo.getInventory() - byeCount);
                        if (mPointExchanged != null) {
                            mPointExchanged.exchangegift(mInfo.getScore() * byeCount);
                        }
                        notifyDataSetChanged();
                        // gift_exchange.setEnabled(false);
                        // gift_exchange.setText("已兑换");
                        if (tips != null) {
                            tips.dismiss();
                            tips = null;
                        }
                        tips = DialogFactory.getDialogFactory().showTipsDialog(mContext, "提示", "我知道了", "你已兑换成功，请到" + gift_address + "处领取");
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });
    }

    private static class ViewHolder {
        ImageView gift_image;
        TextView gift_name, reduce_count, bye_count, add_count, gift_realprice, draw_address;
        TextView gift_price;
        TextView gift_stock, describetext;
        TextView gift_detail;
        Button gift_exchange;
        RelativeLayout rl_realprice;
    }

    Toast toast = null;

    protected void showToast(String message) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(mContext, (!TextUtils.isEmpty(message)) ? message : mContext.getString(R.string.network_error), Toast.LENGTH_SHORT);
        toast.show();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 500);
    }

    public static final int MSG_SHOW_TOAST = 2;
    public Handler baseHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_SHOW_TOAST:
                    if (msg.obj != null) {
                        String mes = (String) msg.obj;
                        if (!TextUtils.isEmpty(mes)) {
                            showToast(mes);
                        }
                    }
                    break;
            }
        }

        ;
    };

    public void showProgress(int resId, boolean cancel) {
        mProgress = new ProgressDialog(mContext);
        if (resId <= 0) {
            mProgress.setMessage(R.string.loading_data, cancel);
        } else {
            mProgress.setMessage(resId, cancel);
        }
        mProgress.show();
    }

    public void cancelmDialog() {
        if (mProgress != null) {
            mProgress.dismiss();
        }

    }
}

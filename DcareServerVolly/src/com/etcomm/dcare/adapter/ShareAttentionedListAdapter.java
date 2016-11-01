package com.etcomm.dcare.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.widget.ProgressDialog;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.AttentionedItems;
import com.etcomm.dcare.netresponse.DisscussItems;
import com.etcomm.dcare.netresponse.HealthNewsItems;
import com.etcomm.dcare.netresponse.SuggestItems;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ShareAttentionedListAdapter extends DcareBaseAdapter<AttentionedItems> {
    private static final String tag = "ShareAttentionedListAdapter";
    private Dialog coDialog;
    private Bundle bundle;
    private HealthNewsItems mHealthNewsInfo;
    private SuggestItems mSuggestInfo;
    private DisscussItems mDisscussItems;
    protected ProgressDialog mProgress;
    private Intent mIntent;
    private String type = "";
    private String share_id;


    public ShareAttentionedListAdapter(Context context, ArrayList<AttentionedItems> mList, Bundle mbundle, Intent intent) {
        super(context);
        this.mList = mList;
        bundle = mbundle;
        mIntent = intent;
        type = mIntent.getStringExtra("type");
        share_id = mIntent.getStringExtra("topic_id");
        if (bundle != null) {
            if (bundle.getBoolean("IsFromSuggest", false)) {
                mSuggestInfo = (SuggestItems) bundle.getSerializable(Preferences.SuggestSportDetail);
                Log.e("", "mSuggestInfo====" + mSuggestInfo.toString());
            } else if (bundle.getBoolean("IsFromTopic", false)) {
                mDisscussItems = (DisscussItems) bundle.getSerializable(Preferences.HealthNewsDetail);
            } else {
                mHealthNewsInfo = (HealthNewsItems) bundle.getSerializable(Preferences.HealthNewsDetail);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shareattentioned, null);
            viewHolder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
            viewHolder.attention_topic = (TextView) convertView.findViewById(R.id.attention_topic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final AttentionedItems mInfo = getItem(position);
        if (null != mInfo) {
            viewHolder.attention_topic.setText(mInfo.getName() + "");
            viewHolder.indicator.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (type.equals("topic")) {
                        //小组类
                        coDialog = showShareDialog(mContext, mIntent.getStringExtra("topic_name"), mIntent.getStringExtra("image"), mIntent.getStringExtra("discuse"), mInfo);

                    } else if (type.equals("activity")) {

                        coDialog = showShareDialog(mContext, mIntent.getStringExtra("topic_name"), mIntent.getStringExtra("image"), mIntent.getStringExtra("discuse"), mInfo);

                    } else {
                        //资讯类
                        if (bundle.getBoolean("IsFromSuggest", false)) {
                            coDialog = showShareDialog(mContext, mSuggestInfo.getTitle(), mSuggestInfo.getImage(), mSuggestInfo.getDesc(), mInfo);
                        } else if (bundle.getBoolean("IsFromTopic", false)) {
                            if (mDisscussItems.getPhotos().size() > 0) {
                                coDialog = showShareDialog(mContext, mDisscussItems.getTitle(), mDisscussItems.getPhotos().get(0).getThumb_image(), mDisscussItems.getContent(), mInfo);

                            } else {
                                coDialog = showShareDialog(mContext, mDisscussItems.getTitle(), "", mDisscussItems.getContent(), mInfo);

                            }
                        } else {

                            coDialog = showShareDialog(mContext, mHealthNewsInfo.getTitle(), mHealthNewsInfo.getImage(), mHealthNewsInfo.getDesc(), mInfo);
                        }
                    }
                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView indicator;
        TextView attention_topic;
    }

    public void alterData(ArrayList<AttentionedItems> mAttentionList) {
        // TODO Auto-generated method stub
        if (mList != null) {
            mList.clear();
        }
        if (mAttentionList != null) {
            mList.addAll(mAttentionList);
        }
        notifyDataSetChanged();
    }

    public Dialog showShareDialog(Context context, String title, String image, String content, final AttentionedItems mInfo) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(false); // 点击其他区域关闭
        customDialog.setContentView(view);
        customDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }

        });
        TextView span_view = (TextView) view.findViewById(R.id.span_view);
        TextView msgtv = (TextView) view.findViewById(R.id.tv_msg);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        msgtv.setText(content);
        dialog_title.setText(title);
        // left btn
        TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.btn_right);

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                if (type.equals("topic")) {
                    SharetoGroup("", mInfo.getTopic_id());
                } else if (type.equals("activity")) {
                    SharetoGroup("", mInfo.getTopic_id());
                } else {
                    if (bundle.getBoolean("IsFromSuggest", false)) {
                        SharetoGroup(mSuggestInfo.getType_id(), mInfo.getTopic_id());
                    } else if (bundle.getBoolean("IsFromTopic", false)) {
                        SharetoGroup(mDisscussItems.getShare_id(), mInfo.getTopic_id());
                    } else {
                        SharetoGroup(mHealthNewsInfo.getNews_id(), mInfo.getTopic_id());
                    }
                }


            }
        });

        ImageView news_image = (ImageView) view.findViewById(R.id.news_image);
        if (image != null && !image.isEmpty()) {
            ImageLoader.getInstance().displayImage(image, news_image);
        }

        customDialog.show();
        WindowManager.LayoutParams lp = customDialog.getWindow().getAttributes();
        lp.width = 1000; //设置宽度
        customDialog.getWindow().setAttributes(lp);
        return customDialog;
    }

    /**
     * 分享到小组；分小组分享和资讯类的分享
     */
    protected void SharetoGroup(String news_id, String topic_id) {
        String url = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("topic_id", topic_id);
        if (type.equals("topic")) {
            //小组分享到小组
            params.put("share_id", share_id);
            url = Constants.TOPIC_SHARE_GROUP;
        } else if (type.equals("activity")) {
            params.put("activity_id", share_id);
            url = Constants.ACTIVITY_SHARE_DROUP;
        } else {
            params.put("news_id", news_id);
            url = Constants.SHARE_GROUP;

        }

        Log.i("SharetoGroup", "SharetoGroup: " + params + "=====url=====" + url);

        showProgress(0, true);

        DcareRestClient.volleyGet(url, params, new

                        FastJsonHttpResponseHandler() {

                            @Override
                            public void onCancel() {
                                cancelmDialog();
                                super.onCancel();
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1, Throwable arg3, JSONObject jsonobject) {
                                cancelmDialog();
                                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    int code = response.getInteger("code");
                                    String message = response.getString("message");

                                    coDialog = showCompleteDialog(mContext);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                cancelmDialog();
                            }

                        }

        );

    }

    public Dialog showCompleteDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sharecomplete, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(false); // 点击其他区域关闭
        customDialog.setContentView(view);
        customDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }

        });

        TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.btn_right);

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent("finish"));
            }
        });
        if (type.equals("topic")) {
            btnRight.setText("返回小组");
        } else if (type.equals("activity")) {
            btnRight.setText("返回活动");
        }
        customDialog.show();
        WindowManager.LayoutParams lp = customDialog.getWindow().getAttributes();
        lp.width = 800; //设置宽度
        customDialog.getWindow().setAttributes(lp);
        return customDialog;
    }

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

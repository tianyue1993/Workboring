package com.etcomm.dcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.widget.ProgressDialog;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.SearchTopicItems;
import com.etcomm.dcare.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SearchTopicListAdapter extends DcareBaseAdapter<SearchTopicItems> {
    private static final String tag = "AroundNotAttentioned2ListAdapter";

    // private List<NotAttentionedItems> mList = new
    // ArrayList<NotAttentionedItems>();

    private NotAttentionAttentioned mListener;

    public SearchTopicListAdapter(Context context, ArrayList<SearchTopicItems> mList, NotAttentionAttentioned listener) {
        super(context);
        this.mList = mList;
        this.mListener = listener;
    }

    public interface NotAttentionAttentioned {
        void onAttentioned(SearchTopicItems item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.around_item_notattentioned, null);
            viewHolder.attention_count = (TextView) convertView.findViewById(R.id.attention_count);
            viewHolder.attention_topic = (TextView) convertView.findViewById(R.id.attention_topic);
            viewHolder.topic_image = (CircleImageView) convertView.findViewById(R.id.topic_image);
            viewHolder.follow_count = (TextView) convertView.findViewById(R.id.follow_count);
            viewHolder.topic_discuss = (TextView) convertView.findViewById(R.id.topic_discuss);
            viewHolder.attention = (Button) convertView.findViewById(R.id.attention);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SearchTopicItems mInfo = getItem(position);
        if (null != mInfo) {
            viewHolder.attention_count.setText(mInfo.getFollows() + "人关注");
            viewHolder.attention_topic.setText(mInfo.getName() + "");
            viewHolder.follow_count.setText(mInfo.getDiscussion_number() + "个帖子");
            if (mInfo.getDesc() != null) {
                viewHolder.topic_discuss.setText(mInfo.getDesc());
            }
            if (!mInfo.getAvatar().isEmpty()) {
                ImageLoader.getInstance().displayImage(mInfo.getAvatar(), viewHolder.topic_image);
            }
            if (mInfo.getIs_followed().equals("1")) {
                viewHolder.attention.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.attention.setVisibility(View.VISIBLE);
                viewHolder.attention.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attention(mInfo);
                    }
                });
            }
        }
        return convertView;
    }

    protected void attention(final SearchTopicItems mInfo) {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic_id", mInfo.getTopic_id());
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        showProgress(0, true);
        DcareRestClient.volleyGet(Constants.AroundAttention, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    // + response.getString("content").toString());
                    if (code == 0) {
                        Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                        ;
                        mInfo.setIs_followed("1");
                        notifyDataSetChanged();
                        // super.onSuccess(statusCode, headers, response);
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cancelmDialog();
            }
        });
    }

    private static class ViewHolder {

        TextView attention_count, follow_count;
        TextView attention_topic, topic_discuss;
        CircleImageView topic_image;
        Button attention;
    }

    public void alterData(ArrayList<SearchTopicItems> mAttentionList) {
        // TODO Auto-generated method stub
        if (mList != null) {
            mList.clear();
        }
        if (mAttentionList != null) {
            mList.addAll(mAttentionList);
        }
        notifyDataSetChanged();
    }

    protected ProgressDialog mProgress;

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

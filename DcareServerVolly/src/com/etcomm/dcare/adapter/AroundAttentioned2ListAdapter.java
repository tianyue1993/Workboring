package com.etcomm.dcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.netresponse.AttentionedItems;
import com.etcomm.dcare.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class AroundAttentioned2ListAdapter extends DcareBaseAdapter<AttentionedItems> {
    private static final String tag = "AroundAttentioned2ListAdapter";


    public AroundAttentioned2ListAdapter(Context context, ArrayList<AttentionedItems> mList) {
        super(context);
        this.mList = mList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.around_item_attentioned, null);
            viewHolder.attention_count = (TextView) convertView.findViewById(R.id.attention_count);
            viewHolder.attention_topic = (TextView) convertView.findViewById(R.id.attention_topic);
            viewHolder.topic_image = (CircleImageView) convertView.findViewById(R.id.topic_image);
            viewHolder.follow_count = (TextView) convertView.findViewById(R.id.follow_count);
            viewHolder.topic_discuss = (TextView) convertView.findViewById(R.id.topic_discuss);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AttentionedItems mInfo = getItem(position);
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

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView attention_count, follow_count;
        TextView attention_topic, topic_discuss;
        CircleImageView topic_image;
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
}

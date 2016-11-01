package com.etcomm.dcare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.model.TopicUser;
import com.etcomm.dcare.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MemberListAdapter extends DcareBaseAdapter<TopicUser> {


    public MemberListAdapter(Context context, List<TopicUser> List) {
        super(context);
        mList = List;
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_topic_member, null);
            viewHolder.member_image = (CircleImageView) convertView.findViewById(R.id.member_image);
            viewHolder.attention_member = (TextView) convertView.findViewById(R.id.attention_member);
            viewHolder.structure_name = (TextView) convertView.findViewById(R.id.structure_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TopicUser mInfo = mList.get(position);
        if (mInfo != null) {
            viewHolder.attention_member.setText(mInfo.nick_name);
            viewHolder.structure_name.setText(mInfo.structure);
            ImageLoader.getInstance().displayImage(mInfo.avatar, viewHolder.member_image);
        }

        return convertView;
    }

    private static class ViewHolder {
        CircleImageView member_image;
        TextView attention_member;
        TextView structure_name;
    }


}

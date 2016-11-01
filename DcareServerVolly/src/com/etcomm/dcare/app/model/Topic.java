package com.etcomm.dcare.app.model;

import java.util.ArrayList;

/**
 * Created by ${tianyue} on 2016/9/18.
 */
public class Topic {

    public String avatar;
    public String desc;
    public String user_number;
    public ArrayList<TopicUser> user;
    public String user_id;
    public String is_followed;
    public String is_activity;
    public String activity_rank;

    @Override
    public String toString() {
        return "Topic{" +
                "avatar='" + avatar + '\'' +
                ", desc='" + desc + '\'' +
                ", user_number='" + user_number + '\'' +
                ", user=" + user +
                ", user_id='" + user_id + '\'' +
                ", is_followed='" + is_followed + '\'' +
                ", is_activity='" + is_activity + '\'' +
                ", activity_rank='" + activity_rank + '\'' +
                '}';
    }
}

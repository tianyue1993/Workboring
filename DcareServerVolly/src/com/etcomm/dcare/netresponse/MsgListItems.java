package com.etcomm.dcare.netresponse;

import java.util.List;

public class MsgListItems extends Items {
    private String news_id;

    private String time;

    private String detail;

    private String title;
    private String topic_name;
    private String topic_list_type;

    private String detail_id;

    //	private List<JSONObject> picNamesArray;
    private List<String> picNamesArray;

    private String is_like;

    private String user_id;

    private String avatar;

    private String type;
    private String topic_id;

    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_id() {
        return this.news_id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }

    public String getDetail_id() {
        return this.detail_id;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getIs_like() {
        return this.is_like;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

//	public List<JSONObject> getPicNamesArray() {
//		return picNamesArray;
//	}
//
//	public void setPicNamesArray(List<JSONObject> picNamesArray) {
//		this.picNamesArray = picNamesArray;
//	}

    public List<String> getPicNamesArray() {
        return picNamesArray;
    }

    public void setPicNamesArray(List<String> picNamesArray) {
        this.picNamesArray = picNamesArray;
    }

    public String getTopic_list_type() {
        return topic_list_type;
    }

    public void setTopic_list_type(String topic_list_type) {
        this.topic_list_type = topic_list_type;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    @Override
    public String toString() {
        return "MsgListItems [news_id=" + news_id + ", time=" + time + ", detail=" + detail + ", title=" + title
                + ", detail_id=" + detail_id + ", picNamesArray=" + picNamesArray + ", is_like=" + is_like
                + ", user_id=" + user_id + ", avatar=" + avatar + ", type=" + type + "]";
    }


}
package com.etcomm.dcare.netresponse;

public class NotAttentionedItems extends Items {
    private String follows;

    private String discussion_number;

    private String topic_id;

    private String name;

    private String user_id;

    private String is_followd;
    private String desc;
    private String avatar;
    private String activity_rank;

    public String getActivity_rank() {
        return activity_rank;
    }

    public void setActivity_rank(String activity_rank) {
        this.activity_rank = activity_rank;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getFollows() {
        return this.follows;
    }

    public void setDiscussion_number(String discussion_number) {
        this.discussion_number = discussion_number;
    }

    public String getDiscussion_number() {
        return this.discussion_number;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_id() {
        return this.topic_id;
    }
    private String activity_id;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setIs_followd(String is_followd) {
        this.is_followd = is_followd;
    }

    public String getIs_followd() {
        return this.is_followd;
    }

    @Override
    public String toString() {
        return "NotAttentionedItems [follows=" + follows + ", discussion_number=" + discussion_number + ", topic_id="
                + topic_id + ", name=" + name + ", user_id=" + user_id + ", is_followd=" + is_followd + "]";
    }

}

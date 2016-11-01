package com.etcomm.dcare.netresponse;

public class SearchTopicItems extends Items {
    /**
     *
     */
    private static final long serialVersionUID = -3996894427273274363L;
    private String follows;//": "12",
    private String name;//": "多肉植物",
    private String topic_id;//": "1",
    private String is_followed;//": "1"
    private String discussion_number;

    private String avatar;

    private String desc;

    public String getDiscussion_number() {
        return discussion_number;
    }

    public void setDiscussion_number(String discussion_number) {
        this.discussion_number = discussion_number;
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

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(String is_followed) {
        this.is_followed = is_followed;
    }

    @Override
    public String toString() {
        return "SearchTopicItems [follows=" + follows + ", name=" + name + ", topic_id=" + topic_id + ", is_followed="
                + is_followed + "]";
    }

}

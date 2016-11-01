package com.etcomm.dcare.netresponse;

import java.util.List;

public class DisscussItems extends Items {
    private String comment_number;

    private String discussion_id;

    private String content;

    private String created_at;

    private String like;

    private String user_id;

    private String nick_name;

    private String avatar;

    private String structure;

    private String is_like;

    private String share_type; // 状态 1：分享 0：帖子

    private String share_id;

    private String title;

    private String detail_url;

    private List<DisscussPhotosItems> photos;

    public String getComment_number() {
        return comment_number;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public String getShare_type() {
        return share_type;
    }

    public void setShare_type(String share_type) {
        this.share_type = share_type;
    }

    public String getDiscussion_id() {
        return discussion_id;
    }

    public void setDiscussion_id(String discussion_id) {
        this.discussion_id = discussion_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }


    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail_url() {
        return detail_url;
    }

    private String share_url;

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public List<DisscussPhotosItems> getPhotos() {
        return photos;
    }

    public void setPhotos(List<DisscussPhotosItems> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "DisscussItems [comment_number=" + comment_number + ", discussion_id=" + discussion_id + ", content=" + content + ", created_at=" + created_at + ", like=" + like + ", user_id=" + user_id + ", nick_name=" + nick_name + ", avatar=" + avatar + ", structure=" + structure + ", is_like=" + is_like + ", share_type=" + share_type + ", share_id=" + share_id + ", title=" + title + ", detail_url=" + detail_url + ", photos=" + photos + "]";
    }

}

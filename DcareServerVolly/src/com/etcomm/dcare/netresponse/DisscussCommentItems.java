package com.etcomm.dcare.netresponse;

public class DisscussCommentItems extends Items {
	private String comment_id;

	private String comment;

	private String created_at;

	private String parent_id;

	private String topic_id;

	private String discussion_id;

	private String user_id;

	private String nick_name;

	private String avatar;

	private String be_reply;

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getComment_id() {
		return this.comment_id;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return this.comment;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getParent_id() {
		return this.parent_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public String getTopic_id() {
		return this.topic_id;
	}

	public void setDiscussion_id(String discussion_id) {
		this.discussion_id = discussion_id;
	}

	public String getDiscussion_id() {
		return this.discussion_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getNick_name() {
		return this.nick_name;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setBe_reply(String be_reply) {
		this.be_reply = be_reply;
	}

	public String getBe_reply() {
		return this.be_reply;
	}

}

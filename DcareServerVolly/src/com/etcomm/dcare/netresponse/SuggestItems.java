package com.etcomm.dcare.netresponse;

public class SuggestItems extends Items {
	/**
	 *
	 */
	private static final long serialVersionUID = -8766066347701524762L;

	private String recommend_id;// 推荐自增ID

	private String customer_id;// 客户Id

	private String type_id; // 推荐类型id （type:health-健康资讯 welfare-企业福利
	// activity-活动）

	private String title; // 标题

	private String detail_url;// "http://113.59.227.10:81/frontend/web/index.php?r=html5/activity-detail&id=5",
	// //详情地址

	private String type;// //类型 （1:健康资讯 2:企业福利 3：活动）

	private String image;// "http://113.59.227.10:81/upload/activity/DHZZEZz6KL6E_AtOimMx1sizh_R_o_cJ.jpg",
	// //图片

	private String start_at;// 活动开始时间

	private String deadline;

	private String end_at;// 活动结束时间

	private String created_at;// 创建时间

	private String pv;// 浏览量

	private String number;// 活动报名人数

	private String status; // 活动进行状态
	private String desc;
	private String share_url;

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setRecommend_id(String recommend_id) {
		this.recommend_id = recommend_id;
	}

	public String getRecommend_id() {
		return this.recommend_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_id() {
		return this.customer_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_id() {
		return this.type_id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setDetail_url(String detail_url) {
		this.detail_url = detail_url;
	}

	public String getDetail_url() {
		return this.detail_url;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	public void setStart_at(String start_at) {
		this.start_at = start_at;
	}

	public String getStart_at() {
		return this.start_at;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getDeadline() {
		return this.deadline;
	}

	public void setEnd_at(String end_at) {
		this.end_at = end_at;
	}

	public String getEnd_at() {
		return this.end_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getPv() {
		return this.pv;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return this.number;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "SuggestItems [recommend_id=" + recommend_id + ", customer_id=" + customer_id + ", type_id=" + type_id + ", title=" + title + ", detail_url=" + detail_url + ", type=" + type + ", image=" + image + ", start_at=" + start_at + ", deadline=" + deadline + ", end_at=" + end_at + ", created_at=" + created_at + ", pv=" + pv + ", number=" + number + ", status=" + status + ", desc=" + desc + "]";
	}

}

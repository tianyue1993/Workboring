package com.etcomm.dcare.netresponse;

public class HealthNewsItems extends Items {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String news_id;

	private String customer_id;

	private String title;

	private String desc;

	private String square_image;
	private String image;

	private String rectangle_image;

	private String like;

	private String pv;

	private String created_at;

	private String is_deleted;

	private String like_status;

	private String detail_url;
	private String share_url;

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public void setNews_id(String news_id) {
		this.news_id = news_id;
	}

	public String getNews_id() {
		return this.news_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_id() {
		return this.customer_id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setSquare_image(String square_image) {
		this.square_image = square_image;
	}

	public String getSquare_image() {
		return this.square_image;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setRectangle_image(String rectangle_image) {
		this.rectangle_image = rectangle_image;
	}

	public String getRectangle_image() {
		return this.rectangle_image;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getLike() {
		return this.like;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}

	public String getPv() {
		return this.pv;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public void setIs_deleted(String is_deleted) {
		this.is_deleted = is_deleted;
	}

	public String getIs_deleted() {
		return this.is_deleted;
	}

	public void setLike_status(String like_status) {
		this.like_status = like_status;
	}

	public String getLike_status() {
		return this.like_status;
	}

	public void setDetail_url(String detail_url) {
		this.detail_url = detail_url;
	}

	public String getDetail_url() {
		return this.detail_url;
	}
}

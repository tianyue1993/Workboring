package com.etcomm.dcare.netresponse;

public class CompanyWelfareItems extends Items {

	/**
	 *
	 */
	private static final long serialVersionUID = 2173113835408815865L;
	private String  welfare_id;//": "2",  //福利id
	private String  customer_id;//": "1",  //客户id
	private String  title;//": "发放福利",  //标题
	private String  image;//": "http://113.59.227.10:81/upload/welfare/4623ec0573a78db77ee91564e7cd9c55.jpg",  //图片
	private String  created_at;//": "2015-12-11 10:57:49", //创建时间
	private String  start_at;//": "0000-00-00 00:00:00" //开始时间
	private String end_at;
	public String getEnd_at() {
		return end_at;
	}
	public void setEnd_at(String end_at) {
		this.end_at = end_at;
	}
	private String  detail_url;//": "http://113.59.227.10:81/frontend/web/index.php?r=html5/welfare-detail&id=1" //详情地址
	public String getWelfare_id() {
		return welfare_id;
	}
	public void setWelfare_id(String welfare_id) {
		this.welfare_id = welfare_id;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getStart_at() {
		return start_at;
	}
	public void setStart_at(String start_at) {
		this.start_at = start_at;
	}
	public String getDetail_url() {
		return detail_url;
	}
	public void setDetail_url(String detail_url) {
		this.detail_url = detail_url;
	}
	@Override
	public String toString() {
		return "CompanyWelfareItems [welfare_id=" + welfare_id + ", customer_id=" + customer_id + ", title=" + title
				+ ", image=" + image + ", created_at=" + created_at + ", start_at=" + start_at + ", detail_url="
				+ detail_url + "]";
	}

}

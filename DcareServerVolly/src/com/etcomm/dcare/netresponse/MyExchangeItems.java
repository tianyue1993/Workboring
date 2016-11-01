package com.etcomm.dcare.netresponse;

/**
 * 我的兑换
 *
 * @author iexpressbox
 *
 */
public class MyExchangeItems extends Items {
	private String list_id;

	private String gift_id;

	private String name;

	private String detail;

	private String inventory;

	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	private String score;

	private String image;

	private String created_at;

	private String status;
	private String show_money;
	private String draw_address;

	public String getDraw_address() {
		return draw_address;
	}

	public void setDraw_address(String draw_address) {
		this.draw_address = draw_address;
	}

	public String getShow_money() {
		return show_money;
	}

	public void setShow_money(String show_money) {
		this.show_money = show_money;
	}

	public void setList_id(String list_id) {
		this.list_id = list_id;
	}

	public String getList_id() {
		return this.list_id;
	}

	public void setGift_id(String gift_id) {
		this.gift_id = gift_id;
	}

	public String getGift_id() {
		return this.gift_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDetail() {
		return this.detail;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getInventory() {
		return this.inventory;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScore() {
		return this.score;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "MyExchangeItems [list_id=" + list_id + ", gift_id=" + gift_id + ", name=" + name + ", detail=" + detail + ", inventory=" + inventory + ", score=" + score + ", image=" + image + ", created_at=" + created_at + ", status=" + status + "]";
	}

}

package com.etcomm.dcare.netresponse;

public class PointsExchangeItems extends Items {
	private String list_id;

	private int gift_id;

	private String name;

	private String detail;

	private int inventory;

	private int score;

	private String image;

	private String status;
	private String show_money;

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

	public void setGift_id(int gift_id) {
		this.gift_id = gift_id;
	}

	public int getGift_id() {
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

	public void setInventory(int inventory) {
		this.inventory = inventory;
	}

	public int getInventory() {
		return this.inventory;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return this.score;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return "PointsExchangeItems [list_id=" + list_id + ", gift_id=" + gift_id + ", name=" + name + ", detail="
				+ detail + ", inventory=" + inventory + ", score=" + score + ", image=" + image + ", status=" + status
				+ "]";
	}

}

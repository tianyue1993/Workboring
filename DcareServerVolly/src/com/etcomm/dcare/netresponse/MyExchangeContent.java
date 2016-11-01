package com.etcomm.dcare.netresponse;

import java.util.List;

/**
 * 我的兑换
 *
 * @author iexpressbox
 *
 */
public class MyExchangeContent extends Content {

	private int page_count;

	private List<MyExchangeItems> model;

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}

	public void setModel(List<MyExchangeItems> model) {
		this.model = model;
	}

	public List<MyExchangeItems> getModel() {
		return this.model;
	}

}

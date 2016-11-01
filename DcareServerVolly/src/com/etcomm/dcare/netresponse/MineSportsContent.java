package com.etcomm.dcare.netresponse;

import java.util.List;

public class MineSportsContent extends Content {
	private int page_count;

	private List<MineSportsItem> items;

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}

	public void setItems(List<MineSportsItem> items) {
		this.items = items;
	}

	public List<MineSportsItem> getItems() {
		return this.items;
	}
}

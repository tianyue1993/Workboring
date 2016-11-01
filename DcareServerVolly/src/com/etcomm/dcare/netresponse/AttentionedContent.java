package com.etcomm.dcare.netresponse;

import java.util.List;

public class AttentionedContent extends Content {
	private List<AttentionedItems> items;

	private int page_count;

	public void setItems(List<AttentionedItems> items) {
		this.items = items;
	}

	public List<AttentionedItems> getItems() {
		return this.items;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}
}

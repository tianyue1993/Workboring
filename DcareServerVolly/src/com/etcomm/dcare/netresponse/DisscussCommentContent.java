package com.etcomm.dcare.netresponse;

import java.util.List;

public class DisscussCommentContent extends Content {
	private List<DisscussCommentItems> items;

	private int page_count;

	public void setItems(List<DisscussCommentItems> items) {
		this.items = items;
	}

	public List<DisscussCommentItems> getItems() {
		return this.items;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}
}

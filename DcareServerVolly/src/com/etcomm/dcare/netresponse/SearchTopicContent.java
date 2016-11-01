package com.etcomm.dcare.netresponse;

import java.util.List;

public class SearchTopicContent extends Content {
	/**
	 * 
	 */
	private static final long serialVersionUID = -716781116724327285L;

	private List<SearchTopicItems> items;

	private int page_count;

	public void setItems(List<SearchTopicItems> items) {
		this.items = items;
	}

	public List<SearchTopicItems> getItems() {
		return this.items;
	}

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}
}

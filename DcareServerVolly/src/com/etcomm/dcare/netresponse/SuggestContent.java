package com.etcomm.dcare.netresponse;

import java.util.List;

public class SuggestContent extends Content {
	private int pages;

	private List<SuggestItems> items;

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getPages() {
		return this.pages;
	}

	public void setItems(List<SuggestItems> items) {
		this.items = items;
	}

	public List<SuggestItems> getItems() {
		return this.items;
	}

	@Override
	public String toString() {
		return "SuggestContent [pages=" + pages + ", items=" + items + "]";
	}
	
}

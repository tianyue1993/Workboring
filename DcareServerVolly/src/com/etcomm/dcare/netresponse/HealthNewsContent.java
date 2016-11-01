package com.etcomm.dcare.netresponse;

import java.util.List;

public class HealthNewsContent extends Content {
	private int page_count;

	private List<HealthNewsItems> model;

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}

	public void setModel(List<HealthNewsItems> model) {
		this.model = model;
	}

	public List<HealthNewsItems> getModel() {
		return this.model;
	}

}

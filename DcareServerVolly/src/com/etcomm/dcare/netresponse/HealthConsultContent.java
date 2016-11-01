package com.etcomm.dcare.netresponse;

import java.util.List;

public class HealthConsultContent extends Content {
	private int page_count;

	private List<HealthConsultItem> model;

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}

	public void setModel(List<HealthConsultItem> model) {
		this.model = model;
	}

	public List<HealthConsultItem> getModel() {
		return this.model;
	}
}

package com.etcomm.dcare.netresponse;

import java.util.List;

public class MyPointsDetailContent extends Content {
	private int page_count;

	private List<MyPointsDetailItems> score;

	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}

	public int getPage_count() {
		return this.page_count;
	}

	public void setScore(List<MyPointsDetailItems> score) {
		this.score = score;
	}

	public List<MyPointsDetailItems> getScore() {
		return this.score;
	}

}

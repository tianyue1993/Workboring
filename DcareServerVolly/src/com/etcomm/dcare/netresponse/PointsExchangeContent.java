package com.etcomm.dcare.netresponse;

import java.util.List;

public class PointsExchangeContent extends Content {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int page_count;

	private List<PointsExchangeItems> model ;

	private int my_score;

	public void setPage_count(int page_count){
	this.page_count = page_count;
	}
	public int getPage_count(){
	return this.page_count;
	}
	public void setModel(List<PointsExchangeItems> model){
	this.model = model;
	}
	public List<PointsExchangeItems> getModel(){
	return this.model;
	}
	public void setMy_score(int my_score){
	this.my_score = my_score;
	}
	public int getMy_score(){
	return this.my_score;
	}

}

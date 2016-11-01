package com.etcomm.dcare.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataPerDay implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4139486595101050983L;

	private String date;// 格式 2015-12-30
	private List<DataPerHour> list;
	public DataPerDay() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<DataPerHour>();
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<DataPerHour> getList() {
		return list;
	}

	public void setList(List<DataPerHour> list) {
		this.list = list;
	}

}

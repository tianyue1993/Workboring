package com.etcomm.dcare.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataSync implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4874521583199756135L;
	private String access_token;//
	private List<DataPerDay> list;
	
	public DataSync() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<DataPerDay>();
	}
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public List<DataPerDay> getList() {
		return list;
	}
	public void setList(List<DataPerDay> list) {
		this.list = list;
	}
	
	
}

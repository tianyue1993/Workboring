package com.etcomm.dcare.widget;

public class Sentence {
	private String name;
	private int index;
	
	public Sentence(int index,String name){
		this.name=name;
		this.index=index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

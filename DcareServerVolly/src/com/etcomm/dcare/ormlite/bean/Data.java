package com.etcomm.dcare.ormlite.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="tb_stepdata")
public class Data {
	
	
	public Data() {
		super();
	}
	@DatabaseField(columnName= "id",generatedId = true)
	private int id;
	@DatabaseField
	private long time;
	@DatabaseField
	private int steps;
	@DatabaseField
	private float miles;
	@DatabaseField
	private float calaries;
	@DatabaseField
	private int seconds;
	@DatabaseField(canBeNull = false)
	private String user_id;
	
	
	
	public Data(long time, int steps, float miles, float calaries, int seconds, String user_id) {
		super();
		this.time = time;
		this.steps = steps;
		this.miles = miles;
		this.calaries = calaries;
		this.seconds = seconds;
		this.user_id = user_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	public float getMiles() {
		return miles;
	}
	public void setMiles(float miles) {
		this.miles = miles;
	}
	public float getCalaries() {
		return calaries;
	}
	public void setCalaries(float calaries) {
		this.calaries = calaries;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "Data [id=" + id + ", time=" + time + ", steps=" + steps + ", miles=" + miles + ", calaries=" + calaries
				+ ", seconds=" + seconds + ", user_id=" + user_id + "]";
	}
	
	
	
}

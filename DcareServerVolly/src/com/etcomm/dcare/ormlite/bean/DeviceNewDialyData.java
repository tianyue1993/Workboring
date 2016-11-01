package com.etcomm.dcare.ormlite.bean;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.field.DatabaseField;

public class DeviceNewDialyData {
	public DeviceNewDialyData() {
		super();
	}
	@DatabaseField(columnName= "id",generatedId = true)
	private int id;
	@DatabaseField
	private String sportSteps;
	@DatabaseField
	private String sportDistances;
	@DatabaseField
	private String sportCalories;
	@DatabaseField
	private int month;
	@DatabaseField
	private int uid;
	@DatabaseField
	private int day;
	@DatabaseField
	private int sportType;
	@DatabaseField
	private int startTime;
	@DatabaseField
	private int count;
	@DatabaseField
	private int activityTime;
	@DatabaseField
	private int endTime;
	@DatabaseField
	private int year;
	public void setSportSteps(String sportSteps) {
		this.sportSteps = sportSteps;
	}

	public String getSportSteps() {
		return this.sportSteps;
	}

	public void setSportDistances(String sportDistances) {
		this.sportDistances = sportDistances;
	}

	public String getSportDistances() {
		return this.sportDistances;
	}

	public void setSportCalories(String sportCalories) {
		this.sportCalories = sportCalories;
	}

	public String getSportCalories() {
		return this.sportCalories;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return this.month;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getUid() {
		return this.uid;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return this.day;
	}

	public void setSportType(int sportType) {
		this.sportType = sportType;
	}

	public int getSportType() {
		return this.sportType;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getStartTime() {
		return this.startTime;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return this.count;
	}

	public void setActivityTime(int activityTime) {
		this.activityTime = activityTime;
	}

	public int getActivityTime() {
		return this.activityTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getEndTime() {
		return this.endTime;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public String toString() {
		return "DeviceNewDialyData [id=" + id + ", sportSteps=" + sportSteps + ", sportDistances=" + sportDistances
				+ ", sportCalories=" + sportCalories + ", month=" + month + ", uid=" + uid + ", day=" + day
				+ ", sportType=" + sportType + ", startTime=" + startTime + ", count=" + count + ", activityTime="
				+ activityTime + ", endTime=" + endTime + ", year=" + year + "]";
	}

	public DeviceNewDialyData parse(String json) {
		// TODO Auto-generated method stub
		return JSON.parseObject(json, DeviceNewDialyData.class);
	}
}

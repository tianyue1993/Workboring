package com.etcomm.dcare.ormlite.bean;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tb_device5mindata")
public class Device5MinData {
	public Device5MinData() {
		super();
	}

	@DatabaseField(columnName= "id",generatedId = true)
	private int id;
	@DatabaseField
	private String device;

	@DatabaseField(canBeNull=false,unique=true)
	private long timestamp;
	@DatabaseField
	private double calorie;
	@DatabaseField
	private int day;
	@DatabaseField
	private int _uploaded;
	@DatabaseField
	private double distance;
	@DatabaseField
	private int _converted;
	@DatabaseField
	private int flag;
	@DatabaseField
	private int hour;
	@DatabaseField
	private int minute;
	@DatabaseField
	private int month;
	@DatabaseField
	private int steps;
	@DatabaseField
	private int bcc;
	@DatabaseField
	private int year;

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice() {
		return this.device;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}

	public double getCalorie() {
		return this.calorie;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return this.day;
	}

	public void set_uploaded(int _uploaded) {
		this._uploaded = _uploaded;
	}

	public int get_uploaded() {
		return this._uploaded;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return this.distance;
	}

	public void set_converted(int _converted) {
		this._converted = _converted;
	}

	public int get_converted() {
		return this._converted;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getFlag() {
		return this.flag;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getHour() {
		return this.hour;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getMinute() {
		return this.minute;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return this.month;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getSteps() {
		return this.steps;
	}

	public void setBcc(int bcc) {
		this.bcc = bcc;
	}

	public int getBcc() {
		return this.bcc;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public String toString() {
		return "DeviceData [id=" + id + ", device=" + device + ", timestamp=" + timestamp + ", calorie=" + calorie
				+ ", day=" + day + ", _uploaded=" + _uploaded + ", distance=" + distance + ", _converted=" + _converted
				+ ", flag=" + flag + ", hour=" + hour + ", minute=" + minute + ", month=" + month + ", steps=" + steps
				+ ", bcc=" + bcc + ", year=" + year + "]";
	}

	public Device5MinData parse(String json) {
		return JSON.parseObject(json, Device5MinData.class);
	}

}

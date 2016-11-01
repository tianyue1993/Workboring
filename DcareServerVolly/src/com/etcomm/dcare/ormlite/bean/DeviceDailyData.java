package com.etcomm.dcare.ormlite.bean;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;

public class DeviceDailyData {

	public DeviceDailyData() {
		super();
	}
	@DatabaseField(columnName= "id",generatedId = true)
	private int id;
	@DatabaseField
	private String __chamobile__;
	@DatabaseField
	private String daily_date;
	@DatabaseField(canBeNull=false,unique=true)
	private long timestamp;
	@DatabaseField
	private int bcc;//uid  i5plus NewData
	@DatabaseField
	private double calorie;
	@DatabaseField
	private int _converted;//sportType   i5plus NewData
	@DatabaseField
	private int day;
	@DatabaseField
	private double distance;
	@DatabaseField
	private int month;
	@DatabaseField
	private int oldornew;//最好得知时间计算方法(startTime,activityTime,endTime)得到时间值
	@DatabaseField
	private int steps;
	@DatabaseField
	private int _uploaded;//count   i5plus NewData
	@DatabaseField
	private int year;

	public void set__chamobile__(String __chamobile__) {
		this.__chamobile__ = __chamobile__;
	}

	public String get__chamobile__() {
		return this.__chamobile__;
	}

	public void setDaily_date(String daily_date) {
		this.daily_date = daily_date;
	}

	public String getDaily_date() {
		return this.daily_date;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setBcc(int bcc) {
		this.bcc = bcc;
	}

	public int getBcc() {
		return this.bcc;
	}

	public void setCalorie(double calorie) {
		this.calorie = calorie;
	}

	public double getCalorie() {
		return this.calorie;
	}

	public void set_converted(int _converted) {
		this._converted = _converted;
	}

	public int get_converted() {
		return this._converted;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return this.day;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return this.distance;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return this.month;
	}

	public void setOldornew(int oldornew) {
		this.oldornew = oldornew;
	}

	public int getOldornew() {
		return this.oldornew;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getSteps() {
		return this.steps;
	}

	public void set_uploaded(int _uploaded) {
		this._uploaded = _uploaded;
	}

	public int get_uploaded() {
		return this._uploaded;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public String toString() {
		return "DeviceDailyData [id=" + id + ", __chamobile__=" + __chamobile__ + ", daily_date=" + daily_date
				+ ", timestamp=" + timestamp + ", bcc=" + bcc + ", calorie=" + calorie + ", _converted=" + _converted
				+ ", day=" + day + ", distance=" + distance + ", month=" + month + ", oldornew=" + oldornew + ", steps="
				+ steps + ", _uploaded=" + _uploaded + ", year=" + year + "]";
	}

	public DeviceDailyData parse(String json) {
		// TODO Auto-generated method stub
		return JSON.parseObject(json, DeviceDailyData.class);
	}

}

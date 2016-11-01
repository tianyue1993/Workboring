package com.etcomm.dcare.data;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.alibaba.fastjson.JSON;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;

public class DataPerDayFromWrist implements Serializable {
	private String dt;// ": "2016-01-14", //日期
	private int s;// 2000, // 步数
	private float c;// ": 50.6, // 卡路里
	private float t;// ": 0.8, // 时长
	private float d;// ": 2 // 距离
	private int tg;// ': 10000, //目标步数
	public DataPerDayFromWrist() {
		this.dt = new DateTime().toString("yyyy-MM-dd");
		this.s = 0;
		this.c = 0;
		this.t = 0;
		this.d = 0;
		this.tg = Integer.valueOf(AppSharedPreferencesHelper.getPedometer_target());
	}
	public DataPerDayFromWrist(String dt,int s,float c,float t,float d,int tg){
		this.dt = dt;
		this.s = s;
		this.c = c;
		this.t = t;
		this.d = d;
		this.tg = tg;
	}
	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public float getC() {
		return c;
	}

	public void setC(float c) {
		this.c = c;
	}

	public float getT() {
		return t;
	}

	public void setT(float t) {
		this.t = t;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}

	public int getTg() {
		return tg;
	}

	public void setTg(int tg) {
		this.tg = tg;
	}

	@Override
	public String toString() {
		return "DataPerDayFromWrist [dt=" + dt + ", s=" + s + ", c=" + c + ", t=" + t + ", d=" + d + ", tg=" + tg + "]";
	}
	public String toParseJsonString(){
//    	this.t = this.t/60;
//    	this.d = this.d/1000;
		return JSON.toJSONString(this);
	}
}

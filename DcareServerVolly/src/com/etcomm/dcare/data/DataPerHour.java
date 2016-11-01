package com.etcomm.dcare.data;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class DataPerHour implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7753299743134363290L;

	private int h;//' : 0,		     		// 0~1点   每小时一次数据
	private int s;//' : 2000,                  	//步数 step
	private float c;//' : 50.6,                  	//卡路里(大卡) Kcal
	private float t;//' : 0.8,                   	//时长 time    h
	private float d;//' : 2                      	//距离 distance   km
	public DataPerHour(int h) {
		// TODO Auto-generated constructor stub
		this.h = h;
		this.s = 0;
		this.c = 0.0000f;
		this.t = 0.0000f;
		this.d = 0.0000f;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
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

	public String toParseJsonString(){
		this.t = this.t/60;
		this.d = this.d/1000;
		return JSON.toJSONString(this);
	}
	@Override
	public String toString() {
		return toParseJsonString();
	}

}

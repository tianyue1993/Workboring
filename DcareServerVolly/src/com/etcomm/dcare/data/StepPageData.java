package com.etcomm.dcare.data;

import com.etcomm.dcare.netresponse.Items;

public class StepPageData extends Items {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3523905633619342690L;
	private int id;
	private String date;//
	private int steps;
	private int suggeststeps;
	private float calories;
	private float miles;
	private float hours;
	
	public StepPageData() {
		super();
		date ="";
		steps =0;
		suggeststeps =10000;
		calories = 0.0f;
		miles = 0.0f;
		hours = 0.0f;
	}

	public StepPageData(String date, int steps, int suggeststeps, float calories, float miles, float hours) {
		super();
		this.date = date;
		this.steps = steps;
		this.suggeststeps = suggeststeps;
		this.calories = calories;
		this.miles = miles;
		this.hours = hours;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getSuggeststeps() {
		return suggeststeps;
	}

	public void setSuggeststeps(int suggeststeps) {
		this.suggeststeps = suggeststeps;
	}

	public float getCalories() {
		return calories;
	}

	public void setCalories(float calories) {
		this.calories = calories;
	}

	public float getMiles() {
		return miles;
	}

	public void setMiles(float miles) {
		this.miles = miles;
	}

	public float getHours() {
		return hours;
	}

	public void setHours(float hours) {
		this.hours = hours;
	}

	@Override
	public String toString() {
		return "StepPageData [id=" + id + ", date=" + date + ", steps=" + steps + ", suggeststeps=" + suggeststeps
				+ ", calories=" + calories + ", miles=" + miles + ", hours=" + hours + "]";
	}
	
}

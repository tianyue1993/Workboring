package com.etcomm.dcare.app.model;


public class PedometerItem  {
//	  private Content content;
//
//	    public  class Content {
//	        private int type;
	        /**
	         * step : 0
	         * distance_text :
	         * total_time : 0
	         * target : 8000
	         * date : 07/05
	         * calorie : 0
	         * distance : 0.0
	         * calorie_text :
	         */

//	        private List<DataBean> data;
//
//
//	        public  class DataBean {
//	            private int step;
//	            private String distance_text;
//	            private int total_time;
//	            private int target;
//	            private String date;
//	            private int calorie;
//	            private String distance;
//	            private String calorie_text;
//
//
//	        }
//	    }
//	
	
	
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 8266965526538016306L;

	private String date;

	private String step;

	private String calorie;

	private String distance;

	private String total_time;

	private String calorie_text;

	private String distance_text;
	private String target;

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return this.date;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getStep() {
		return this.step;
	}

	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}

	public String getCalorie() {
		return this.calorie;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDistance() {
		return this.distance;
	}

	public void setTotal_time(String total_time) {
		this.total_time = total_time;
	}

	public String getTotal_time() {
		return this.total_time;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setCalorie_text(String calorie_text) {
		this.calorie_text = calorie_text;
	}

	public String getCalorie_text() {
		return this.calorie_text;
	}

	public void setDistance_text(String distance_text) {
		this.distance_text = distance_text;
	}

	public String getDistance_text() {
		return this.distance_text;
	}

	@Override
	public String toString() {
		return "PedometerItem [date=" + date + ", step=" + step + ", calorie=" + calorie + ", distance=" + distance
				+ ", total_time=" + total_time + ", calorie_text=" + calorie_text + ", distance_text=" + distance_text
				+ "]";
	}

}

package com.etcomm.dcare.netresponse;

public class WeatherContent extends Content {
	private String aqi;

	private String qlty;

	private String temp;

	private String weather_status;

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}

	public String getAqi() {
		return this.aqi;
	}

	public void setQlty(String qlty) {
		this.qlty = qlty;
	}

	public String getQlty() {
		return this.qlty;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getTemp() {
		return this.temp;
	}

	public void setWeather_status(String weather_status) {
		this.weather_status = weather_status;
	}

	public String getWeather_status() {
		return this.weather_status;
	}
}

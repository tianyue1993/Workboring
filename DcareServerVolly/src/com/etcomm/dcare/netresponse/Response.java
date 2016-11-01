package com.etcomm.dcare.netresponse;

public class Response {
	private int code;

	private String message;

	private Content content;

	public void setCode(int code){
	this.code = code;
	}
	public int getCode(){
	return this.code;
	}
	public void setMessage(String message){
	this.message = message;
	}
	public String getMessage(){
	return this.message;
	}
	public void setContent(Content content){
	this.content = content;
	}
	public Content getContent(){
	return this.content;
	}

}

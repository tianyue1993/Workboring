package com.etcomm.dcare.netresponse;

import java.util.List;

public class MsgListContent extends Content {
	private List<MsgListItems> content;

	private String message;

	private int code;

	public void setContent(List<MsgListItems> content) {
		this.content = content;
	}

	public List<MsgListItems> getContent() {
		return this.content;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}

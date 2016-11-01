package com.etcomm.dcare.netresponse;

public class AvatarContent extends Content {
	private String avatar;

	private int gender;

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return this.gender;
	}
}

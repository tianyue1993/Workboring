package com.etcomm.dcare.netresponse;

public class DisscussPhotosItems extends Items {
	private String discussion_image_id;
	private String image;
	private String thumb_image;
	

	public String getThumb_image() {
		return thumb_image;
	}

	public void setThumb_image(String thumb_image) {
		this.thumb_image = thumb_image;
	}

	public void setDiscussion_image_id(String discussion_image_id) {
		this.discussion_image_id = discussion_image_id;
	}

	public String getDiscussion_image_id() {
		return this.discussion_image_id;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}
}

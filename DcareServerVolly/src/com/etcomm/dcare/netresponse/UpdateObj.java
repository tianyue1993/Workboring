package com.etcomm.dcare.netresponse;

public class UpdateObj extends Items {
	/**
	 *
	 */
	private static final long serialVersionUID = -2605625331191262744L;
	private int id;//": 1, //版本标识id
	private String version;//": "1.0", //当前版本号
	private String description;//": "第一个版本", //描述
	private String file;//": "http://113.59.227.10:81/upload/android/Dcare.apk", //地址
	private String created_at;//": "2016-01-18 13:21:46" //更新时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	@Override
	public String toString() {
		return "UpdateObj [id=" + id + ", version=" + version + ", description=" + description + ", file=" + file
				+ ", created_at=" + created_at + "]";
	}



}

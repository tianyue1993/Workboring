package com.etcomm.dcare.ormlite.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="tb_user")
public class User {


	public User() {
		super();
	}
	@DatabaseField(columnName= "id",generatedId= true)
	private int   id;//: "1", //用户ID

	@DatabaseField(columnName= "user_id",canBeNull=false,unique= true)
	private String   user_id;//: "1", //用户ID

	@DatabaseField(columnName="department_id")
	private String   department_id;//": "1", //组织结构ID
	@DatabaseField(columnName="customer_id")
	private String   customer_id;//": "1;//", //客户ID
	@DatabaseField(columnName="serial_number_id")
	private String   serial_number_id;//": "1;//", //激活码ID
	@DatabaseField(columnName="nick_name")
	private String   nick_name;//": "心灵的旅程", //昵称
	@DatabaseField(columnName="real_name")
	private String   real_name;//": "王杰", //真实姓名
	@DatabaseField(columnName="access_token")
	private String   access_token;//": "123", //access_token
	@DatabaseField(columnName="birthday")
	private String   birthday;//": "1985-01-15", //生日
	@DatabaseField(columnName="height")
	private String   height;//": "172", //身高CM
	@DatabaseField(columnName="weight")
	private String   weight;//": "68", //体重KG
	@DatabaseField(columnName="avatar")
	private String   avatar;//": "http://113.59.227.10:81/upload/user/wCWC-FcWH_J6CBtj-8UgsoDDw5S_gF1H.jpg", //头像
	@DatabaseField(columnName="mobile")
	private String   mobile;//": "15210278793", //手机
	@DatabaseField(columnName="email")
	private String   email;//": "613972@qq.com", //邮件
	@DatabaseField(columnName="job_number")
	private String   job_number;//": "", //工号
	@DatabaseField(columnName="score")
	private String   score;//": "1000", // 积分
	@DatabaseField(columnName="pedometer_target")
	private String   pedometer_target;//": "10000", //运动目标
	@DatabaseField(columnName="pedometer_distance")
	private String   pedometer_distance;//": "0.00", //运动积累距离
	@DatabaseField(columnName="pedometer_time")
	private String   pedometer_time;//": "0.00", //运动积累时间
	@DatabaseField(columnName="pedometer_consume")
	private String   pedometer_consume;//": "0.00", //运动积累消耗
	@DatabaseField(columnName="is_leader")
	private String   is_leader;//": "0", //是否领导
	@DatabaseField(columnName="created_at")
	private String   created_at;//": "2015-11-17 09:50:19", //注册时间
	@DatabaseField(columnName="customer_image")
	private String   customer_image;//": "2015-11-17 09:50:19", //app启动页
	@Override
	public String toString() {
		return "User [id=" + id + ", department_id=" + department_id + ", customer_id=" + customer_id
				+ ", serial_number_id=" + serial_number_id + ", nick_name=" + nick_name + ", real_name=" + real_name
				+ ", access_token=" + access_token + ", birthday=" + birthday + ", height=" + height + ", weight="
				+ weight + ", avatar=" + avatar + ", mobile=" + mobile + ", email=" + email + ", job_number="
				+ job_number + ", score=" + score + ", pedometer_target=" + pedometer_target + ", pedometer_distance="
				+ pedometer_distance + ", pedometer_time=" + pedometer_time + ", pedometer_consume=" + pedometer_consume
				+ ", is_leader=" + is_leader + ", created_at=" + created_at + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getSerial_number_id() {
		return serial_number_id;
	}
	public void setSerial_number_id(String serial_number_id) {
		this.serial_number_id = serial_number_id;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJob_number() {
		return job_number;
	}
	public void setJob_number(String job_number) {
		this.job_number = job_number;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getPedometer_target() {
		return pedometer_target;
	}
	public void setPedometer_target(String pedometer_target) {
		this.pedometer_target = pedometer_target;
	}
	public String getPedometer_distance() {
		return pedometer_distance;
	}
	public void setPedometer_distance(String pedometer_distance) {
		this.pedometer_distance = pedometer_distance;
	}
	public String getPedometer_time() {
		return pedometer_time;
	}
	public void setPedometer_time(String pedometer_time) {
		this.pedometer_time = pedometer_time;
	}
	public String getPedometer_consume() {
		return pedometer_consume;
	}
	public void setPedometer_consume(String pedometer_consume) {
		this.pedometer_consume = pedometer_consume;
	}
	public String getIs_leader() {
		return is_leader;
	}
	public void setIs_leader(String is_leader) {
		this.is_leader = is_leader;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}



}

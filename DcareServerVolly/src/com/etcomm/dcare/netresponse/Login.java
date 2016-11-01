package com.etcomm.dcare.netresponse;

public class Login extends Content {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String user_id;

    private String avatar;
    public String job_number; //工号
    private String access_token;

    private String structure;
    private String structure_id;

    private String customer_id;

    private String serial_number_id;

    private String gender;
    private String nick_name;

    private String height;

    private String weight;

    private String score;

    private String customer_image;
    private String info_status;
    private String real_name;

    private String is_like;
    private String is_comment;
    private String islevel;
    private String mobile;
    private String email;

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

    public String getIslevel() {
        return islevel;
    }

    public void setIslevel(String islevel) {
        this.islevel = islevel;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public String getJob_number() {
        return job_number;
    }

    public void setJob_number(String job_number) {
        this.job_number = job_number;
    }

    public String getInfo_status() {
        return info_status;
    }

    public void setInfo_status(String info_status) {
        this.info_status = info_status;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getCustomer_image() {
        return customer_image;
    }

    public void setCustomer_image(String customer_image) {
        this.customer_image = customer_image;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    private String total_score;

    private String pedometer_target;

    private String pedometer_distance;

    private String pedometer_time;
    private String pedometer_consume;

    private String is_leader;

    private String birthday;
    private String birth_year;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public void setStructure_id(String structure_id) {
        this.structure_id = structure_id;
    }

    public String getStructure_id() {
        return this.structure_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_id() {
        return this.customer_id;
    }

    public void setSerial_number_id(String serial_number_id) {
        this.serial_number_id = serial_number_id;
    }

    public String getSerial_number_id() {
        return this.serial_number_id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return this.height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return this.score;
    }

    public void setPedometer_target(String pedometer_target) {
        this.pedometer_target = pedometer_target;
    }

    public String getPedometer_target() {
        return this.pedometer_target;
    }

    public void setPedometer_distance(String pedometer_distance) {
        this.pedometer_distance = pedometer_distance;
    }

    public String getPedometer_distance() {
        return this.pedometer_distance;
    }

    public void setPedometer_time(String pedometer_time) {
        this.pedometer_time = pedometer_time;
    }

    public String getPedometer_time() {
        return this.pedometer_time;
    }

    public void setPedometer_consume(String pedometer_consume) {
        this.pedometer_consume = pedometer_consume;
    }

    public String getPedometer_consume() {
        return this.pedometer_consume;
    }

    public void setIs_leader(String is_leader) {
        this.is_leader = is_leader;
    }

    public String getIs_leader() {
        return this.is_leader;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return this.birthday;
    }

    @Override
    public String toString() {
        return "Login [id=" + id + ", user_id=" + user_id + ", avatar=" + avatar + ", access_token=" + access_token
                + ", structure=" + structure + ", structure_id=" + structure_id + ", customer_id=" + customer_id
                + ", serial_number_id=" + serial_number_id + ", gender=" + gender + ", nick_name=" + nick_name
                + ", height=" + height + ", weight=" + weight + ", score=" + score + ", total_score=" + total_score
                + ", pedometer_target=" + pedometer_target + ", pedometer_distance=" + pedometer_distance
                + ", pedometer_time=" + pedometer_time + ", pedometer_consume=" + pedometer_consume + ", is_leader="
                + is_leader + ", birthday=" + birthday + ", birth_year=" + birth_year + "]";
    }

}

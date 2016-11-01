package com.etcomm.dcare.netresponse;

public class MineSportsItem extends Items {
    private String activity_id;

    private String totals;

    private String customer_id;// 客户Id

    private String type_id; // 推荐类型id （type:health-健康资讯 welfare-企业福利
    // activity-活动）

    private String title; // 标题

    private String detail_url;// "http://113.59.227.10:81/frontend/web/index.php?r=html5/activity-detail&id=5",
    // //详情地址

    private String type;// //类型 （1:健康资讯 2:企业福利 3：活动）

    private String image;// "http://113.59.227.10:81/upload/activity/DHZZEZz6KL6E_AtOimMx1sizh_R_o_cJ.jpg",
    // //图片

    private String start_at;// 活动开始时间

    private String deadline;

    private String end_at;// 活动结束时间

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    private String created_at;// 创建时间

    private String pv;// 浏览量

    private String number;// 活动报名人数

    private String status; // 活动进行状态
    private String desc;
    private String share_url;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_id() {
        return this.activity_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_id() {
        return this.customer_id;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getStart_at() {
        return this.start_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getEnd_at() {
        return this.end_at;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadline() {
        return this.deadline;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public void setTotals(String totals) {
        this.totals = totals;
    }

    public String getTotals() {
        return this.totals;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getDetail_url() {
        return this.detail_url;
    }
}

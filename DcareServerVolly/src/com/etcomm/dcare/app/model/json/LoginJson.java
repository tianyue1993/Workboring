package com.etcomm.dcare.app.model.json;

import com.etcomm.dcare.app.model.Header;

/**
 * @ClassName: UserInfoJson 
 * @Description: 登陆bean
 * @author etc 
 * @date 13 Apr, 2016 11:06:16 AM
 */
public class LoginJson extends BaseJson {
	
	public Content content;

	public class Content {
		public String user_id; //用户ID
		public String avatar; //用户头像 
		public String nick_name; //用户昵称
		public String access_token; //token
		public String structure_id; // 组织ID
		public String structure; //组织名称
		public String customer_id; //客户ID
		public String gender; //性别 1男
		public String job_number; //工号
		public String email; //邮箱
		public String height; //身高
		public String weight; //体重
		public String score; // 积分
		public String total_score; //累积积分
		public String pedometer_target; //运动目标
		public String pedometer_distance; //运动积累距离
		public String pedometer_time;//运动积累时间
		public String pedometer_consume;//运动积累消耗
		public String is_leader; //是否领导
		public String birthday; //生日
		public String birth_year; //出生年份
		public String is_sign; ////签到状态 1:已签到 0：未签到

	}
}

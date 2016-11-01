package com.etcomm.dcare.common;

import com.etcomm.dcare.app.utils.StringUtils;

public class PointsRule {

	/**
	 * 每日签到，连续签到，获取积分规则
	 */
	public static String getPointsByDay(String day){
		if(StringUtils.isEmpty(day)){
			day ="1";
		}
		int d = Integer.valueOf(day);
		if(d>=8){
			return "+5";
		}
		return "+2";
	}
}

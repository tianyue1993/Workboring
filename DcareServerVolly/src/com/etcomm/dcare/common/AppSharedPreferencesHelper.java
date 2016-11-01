package com.etcomm.dcare.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Preferences;

/**
 * @ClassName: AppSharedPreferencesHelper
 * @Description: 常用数据保存
 * 
 */

public class AppSharedPreferencesHelper {

	private static SharedPreferences mSharedPreferences;
	private static Editor mEditor;

	public static SharedPreferences getSharedPreferences() {

		if (mSharedPreferences == null) {
			mSharedPreferences = DcareApplication.getInstance().getSharedPreferences(Preferences.UserInfo, Context.MODE_PRIVATE);
		}
		return mSharedPreferences;
	}

	public static void setUserName(String name) {
		getSharedPreferences().edit().putString(Preferences.USER_NAME, name).commit();
	}

	public static String getUserName() {
		return getSharedPreferences().getString(Preferences.USER_NAME, "");
	}

	public static void setUserId(String id) {
		getSharedPreferences().edit().putString(Preferences.UserId, id).commit();
	}

	public static String getUserId() {
		return getSharedPreferences().getString(Preferences.UserId, "");
	}

	public static void setAccessToken(String name) {
		getSharedPreferences().edit().putString(Preferences.ACCESS_TOKEN, name).commit();
	}

	public static String getAccessToken() {
		return getSharedPreferences().getString(Preferences.ACCESS_TOKEN, "");
	}

	public static Editor getEditor() {

		if (mEditor == null) {
			mEditor = getSharedPreferences().edit();
		}
		return mEditor;
	}

	public static void clear() {
		mEditor = getEditor();
		mEditor.clear();
		mEditor.commit();
	}

	// public static String getUserWeight() {
	// return getSharedPreferences().getString(Constant.UserWeight, "65");
	// }
	// public static String getUserHeight() {
	// return getSharedPreferences().getString(Constant.UserHeight, "170");
	// }
	public static String getUserStepLeight() {
		return getSharedPreferences().getString(Preferences.UserStepLeight, "66");
	}

	public static void setBirth_year(String Birth_year) {
		getSharedPreferences().edit().putString("Birth_year", Birth_year).commit();
	}

	public static String getBirth_year() {
		return getSharedPreferences().getString("Birth_year", "1980");
	}

	public static void setAvatar(String avatar) {
		getSharedPreferences().edit().putString(Preferences.Avator, avatar).commit();
	}

	public static String getAvatar() {
		return getSharedPreferences().getString(Preferences.Avator, "");
	}

	public static void setStructure(String structure) {
		getSharedPreferences().edit().putString(Preferences.StructureName, structure).commit();
	}

	public static String getStructure() {
		return getSharedPreferences().getString(Preferences.StructureName, "");
	}

	public static void setNick_name(String nick_name) {
		getSharedPreferences().edit().putString(Preferences.NICK_NAME, nick_name).commit();
	}

	public static String getNick_name() {
		return getSharedPreferences().getString(Preferences.NICK_NAME, "");
	}

	// 真是姓名
	public static void setReal_name(String nick_name) {
		getSharedPreferences().edit().putString(Preferences.User_NAME, nick_name).commit();
	}

	public static String getJob_number() {
		return getSharedPreferences().getString(Preferences.JOB_NUMBER, "");
	}

	public static void setJob_number(String nick_name) {
		getSharedPreferences().edit().putString(Preferences.JOB_NUMBER, nick_name).commit();
	}

	public static String getReal_name() {
		return getSharedPreferences().getString(Preferences.User_NAME, "");
	}

	public static void setGender(String gender) {
		getSharedPreferences().edit().putString(Preferences.UserSex, gender).commit();
	}

	public static String getGender() {
		return getSharedPreferences().getString(Preferences.UserSex, "1");
	}

	public static void setHeight(String height) {
		getSharedPreferences().edit().putString(Preferences.UserHeight, height).commit();
	}

	public static String getHeight() {
		return getSharedPreferences().getString(Preferences.UserHeight, "170");
	}

	public static void setWeight(String weight) {
		getSharedPreferences().edit().putString(Preferences.UserWeight, weight).commit();
	}

	public static String getWeight() {
		return getSharedPreferences().getString(Preferences.UserWeight, "65");
	}

	/**
	 * 设置积分
	 * 
	 * @return
	 */
	public static void setScore(String score) {
		getSharedPreferences().edit().putString(Preferences.Score, score).commit();
	}

	/**
	 * 获取积分
	 * 
	 * @return
	 */
	public static String getScore() {
		return getSharedPreferences().getString(Preferences.Score, "");
	}

	public static void setPedometer_target(String pedometer_target) {
		getSharedPreferences().edit().putString(Preferences.PedometerTarget, pedometer_target).commit();
	}

	public static String getPedometer_target() {
		return getSharedPreferences().getString(Preferences.PedometerTarget, "10000");
	}

	public static void setPedometer_distance(String pedometer_target) {
		getSharedPreferences().edit().putString(Preferences.Pedometer_Distance, pedometer_target).commit();
	}

	public static String getPedometer_distance() {
		return getSharedPreferences().getString(Preferences.Pedometer_Distance, "");
	}

	public static void setPedometer_time(String pedometer_time) {
		getSharedPreferences().edit().putString(Preferences.Pedometer_Time, pedometer_time).commit();
	}

	public static String getPedometer_time() {
		return getSharedPreferences().getString(Preferences.Pedometer_Time, "");
	}

	public static void setPedometer_consume(String pedometer_consume) {
		getSharedPreferences().edit().putString(Preferences.Pedometer_Consume, pedometer_consume).commit();
	}

	public static String getPedometer_consume() {
		return getSharedPreferences().getString(Preferences.Pedometer_Consume, "");
	}

	public static void setIs_leader(String is_leader) {
		getSharedPreferences().edit().putString(Preferences.Is_Leader, is_leader).commit();
	}

	public static String getIs_leader() {
		return getSharedPreferences().getString(Preferences.Is_Leader, "");
	}

	public static void setMacAddress(String address) {
		// TODO Auto-generated method stub
		getSharedPreferences().edit().putString(Preferences.MACAddress, address).commit();
	}

	public static String getMacAddress() {
		// TODO Auto-generated method stub
		return getSharedPreferences().getString(Preferences.MACAddress, "");
	}

	public static void setBlueDeviceRssi(int rssi) {
		// TODO Auto-generated method stub
		getSharedPreferences().edit().putInt(Preferences.BlueDeviceRssi, rssi).commit();
	}

	public static int getBlueDeviceRssi() {
		// TODO Auto-generated method stub
		return getSharedPreferences().getInt(Preferences.BlueDeviceRssi, 0);
	}

	public static void setBlueDeviceName(String name) {
		// TODO Auto-generated method stub
		getSharedPreferences().edit().putString(Preferences.BlueDeviceName, name).commit();
	}

	public static String getBlueDeviceName() {
		// TODO Auto-generated method stub
		return getSharedPreferences().getString(Preferences.BlueDeviceName, "");
	}

	public static boolean getHaveReceiveUnReadData() {
		return getSharedPreferences().getBoolean(Preferences.IsHaveReceiveUnReadData, false);

	}

	public static void setHaveReceiveUnReadData(boolean isRead) {
		getSharedPreferences().edit().putBoolean(Preferences.IsHaveReceiveUnReadData, isRead).commit();
	}

	/**
	 * 设置积分
	 * 
	 * @return
	 */
	public static void setTotalScore(String score) {
		getSharedPreferences().edit().putString(Preferences.TotalScore, score).commit();
	}

	/**
	 * 获取积分
	 * 
	 * @return
	 */
	public static String getTotalScore() {
		return getSharedPreferences().getString(Preferences.TotalScore, "");
	}

}

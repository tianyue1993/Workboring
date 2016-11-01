package com.etcomm.dcare.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Preferences;

public class AppSettingPreferencesHelper {
	private static SharedPreferences mSharedPreferences;
	private static Editor mEditor;

	public static SharedPreferences getSharedPreferences() {

		if (mSharedPreferences == null) {
			mSharedPreferences = DcareApplication.getInstance().getSharedPreferences(Preferences.SPSetting,
					Context.MODE_PRIVATE);
		}
		return mSharedPreferences;
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

	public static void setIsScreenLongOn(boolean isScreenOn) {
		getSharedPreferences().edit().putBoolean(Preferences.SPSetting_ScreenLongOn, isScreenOn).commit();
	}

	public static boolean isScreenLongOn() {
		return getSharedPreferences().getBoolean(Preferences.SPSetting_ScreenLongOn, false);//默认计步设置-常亮设置
	}
	public static void setIsPushMsg(boolean ispush) {
		getSharedPreferences().edit().putBoolean(Preferences.SPSetting_PushMsg, ispush).commit();
	}

	public static boolean isPushMsg() {
		return getSharedPreferences().getBoolean(Preferences.SPSetting_PushMsg, true);
	}
	public static void setIsPushMsg_Like(boolean ispush) {
		getSharedPreferences().edit().putBoolean(Preferences.SPSetting_PushMsg_Like, ispush).commit();
	}

	public static boolean isPushMsg_Like() {
		return getSharedPreferences().getBoolean(Preferences.SPSetting_PushMsg_Like, true);
	}
	public static void setIsPushMsg_Comment(boolean ispush) {
		getSharedPreferences().edit().putBoolean(Preferences.SPSetting_PushMsg_Comment, ispush).commit();
	}

	public static boolean isPushMsg_Comment() {
		return getSharedPreferences().getBoolean(Preferences.SPSetting_PushMsg_Comment, true);
	}

	public static void setIfSoftPedometerOn(boolean ispush) {
		getSharedPreferences().edit().putBoolean(Preferences.IfSoftPedometerOn, ispush).commit();
	}

	public static boolean isSoftPedometerOn() {
		return getSharedPreferences().getBoolean(Preferences.IfSoftPedometerOn, true);
	}
	public static void setSoftPedometerSensitivity(int s) {
		getSharedPreferences().edit().putInt(Preferences.SoftPedometerSensitivity, s).commit();
	}

	public static int getSoftPedometerSensitivity() {
		return getSharedPreferences().getInt(Preferences.SoftPedometerSensitivity, 5);
	}
}

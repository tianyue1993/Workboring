package com.etcomm.dcare.app.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.etcomm.dcare.app.common.DcareApplication;

/**
 * 
 * @ClassName: SystemInfoUtils
 * @Description: 系统信息
 * @author etc
 * @date 12 Apr, 2016 5:56:41 PM
 */
public class SystemInfoUtils {
	/**
	 * @Title: readDeviceId
	 * @Description: 获取手机信息？ 后期优化
	 * @param @param ctx
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String readDeviceId(Context ctx) {
		String device_id = "";
		device_id = "" + android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		if (device_id.length() < 5) {
			TelephonyManager tm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
			String tmdevice_id = "" + tm.getDeviceId();
			if (tmdevice_id.length() < 5) {
				String deviceSereisId = tm.getSimSerialNumber();
				if (deviceSereisId.length() < 5) {
					String m_szDevIDShort = "35" + // we make this look like a
													// valid IMEI
							Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; // 13
																																																																																																				// digits
					device_id = "imei" + m_szDevIDShort;
				} else {
					device_id = "tmSimSerialNumber" + deviceSereisId;
				}
			} else {
				device_id = "tmDeviceId" + tmdevice_id;
			}
		}
		return device_id;
	}

	/**
	 * @Title: checkNetworkAvailable
	 * @Description: 网络是否连接
	 * @param @param context
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean checkNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return true;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测service是否正在运行
	 * 
	 * @param serviceClassName
	 * @return
	 */
	public static boolean isServiceRunning(String serviceClassName) {
		final ActivityManager activityManager = (ActivityManager) DcareApplication.mCon.getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

		for (RunningServiceInfo runningServiceInfo : services) {
			if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取APP是是否在前台运行
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					LogUtil.d("后台", appProcess.processName);
					return true;
				} else {
					LogUtil.d("前台", appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
}

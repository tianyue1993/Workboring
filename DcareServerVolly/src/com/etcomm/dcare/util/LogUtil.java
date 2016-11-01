package com.etcomm.dcare.util;

import android.util.Log;

public class LogUtil {
	protected static final String TAG = "Dcare";

	private static boolean disable = false; //TODO: true (release)
	
	/**
	 * the constructor
	 */
	public LogUtil() {
	}

	/**
	 * check if the log is disabled
	 * 
	 * @return
	 */
	public static boolean isEnabled() {
		return !disable;
	}
	
	public static void setEnable(boolean toggle) {
		disable = !toggle;
	}

	/**
	 * send a VERBOSE log message.
	 * 
	 * @param msg
	 */
	public static void v(String msg) {
		if (isEnabled()) {
			Log.v(TAG, msg);
		}
	}
	
	public static void v(String tag, String msg) {
		if (isEnabled()) {
			Log.v(tag, msg);
		}
	}

	/**
	 * send a VERBOSE log message and log the exception
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void v(String msg, Throwable throwable) {
		if (isEnabled()) {
			Log.v(TAG, msg, throwable);
		}
	}
	
	public static void v(String tag, String msg, Throwable throwable) {
		if (isEnabled()) {
			Log.v(tag, msg, throwable);
		}
	}

	/**
	 * send a DEBUG log message
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (isEnabled()) {
			Log.d(TAG, msg);
		}
	}
	
	public static void d(String tag, String msg) {
		if (isEnabled()) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable throwable) {
		if (isEnabled()) {
			Log.d(tag, msg);
		}
	}
	

	/**
	 * send a INFO log message
	 * 
	 * @param msg
	 */
	public static void i(String msg) {
		if (isEnabled()) {
			Log.i(TAG, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (isEnabled()) {
			Log.i(tag, msg);
		}
	}

	/**
	 * send a INFO log message and log the exception
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void i(String msg, Throwable throwable) {
		if (isEnabled()) {
			Log.i(TAG, msg);
		}
	}
	
	/**
	 * send a ERROR log message
	 * 
	 * @param msg
	 */
	public static void e(String msg) {
		if (isEnabled()) {
			Log.e(TAG, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (isEnabled()) {
			Log.e(tag, msg);
		}
	}

	/**
	 * send a ERROR log message and log the exception
	 * 
	 * @param msg
	 * @param throwable
	 */
	public static void e(String msg, Throwable throwable) {
		if (isEnabled()) {
			Log.e(TAG, msg);
		}
	}
	
	public static void e(String tag, String msg, Throwable throwable) {
		if (isEnabled()) {
			Log.e(tag, msg);
		}
	}
}

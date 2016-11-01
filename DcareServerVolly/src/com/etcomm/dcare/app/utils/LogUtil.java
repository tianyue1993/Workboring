package com.etcomm.dcare.app.utils;

import android.util.Log;

import com.etcomm.dcare.app.common.config.Constants;
import com.google.code.microlog4android.Logger;

/** 
 * @ClassName: LogUtils 
 * @author etc 
 * @date 2 Apr, 2016 2:02:56 PM  
 */
public class LogUtil {
		public static  Logger logger ; 
		public static  boolean IS_LOG = true;
		public static void d(String tag, String msg) {
			try {
				if (IS_LOG) {
					Log.d(tag, tag + " : " + msg);
				}
			} catch (Throwable t) {
			    t.printStackTrace();
			}
			if(Constants.log2File){
				logger.debug(tag + " : " + msg); 
			}
		}

		public static void i(String tag, String msg) {
			try {
				if (IS_LOG) {
					Log.i(tag, tag + " : " + msg);
				}
			} catch (Throwable t) {
			}
			if(Constants.log2File){
				logger.info(tag + " : " + msg); 
			}
		}
		
		public static void v(String tag, String msg) {
			try {
				if (IS_LOG) {
					Log.v(tag, tag + " : " + msg);
				}
			} catch (Throwable t) {
			}
			if(Constants.log2File){
				logger.info(tag + " : " + msg); 
			}
		}

		public static void e(String tag, String msg) {
			try {
					Log.e(tag, tag + " : " + msg);
			} catch (Throwable t) {
			}
			
			if(Constants.log2File){
				logger.fatal(tag + " : " + msg); 
			}
		}
		
		public static void w(String tag, String msg) {
			try {
				if (IS_LOG) {
					Log.w(tag, tag + " : " + msg);
				}
			} catch (Throwable t) {
			}
			
			if(Constants.log2File){
				logger.info(tag + " : " + msg); 
			}
		}

}

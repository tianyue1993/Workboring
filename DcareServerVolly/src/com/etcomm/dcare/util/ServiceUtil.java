package com.etcomm.dcare.util;


import java.util.ArrayList;

import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.service.DispatchService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * 
 * @author iexpressbox
 *
 */
public class ServiceUtil {

	    private static  String tag ="ServiceUtil";

		public static void invokeTimerDispatchService(Context context){
	        Log.i(tag, "invokeTimerPOIService wac called.." );
	        PendingIntent alarmSender = null;
	        Intent startIntent = new Intent(context, DispatchService.class);
	        startIntent.setAction(Preferences.Dispatch_SERVICE_ACTION);
	        try {
	            alarmSender = PendingIntent.getService(context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	        } catch (Exception e) {
	            Log.i(tag , "failed to start " + e.toString());
	        }
	        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
	        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Preferences.ELAPSED_TIME, alarmSender);
	    }

	    public static void cancleAlarmManager(Context context){
	        Log.i(tag, "cancleAlarmManager to start ");
	        Intent intent = new Intent(context,DispatchService.class);
	    	intent.setAction(Preferences.Dispatch_SERVICE);
	        PendingIntent pendingIntent=PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
	        AlarmManager alarm=(AlarmManager)context.getSystemService(Activity.ALARM_SERVICE);
	        alarm.cancel(pendingIntent);
	    }

		public static boolean isServiceRunning(Context mContext, String dispatchService) {
			ActivityManager myManager = (ActivityManager) mContext.getApplicationContext()
					.getSystemService(Context.ACTIVITY_SERVICE);
			ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
					.getRunningServices(Integer.MAX_VALUE);
			for (int i = 0; i < runningService.size(); i++) {
				if (runningService.get(i).service.getClassName().toString()
						.equals(dispatchService)) {
					return true;
				}
			}
			return false;
		}
		
}

package com.etcomm.dcare.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.util.ServiceUtil;

import org.joda.time.DateTime;

import me.chunyu.pedometerservice.PedometerCounterService;

/**
 * 调度计划
 * 1. 系统开启时后台运行程序
 * 2. alarm 信息保存与执行，0点时发送日期广播
 * 3. 数据定时上传功能，有待进一步完善 
 * @author iexpressbox
 *
 */
public class DispatchService extends BaseService {
	private Context mContext;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		Log.i(tag, "onCreate");
		if(!ServiceUtil.isServiceRunning(this, Preferences.DcareService)){
			PedometerCounterService.initAppService(mContext);
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				stopSelf();
			}
		}, 5*1000);
		Log.i(tag, "onStartCommand");
		AlarmManager am = (AlarmManager) this.getSystemService(Activity.ALARM_SERVICE);
		Intent alarmintent=new Intent(this,BootBroadcastReceiver.class);
		alarmintent.setAction(Preferences.ACTION_NOTIFY_DATE_CHANGED);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, alarmintent,0);
		DateTime datetime = new DateTime();
//	        datetime.plusMinutes(2);
//	        am.setRepeating(AlarmManager.RTC_WAKEUP, datetime.getMillis(), 60*1000, pi);
		long maxTodayTime = datetime.millisOfDay().withMaximumValue().getMillis();
		Log.i(tag, "maxTodayTime: "+maxTodayTime);
		am.setRepeating(AlarmManager.RTC_WAKEUP, maxTodayTime, 24*60*60*1000, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		Log.i(tag, "onDestroy");
		super.onDestroy();
	}
}

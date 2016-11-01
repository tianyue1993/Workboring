package com.etcomm.dcare.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.util.ServiceUtil;

import me.chunyu.pedometerservice.PedometerCounterService;

public class BootBroadcastReceiver extends BroadcastReceiver {

	private static final String tag = "BootBroadcastReceiver";
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mContext = context;
		if (intent == null || intent.getAction() == null) {
			if (!ServiceUtil.isServiceRunning(mContext, Preferences.DcareService)) {
				PedometerCounterService.initAppService(mContext);
			}
			return;
		}
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.i(tag, "BroadcastReceiver onReceive here,action = " + intent.getAction());
			Handler handler = new Handler(Looper.getMainLooper());
			// after reboot the device,about 2 minutes later,upload the POI info
			// to server
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (!ServiceUtil.isServiceRunning(mContext, Preferences.Dispatch_SERVICE)) {
						// ServiceUtil.invokeTimerDispatchService(mContext);
						Intent intent = new Intent(mContext, DispatchService.class);
						mContext.startService(intent);
					}
				}
			}, Preferences.BROADCAST_ELAPSED_TIME_DELAY);
		} else if (intent.getAction().equals(Preferences.ACTION_NOTIFY_DATE_CHANGED)) {
			Log.i(tag, "BroadcastReceiver onReceive here,action = " + intent.getAction());
			mContext.sendBroadcast(new Intent(Preferences.ACTION_DATE_CHANGED));
		}
	}

}

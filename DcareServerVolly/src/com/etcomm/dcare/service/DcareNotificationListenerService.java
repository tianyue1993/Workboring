package com.etcomm.dcare.service;

import android.annotation.SuppressLint;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

@SuppressLint("NewApi")
public class DcareNotificationListenerService extends NotificationListenerService {

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void onNotificationPosted(StatusBarNotification sbn) {
//		// TODO Auto-generated method stub
//		super.onNotificationPosted(sbn);
//	}
//	@Override
//	public void onNotificationRemoved(StatusBarNotification sbn) {
//		// TODO Auto-generated method stub
//		super.onNotificationRemoved(sbn);
//	}
//	@Override
//	public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
//		// TODO Auto-generated method stub
//		super.onNotificationRemoved(sbn, rankingMap);
//	}
}

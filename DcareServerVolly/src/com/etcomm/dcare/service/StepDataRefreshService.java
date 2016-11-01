package com.etcomm.dcare.service;

import android.content.Intent;

/*
 * 每次开启应用，对数据进行整理
 * 对Data 中的计步数进行统计。
 * 分别生成小时步数表和天步数表,供显示使用
 */
public class StepDataRefreshService extends BaseService {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}

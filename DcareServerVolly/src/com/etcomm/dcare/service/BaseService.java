package com.etcomm.dcare.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * 集成发送广播，记录数据的基础service
 * @author iexpressbox
 *
 */
public class BaseService extends Service {
	protected final String tag = getClass().getSimpleName();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.etcomm.dcare;

import android.app.Activity;

import com.etcomm.dcare.app.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class SettingsPedometerSettingsActivity extends BaseActivity {

	@Override
	protected Activity atvBind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initDatas() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int setLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(tag);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(tag);
	}

}

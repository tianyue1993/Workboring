package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;

public class BindHcActivity extends BaseActivity {

	@Override
	protected Activity atvBind() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected void initDatas() {
		// TODO Auto-generated method stub
		setFinishOnTouchOutside(false); //设置区域外不关闭

	}

	@Override
	protected int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.bindhc_activity;
	}

}

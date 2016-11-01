package com.etcomm.dcare.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.etcomm.dcare.MsgListActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.login.LoginActivity;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.widget.ProgressDialog;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;

public abstract class BaseFragment extends Fragment {
	protected final int DIALOG_DEFAULT=0;
	protected ProgressDialog mProgress;
	String tag = getClass().getSimpleName();
	Activity mContext;
	Intent intent;
	protected View.OnClickListener msgClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent(mContext,MsgListActivity.class);
			startActivity(intent);
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		registerBroadcastReceiver();

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		registerBroadcastReceiver();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment, null);
		TextView textView = (TextView) view.findViewById(R.id.txt_content);
		textView.setText(initContent());
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mContext = activity;
		super.onAttach(activity);
	}
	public abstract String initContent();
	public abstract void receive_msg_data();
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
//    	unregisterBroadcastReceiver();
		unregisterBroadcastReceiver();
		super.onDestroy();
	}
	void registerBroadcastReceiver(){
		IntentFilter filter = new IntentFilter(Preferences.ACTION_MSG_DATA);
		getActivity().registerReceiver(mReceiver, filter);
	}
	void unregisterBroadcastReceiver(){
		if(mReceiver!=null){
			getActivity().unregisterReceiver(mReceiver);
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Check action just to be on the safe side.
			Log.i(tag, "onReceive:  " + intent.getAction());
			if (intent.getAction().equals(Preferences.ACTION_MSG_DATA)) {
				AppSharedPreferencesHelper.setHaveReceiveUnReadData(true);
				receive_msg_data();
			} else {
				// updateViewByBlueData();
			}
		}
	};

	public static final int MSG_CLOSE_PROGRESS = 1;
	public static final int MSG_SHOW_TOAST = 2;
	public Handler baseHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case MSG_CLOSE_PROGRESS:
					// cancelProgress(); //加载进度条
					break;
				case MSG_SHOW_TOAST:
					int resid = msg.arg1;
					if (resid > 0) {
						showToast(resid);
					} else if (msg.obj != null) {
						String mes = (String) msg.obj;
						if (!TextUtils.isEmpty(mes)) {
							showToast(mes);
						}
					}
					break;
			}
		};
	};
	Toast toast = null;

	protected void showToast(String message) {
		if (toast != null) {
			return;
		}
		toast = Toast.makeText(mContext, (!TextUtils.isEmpty(message)) ? message
				: this.getString(R.string.network_error), Toast.LENGTH_SHORT);
		toast.show();
		baseHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (toast != null) {
					toast.cancel();
					toast = null;
				}
			}
		}, 2000);
	}

	protected void showToast(int resid) {
		if (toast != null) {
			return;
		}
		toast = Toast.makeText(mContext, resid, Toast.LENGTH_SHORT);
		toast.show();
		baseHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (toast != null) {
					toast.cancel();
					toast = null;
				}
			}
		}, 2000);
	}

	protected void startAtvTask(Class cls) {
		DcareApplication.mCon.startActivity(setAtvTaskParameters(cls));
	}

	protected void startAtvTask(Class cls, String... str) {
		if (str.length < 2) {
			// Log.e(TAG, StringUtils.id2String(R.string.atv_parameter));
			showToast(R.string.atv_parameter);
		}
		DcareApplication.mCon.startActivity(setAtvTaskParameters(cls, str));
	}

	protected Intent setAtvTaskParameters(Class cl) {
		intent = new Intent(DcareApplication.mCon, cl);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	protected Intent setAtvTaskParameters(Class cl, String... str) {
		// intent = new Intent(DcareApplication.mCon, cl);

		intent = setAtvTaskParameters(cl);
		for (int i = 0; i < str.length; i += 2) {
			intent.putExtra(str[i], str[i + 1]);
		}
		return intent;
	}
	static int count =0;
	protected synchronized void exceptionCode(int code){
		switch (code) {
			case 10000:
				showToast(R.string.token_error);
//			if (count<=1) {
				mContext.sendBroadcast(new Intent(Preferences.ACTION_USER_EXIT));
				startAtvTask(LoginActivity.class);
//				startAtvTask(LoginActivityNew.class);
//			}

				break;

			default:

				break;
		}
	}

	public void showProgress(int resId, boolean cancel) {
		mProgress = new ProgressDialog(mContext);
		if (resId <= 0) {
			mProgress.setMessage(R.string.loading_data, cancel);
		} else {
			mProgress.setMessage(resId, cancel);
		}
		mProgress.show();
	}
	public void cancelmDialog(){
		if (mProgress != null) {
			mProgress.dismiss();
		}

	}
}

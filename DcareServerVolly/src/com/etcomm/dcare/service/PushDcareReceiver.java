package com.etcomm.dcare.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.MainActivity;
import com.etcomm.dcare.MsgListActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.util.NotifyUtil;
import com.igexin.sdk.PushConsts;

public class PushDcareReceiver extends BroadcastReceiver {
	private static final String tag = "PushDcareReceiver";
	/**
	 * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView ==
	 * null)
	 */
	// public static StringBuilder payloadData = new StringBuilder();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences sp = context.getSharedPreferences("MSGNotify", Context.MODE_PRIVATE);
		int msgcount = sp.getInt("MSGNotifyId", 100);
		Bundle bundle = intent.getExtras();
		Log.d(tag, "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
			case PushConsts.GET_MSG_DATA:
				// 获取透传数据
				String appid = bundle.getString("appid");
				byte[] payload = bundle.getByteArray("payload");

				String taskid = bundle.getString("taskid");
				String messageid = bundle.getString("messageid");
				// String actionLocKey = bundle.getString("actionLocKey");
				// String badge = bundle.getString("badge");
				// String message = bundle.getString("message");
				// String sound = bundle.getString("sound");
				// String locKey = bundle.getString("locKey");
				// String locArgs = bundle.getString("locArgs");
				// String launchImage = bundle.getString("launchImage");
				// Log.i(tag, "actionLocKey: "+actionLocKey+" badge: "+badge +"
				// message: "+message+" sound: "
				// +sound+" locKey: "+locKey+" locArgs: "+locArgs+" launchImage:
				// "+launchImage);
				// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
				// boolean result =
				// PushManager.getInstance().sendFeedbackMessage(context, taskid,
				// messageid, 90001);
				// Log.i(tag,"taskid: "+taskid+" messageid: "+messageid +" result:
				// "+ (result ? "成功" : "失败"));

				if (payload != null) {
					String data = new String(payload);
					// if(!AppSettingPreferencesHelper.isPushMsg()){
					// Log.i(tag, "isPushMsg fasle");
					// return;
					// }
					Log.d(tag, "receiver payload : " + data);
					if (MainActivity.isActive) {// app正在运行,发送广播
						// if(!isAppIsInBackground(context)){//app正在运行,发送广播
						Log.i(tag, "MainActivity.isActive  sendBroadcast");
						JSONObject obj = JSON.parseObject(data);
						int type = obj.getInteger("type");
						// if(type == 2){
						// if(!AppSettingPreferencesHelper.isPushMsg_Like()){
						// return;
						// }
						// if(!AppSettingPreferencesHelper.isPushMsg_Comment()){
						// return;
						// }

//					AppSharedPreferencesHelper.setHaveReceiveUnReadData(true);
						// }else{
						//
						// }

					}

					JSONObject obj = JSON.parseObject(data);
					// "type": 2, //推送类型 1:挑战 2:话题 3:活动  0：通知
					int type = obj.getInteger("type");
					if (type == 2) {
						// 发送通知
						AppSharedPreferencesHelper.setHaveReceiveUnReadData(true);
						Intent broadcast = new Intent();
						broadcast.setAction(Preferences.ACTION_MSG_DATA);
						context.sendBroadcast(broadcast);
						String topic_id = obj.getString("topic_id");
						String detail_id = obj.getString("detail_id");
						String user_id = obj.getString("user_id");
						String is_like = obj.getString("is_like");
						String topic_list_type = obj.getString("topic_list_type");
						String topic_name = obj.getString("topic_name");
						String detail = obj.getString("detail");
						String titlestr = obj.getString("title");
						// if(!AppSettingPreferencesHelper.isPushMsg_Like()){
						// return;
						// }
						// if(!AppSettingPreferencesHelper.isPushMsg_Comment()){
						// return;
						// }
						// topic_id = intent.getStringExtra("topic_id");
						// user_id = intent.getStringExtra("user_id");
						// topic_name = intent.getStringExtra("topic_name");
						// isAttentioned =
						// intent.getBooleanExtra("isAttentioned", false);
						// 设置想要展示的数据内容
						Intent notifyintent;
						if(topic_list_type.equals("1")){//点赞
							notifyintent = new Intent(context, MsgListActivity.class);
							notifyintent.putExtra("topic_id", topic_id);
							notifyintent.putExtra("user_id", user_id);
//						notifyintent.putExtra("isAttentioned",is_like.equals("1")?true:false);
							notifyintent.putExtra("isAttentioned",is_like);
							notifyintent.putExtra("topic_name", topic_name);/// ??????????????????????????
							notifyintent.putExtra("isFromMSG", true);
//						startActivity(notifyintent);
						}else{
							notifyintent = new Intent(context, MsgListActivity.class);
							notifyintent.putExtra("topic_id", topic_id);
							notifyintent.putExtra("user_id", user_id);
							notifyintent.putExtra("disscuss_id", detail_id);
							notifyintent.putExtra("isAttentioned", is_like.equals("1")?true:false);
							notifyintent.putExtra("topic_name", topic_name);/// ??????????????????????????
							notifyintent.putExtra("isFromMSG", true);
//						startActivity(notifyintent);
						}
//					Intent notifyintent = new Intent(context, TopicDisscussListActivity.class);
//					notifyintent.putExtra("topic_id", String.valueOf(topic_id));
//					notifyintent.putExtra("user_id", String.valueOf(user_id));
//					notifyintent.putExtra("isAttentioned", is_like);
//					notifyintent.putExtra("topic_name", topic_name);
//					notifyintent.putExtra("detail", detail);
//					notifyintent.putExtra("isFromMSG", true);
//					notifyintent.setFlags(Intent.FLAG_ACTIVITySINGLE_TOP);
						PendingIntent pIntent = PendingIntent.getActivity(context, 1, notifyintent,
								PendingIntent.FLAG_UPDATE_CURRENT);
						int smallIcon = R.drawable.logo;
						String ticker = titlestr;
						String title = "上班趣";
						String content = titlestr;
						Log.w(tag, "显示 通知 ");
						// 实例化工具类，并且调用接口
						Log.i(tag, "notify1 msgcount  "+msgcount);
						NotifyUtil notify1 = new NotifyUtil(context, msgcount);
						msgcount++;
						sp.edit().putInt("MSGNotifyId", msgcount).commit();
						notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, true, true, false);
					}else if(type == 3){
						Intent broadcast = new Intent();
						broadcast.setAction(Preferences.ACTION_MSG_DATA);
						context.sendBroadcast(broadcast);
						String detail = obj.getString("detail");
						String titlestr = obj.getString("title");
						NotifyUtil notify1 = new NotifyUtil(context, msgcount);
						msgcount++;
						sp.edit().putInt("MSGNotifyId", msgcount).commit();
						Intent notifyintent = new Intent(context, MsgListActivity.class);
						notifyintent.putExtra("isFromMSG", true);
//					notifyintent.putExtra("isAttentioned",is_like.equals("1")?true:false);
						PendingIntent pIntent = PendingIntent.getActivity(context, 1, notifyintent ,
								PendingIntent.FLAG_UPDATE_CURRENT);
						int smallIcon = R.drawable.logo;
						String ticker = titlestr ;
						notify1.notify_normal_singline(pIntent, smallIcon, ticker , titlestr, detail, true, true, false);
					}else{
						String detail = obj.getString("detail");
						String titlestr = obj.getString("title");
						NotifyUtil notify1 = new NotifyUtil(context, msgcount);
						msgcount++;
						sp.edit().putInt("MSGNotifyId", msgcount).commit();
						Intent notifyintent = new Intent();
						PendingIntent pIntent = PendingIntent.getActivity(context, 1, notifyintent ,
								PendingIntent.FLAG_UPDATE_CURRENT);
						int smallIcon = R.drawable.logo;
						String ticker = titlestr ;
						notify1.notify_normal_singline(pIntent, smallIcon, ticker , titlestr, detail, true, true, false);
					}
					// }
					// payloadData.append(data);
					// payloadData.append("\n");

					// if (GetuiSdkDemoActivity.tLogView != null) {
					// GetuiSdkDemoActivity.tLogView.append(data + "\n");
					// }
				}
				break;

			case PushConsts.GET_CLIENTID:
				// 获取ClientID(CID)
				// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
				String cid = bundle.getString("clientid");
				Log.i(tag, "GET_CLIENTID:" + cid);
				// if (GetuiSdkDemoActivity.tView != null) {
				// GetuiSdkDemoActivity.tView.setText(cid);
				// }
				break;

			case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d(tag, "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = "
			 * + taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid);
			 * Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			case PushConsts.GET_SDKONLINESTATE:
				// 获取透传数据
				byte[] payload2 = bundle.getByteArray("payload");
				if (payload2 != null) {
					String data = new String(payload2);

					Log.d(tag, "receiver payload : " + data);
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 判断某个界面是否在前台
	 *
	 * @param context
	 * @param className
	 *            某个界面名称
	 */
	@SuppressWarnings("deprecation")
	private boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	private boolean isAppIsInBackground(Context context) {
		boolean isInBackground = true;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
			List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
				// 前台程序
				if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					for (String activeProcess : processInfo.pkgList) {
						if (activeProcess.equals(context.getPackageName())) {
							isInBackground = false;
						}
					}
				}
			}
		} else {
			@SuppressWarnings("deprecation")
			List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
			ComponentName componentInfo = taskInfo.get(0).topActivity;
			if (componentInfo.getPackageName().equals(context.getPackageName())) {
				isInBackground = false;
			}
		}

		return isInBackground;
	}
}

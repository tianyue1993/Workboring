package com.etcomm.dcare;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.activity.login.LoginActivity;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.fragment.FragmentFactory;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.UpdateObj;
import com.etcomm.dcare.service.DispatchService;
import com.etcomm.dcare.widget.DialogFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import me.chunyu.pedometerservice.IntentConsts;
import me.chunyu.pedometerservice.PedometerCounterService;

public class MainActivity extends FragmentActivity {
    protected static final String tag = "MainActivity";
    public static boolean isActive = false;
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;
    private RadioButton radio_suggest;
    private RadioButton radio_around;
    private RadioButton radio_walk;
    private RadioButton radio_find;
    private RadioButton radio_mine;
    private Dialog noticeDialog;
    private Dialog mustUpdateDialog;
    public Context mContext;
    protected String apkurl;
    private DownLoadBroadcastReceiver receiver;
    private ScreenStatusReceiver receiver2;
    private boolean isFromMSG;
    private boolean flag = true;

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

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Log.i(tag, "onNewIntent");
        if (intent != null) {
            isFromMSG = intent.getBooleanExtra("isFromMSG", false);
        }
        if (isFromMSG) {
            radio_suggest.setChecked(true);
        } else {
            radio_walk.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Preferences.ACTION_USER_EXIT);
        filter.addAction(Preferences.ACTION_USER_LOGIN);
        filter.addAction(Preferences.ACTION_USER_REGISTER);
        filter.addAction(Preferences.ACTION_CLEAR_ALLDATA);
        filter.addAction(Preferences.OPEN_NEWS);
        registerReceiver(mReceiver, filter);
        if (intent != null) {
            isFromMSG = intent.getBooleanExtra("isFromMSG", false);
        }
        startService(new Intent(this, PedometerCounterService.class));
        mContext = this;
        DcareApplication.getInstance().addActivity(this);
        Intent stepCountIntent = new Intent(IntentConsts.SET_OFFSET_DATA_FILTER);
        stepCountIntent.putExtra(IntentConsts.SCS_OFFSET_DATA_EXTRA, 0);
        sendBroadcast(stepCountIntent);

        Intent motionTimeIntent = new Intent(IntentConsts.SET_MOTION_TIME_FILTER);
        motionTimeIntent.putExtra(IntentConsts.SCS_MOTION_TIME_EXTRA, 0L);
        sendBroadcast(motionTimeIntent);

        setContentView(R.layout.activity);
        Log.i(tag, "Uid: " + android.os.Process.myUid() + " Tid: " + android.os.Process.myTid() + " Pid: " + android.os.Process.myPid());

        fragmentManager = getSupportFragmentManager();
        radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        radio_suggest = (RadioButton) findViewById(R.id.radio_suggest);
        radio_around = (RadioButton) findViewById(R.id.radio_around);
        radio_walk = (RadioButton) findViewById(R.id.radio_walk);
        radio_find = (RadioButton) findViewById(R.id.radio_find);
        radio_mine = (RadioButton) findViewById(R.id.radio_mine);
        registerReceiver();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Log.i(tag, "onCheckedChanged checkedId:" + checkedId);
                switch (checkedId) {
                    case 1:
                    case R.id.radio_suggest:
                        // 推荐 ->头条
                        radio_suggest.setTextColor(getResources().getColor(R.color.dcare_tab_selected));
                        radio_around.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_walk.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_find.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_mine.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        break;
                    case 2:
                    case R.id.radio_around:
                        // 身边
                        radio_suggest.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_around.setTextColor(getResources().getColor(R.color.dcare_tab_selected));
                        radio_walk.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_find.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_mine.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        break;
                    case 3:
                    case R.id.radio_walk:
                        // 健步
                        radio_suggest.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_around.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_walk.setTextColor(getResources().getColor(R.color.dcare_tab_selected));
                        radio_find.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_mine.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        break;
                    case 4:
                    case R.id.radio_find:
                        // 发现
                        radio_suggest.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_around.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_walk.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_find.setTextColor(getResources().getColor(R.color.dcare_tab_selected));
                        radio_mine.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        break;
                    case 5:
                    case R.id.radio_mine:
                        // 我的
                        radio_suggest.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_around.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_walk.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_find.setTextColor(getResources().getColor(R.color.dcare_tab_unselected));
                        radio_mine.setTextColor(getResources().getColor(R.color.dcare_tab_selected));
                        break;
                }
                Fragment fragment = FragmentFactory.getInstanceByIndex(checkedId);
                transaction.replace(R.id.content, fragment);
                transaction.commit();
            }
        });
        if (isFromMSG) {
            radio_suggest.performClick();
            radio_suggest.setChecked(true);
            Log.i(tag, "radio_suggest.setChecked");
            Fragment fragment = FragmentFactory.getInstanceByIndex(R.id.radio_suggest);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.commit();
        } else {
            radio_walk.setChecked(true);
        }
        if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
            initnotification();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        isActive = true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        isActive = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        unregisterReceiver(mReceiver);
        if (mNotificationManager != null) {
            mNotificationManager.cancel("Dcare", 100);
        }

        super.onDestroy();
    }

    @Override
    public void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        checkUpdate(getVersion());
    }

    // 在开启一个服务之前应该判断该服务知否已经在运行

    private void checkUpdate(String version) {
        // TODO Auto-generated method stub
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("version", version);

        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.CheckUpdate, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (message.equals("已是最新版本")) {
                        Log.i(tag, "message " + message);
                        return;
                    }
                    if (code == 45000) {
                        UpdateObj content = JSON.parseObject(response.getString("content"), UpdateObj.class);
                        String versiononServer = content.getVersion();// ("version");
                        String dec = content.getDescription();// getString("versiononServer");
                        apkurl = content.getFile();// getString("file");
                        // String versionOnServer = content;
                        if (Float.parseFloat(getVersion()) < Float.parseFloat(versiononServer)) {// 粗略检测version值合法性
                            showNoticeDialog(dec);
                            return;
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }

    // 本方法判断自己些的一个Service-->com.android.controlAddFunctions.PhoneService是否已经运行
    public boolean isWorked() {
        ActivityManager myManager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals("com.etcomm.dcare.service.DcareService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            LogUtil.e(tag, "version>>>>" + version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showNoticeDialog(String updatecontent) {

        noticeDialog = DialogFactory.getDialogFactory().showUpdateVersionDialog(this, getString(R.string.update_title), updatecontent, getString(R.string.update_later), getString(R.string.update_now), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // noticeDialog.dismiss();
                Toast.makeText(mContext, "建议现在更新", Toast.LENGTH_SHORT).show();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == true) {
                    intoDownloadManager();
                }

                Toast.makeText(mContext, "后台正在下载安装包", Toast.LENGTH_LONG).show();
            }
        }, R.color.switch_texton_color, R.color.switch_texton_color);
        noticeDialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false; // 默认返回 false
                }
            }
        });
    }

    private void intoDownloadManager() {
        flag = false;
        DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Log.i(tag, "intoDownloadManager");
        Uri uri = Uri.parse(apkurl);
        Request request = new Request(uri);
        // 设置下载路径和文件名
        request.setDestinationInExternalPublicDir("dcare", "dcare.apk");
        request.setDescription(getString(R.string.app_name) + "更新");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
        // 把当前下载的ID保存起来
        SharedPreferences sPreferences = getSharedPreferences("downloadapp", 0);
        sPreferences.edit().putLong("plato", refernece).commit();

    }

    public class DownLoadBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            SharedPreferences sPreferences = context.getSharedPreferences("downloadapp", 0);
            long refernece = sPreferences.getLong("plato", 0);
            if (refernece == myDwonloadID) {
                String serviceString = Context.DOWNLOAD_SERVICE;
                DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }
    }

    public class ScreenStatusReceiver extends BroadcastReceiver {
        String SCREEN_ON = "android.intent.action.SCREEN_ON";
        String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

        public void onReceive(Context context, Intent intent) {
            // 屏幕唤醒
            if (SCREEN_ON.equals(intent.getAction())) {
                if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
                    PedometerCounterService.initAppService(mContext);
                }
            }
            // 屏幕休眠
            else if (SCREEN_OFF.equals(intent.getAction())) {
                PedometerCounterService.initAppService(mContext);
            }
        }
    }

    void registerReceiver() {
        receiver = new DownLoadBroadcastReceiver();
        receiver2 = new ScreenStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(receiver, filter);
        registerReceiver(receiver2, filter);
    }

    void unregisterReceiver() {
        unregisterReceiver(receiver);
        unregisterReceiver(receiver2);
    }

    private NotificationManager mNotificationManager;
    private RemoteViews mRemoteViews;
    private NotificationCompat.Builder mNotificationBuilder;
    private Notification mNotification;
    public static int requestCode = 333;

    // 消息推送
    private void initnotification() {
        Object localObject1 = null;
        if (SharePreferencesUtil.getToken(mContext).length() > 2) {
            localObject1 = new Intent(mContext, MainActivity.class);
        } else {
            localObject1 = new Intent(mContext, LoginActivity.class);
        }
        localObject1 = PendingIntent.getActivity(mContext, requestCode, (Intent) localObject1, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mRemoteViews == null) {
            Object localObject2 = new Intent(this, SettingsPedometerSettingsActivity.class);
            localObject2 = PendingIntent.getActivity(this, requestCode, (Intent) localObject2, Intent.FLAG_ACTIVITY_NEW_TASK);
            mRemoteViews = new RemoteViews(getPackageName(), R.layout.steps_notification);
            mRemoteViews.setOnClickPendingIntent(R.id.notification, (PendingIntent) localObject1);
        } else {
            Log.e(tag, "mRemoteViews!=null");
        }
        if (mNotificationBuilder == null) {
            Object dellocalObject = new Intent(mContext, DispatchService.class);
            PendingIntent deleteintent = PendingIntent.getService(mContext, requestCode, (Intent) dellocalObject, PendingIntent.FLAG_ONE_SHOT);
            mNotificationBuilder = new NotificationCompat.Builder(this).setShowWhen(false).setContent(mRemoteViews).setSmallIcon(R.drawable.logo).setContentIntent((PendingIntent) localObject1).setOngoing(true).setDeleteIntent(deleteintent);
            if (Build.VERSION.SDK_INT >= 21)
                mNotificationBuilder.setVisibility(View.VISIBLE);
            mNotificationBuilder.setAutoCancel(false);
        } else {
            Log.e(tag, "mNotificationBuilder!=null");
        }
        mNotification = mNotificationBuilder.build();
        mNotification.defaults = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("Dcare", 100, mNotification);

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Preferences.ACTION_USER_LOGIN)) {// 用户登陆
                LogUtil.e("<<<<<", "用户登录了 登录了");
                if (mNotificationManager != null) {
                    if (mNotification != null) {
                        mNotificationManager.notify("Dcare", 100, mNotification);
                    }
                }

            } else if (intent.getAction().equals(Preferences.ACTION_USER_EXIT)) {// 用户退出登陆

                if (mNotificationManager != null) {
                    mNotificationManager.cancel("Dcare", 100);
                }
            } else if (intent.getAction().equals(Preferences.OPEN_NEWS)) {
                initnotification();
            }
        }
    };

}

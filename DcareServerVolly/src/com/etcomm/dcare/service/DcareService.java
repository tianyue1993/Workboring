package com.etcomm.dcare.service;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.etcomm.dcare.MainActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.SettingsPedometerSettingsActivity;
import com.etcomm.dcare.Watcher;
import com.etcomm.dcare.app.activity.login.LoginActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.ormlite.bean.Data;
import com.etcomm.dcare.ormlite.bean.DataDao;
import com.etcomm.dcare.ormlite.bean.User;
import com.etcomm.dcare.ormlite.bean.UserDao;

/**
 * 数据读取协议，正常后台情况下，每5分钟或者10，30分钟读取一次数据 如果Activity正在运行，则1s读取一次数据
 *
 * @author iexpressbox
 * @Time 2016/4/11 17:00 //只有用户账号正常的时候 才计步 退出登陆停止计步 登陆时开始计步
 */
public class DcareService extends BaseService {
    private final static String tag = "DcareService";
    protected static final int GetCurSteps = 0;
    protected static final int UpdateUIperStep = 1;
    protected static final int GetBroadCastToStartTimerPerSecond = 2;
    protected static final int StopPerSecondStepTimer = 3;
    private static final String dateformat = "yyyyMMdd";
    private PowerManager.WakeLock wakeLock;
    Timer timer;
    /**
     * 每秒读取一次数据，只在开启主界面时运行，离开主界面不运行
     */
    Timer persecondtimer;
    int i = 0;
    private static Watcher watcher;
    private boolean isScreenOff = false;
    public static int requestCode = 333;
    private RemoteViews mRemoteViews;
    private Context mContext;
    private NotificationCompat.Builder mNotificationBuilder;
    private Notification mNotification;
    private UserDao userDao;
    private DataDao dataDao;
    protected int todayTotalSteps = 0;
    protected float todayTotalCaliries = 0;
    protected float todayTotalMiles = 0;
    protected float todayTotalSeconds = 0;
    protected int dblastsavedSteps = 0;
    protected float dblastsavedCaliries = 0;
    protected float dblastsavedMiles = 0;
    protected float dblastsavedSeconds = 0;
    protected int lastSteps = 0;
    protected float lastCaliries = 0;
    protected float lastMiles = 0;
    protected float lastSeconds = 0;
    protected int deltaSteps = 0;
    protected float deltaCaliries = 0;
    protected float deltaMiles = 0;
    protected float deltaSeconds = 0;
    private boolean isReadData = false;
    /**
     * 每日数据记录 每日分别记录今天的各个数据使用20160217-id-**类似标签
     */
    private SharedPreferences sp;
    private Editor editor;
    private int testSteps = 0;
    private Object stepsReadWriteSpObject = new Object();
    protected User curUser;
    /**
     * 当日的日期，如果保存数据时，发现新获取的日期不同，则日期改变，需要保存之前数据，并清空当前 的数据
     */
    private String todayDateStr;
    private String todayUserIdStr;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case GetCurSteps:
                    if (watcher != null) {
                        int curstep = watcher.getCurSteps();
                        Log.i(tag, "curstep:  " + curstep);
                    }
                    break;
                case UpdateUIperStep:
                    break;
                case GetBroadCastToStartTimerPerSecond:
                    break;
                case StopPerSecondStepTimer:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = this;
        acquireWakeLock();
        watcher = Watcher.getInstance(this);// new Watcher(this);//
        Log.i(tag, "startListenSensorManager start");
        String path;
        if (Environment.isExternalStorageEmulated()) {
            path = Environment.getExternalStorageDirectory().toString();
        } else {
            path = getFilesDir().toString();
        }
        if (!path.endsWith("/")) {
            path += "/";
        }
        path += "dcare/";
        if (!new File(path).exists()) {
            new File(path).mkdir();
        }
        Log.i(tag, "path:  " + path);
        watcher.setfilepath(path);
        float bodyweight = Float.parseFloat(AppSharedPreferencesHelper.getWeight());
        float bodyheight = Float.parseFloat(AppSharedPreferencesHelper.getHeight());
        float steplength = Float.parseFloat(AppSharedPreferencesHelper.getUserStepLeight());
        if (bodyweight == 0) {
            bodyweight = 65;
        }
        if (bodyheight == 0) {
            bodyheight = 172;
        }
        if (steplength == 0) {
            steplength = 66;
        }
        Log.i(tag, "bodyweight:  " + bodyweight + "bodyheight:  " + bodyheight + "steplength:  " + steplength);
        watcher.setWeightHeightSteplength(bodyweight, bodyheight, steplength);
        int s = AppSettingPreferencesHelper.getSoftPedometerSensitivity();// app计步灵敏度
        if (s >= Preferences.pedometer_sensitivity.length) {
            s = Preferences.pedometer_sensitivity.length - 1;
        }
        if (s < 0) {
            s = 0;
        }
        watcher.setSensitivity(getSensitivity(AppSettingPreferencesHelper.getSoftPedometerSensitivity()));

        // 1.97 2.96 4.44 6.66 10.00 15.00 22.50 33.75 50.62//越小值越灵敏
        // 7.28号上线本灵敏度为10.00
        // 华为mates,敏感度为6.66时误差测试比实际少了25%
        // 华为mate7,敏感度为6.66时误差测试比实际少了25%
        // 华为mate7,敏感度为6.66时误差测试比实际少了25%
        watcher.EnablePedometer(AppSettingPreferencesHelper.isSoftPedometerOn());
        // 只有用户账号正常的时候 才计步
        if (!TextUtils.isEmpty(SharePreferencesUtil.getToken(mContext))) {
            watcher.startListenSensorManager();
        }
        Log.i(tag, "startListenSensorManager end");
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo("com.etcomm.dcare", PackageManager.GET_SERVICES);
            int userId = ai.uid;
            Log.i(tag, "" + userId);
            // watcher.createAppMonitor(String.valueOf(userId));
        } catch (Exception e) {
            Log.e(tag, "Exception");
            // TODO: handle exception
        }
        userDao = new UserDao(mContext);
        curUser = userDao.getDefaultUser(AppSharedPreferencesHelper.getUserId());
        if (curUser == null) {// 如果空，则插入当前的用户信息
            User user = new User();
            user.setAccess_token(SharePreferencesUtil.getToken(mContext));
            user.setUser_id(AppSharedPreferencesHelper.getUserId());
            user.setBirthday(AppSharedPreferencesHelper.getBirth_year());
            user.setAvatar(AppSharedPreferencesHelper.getAvatar());
            user.setNick_name(AppSharedPreferencesHelper.getNick_name());
            user.setReal_name(AppSharedPreferencesHelper.getUserName());
            // user.set
            userDao.add(user);
            curUser = userDao.getDefaultUser(AppSharedPreferencesHelper.getUserId());
        } else {
            Log.i(tag, "curUser 已经存在  " + AppSharedPreferencesHelper.getUserId());
        }
        dataDao = new DataDao(mContext);
        sp = getApplicationContext().getSharedPreferences(Preferences.DayData, MODE_PRIVATE);
        editor = sp.edit();
        readTodayMostData();

        /***
         * init data数据 改用Shareprefrance 保存
         */

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Preferences.ACTION_USER_EXIT);
        filter.addAction(Preferences.ACTION_USER_LOGIN);
        filter.addAction(Preferences.ACTION_USER_REGISTER);
        filter.addAction(Preferences.ACTION_DATE_CHANGED);
        filter.addAction(Preferences.ACTION_ENALBE_PEDOMETER);
        filter.addAction(Preferences.ACTION_CLEAR_ALLDATA);
        filter.addAction("changesensitivity");
        filter.addAction("sendstep");
        registerReceiver(mReceiver, filter);

        timer = new Timer();
        // 定时器，一分钟执行一个请求周期
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Log.i(tag, "EXEC" + i);
                if (isScreenOff) {
                    // 屏幕常亮功能
                    Log.i(tag, "isScreenOff  restartListenSensorManager");
                    watcher.restartListenSensorManager();
                }
                if (watcher != null) {
                    // mHandler.sendEmptyMessage(GetCurSteps);
                    int curstep = watcher.getCurSteps();// 读取当前步数
                    float[] cal = watcher.getCurMileSecondCalory();// 获取步行相关数据
                    watcher.resetCalMileData();// 重置数据，重新开始计步
                    Log.i(tag, "curstep:  " + curstep + " MILES cal[0]" + cal[0] + " Seconds: " + cal[1] + "  Calory: " + cal[2]);
                    todayTotalSteps += curstep;// 运动总步数
                    todayTotalMiles += cal[0];// 运动总里程
                    todayTotalSeconds += cal[1];// 时长
                    todayTotalCaliries += cal[2];// 卡路里
                    saveTodayMostData();

                } else {
                    Log.e(tag, "watcher null");
                }
                // Looper.loop();
                i++;
            }
        }, 1000 * 5, 1000 * 60 * 2);
        initnotification();
    }

    /**
     * 当日的累计数据保存到sp中 服务结束时保存到sp中
     */
    protected void saveTodayMostData() {
        synchronized (stepsReadWriteSpObject) {
            DateTime nowTime = new DateTime();
            if (StringUtils.isEmpty(todayDateStr)) {
                todayDateStr = nowTime.toString(dateformat);
            }

            if (!todayDateStr.equals(nowTime.toString(dateformat))) {
                Log.i(tag, "日期改变，保存原有数据");
                if (sp.getFloat(todayDateStr + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySteps, 0) < todayTotalSteps) {
                    editor.putFloat(todayDateStr + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayMile, todayTotalMiles);
                    editor.putFloat(todayDateStr + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayCalories, todayTotalCaliries);
                    editor.putFloat(todayDateStr + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySeconds, todayTotalSeconds);
                    editor.putFloat(todayDateStr + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySteps, todayTotalSteps);
                    Log.i(tag, "put SP total:   todayTotalSteps: " + todayTotalSteps + " todayTotalCaliries: " + todayTotalCaliries + " todayTotalMiles:" + todayTotalMiles + " todayTotalSeconds: " + todayTotalSeconds);
                    if (!TextUtils.isEmpty(SharePreferencesUtil.getToken(mContext))) {
                        editor.commit();
                    }
                }
                Log.i(tag, "日期改变，统计数据清零");
                todayDateStr = nowTime.toString(dateformat);
                todayTotalMiles = 0.0f;
                todayTotalCaliries = 0.0f;
                todayTotalSeconds = 0.0f;
                todayTotalSteps = 0;
                initCurValues();
                if (watcher != null) {
                    Log.i(tag, "日期改变，计步数据重置 ");
                    watcher.resetCalMileData();
                }
                return;
            } else {

            }
            if (StringUtils.isEmpty(todayUserIdStr)) {
                todayUserIdStr = AppSharedPreferencesHelper.getUserId();
            }
            if (!todayUserIdStr.equals(AppSharedPreferencesHelper.getUserId())) {
                if (sp.getFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySteps, 0) < todayTotalSteps) {
                    editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DayMile, todayTotalMiles);
                    editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DayCalories, todayTotalCaliries);
                    editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DaySeconds, todayTotalSeconds);
                    editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DaySteps, todayTotalSteps);

                    Log.i(tag, "put SP total:   todayTotalSteps: " + todayTotalSteps + " todayTotalCaliries: " + todayTotalCaliries + " todayTotalMiles:" + todayTotalMiles + " todayTotalSeconds: " + todayTotalSeconds);
                    todayUserIdStr = AppSharedPreferencesHelper.getUserId();
                    if (!TextUtils.isEmpty(SharePreferencesUtil.getToken(mContext))) {
                        editor.commit();
                    }
                }
                return;
            } else {
                if (sp.getFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySteps, 0) < todayTotalSteps) {
                    editor.putFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayMile, todayTotalMiles);
                    editor.putFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayCalories, todayTotalCaliries);
                    editor.putFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySeconds, todayTotalSeconds);
                    editor.putFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySteps, todayTotalSteps);
                    Log.i(tag, "put SP total:   todayTotalSteps: " + todayTotalSteps + " todayTotalCaliries: " + todayTotalCaliries + " todayTotalMiles:" + todayTotalMiles + " todayTotalSeconds: " + todayTotalSeconds);
                    if (!TextUtils.isEmpty(SharePreferencesUtil.getToken(mContext))) {
                        editor.commit();
                    }
                    return;
                }
            }
        }
    }

    // 获取用户登录后步行数据
    protected void readTodayMostData() {
        synchronized (stepsReadWriteSpObject) {
            DateTime nowTime = new DateTime();
            Log.i(tag, "readTodayObject: todayUserIdStr:" + todayUserIdStr + "   UserId: " + AppSharedPreferencesHelper.getUserId());
            if (StringUtils.isEmpty(todayUserIdStr)) {
                todayUserIdStr = AppSharedPreferencesHelper.getUserId();
            }
            if (!todayUserIdStr.equals(AppSharedPreferencesHelper.getUserId())) {
                Log.i(tag, "用户ID发生变化  put" + nowTime.toString(dateformat) + "-" + todayUserIdStr);
                editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DayMile, todayTotalMiles);
                editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DayCalories, todayTotalCaliries);
                editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DaySeconds, todayTotalSeconds);
                editor.putFloat(nowTime.toString(dateformat) + "-" + todayUserIdStr + Preferences.DaySteps, todayTotalSteps);
                Log.i(tag, "put SP total:   todayTotalSteps: " + todayTotalSteps + " todayTotalCaliries: " + todayTotalCaliries + " todayTotalMiles:" + todayTotalMiles + " todayTotalSeconds: " + todayTotalSeconds);

                if (!TextUtils.isEmpty(SharePreferencesUtil.getToken(mContext))) {
                    editor.commit();
                }
                Log.i(tag, "用户ID发生变化，统计数据清零");
                todayUserIdStr = AppSharedPreferencesHelper.getUserId();
                initCurValues();
                if (watcher != null) {
                    Log.i(tag, "用户ID发生变化，计步数据重置 ");
                    watcher.resetCalMileData();
                }
            } else {

            }
            todayTotalMiles = sp.getFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayMile, 0);
            todayTotalCaliries = sp.getFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayCalories, 0);
            todayTotalSeconds = sp.getFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySeconds, 0);
            todayTotalSteps = (int) sp.getFloat(nowTime.toString(dateformat) + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySteps, 0);
            editor.putLong(Preferences.LastClearTime, System.currentTimeMillis()).commit();
            Log.i(tag, "read SP data:   todayTotalSteps: " + todayTotalSteps + " todayTotalCaliries: " + todayTotalCaliries + " todayTotalMiles:" + todayTotalMiles + " todayTotalSeconds: " + todayTotalSeconds);

        }
    }

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int wakeFlags;
        if (AppSettingPreferencesHelper.isScreenLongOn()) {
            wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP;
        } else {
            wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
        }
        wakeLock = pm.newWakeLock(wakeFlags, tag);
        wakeLock.acquire();
    }

    @Override
    public void onDestroy() {
        wakeLock.release();
        unregisterReceiver(mReceiver);
        if (mNotificationManager != null) {
            mNotificationManager.cancel("Dcare", 100);
        }

        saveTodayMostData();
        if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
            startService(new Intent(mContext, DcareService.class));
        }

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(tag, "onStartCommand");
        if (intent != null && intent.getBooleanExtra(Preferences.StartSecondBroadCastStepCount, false)) {
            if (persecondtimer == null) {
                persecondtimer = new Timer();
                persecondtimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        int curstep = watcher.getCurSteps();
                        float[] cal = watcher.getCurMileSecondCalory();
                        watcher.resetCalMileData();
                        Log.i(tag, "sendBroadCast persecond curstep:" + curstep + " MILES cal[0]" + cal[0] + " Seconds:" + cal[1] + " Calory: " + cal[2]);
                        todayTotalSteps += curstep;
                        todayTotalMiles += cal[0];
                        todayTotalSeconds += cal[1];
                        todayTotalCaliries += cal[2];
                        saveTodayMostData();
                        // 每秒获取一次步数，并发送广播到健步页面刷新view的步数显示
                        Intent broadintent = new Intent(Preferences.ActionGetCurStepCount);
                        broadintent.putExtra(Preferences.BroadCastCurSteps, todayTotalSteps);
                        broadintent.putExtra(Preferences.BroadcastMiles, todayTotalMiles);
                        broadintent.putExtra(Preferences.BroadcastSeconds, todayTotalSeconds);
                        broadintent.putExtra(Preferences.BroadcastCaliries, todayTotalCaliries);
                        Log.i(tag, "sendBroadCast curstep:" + todayTotalSteps + " MILES " + todayTotalMiles + "Seconds: " + todayTotalSeconds + " Calory:" + todayTotalCaliries);
                        sendBroadcast(broadintent);
                        LogUtil.i("时时数据", todayTotalSteps + "");
                        String dailydate = "";
                        dailydate = new DateTime().toString(dateformat);
                        SharedPreferences datasp = mContext.getSharedPreferences(Preferences.DayData, Context.MODE_PRIVATE);
                        Editor dataspEditor = datasp.edit();
                        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySteps, todayTotalSteps);
                        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayMile, todayTotalMiles);
                        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DayCalories, todayTotalCaliries);
                        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.DaySeconds, todayTotalSeconds);
                        dataspEditor.commit();
                    }
                }, 1000, 1000);

                Log.i(tag, "persecondtimer  start");
            } else {

            }
        } else {
            if (persecondtimer != null) {
                persecondtimer.cancel();
                persecondtimer.purge();
                Log.i(tag, "persecondtimer  null");
                persecondtimer = null;
            }
        }
        if (intent != null && intent.getBooleanExtra(Preferences.isFinishWork, false)) {
            Log.e(tag, "intent.getBooleanExtra(Constant.isFinishWork");
            if (watcher != null) {
                watcher.resetCalMileData();
                todayTotalSteps = 0;
                todayTotalMiles = 0;
                todayTotalSeconds = 0;
                todayTotalCaliries = 0;
                initCurValues();
            }
        }
        saveTodayMostData();
        readTodayMostData();
        if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
            flags = START_STICKY;
            return super.onStartCommand(intent, flags, startId);
        } else {
            return super.onStartCommand(intent, flags, startId);
        }

    }

    private void initCurValues() {
        lastSteps = 0;
        lastMiles = 0;
        lastSeconds = 0;
        lastCaliries = 0;
        deltaSteps = 0;
        deltaMiles = 0;
        deltaSeconds = 0;
        deltaCaliries = 0;
    }

    // BroadcastReceiver for handling ACTION_SCREEN_OFF.ACTION_SCREEN_ON
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            Log.i(tag, "onReceive:  " + intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                isScreenOff = true;
                acquireWakeLock();
            } else if (intent.getAction().equals(Preferences.ACTION_ENALBE_PEDOMETER)) {
                if (watcher != null) {
                    Log.i(tag, "EnablePedometer " + AppSettingPreferencesHelper.isSoftPedometerOn());
                    watcher.EnablePedometer(AppSettingPreferencesHelper.isSoftPedometerOn());
                    if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
                        watcher.restartListenSensorManager();
                    }
                }
            } else if (intent.getAction().equals(Preferences.ACTION_USER_LOGIN)) {// 用户登陆
                LogUtil.e("<<<<<", "用户登录了 登录了");
                if (mNotificationManager != null) {
                    if (mNotification != null) {
                        mNotificationManager.notify("Dcare", 100, mNotification);
                    }
                }
                if (watcher != null) {
                    watcher.EnablePedometer(true);
                    readTodayMostData();
                    initCurValues();
                    watcher.restartListenSensorManager();
                }
            } else if (intent.getAction().equals(Preferences.ACTION_USER_REGISTER)) {
                if (watcher != null) {
                    watcher.EnablePedometer(true);
                    readTodayMostData();
                    initCurValues();
                    watcher.restartListenSensorManager();
                }
            } else if (intent.getAction().equals(Preferences.ACTION_USER_EXIT)) {// 用户退出登陆
                if (watcher != null) {
                    watcher.EnablePedometer(false);
                    watcher.resetCalMileData();
                    initCurValues();
                }
                if (mNotificationManager != null) {
                    mNotificationManager.cancel("Dcare", 100);
                }
            } else if (intent.getAction().equals(Preferences.ACTION_SCREENLONGON)) {

            } else if (intent.getAction().equals(Preferences.ACTION_CLEAR_ALLDATA)) {
                Log.i(tag, "ACTION_CLEAR_ALLDATA");
                // / 保存上次数据 到日 计步表中???????????????????????????????????????????
                Log.e(tag, "重新开始计步");
                // saveTodayMostData();
                if (watcher != null) {
                    watcher.resetCalMileData();
                }
                todayDateStr = "";
                todayUserIdStr = "";
                Log.i(tag, "已经清空所有数据");
                todayTotalMiles = 0.0f;
                todayTotalCaliries = 0.0f;
                todayTotalSeconds = 0.0f;
                todayTotalSteps = 0;
                initCurValues();
                SharePreferencesUtil.saveTmpStep(mContext, todayTotalSteps, todayTotalMiles, todayTotalSeconds, todayTotalCaliries);
                stopSelf();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                isScreenOff = false;
            } else if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED) || intent.getAction().equals(Preferences.ACTION_DATE_CHANGED)) {
                Log.i(tag, "ACTION_DATE_CHANGED");
                if (watcher != null) {
                    watcher.resetCalMileData();
                }
                if (watcher != null) {
                    int curstep = watcher.getCurSteps();
                    float[] cal = watcher.getCurMileSecondCalory();
                    Log.i(tag, "curstep:  " + curstep + " MILES cal[0]" + cal[0] + " Seconds: " + cal[1] + "  Calory: " + cal[2]);
                    Data data = new Data(System.currentTimeMillis(), curstep, cal[0], cal[2], (int) cal[1], null);
                    dataDao.add(data);
                    if (curstep >= lastSteps) {// 数据增加中，不处理 //总和不断累加
                        deltaSteps = curstep - lastSteps;
                        deltaMiles = cal[0] - lastMiles;
                        deltaSeconds = cal[1] - lastSeconds;
                        deltaCaliries = cal[2] - lastCaliries;
                        todayTotalSteps += deltaSteps;
                        todayTotalMiles += deltaMiles;
                        todayTotalSeconds += deltaSeconds;
                        todayTotalCaliries += deltaCaliries;
                        lastSteps = curstep;
                        lastMiles = cal[0];
                        lastSeconds = cal[1];
                        lastCaliries = cal[2];
                        saveTodayMostData();
                    } else {
                        lastSteps = curstep;
                        lastMiles = cal[0];
                        lastSeconds = cal[1];
                        lastCaliries = cal[2];
                        saveTodayMostData();
                    }
                }
                saveTodayMostData();
                // / 保存上次数据 到日 计步表中???????????????????????????????????????????
                editor.putLong(Preferences.LastClearTime, System.currentTimeMillis()).commit();
                editor.putLong(Preferences.LastDateChangedMillis, System.currentTimeMillis()).commit();
                Log.e(tag, "新的一天开始了，从0开始");
                todayTotalSteps = 0;
                todayTotalCaliries = 0;
                todayTotalMiles = 0;
                todayTotalSeconds = 0;
                initCurValues();
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                Log.e(tag, "Intent.ACTION_TIME_TICK");// Intent.ACTION_TIME_CHANGED
                // Intent.ACTION_TIMEZONE_CHANGED
            } else if (intent.getAction().equals("changesensitivity")) {
                // 重置灵敏度
                watcher.setSensitivity(getSensitivity(intent.getIntExtra("sensity", 0)));
                Log.e(tag, "灵敏度" + intent.getIntExtra("sensity", 0));
            } else if (intent.getAction().equals("sendstep")) {
                // 第一次登陆获取步数
                todayTotalSteps = intent.getIntExtra("step", 0);
            }
        }
    };
    private NotificationManager mNotificationManager;

    // 消息推送
    private void initnotification() {
        Object localObject1 = null;
        if (SharePreferencesUtil.getToken(mContext).length() > 2) {
            localObject1 = new Intent(mContext, MainActivity.class);
        } else {
            localObject1 = new Intent(mContext, LoginActivity.class);
        }
        localObject1 = PendingIntent.getActivity(this, requestCode, (Intent) localObject1, PendingIntent.FLAG_UPDATE_CURRENT);
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
        // mNotification.deleteIntent
        mNotification.defaults = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        // |Notification.FLAG_ONGOING_EVENT;// FLAG_NO_CLEAR 该通知不能被状态栏的清除按钮给清除掉
        // FLAG_ONGOING_EVENT 通知放置在正在运行
        // mNotification.contentView = mRemoteViews;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("Dcare", 100, mNotification);

        // get
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    private boolean isForeground(Context context, String className) {
        if (context == null || StringUtils.isEmpty(className)) {
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

    public static float getSensitivity(int number) {
        // 1.97 2.96 4.44 6.66 10.00 15.00 22.50 33.75 50.62//越小值越灵敏
        if (number == 9) {
            return 1.97f;
        } else if (number == 8) {
            return 2.96f;
        } else if (number == 7) {
            return 4.44f;
        } else if (number == 6) {
            return 6.66f;
        } else if (number == 5) {
            return 10.00f;
        } else if (number == 4) {
            return 15.00f;
        } else if (number == 3) {
            return 22.50f;
        } else if (number == 2) {
            return 33.75f;
        } else if (number == 1) {
            return 50.62f;
        } else {
            return 10.00f;
        }

    }

}

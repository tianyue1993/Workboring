package com.etcomm.dcare.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.RankActivity;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.model.PedometerItem;
import com.etcomm.dcare.app.model.json.PedometerDataJson;
import com.etcomm.dcare.app.services.BluetoothService;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.bluetooth.BluetoothUtils;
import com.etcomm.dcare.common.ACache;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.common.CacheConstant;
import com.etcomm.dcare.data.DataPerDayFromWrist;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.Login;
import com.etcomm.dcare.netresponse.WeatherContent;
import com.etcomm.dcare.ormlite.bean.Device5MinData;
import com.etcomm.dcare.ormlite.bean.DeviceDailyData;
import com.etcomm.dcare.ormlite.db.DatabaseHelper;
import com.etcomm.dcare.service.StepDataUploadService;
import com.etcomm.dcare.widget.AutoTextView;
import com.etcomm.dcare.widget.DialogFactory;
import com.etcomm.dcare.widget.StepPageDataView;
import com.excheer.library.BluetoothConnectListener;
import com.excheer.library.BluetoothLeManager;
import com.excheer.library.DaySportsData;
import com.iwown.android_iwown_ble.bluetooth.WristBandDevice;
import com.iwown.android_iwown_ble.model.WristBand;
import com.j256.ormlite.dao.Dao;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import me.chunyu.pedometerservice.IntentConsts;
import me.chunyu.pedometerservice.PedometerCounterService;

/**
 * @author etc
 * @ClassName: WalkPageFragment
 * @Description: 主页-健步
 * @date 8 Apr, 2016 1:37:05 PM 米4 honor 6 初次加载onpageSelected不执行，需要处理？？
 */
public class WalkPageFragment extends BaseFragment implements BluetoothConnectListener {
    final String TAG = "WalkPageFragment";
    protected Dialog dialog;

    /**
     * 敏狐手环连接相关对象
     */
    private BluetoothLeManager bluetoothLeManager;
    /**
     * 记步传感器的步数
     */
    private int mCYStepCounterSensorValue;
    /**
     * 计步传感器的时间
     */
    private long mSCSMotionTime;
    /**
     * 加速度传感器的步数
     */
    private int mCYAccelerateSensorValue;
    /**
     * 加速度传感器的时间
     */
    private long mASMotionTime;
    /**
     * 首次登陆 数据计算标示
     */
    private boolean firstLoginFlag = false;
    /**
     * 保留小数点后两位
     */
    private DecimalFormat decimalFormat;
    /**
     * 判断重新填充本机计步临时数据
     */
    private Boolean isInStepCount = false;
    /**
     * 黑屏后者退出后台
     */
    private boolean isCallBackPause = true;

    static final int IS_TODAY = 0x09;
    private static final String tag = WalkPageFragment.class.getSimpleName();
    private static final String tagPage = WalkPageFragment.class.getSimpleName() + "Page";
    private static final String tagBlue = WalkPageFragment.class.getSimpleName() + "Blue";
    protected static final int SendDevice5Mins = 11;
    protected static final int SendDeviceDaily = 12;
    protected static final int GetLocalSport255 = 21;
    protected static final int SendDevicePower = 13;
    protected static final int SendDiviceBlueDataUpdateView = 22;
    private static final long SigninDialogDismissTime = 1500;
    protected static final int SyncBlueDeviceData = 15;
    protected static final int SendDeviceTimeSync = 16;
    private static final String dateformat = "yyyyMMdd";
    // private VerticalScrollTextView tv_weather;
    private ImageView iv_currank;

    private TextView userrank_tv;
    private TextView userrank;
    private TextView totalrankcount;
    private TextView tv_currank;
    private TextView tv_calendar;
    private ImageView iv_calendar;
    private ViewPager viewpager;

    private TextView tv_tab_walk;
    private ImageView trend;
    private ImageView msg_iv;

    private ImageView walk_page_leftcircle;
    private ImageView walk_page_rightcircle;

    private RelativeLayout wristband;
    private ImageView wrist;
    private ImageView wrist_battery;
    private TextView wrist_status;
    private TextView tv_motiontimes;
    private TextView tv_mileage;
    private TextView tv_caliries;
    private TextView tv_mileage_unit;
    private TextView tv_motiontimes_unit;
    private TextView tv_caliries_unit;
    private SeekBar curprogress;
    private TextView caliriesinfo;
    private TextView distanceinfo;
    /**
     * app内部计步的数据
     */
    protected int curStep = 0;
    protected float Miles = 0f;
    protected float Seconds = 0f;
    protected float Calories = 0f;
    /**
     * 蓝牙手环计步的数据
     */
    protected int blueStep = 0; // 步数
    protected float blueMiles = 0f;
    protected float blueSeconds = 0f;
    protected float blueCalories = 0f;// 卡路里
    /**
     * localsp 保存当前的蓝牙数据 如果手机计步的数据小于蓝牙手环的数据，则使用蓝牙手环的数据
     * 保存名为blue_yyyy-MM-dd_userid_data
     */

    /**
     * 回传蓝牙计步数据
     */
    protected String blueActionStep = ""; // 时时同步蓝牙计步数据
    DataPerDayFromWrist wristData = new DataPerDayFromWrist();

    private void saveWristDataToSp() {
        if (wristData.getDt().equals(new DateTime().toString("yyyy-MM-dd"))) {// 首先判断日期是否相同，如果不同，就已经过了一天了，需要清空数据

            sp.edit().putString("blue_" + new DateTime().toString("yyyy-MM-dd") + AppSharedPreferencesHelper.getUserId() + "_data", wristData.toParseJsonString()).commit();
        } else {
            sp.edit().putString("blue_" + wristData.getDt() + AppSharedPreferencesHelper.getUserId() + "_data", wristData.toParseJsonString()).commit();
            readWristDataFromSp();
        }
    }

    private void readWristDataFromSp() {
        wristData = new DataPerDayFromWrist();
        String data = sp.getString("blue_" + new DateTime().toString("yyyy-MM-dd") + AppSharedPreferencesHelper.getUserId() + "_data", wristData.toParseJsonString());
        wristData = JSON.parseObject(data, DataPerDayFromWrist.class);

    }

    /**
     * 当前今天显示的数据是蓝牙手环的还是手机的 蓝牙设备，如果本地计步数据小于蓝牙数据，使用蓝牙数据 为真时使用蓝牙数据
     */
    private boolean isBlueDeviceData = false;
    private Timer mBlueTimer;

    protected int curp = 0;
    protected WristBand mDevice;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i(tag, "Handler: " + msg.what);
            byte[] data = null;
            switch (msg.what) {
                case SendDiviceBlueDataUpdateView:
                    updateViewByBlueData();
                    break;
                case SyncBlueDeviceData:
                    wrist_status.setText("正在同步手环数据...");
                    break;
                case GetLocalSport255:
                    wrist_status.setText("已连接");
                    break;
                case IS_TODAY:
                    signin();

                    break;
                default:
                    break;
            }
        }

        ;
    };

    private Context mContext;
    private LinearLayout currank_li;
    protected ArrayList<PedometerItem> pedometerlist = new ArrayList<PedometerItem>();
    protected int mSuggestSteps;
    private PagerAdapter pageAdapter;
    protected int curPageIndex;
    /**
     * 本地数据缓存
     */
    private SharedPreferences localsp;
    private BluetoothUtils mBluetoothUtils;
    /**
     * 寻问是否请求开启蓝牙
     */
    private boolean isNotAskToOpenBlue = false;
    private TimerTask mBlueTask;
    private SharedPreferences sp;
    private boolean LooperPrepared = false;

    private Dao daoDevice5MinData;
    private Dao daoDeviceDailyData;
    private ACache acache;
    private AutoTextView switcher;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        showToast("123");
        sp = activity.getPreferences(Context.MODE_PRIVATE);
    }

    /**
     * 是否已经被绑定
     */
    private boolean hasAskEnableBlueTooth = false;

    @Override
    public void onResume() {
        super.onResume();

        LogUtil.e(TAG, TAG + "onResume>>>>");

//        parseSteps();
        /**
         * 当前计步为第三方手环计步 模仿我的手环列表页面的自动连接功能，但是必须通过事件轮询实现，后期需要优化
         */
        if (AppSharedPreferencesHelper.getMacAddress().length() > 5 && org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getBlueDeviceName(), "Braceli5")) {
            // 更新艾薇手环蓝牙数据显示
            final boolean mIsBluetoothOn = mBluetoothUtils.isBluetoothOn();
            final boolean mIsBluetoothLePresent = mBluetoothUtils.isBluetoothLeSupported();

            if (!hasAskEnableBlueTooth) {
                mBluetoothUtils.askUserToEnableBluetoothIfNeeded();
                hasAskEnableBlueTooth = true;
            }
            if (mIsBluetoothOn && mIsBluetoothLePresent) {
                if (!StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
                    if (!WristBandDevice.getInstance(mContext).isConnected()) {
                        if (mDevice == null) {
                            mDevice = new WristBand();
                            mDevice.setAddress(AppSharedPreferencesHelper.getMacAddress());
                            mDevice.setName(AppSharedPreferencesHelper.getBlueDeviceName());
                            mDevice.setRssi(AppSharedPreferencesHelper.getBlueDeviceRssi());
                            LogUtil.i(TAG, "Create mDevice");
                        }
                        WristBandDevice.getInstance(mContext).connect(mDevice);
                    }
                }
            }
        }

        if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
            msg_iv.setImageResource(R.drawable.icon_msg_unread);
        } else {
            msg_iv.setImageResource(R.drawable.icon_msg);
        }
        if (!StringUtils.isEmpty(AppSharedPreferencesHelper.getPedometer_target())) {
            mSuggestSteps = Integer.parseInt(AppSharedPreferencesHelper.getPedometer_target());
        }
        // bindSer();
        if (!StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
            if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                final boolean mIsBluetoothOn = mBluetoothUtils.isBluetoothOn();
                final boolean mIsBluetoothLePresent = mBluetoothUtils.isBluetoothLeSupported();
                if (!isNotAskToOpenBlue) {
                    isNotAskToOpenBlue = true;
                    mBluetoothUtils.askUserToEnableBluetoothIfNeeded();
                }
            }

            wristband.setVisibility(View.VISIBLE);
            if (mBlueTimer == null) {
                mBlueTimer = new Timer();
                mBlueTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!LooperPrepared) {
                            Looper.prepare();
                            LooperPrepared = true;
                        }
                        Log.i(tag, "mBlueTimer mBlueTask");
                        Log.i(tag, "SendDeviceDaily");
                        mHandler.sendEmptyMessage(SendDeviceDaily);
                    }
                };
                mBlueTimer.schedule(mBlueTask, 45 * 1000, 15 * 1000);
            } else {
                mBlueTask.cancel();
                mBlueTimer.cancel();
                if (mBlueTask != null) {
                    mBlueTimer.schedule(mBlueTask, 45 * 1000, 15 * 1000);
                }
            }
        } else {
            wristband.setVisibility(View.INVISIBLE);
        }
        /**
         * 进行服务的重新启动，进行除第一次进入首页外的数据更新，可能会浪费内存，后期需要优化
         */
        Intent serviceIntent = new Intent(getActivity(), PedometerCounterService.class);
        getActivity().startService(serviceIntent);
    }

    @Override
    public void onPause() {
        Log.i(tag, "onPause mBlueTask cancel");
        if (mBlueTimer != null) {
            mBlueTask.cancel();
            mBlueTimer.cancel();
            mBlueTimer.purge();
            mBlueTask = null;
            mBlueTimer = null;
        }
        /**
         * 进入下一级页面
         */
        if (SharePreferencesUtil.getIsCallBackPause(mContext)) {
            isCallBackPause = true;
        } else {
            isCallBackPause = false;
        }
        super.onPause();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(tag, "onCreate");
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // 判断系统是否支持蓝牙连接
            mBluetoothUtils = new BluetoothUtils(getActivity());
        }
        acache = ACache.get(mContext);
        registerReceiver();
        registerDeviceReceiver();
        localsp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sp = getActivity().getSharedPreferences(Preferences.SignIn, Context.MODE_PRIVATE);
        String us = sp.getString(Preferences.MineCurrent, "");
        if (!StringUtils.isEmpty(us)) {
            Login login = JSON.parseObject(us, Login.class);
            mSuggestSteps = Integer.parseInt(login.getPedometer_target());
        } else {
            String loginstr = sp.getString(Preferences.UserInfo, "");// minecurrent
            if (!StringUtils.isEmpty(loginstr)) {
                Login login = JSON.parseObject(loginstr, Login.class);
                if (login != null) {
                    mSuggestSteps = Integer.parseInt(login.getPedometer_target());
                }
            }
        }
        try {
            daoDevice5MinData = DatabaseHelper.getHelper(mContext).getDao(Device5MinData.class);
            daoDeviceDailyData = DatabaseHelper.getHelper(mContext).getDao(DeviceDailyData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (daoDeviceDailyData == null || daoDevice5MinData == null) {
            Log.e(tag, "daoDeviceDailyData null    or   daoDevice5MinData null");
        }
        super.onCreate(savedInstanceState);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                signin();
            }
        }, 1000);
    }

    protected void signin() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DaySignUp, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    // + response.getString("content").toString()
                    if (code == 0) {
                        JSONObject signin = response.getJSONObject("content");
                        String days = signin.getString("day");
                        String score = signin.getString("score");
                        AppSharedPreferencesHelper.setScore(score);
                        showSignedDialoag(days, score);
                    } else {// code不为0 发生异常
                        // showToast(message);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }

    protected void showSignedDialoag(String days, String score) {
        final Dialog signinDialog = DialogFactory.getDialogFactory().showSignedDialog(mContext, days, score);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                signinDialog.dismiss();
            }
        }, SigninDialogDismissTime);
    }

    @Override
    public void onStart() {
        Log.i(tag, "onStart");
        super.onStart();
        getTodayRank();
        getWeather();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(tag, "onViewCreated");

    }

    private void getTodayRank() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetTodayRank, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelDialog();
                String lastRank = localsp.getString("LastRank", "");
                if (!StringUtils.isEmpty(lastRank)) {
                    String[] strs = lastRank.split("/");
                    Log.e(tag, "lastRank: " + lastRank);
                    if (strs.length > 1) {
                        String user = strs[0];
                        String total = strs[1];
                        Log.e(tag, "user: " + user + "total: " + total);
                        userrank.setText(user);
                        userrank_tv.setVisibility(View.VISIBLE);
                        totalrankcount.setText(total);
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelDialog();

                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        String rank = response.getString("content");
                        localsp.edit().putString("LastRank", response.getString("content")).commit();

                        String[] strs = rank.split("/");
                        Log.e(tag, "rank: " + rank);
                        if (strs.length > 1) {
                            String user = strs[0];
                            String total = strs[1];
                            Log.e(tag, "user: " + user + "total: " + total);
                            userrank.setText(user);
                            userrank_tv.setVisibility(View.VISIBLE);
                            totalrankcount.setText(total);
                        }
                    } else {// code不为0 发生异常
                        showToast(message);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }

    private void getWeather() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetWeather, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                cancelDialog();
                Log.w(tag, "post cancel" + this.getRequestURI());
                String lastWeather = acache.getAsStringWithDefaultString(CacheConstant.Weather, "");
                updateWeather(lastWeather);
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelDialog();
                String lastWeather = acache.getAsStringWithDefaultString(CacheConstant.Weather, "");
                updateWeather(lastWeather);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "天气 : onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    Log.i(tag, ">>> onSuccess  code: " + code + " message: " + message + "content: " + response);
                    // + response.getString("content").toString()
                    if (code == 0) {
                        WeatherContent weather = JSON.parseObject(response.getString("content"), WeatherContent.class);
                        localsp.edit().putString("LastWeather", response.getString("content")).commit();
                        acache.put(CacheConstant.Weather, response.getString("content"));
                        if (!TextUtils.isEmpty(weather.getTemp())) {
                            switcher.setUpText(weather.getWeather_status() + "\n" + weather.getTemp());
                            switcher.setDownText("空气质量" + "\n" + weather.getQlty());
                        }

                    } else {// code不为0 发生异常
                        showToast(R.string.server_error);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }

    protected void updateWeather(String lastWeather) {
        if (!StringUtils.isEmpty(lastWeather)) {
            WeatherContent weather = JSON.parseObject(lastWeather, WeatherContent.class);
            switcher.setUpText(weather.getWeather_status() + "\n" + weather.getTemp());
            switcher.setDownText("空气质量" + "\n" + weather.getQlty());
        }
    }

    @Override
    public void onDestroy() {
        Log.i(tag, "onDestroy");
        unregisterReceiver();
        unregisterDeviceReceiver();
        Intent service = new Intent(getActivity(), StepDataUploadService.class);
        service.putExtra("isexit", true);
        getActivity().startService(service);
        //敏狐手环连接 结束当前生命时断开连接
        if (AppSharedPreferencesHelper.getMacAddress().length() > 5 && org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), "F4:06:A5")) {
            //判断为敏狐手环
            if (bluetoothLeManager.isConnected()) {
                bluetoothLeManager.disconnect();
                Log.d("sampleBLE", "onDestroy");
            }
        }
        super.onDestroy();
    }

    SparseArray<StepPageDataView> sparseArray = new SparseArray<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_page_walk, null);
        switcher = (AutoTextView) layout.findViewById(R.id.switcher);
        tv_currank = (TextView) layout.findViewById(R.id.tv_currank);
        currank_li = (LinearLayout) layout.findViewById(R.id.currank_li);
        userrank = (TextView) layout.findViewById(R.id.userrank);
        userrank_tv = (TextView) layout.findViewById(R.id.userrank_tv);
        totalrankcount = (TextView) layout.findViewById(R.id.totalrankcount);
        viewpager = (ViewPager) layout.findViewById(R.id.viewpager);
        trend = (ImageView) layout.findViewById(R.id.trend);
        msg_iv = (ImageView) layout.findViewById(R.id.msg_iv);
        msg_iv.setOnClickListener(msgClickListener);

        tv_tab_walk = (TextView) layout.findViewById(R.id.tv_tab_walk);
        trend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RankActivity.class);
                intent.putExtra("islevel", SharePreferencesUtil.getIslevel(mContext));
                intent.putExtra("token", SharePreferencesUtil.getToken(mContext));
                startActivity(intent);
            }
        });
        walk_page_leftcircle = (ImageView) layout.findViewById(R.id.walk_page_leftcircle);
        walk_page_rightcircle = (ImageView) layout.findViewById(R.id.walk_page_rightcircle);

        wristband = (RelativeLayout) layout.findViewById(R.id.wristband);
        wrist = (ImageView) layout.findViewById(R.id.wrist);
        wrist_battery = (ImageView) layout.findViewById(R.id.wrist_battery);
        wrist_status = (TextView) layout.findViewById(R.id.wrist_status);
        tv_caliries = (TextView) layout.findViewById(R.id.iv_caliries);
        tv_mileage = (TextView) layout.findViewById(R.id.tv_mileage);
        tv_motiontimes = (TextView) layout.findViewById(R.id.tv_motiontimes);
        tv_caliries_unit = (TextView) layout.findViewById(R.id.iv_caliries_unit);
        tv_mileage_unit = (TextView) layout.findViewById(R.id.tv_mileage_unit);
        tv_motiontimes_unit = (TextView) layout.findViewById(R.id.tv_motiontimes_unit);
        tv_motiontimes_unit.setText("小时");
        tv_motiontimes_unit.setVisibility(View.VISIBLE);
        curprogress = (SeekBar) layout.findViewById(R.id.curprogress);
        curprogress.setClickable(false);
        curprogress.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        caliriesinfo = (TextView) layout.findViewById(R.id.caliriesinfo);
        distanceinfo = (TextView) layout.findViewById(R.id.distanceinfo);
        initData();
        pageAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                StepPageDataView view = new StepPageDataView(mContext);
                if (position == pedometerlist.size() - 1) {
                    view.setData(pedometerlist.get(position), mSuggestSteps, true);
                } else {
                    view.setData(pedometerlist.get(position), mSuggestSteps, false);
                }
                container.addView(view);
                Log.e(tagPage, "sparseArray put : " + position);
                sparseArray.put(position, view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {

                return pedometerlist.size();
            }

            @Override
            public int getItemPosition(Object object) {

                if (object instanceof StepPageDataView) {
                    /**
                     * 保留的可能有意义的字段
                     */
//					PedometerItem mPedometer = ((StepPageDataView) object).getmPedometer();
//					if (mPedometer != null) {
//						if (mPedometer.getDate().equals("今天") && curPageIndex == 0) {
//							Log.i(tag, "pedometerlist: " + pedometerlist);
//							Log.i(tagPage, "mPedometer 今天");
//							return POSITION_NONE;
//						} else {
//							return POSITION_UNCHANGED;
//						}
//					}
                } else {
                    Log.e(tagPage, "getItemPosition  object  not  StepPageDataView");
                }
                return POSITION_NONE;
            }
        };

        viewpager.setAdapter(pageAdapter);
        viewpager.addOnPageChangeListener(new OnPageChangeListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onPageSelected(int arg0) {
                curPageIndex = arg0;
                PedometerItem object = pedometerlist.get(arg0);
                Log.i(tagPage, "onPageSelected: " + arg0 + " object: " + object.toString());
                tv_caliries.setText(object.getCalorie());
                tv_mileage.setText(object.getDistance());
                tv_motiontimes.setText(decimalFormat.format(Float.valueOf(object.getTotal_time())));
                float calirs = Float.valueOf(object.getCalorie());
                float totaltime = Float.valueOf(object.getTotal_time());
                if (totaltime == 0 && calirs > 0) {
                    tv_motiontimes.setText(decimalFormat.format(Float.valueOf(object.getTotal_time())));
                    tv_motiontimes_unit.setVisibility(View.VISIBLE);
                } else {
                    tv_motiontimes.setText(decimalFormat.format(Float.valueOf(object.getTotal_time())));
                    tv_motiontimes_unit.setVisibility(View.VISIBLE);
                }
                int curpro = (int) (100 * Float.parseFloat(object.getStep().equals("-") ? "0" : object.getStep()) / Float.valueOf((object.getTarget())));
                Log.i(tagPage, "curpro: " + curpro + "object.getStep(): " + object.getStep() + " object.getTarget():" + object.getTarget());
                if (curpro > 100) {
                    curpro = 100;
                }
                Log.i(tag, "setProgress: 3" + curpro);
                curprogress.setProgress(curpro);
                caliriesinfo.setText(object.getCalorie_text());
                distanceinfo.setText(object.getDistance_text());
                if (curpro > 50) {
                    caliriesinfo.setGravity(Gravity.LEFT);
                    distanceinfo.setGravity(Gravity.RIGHT);
                } else {
                    caliriesinfo.setGravity(Gravity.RIGHT);
                    distanceinfo.setGravity(Gravity.LEFT);
                }
                if (pedometerlist.size() == 1) {
                    walk_page_leftcircle.setVisibility(View.INVISIBLE);
                    walk_page_rightcircle.setVisibility(View.INVISIBLE);
                } else if (arg0 == 0 && arg0 == pedometerlist.size() - 1) {
                    walk_page_leftcircle.setVisibility(View.INVISIBLE);
                    walk_page_rightcircle.setVisibility(View.INVISIBLE);
                } else if (arg0 == 0) {
                    walk_page_leftcircle.setVisibility(View.INVISIBLE);
                    walk_page_rightcircle.setVisibility(View.VISIBLE);
                } else if (arg0 == pedometerlist.size() - 1) {
                    walk_page_leftcircle.setVisibility(View.VISIBLE);
                    walk_page_rightcircle.setVisibility(View.INVISIBLE);
                } else {
                    walk_page_leftcircle.setVisibility(View.VISIBLE);
                    walk_page_rightcircle.setVisibility(View.VISIBLE);
                }
                // cancelmDialog();
                StepPageDataView cur = sparseArray.get(arg0);
                if (cur != null) {
                    Log.e(tagPage, " sparseArray get " + arg0);
                    cur.startProcessAnimation((int) (Float.valueOf(pedometerlist.get(arg0).getStep()) * 100 / Float.valueOf(pedometerlist.get(arg0).getTarget())));
                } else {
                    Log.e(tagPage, "sparseArray get NULL");
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg1 != 0 || arg2 != 0) {
                    walk_page_leftcircle.setVisibility(View.INVISIBLE);
                    walk_page_rightcircle.setVisibility(View.INVISIBLE);
                } else {
                    walk_page_leftcircle.setVisibility(View.VISIBLE);
                    walk_page_rightcircle.setVisibility(View.VISIBLE);
                    if (arg0 == 0) {
                        walk_page_leftcircle.setVisibility(View.INVISIBLE);
                    } else if (arg0 == pedometerlist.size() - 1) {
                        walk_page_rightcircle.setVisibility(View.INVISIBLE);
                    }
                    if (pedometerlist.size() == 1) {
                        walk_page_leftcircle.setVisibility(View.INVISIBLE);
                        walk_page_rightcircle.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做
            public void onPageScrollStateChanged(int arg0) {
                Log.i(tagPage, "onPageScrollStateChanged: " + arg0);
            }
        });
        initFirstPageWithLocalData();
        return layout;
    }

    /**
     * 防止开始时进入 的卡顿，部分手机（华为）进入viewPager，不执行OnPageSelected
     */
    private void initFirstPageWithLocalData() {
        PedometerItem object = pedometerlist.get(0);
        tv_caliries.setText(object.getCalorie());
        tv_mileage.setText(object.getDistance());
        tv_motiontimes.setText(decimalFormat.format(Float.valueOf(object.getTotal_time())));
        tv_motiontimes.setText(decimalFormat.format(Float.valueOf(object.getTotal_time())));
        tv_motiontimes_unit.setVisibility(View.VISIBLE);
        // }
        int curpro = (int) (100 * Float.parseFloat(object.getStep()) / Float.valueOf(object.getTarget()));
        if (curpro > 100) {
            curpro = 100;
        }
        Log.i(tag, "setProgress: 2" + curpro);
        curprogress.setProgress(curpro);
        caliriesinfo.setText(object.getCalorie_text());
        distanceinfo.setText(object.getDistance_text());
        if (curpro > 50) {
            caliriesinfo.setGravity(Gravity.LEFT);
            distanceinfo.setGravity(Gravity.RIGHT);
        } else {
            caliriesinfo.setGravity(Gravity.RIGHT);
            distanceinfo.setGravity(Gravity.LEFT);
        }
        Log.i(tagPage, "updataTodayView: " + (curStep > blueStep ? curStep : blueStep));
        if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
            updataTodayView(curStep);
        } else {
            updataTodayView(blueStep);
        }

    }

    private void readTodayFromdataSp() {
        dailydate = new DateTime().toString(dateformat);
        SharedPreferences datasp = mContext.getSharedPreferences(Preferences.DayData, Context.MODE_PRIVATE);
        curStep = (int) SharePreferencesUtil.getTmpStep(mContext, dailydate);
        Miles = SharePreferencesUtil.getTmpMiles(mContext, dailydate);
        Calories = (int) SharePreferencesUtil.getTmpCaliries(mContext, dailydate);
        Seconds = SharePreferencesUtil.getTmpSeconds(mContext, dailydate);
        Log.i(TAG, "TodayStep sp : " + "代表用户每次存入数据库的数据" + curStep);
        blueStep = (int) datasp.getFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDaySteps, 0);
        blueMiles = datasp.getFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDayMile, 0);
        blueCalories = datasp.getFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDayCalories, 0);
        blueSeconds = datasp.getFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDaySeconds, 0);
        Log.i(TAG, "ReadTodayFrom sp : " + "curStep" + curStep + " Calories " + Calories + " blueStep" + blueStep + " blueCalories" + blueCalories);
    }

    String dailydate = "";

    private void saveTodayToDataSp(float bStep, float bMiles, float bCalor, float bSecondes) {
        dailydate = new DateTime().toString(dateformat);
        SharedPreferences datasp = mContext.getSharedPreferences(Preferences.DayData, Context.MODE_PRIVATE);
        Editor dataspEditor = datasp.edit();
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDaySteps, bStep);
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDayMile, bMiles);
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDayCalories, bCalor);
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDaySeconds, bSecondes);
        dataspEditor.commit();
    }

    String strMile = "";// UI 攀登公里数显示

    private void initData() {
        /**
         *  暂时存储计步 方便后续使用 固定时间的值
         *  第一时间清除老数据
         */
//        SharePreferencesUtil.saveHocStep(mContext, 0);
        decimalFormat = new DecimalFormat("0.0");
//        readTodayFromdataSp();
//        mCYStepCounterSensorValue = curStep;
        dailydate = new DateTime().toString(dateformat);
        Log.i(tagPage, "0 .--> getFrom SP curStep : " + curStep);
        PedometerItem object = new PedometerItem();

        DateTime datetime = new DateTime();
        // String today = datetime.toString("MM/dd");
        object.setDate("今天");
        if (!AppSettingPreferencesHelper.isSoftPedometerOn()) {
            if (SharePreferencesUtil.getBlueConn(mContext)) {
                wrist_status.setText("已连接");
            } else {
                wrist_status.setText("未连接");
            }

            /**
             * 蓝牙获取到的设备号判断当前设备
             */
            if (AppSharedPreferencesHelper.getMacAddress().length() > 5 && org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), "0903")) {
                wrist_status.setText("已连接");
                // TODO 初始化拿到903返回数据

            } else if (AppSharedPreferencesHelper.getMacAddress().length() > 5 && org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getBlueDeviceName(), "Braceli5")) {
                String blueData = SharePreferencesUtil.getBlueData(getContext());
                // 更新艾薇手环蓝牙数据显示
                syncConnSyncData(blueData);
                /**
                 * 开启记步动态监听服务器 实现动态更新首页记步数据
                 */
                getActivity().startService(new Intent(mContext, BluetoothService.class));
            } else if (AppSharedPreferencesHelper.getMacAddress().length() > 5 && org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), "F4:06:A5")) {
                //判断为敏狐手环
                /**
                 * 创建敏狐连接对象
                 */
                bluetoothLeManager = BluetoothLeManager.getInstance(getActivity(), WalkPageFragment.this, true);
                /**
                 * 处理耗时操作
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 设置同步数据
                         */
                        bluetoothLeManager.setSyncFlag(true);
                        /**
                         * 连接设备
                         */
                        bluetoothLeManager.connect(AppSharedPreferencesHelper.getMacAddress());
                        Log.d("sampleBLE", "start");
                    }
                }).start();

            }
            isBlueDeviceData = true;
            object.setCalorie(decimalFormat.format(blueCalories));
            object.setDistance(decimalFormat.format(blueMiles));
            object.setStep(String.valueOf(blueStep));
            object.setTotal_time("0");

            object.setTarget(AppSharedPreferencesHelper.getPedometer_target());
            String strCalories = StringUtils.calorie2Text(blueCalories);
            object.setCalorie_text(strCalories);
            strMile = StringUtils.distance2Text(blueMiles);
            object.setDistance_text(strMile);
        } else {
            isBlueDeviceData = false;
            object.setCalorie(decimalFormat.format(Calories));
            object.setDistance(decimalFormat.format(Miles));
            object.setStep(String.valueOf(curStep));
            object.setTotal_time(decimalFormat.format(Seconds));
            object.setTarget(AppSharedPreferencesHelper.getPedometer_target());
            String strCalories = StringUtils.calorie2Text(Calories);
            object.setCalorie_text(strCalories);
            strMile = StringUtils.distance2Text(Miles);
            LogUtil.e(TAG, TAG + "initData : curStep>>" + curStep + " >>Calories>>" + Calories + " >>Miles>>" + Miles);
            object.setDistance_text(strMile);
        }

        Log.i(tag, "add " + object.toString());
        pedometerlist.add(object);
        LogUtil.e("kkWalk", ">>>>>>>>>>>>>>>>getTwoMonthDataFromNet before");
        /**
         * 注释掉的网络请求 在新记步算法确定前一切走本地
         */
        getTwoMonthDataFromNet();
        LogUtil.e("kkWalk", "<<<<<<<<<getTwoMonthDataFromNet after");
    }

    PedometerDataJson pedomJson = null;

    private int timeminStep;

    /**
     * 需要保留的网络请求方法
     */
    private void getTwoMonthDataFromNet() {
        LogUtil.e("kkWalk", "getTwoMonthDataFromNet init>>>>>>>>>");
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        LogUtil.e("kkWalk", "params" + params.toString());
        DcareRestClient.volleyGet(Constants.GetPedometerTwoMonth, params, new FastJsonHttpResponseHandler() {

            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelDialog();
                LogUtil.e(tag, "获取记步数据出错，请检查网络连接");
                LogUtil.e("kkWalk", "OnFailure" + errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode);// +
                LogUtil.e("kkWalk", "onsuccess>>" + response.toString());
                cancelDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    pedomJson = response.parseObject(response.toString(), PedometerDataJson.class);
                    /**
                     * 需要第一次加载时存储的步数
                     */
                    curStep = Integer.valueOf(pedomJson.content.data.get(pedomJson.content.data.size() - 1).getStep());
                    // 返回八天数据，今天在这个当日当回数据的基础上计步
                    if (code == 0) {
                        Log.i("kkWalk", "dataSize:" + pedomJson.content.data);
                        if (pedomJson != null && (pedomJson.content.data.size() - 1) > 0 && pedomJson.content.type == 1) {
                            // 如果是903用户，则同步903数据
                            sync903Data(pedomJson.content.data.get(pedomJson.content.data.size() - 1).getStep(), pedomJson.content.data.get(pedomJson.content.data.size() - 1).getCalorie(), pedomJson.content.data.get(pedomJson.content.data.size() - 1).getDistance(), pedomJson.content.data.get(pedomJson.content.data.size() - 1).getTotal_time());
                            Log.i("kkWalk", "\n onSuccess  code: " + code + "\n message: " + message + "\ncontent: " + response.getString("content").toString() + "\nStep" + pedomJson.content.data.get(pedomJson.content.data.size() - 1).getStep() + "\nDate" + pedomJson.content.data.get(pedomJson.content.data.size() - 1).getDate() + "\ntype" + pedomJson.content.type);
                        }

                        List<PedometerItem> li = pedomJson.content.data;
                        if (li != null && li.size() > 0) {
                            PedometerItem pedometerItem = null;
                            for (int i = 0; i < li.size(); i++) {
                                if ((li.size() - 2 - i) > -1) {
                                    Log.i("kkWalk", "foreach pedomJson.content.type111 " + pedomJson.content.type + "\nli.size" + li.size() + "\nitem:" + (li.size() - 2 - i));
                                    pedometerItem = (PedometerItem) li.get(li.size() - 2 - i);
                                }

                                if (!pedometerlist.contains(pedometerItem) && pedometerlist.size() > 0) {
                                    Log.i("kkWalk", "add>>> " + pedometerItem.toString());
                                    pedometerlist.add(0, pedometerItem);
                                }
                            }
                            sparseArray.clear();
                            pageAdapter.notifyDataSetChanged();
                            isInStepCount = true;
                            viewpager.setCurrentItem(pedometerlist.size() - 1, false);
                        }
                        // 给View传递数据
                    } else {// code不为0 发生异常
                        showToast(message);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Log.i("kkWalk", "NullPointerException " + e.toString());
                }

            }
        });
    }

    /**
     * 计步传入步数参数
     */
    private int stepCountInt;
    /**
     * 计步传入计步时间参数
     */
    private long stepTimeFloat;

    /**
     * 春雨计步算法
     */
    private BroadcastReceiver stepReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (AppSettingPreferencesHelper.isSoftPedometerOn()) {

                switch (intent.getAction()) {
//                    // 计步传感器的步数增加广播
//                    case IntentConsts.STEP_COUNTER_SENSOR_VALUE_FILTER:
//                        // 计步传感器的步数
//                        mCYStepCounterSensorValue = intent.getIntExtra(IntentConsts.CY_STEP_SENSOR_VALUE_EXTRA, 0);
//                        // 计步传感器的时间
//                        mSCSMotionTime = intent.getLongExtra(IntentConsts.MOTION_TIME_STEP_SENSOR_EXTRA, 0L);
//                        /**
//                         * 计步数据获得为0 说明从新开始 本地暂存数据清零
//                         */
//                        if (mCYStepCounterSensorValue == 0) {
//                            SharePreferencesUtil.saveHocStep(mContext, 0);// 暂时存储计步 方便后续使用 固定时间的值
//                        }
//                        /**
//                         * 代表用户切换用户 或者第一次登陆
//                         */
//                        if (SharePreferencesUtil.getReData(mContext)) {
//                            SharePreferencesUtil.saveHocStep(mContext, mCYStepCounterSensorValue);// 暂时存储计步 方便后续使用 固定时间的值
//                            /**
//                             * 计算运动距离
//                             */
//                            calculateActiveMile(timeminStep);
//                            /**
//                             * 计算运动卡路里
//                             */
//                            calculateActiveCalory(timeminStep);
//                            SharePreferencesUtil.saveTmpStep(mContext, (float) timeminStep, (float) mDistance, (float) mSCSMotionTime, (float) mCalories);
//                            SharePreferencesUtil.saveReData(mContext, false);
//                            isInStepCount = false;
//                            Log.i(TAG, "TodayStep sp : " + "代表用户切换用户 或者第一次登陆");
//                        } else {
//                            /**
//                             * 服务器与本地数据偏差值不一样
//                             */
//                            Log.i(TAG, "TodayStep sp : " + String.valueOf(mCYStepCounterSensorValue - SharePreferencesUtil.getHocStep(mContext) + curStep) + "    页面显示步数");
//                            Log.i(TAG, "TodayStep sp : " + String.valueOf(SharePreferencesUtil.getHocStep(mContext)) + "    暂存数据库步数");
//                            Log.i(TAG, "TodayStep sp : " + String.valueOf(mCYStepCounterSensorValue) + "    本地步数");
//                            Log.i(TAG, "TodayStep sp : " + String.valueOf(curStep) + "   服务器步数");
//                            stepCountInt = (mCYStepCounterSensorValue - SharePreferencesUtil.getHocStep(mContext) + timeminStep);
//                            stepTimeFloat = mSCSMotionTime;
//
//                            if (isInStepCount) {
//                                SharePreferencesUtil.saveHocStep(mContext, mCYStepCounterSensorValue);// 暂时存储计步 方便后续使用 固定时间的值
//                                isInStepCount = false;
//                            }
//
//                            if (stepCountInt > 0) {
//                                /**
//                                 * 计算运动距离
//                                 */
//                                calculateActiveMile(stepCountInt);
//                                /**
//                                 * 计算运动卡路里
//                                 */
//                                calculateActiveCalory(stepCountInt);
//                                SharePreferencesUtil.saveTmpStep(mContext, stepCountInt, (float) mDistance, (float) stepTimeFloat, (float) mCalories);
//                            } else {
//                                stepCountInt = 0;
//                                /**
//                                 * 计算运动距离
//                                 */
//                                calculateActiveMile(0);
//                                /**
//                                 * 计算运动卡路里
//                                 */
//                                calculateActiveCalory(0);
//                                SharePreferencesUtil.saveTmpStep(mContext, 0, (float) mDistance, (float) stepTimeFloat, (float) mCalories);
//                            }
//                        }
//                        break;
//
//                    // 加速度传感器的步数增加广播
//                    case IntentConsts.ACCELERATE_SENSOR_VALUE_FILTER:
//                        // 加速度传感器的步数
//                        mCYAccelerateSensorValue = intent.getIntExtra(IntentConsts.CY_ACCELERATE_SENSOR_VALUE_EXTRA, 0);
//                        // 加速度传感器的时间
//                        mASMotionTime = intent.getLongExtra(IntentConsts.MOTION_TIME_ACCELERATE_EXTRA, 0L);
//
//                        /**
//                         * 计步数据获得为0 说明从新开始 本地暂存数据清零
//                         */
//                        if (mCYAccelerateSensorValue == 0) {
//                            SharePreferencesUtil.saveHocStep(mContext, 0);// 暂时存储计步 方便后续使用 固定时间的值
//                        }
//                        /**
//                         * 代表用户切换用户 或者第一次登陆
//                         */
//                        if (SharePreferencesUtil.getReData(mContext)) {
//                            SharePreferencesUtil.saveHocStep(mContext, mCYAccelerateSensorValue);// 暂时存储计步 方便后续使用 固定时间的值
//                            /**
//                             * 计算运动距离
//                             */
//                            calculateActiveMile(timeminStep);
//                            /**
//                             * 计算运动卡路里
//                             */
//                            calculateActiveCalory(timeminStep);
//                            SharePreferencesUtil.saveTmpStep(mContext, (float) timeminStep, (float) mDistance, (float) mSCSMotionTime, (float) mCalories);
//                            SharePreferencesUtil.saveReData(mContext, false);
//                        } else {
//                            /**
//                             * 服务器与本地数据偏差值不一样
//                             */
//                            Log.i(TAG, "TodayStep sp : " + String.valueOf(mCYAccelerateSensorValue - SharePreferencesUtil.getHocStep(mContext) + curStep));
//                            stepCountInt = (mCYAccelerateSensorValue - SharePreferencesUtil.getHocStep(mContext) + timeminStep);
//                            stepTimeFloat = mASMotionTime;
//
//                            if (isInStepCount) {
//                                SharePreferencesUtil.saveHocStep(mContext, mCYAccelerateSensorValue);// 暂时存储计步 方便后续使用 固定时间的值
//                            }
//                            if (stepCountInt > 0) {
//                                /**
//                                 * 计算运动距离
//                                 */
//                                calculateActiveMile(stepCountInt);
//                                /**
//                                 * 计算运动卡路里
//                                 */
//                                calculateActiveCalory(stepCountInt);
//                                SharePreferencesUtil.saveTmpStep(mContext, stepCountInt, (float) mDistance, (float) stepTimeFloat, (float) mCalories);
//                            } else {
//                                stepCountInt = 0;
//                                /**
//                                 * 计算运动距离
//                                 */
//                                calculateActiveMile(0);
//                                /**
//                                 * 计算运动卡路里
//                                 */
//                                calculateActiveCalory(0);
//                                SharePreferencesUtil.saveTmpStep(mContext, 0, (float) mDistance, (float) stepTimeFloat, (float) mCalories);
//                            }
//                        }
//                        break;
                    // 计步传感器的步数增加广播
                    case IntentConsts.STEP_COUNTER_SENSOR_VALUE_FILTER:
                        // 计步传感器的步数
                        mCYStepCounterSensorValue = intent.getIntExtra(IntentConsts.CY_STEP_SENSOR_VALUE_EXTRA, 0);
                        // 计步传感器的时间
                        mSCSMotionTime = intent.getLongExtra(IntentConsts.MOTION_TIME_STEP_SENSOR_EXTRA, 0L);
                        calculateActiveMile(mCYStepCounterSensorValue);
                        calculateActiveCalory(mCYStepCounterSensorValue);
                        curStep = mCYStepCounterSensorValue;
                        SharePreferencesUtil.saveTmpStep(mContext, (float) mCYStepCounterSensorValue, (float) mDistance, (float) mSCSMotionTime / 3600000, (float) mCalories);

                        break;

                    // 加速度传感器的步数增加广播
                    case IntentConsts.ACCELERATE_SENSOR_VALUE_FILTER:
                        // 加速度传感器的步数
                        mCYAccelerateSensorValue = intent.getIntExtra(IntentConsts.CY_ACCELERATE_SENSOR_VALUE_EXTRA, 0);
                        // 加速度传感器的时间
                        mASMotionTime = intent.getLongExtra(IntentConsts.MOTION_TIME_ACCELERATE_EXTRA, 0L);
                        calculateActiveMile(mCYAccelerateSensorValue);
                        calculateActiveCalory(mCYAccelerateSensorValue);
                        curStep = mCYAccelerateSensorValue;
                        SharePreferencesUtil.saveTmpStep(mContext, (float) mCYAccelerateSensorValue, (float) mDistance, (float) mASMotionTime / 3600000, (float) mCalories);
                        break;
                }

                isBlueDeviceData = false;
                if (curPageIndex == pedometerlist.size() - 1) {
                    readTodayFromdataSp();
                    updateViewFromView();
                }
            } else {
                isBlueDeviceData = true;
                if (intent.getAction().equals(Preferences.HOME_STEP_UPDATE)) {
                    if (!SharePreferencesUtil.getBlueData(getContext()).equals("")) {
                        CurrData_0x29_Convert_DailyData(SharePreferencesUtil.getBlueData(getContext()));
                    }
                    readTodayFromdataSp();
                    updateViewByBlueData();

                }
                updateViewByBlueData();
            }
        }
    };


    void registerReceiver() {
        IntentFilter filter = new IntentFilter();
//		IntentFilter fi = new IntentFilter();
        // 原有 软件计步数据
        filter.addAction(IntentConsts.STEP_COUNTER_SENSOR_VALUE_FILTER);
        filter.addAction(IntentConsts.ACCELERATE_SENSOR_VALUE_FILTER);
        filter.addAction(IntentConsts.TIME_CHANGE_FILTER);
        filter.addAction(Preferences.HOME_STEP_UPDATE);
//		fi.addAction(Preferences.ActionGetCurStepCount);
//		// 同步当日手环步数广播 蓝牙计步数据
//		fi.addAction(Preferences.ActionGetBlueStepCount);
        getActivity().registerReceiver(stepReceiver, filter);
//		getActivity().registerReceiver(mReceiver, fi);
    }

    /**
     * 更新蓝牙连接数据
     */
    @SuppressLint("NewApi")
    protected void updateViewByBlueData() {
        PedometerItem object = pedometerlist.get(pedometerlist.size() - 1);
        if (object.getDate().equals("今天")) {
            LogUtil.i(TAG, "3 .--> updateViewByBlueData");
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            object.setCalorie(decimalFormat.format(blueCalories));
            object.setDistance(decimalFormat.format(blueMiles));
            object.setStep(blueStep + "");
            object.setTotal_time(decimalFormat.format(blueSeconds));
            String strCalories = StringUtils.calorie2Text(blueCalories);
            object.setCalorie_text(strCalories);
            strMile = StringUtils.distance2Text(blueMiles);
            object.setDistance_text(strMile);
            wrist_status.setText("已连接");
            if (curPageIndex == pedometerlist.size() - 1) {
                pageAdapter.notifyDataSetChanged();
                updataTodayView(blueStep);
                tv_caliries.setText(decimalFormat.format(blueCalories));
                tv_mileage.setText(decimalFormat.format(blueMiles));
                tv_motiontimes.setText(decimalFormat.format(blueSeconds));
                tv_motiontimes_unit.setVisibility(View.VISIBLE);
                caliriesinfo.setText(strCalories);
                distanceinfo.setText(strMile);
                int curpro = (int) (100 * blueStep / Float.valueOf(AppSharedPreferencesHelper.getPedometer_target()));
                if (curpro > 100) {
                    curpro = 100;
                }
                Log.i(tag, "setProgress:1 " + curpro);
                curprogress.setProgress(curpro);
                if (curpro > 50) {
                    caliriesinfo.setGravity(Gravity.LEFT);
                    distanceinfo.setGravity(Gravity.RIGHT);
                } else {
                    caliriesinfo.setGravity(Gravity.RIGHT);
                    distanceinfo.setGravity(Gravity.LEFT);
                }
            }
        } else {
            Log.e(tag, "最后一个不是今天 ");
        }
    }

    /**
     * 圆形进度条监听
     *
     * @param blueStep2
     */
    private void updataTodayView(int blueStep2) {
        // TODO Auto-generated method stub
        StepPageDataView cur = sparseArray.get(pedometerlist.size() - 1);
        if (cur != null) {
            Log.e(tag, " sparseArray get " + (int) (pedometerlist.size() - 1));
            cur.setCurStep(blueStep2);// 当日步数更新
            cur.postInvalidate();// 刷新view
        } else {
            Log.e(tag, "sparseArray get NULL");
        }
        saveTodayToDataSp(blueStep, (float) calculateActiveMile(blueStep), blueCalories, Float.valueOf(decimalFormat.format(blueStep * 500 / 3600000f)));
    }

    /**
     * 更新非蓝牙数据
     */
    @SuppressLint("NewApi")
    protected void updateViewFromView() {
        PedometerItem object = pedometerlist.get(pedometerlist.size() - 1);
        if (object.getDate().equals("今天")) {
            object.setCalorie(decimalFormat.format(Calories));
            object.setDistance(decimalFormat.format(Miles));
            object.setStep(curStep + "");
            object.setTotal_time((Seconds) + "");
            String strCalories = StringUtils.calorie2Text(Calories);
            object.setCalorie_text(strCalories);
            strMile = StringUtils.distance2Text(Miles);
            object.setDistance_text(strMile);
            if (curPageIndex == pedometerlist.size() - 1) {
                Log.i(TAG, "TodayStep sp : " + "代表用户摇晃手机" + curStep);
                isBlueDeviceData = false;
                pageAdapter.notifyDataSetChanged();
                updataTodayView(curStep);
                tv_caliries.setText(decimalFormat.format((float) Calories));
                tv_mileage.setText(decimalFormat.format(Miles));
                tv_motiontimes.setText(decimalFormat.format(Seconds));
                tv_motiontimes_unit.setVisibility(View.VISIBLE);
                caliriesinfo.setText(strCalories);
                distanceinfo.setText(strMile);
                int curpro = (int) (100 * curStep / Float.valueOf(AppSharedPreferencesHelper.getPedometer_target()));
                Log.i(tagPage, "curpro: " + curpro + "object.getStep(): " + object.getStep() + " object.getTarget():" + object.getTarget());
                if (curpro > 100) {
                    curpro = 100;
                }
                Log.i(tag, "setProgress: 4" + curpro);
                curprogress.setProgress(curpro);
                if (curpro > 50) {
                    caliriesinfo.setGravity(Gravity.LEFT);
                    distanceinfo.setGravity(Gravity.RIGHT);
                } else {
                    caliriesinfo.setGravity(Gravity.RIGHT);
                    distanceinfo.setGravity(Gravity.LEFT);
                }
            }
        } else {
            Log.e(tag, "最后一个不是今天 ");
        }

    }


    /**
     * 卡路里
     */
    private double mCalories = 0;
    private static double METRIC_RUNNING_FACTOR = 1.02784823;
    private static double METRIC_WALKING_FACTOR = 0.708;

    /**
     * 监听运动卡路里
     */
    public double calculateActiveCalory(int step) {
        mCalories = +step * (Float.parseFloat(AppSharedPreferencesHelper.getWeight()) * (false ? METRIC_RUNNING_FACTOR : METRIC_WALKING_FACTOR)) * Float.parseFloat(AppSharedPreferencesHelper.getUserStepLeight()) / 100000.0;
        return mCalories;
    }

    /**
     * 运动距离
     */
    double mDistance = 0;

    /**
     * 监听运动距离
     */
    public double calculateActiveMile(int step) {

        mDistance = +step * Float.parseFloat(AppSharedPreferencesHelper.getUserStepLeight()) / 100000.0;
        return mDistance;
    }


    void unregisterReceiver() {
        getActivity().unregisterReceiver(stepReceiver);
    }

    private void registerDeviceReceiver() {
        // TODO Auto-generated method stub
        IntentFilter filter = new IntentFilter();
        // 自拍广播
        filter.addAction("com.ACTION_SELFIE_DATA");
        // 连接广播
        filter.addAction("com.WRISTBAND_CONNECT");
        // 断开广播
        filter.addAction("com.WRISTBAND_DISCONNECT");
        // 找手机
        filter.addAction("com.ACTION_FIND_PHONE");
        // 简单的后台扫描广播，用来检测手环和手机蓝牙的连接状态，15秒钟发一次广播状态。广播在timeSerivce.java种发出的
        filter.addAction("COM.ACTION.TIMESERVICE");
        mContext.registerReceiver(mBlueDeviceReceiver, filter);
    }

    private void unregisterDeviceReceiver() {
        // TODO Auto-generated method stub
        mContext.unregisterReceiver(mBlueDeviceReceiver);
    }

    private BroadcastReceiver mBlueDeviceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(tag, "onReceive: " + intent.getAction());
            if ("com.ACTION_SELFIE_DATA".equalsIgnoreCase(intent.getAction())) {
                showToast("自拍开启");
            } else if ("com.WRISTBAND_CONNECT".equals(intent.getAction())) {
                Log.i(tagBlue, "WRISTBAND_CONNECT");
                if (SharePreferencesUtil.getBlueConn(mContext)) {
                    wrist_status.setText("已连接");
                } else {
                    wrist_status.setText("未连接");
                }
                mHandler.sendEmptyMessage(SendDeviceTimeSync);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(SendDeviceDaily);
                    }
                }, 10 * 1000);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(SendDevice5Mins);
                    }
                }, 40 * 1000);
            } else if ("com.WRISTBAND_DISCONNECT".equals(intent.getAction())) {
                wrist_status.setText("未连接");
                Log.i(tagBlue, "WRISTBAND_DISCONNECT");
            } else if ("com.ACTION_FIND_PHONE".equals(intent.getAction())) {
            } else if ("COM.ACTION.TIMESERVICE".equals(intent.getAction())) {
            }
        }
    };

    // 广播同步手环数据 0x29
    // void syncConnSyncData(String Year, String updataTodayView) {
    void syncConnSyncData(String data29) {
        if (TextUtils.isEmpty(data29)) {
            showToast("请等待....");
            mContext.sendBroadcast(new Intent(Preferences.ActionBlueSysn)); // 同步蓝牙数据请求
            return;
        }
        DeviceDailyData dailySport = CurrData_0x29_Convert_DailyData(data29);
        try {
            int Year = dailySport.getYear();
            if (Year == 255) {
                blueStep = dailySport.getSteps();
                updataTodayView(dailySport.getSteps());
                blueCalories = (float) dailySport.getCalorie();
                blueMiles = (float) calculateActiveMile(blueStep);
                blueSeconds = Float.valueOf(decimalFormat.format(blueStep * 500 / 3600000f));
                if (SharePreferencesUtil.getBlueConn(mContext)) {
                    wrist_status.setText("已连接");
                } else {
                    wrist_status.setText("未连接");
                }
                isBlueDeviceData = true;
                LogUtil.e(TAG, "2 .--> Year>" + "blueStep" + blueStep + "blueMiles" + blueMiles + "blueCalories" + blueCalories + "blueSeconds" + blueSeconds);
                if (blueStep != wristData.getS()) {
                    wristData.setC(blueCalories);
                    wristData.setD((float) calculateActiveMile(blueStep));
                    wristData.setS(blueStep);
                    wristData.setT(blueSeconds);
                    saveWristDataToSp();
                }
                saveTodayToDataSp(blueStep, (float) calculateActiveMile(blueStep), blueCalories, blueSeconds);
                // float bStep,float bMiles,float bCalor,float bSecondes
            } else {
                showToast("请等待....");
            }
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            LogUtil.e(TAG, "syncConnSyncData>>NullPointerException");
            // e.printStackTrace();
        }

    }

    // TODO 同步903
    void sync903Data(String bStep, String bCalories, String bDistance, String bSeconds) {
        blueStep = Integer.valueOf(bStep);
        updataTodayView(blueStep);
        blueCalories = Float.valueOf(bCalories);
        blueMiles = (float) calculateActiveMile(blueStep);
        blueSeconds = Float.valueOf(decimalFormat.format(blueStep * 500 / 3600000f));
        isBlueDeviceData = true;
        LogUtil.e(TAG, "2 .--> Year>" + "blueStep" + blueStep + "blueMiles" + blueMiles + "blueCalories" + blueCalories + "blueSeconds" + blueSeconds);
        if (blueStep != wristData.getS()) {
            wristData.setC(blueCalories);
            wristData.setD(blueMiles);
            wristData.setS(blueStep);
            wristData.setT(blueSeconds);
            saveWristDataToSp();
        }
        /**
         * 存储蓝牙903数据
         */
        saveTodayToDataSp(blueStep, (float) calculateActiveMile(blueStep), blueCalories, blueSeconds);
        updateViewByBlueData();
    }

    /**
     * 是否已经发送同步
     */
    public boolean isSendSync = false;

    @Override
    public void receive_msg_data() {
        msg_iv.setImageResource(R.drawable.icon_msg_unread);
    }

    public DeviceDailyData CurrData_0x29_Convert_DailyData(String json) {
        DeviceDailyData dailyData = new DeviceDailyData();
        JSONObject jobject;
        try {
            jobject = JSON.parseObject(json);
        } catch (Exception e) {
            return null;
        }
        dailyData.setYear(jobject.getIntValue("year"));
        dailyData.setMonth(jobject.getIntValue("month"));
        dailyData.setDay(jobject.getIntValue("day"));
        dailyData.setSteps(jobject.getIntValue("sportSteps"));
        dailyData.setDistance(jobject.getFloatValue("sportDistances/1000"));
        dailyData.setCalorie(jobject.getFloatValue("sportCalories"));
        dailyData.setBcc(jobject.getIntValue("uid"));
        dailyData.set_converted(jobject.getIntValue("sportType"));
        dailyData.set_uploaded(jobject.getIntValue("count"));
        saveTodayToDataSp(dailyData.getSteps(), (float) calculateActiveMile(dailyData.getSteps()), (float) dailyData.getCalorie(), Float.valueOf(decimalFormat.format(dailyData.getSteps() * 500 / 3600000f)));
        return dailyData;
    }

    /**
     * 蓝牙连接回调
     *
     * @param b 是否连接
     * @param i 1 连接过程中断开，2 连接设备超时， 3 连接状态下断开连接；4 蓝牙开关关闭；6 主动断开连接
     */
    @Override
    public void isConnected(boolean b, int i) {


    }

    /**
     * 蓝牙扫描结果回调
     *
     * @param s  MAC地址 产品序列号
     * @param s1 SSID 设备广播名 F4T
     */
    @Override
    public void onScanResult(String s, String s1) {
        Log.d("sampleBLE", "code" + s1);
    }

    /**
     * 数据同步过程数据流
     *
     * @param i  各个状态的步数
     * @param i1 9 静止；10 走路；11 跑步
     * @param l  开始时间戳
     * @param l1 结束时间戳
     * @param i2 同步过程百分比
     */
    @Override
    public void onSyncStepData(int i, int i1, long l, long l1, int i2) {
        showToast("请等待....");
        /**
         * 代码走到这步证明已经连接成功 设置标示为true
         */
        SharePreferencesUtil.saveBlueConn(DcareApplication.mCon, true);
        Log.d("sampleBLE", "onSyncStepData");
    }

    /**
     * 同步完成
     *
     * @param i  硬件设备版本号
     * @param i1 当前电池电量
     */
    @Override
    public void onSyncFinished(int i, int i1) {
        /**
         * getDaySportsData
         * 0 当天数据 1 昨天 2 前天 一次类推
         * 设备MAC地址
         * 身高
         * 体重
         */
        Log.d("sampleBLE", "sync finished, version " + i + ", battery " + i1);
        DaySportsData data = bluetoothLeManager.getDaySportsData(0, AppSharedPreferencesHelper.getMacAddress(), 170, 65);
        if (data == null) {
            Log.d("sampleBLE", "no sports data!");
        } else {
            Log.d("sampleBLE", "steps: " + data.totalSteps + ", distance " + data.totalDistance + ", time " + data.totalTime + ", calories " + data.totalCalories);
            saveTodayToDataSp(data.totalSteps, (data.totalDistance / 1000f), data.totalCalories, (data.totalTime / 3600f));
            mContext.sendBroadcast(new Intent(Preferences.HOME_STEP_UPDATE));
        }
        mContext.sendBroadcast(new Intent("com.WRISTBAND_CONNECT"));
    }

    /**
     * 硬件升级回调
     *
     * @param l  当前升级的文件位置
     * @param l1 总文件大小
     */
    @Override
    public void onOTAProgress(long l, long l1) {


    }

    /**
     * 硬件升级完成
     */
    @Override
    public void onOTAFinished() {

    }

    /**
     * 硬件升级失败
     *
     * @param i 一般为0的错误码
     */
    @Override
    public void onOTAError(int i) {

    }


    @Override
    public String initContent() {
        return null;
    }

    void cancelDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}

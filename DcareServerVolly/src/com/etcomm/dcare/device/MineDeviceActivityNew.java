package com.etcomm.dcare.device;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.services.BluetoothService;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.bluetooth.BlueDeviceListAdapter;
import com.etcomm.dcare.bluetooth.BluetoothUtils;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.DialogFactory;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.google.gson.Gson;
import com.iwown.android_iwown_ble.bluetooth.WristBandDevice;
import com.iwown.android_iwown_ble.model.WristBand;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import me.chunyu.pedometerservice.PedometerCounterService;

/**
 * 设置-我的计步-我的设备 绑定手环界面
 *
 * @author etc
 */
public class MineDeviceActivityNew extends BaseActivity {
    protected static final int StartScan = 0;

    private static final long CHECK_TIME = 5000;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.framelayout)
    FrameLayout framelayout;
    @Bind(R.id.searchbluedevices)
    LinearLayout searchbluedevices;
    @Bind(R.id.search_device)
    ImageView search_device;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.bindeddevice)
    LinearLayout bindeddevice;
    @Bind(R.id.device_code)
    TextView device_code;
    @Bind(R.id.device_status)
    TextView device_status;
    @Bind(R.id.tv_conngsm)
    TextView tvConnGsm;
    @Bind(R.id.et_gsm)
    EditText etGsm;
    @Bind(R.id.unbinddevice)
    Button unbinddevice;
    final String deiverName = "0903"; // 903设备名
    // @Bind(R.id.ll_hc_gsm)
    // LinearLayout llHcGsm; // 添加GSM 901/903 设备
    private BluetoothUtils mBluetoothUtils;
    public static List<WristBand> devs = new ArrayList<WristBand>();
    boolean isConn = false; // 连接状态
    byte[] datas = null;
    /**
     * 敏狐手环连接相关对象
     */
    /**
     * 当前是否在搜索
     */
    private boolean isCurScan = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case StartScan:
                    startscan();
                    // if (Build.VERSION.SDK_INT >= 18) {
                    startService(new Intent(mContext, BluetoothService.class));
                    // } else {
                    // showToast("您的手机暂时不支持");
                    // }

                    mHandler.postDelayed(checkIfBloothClosedWhenScan, CHECK_TIME);
                    break;

                default:
                    break;
            }
        }

        ;
    };


    private BlueDeviceListAdapter mAdapter;
    protected WristBand mDevice;
    protected int connectCount = 0;
    protected BluetoothActionListener mListener = new BluetoothActionListener() {
        @Override
        public void startConnectDevice() {
            cancelmDialog();
            showProgress(DIALOG_DEFAULT, true);
        }

        @Override
        public void onDeviceDisConnected() {
            device_status.setText("已断开连接");
            cancelmDialog();
            if (!StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
                if (connectCount <= 3) {
                    startService(new Intent(mContext, BluetoothService.class));
                    connectCount++;
                }
            }
        }

        @Override
        public void onDeviceConnected() {
            cancelmDialog();
            connectCount = 0;
            searchbluedevices.setVisibility(View.GONE);
            bindeddevice.setVisibility(View.VISIBLE);
            /**
             * 判断是否是敏狐手环 F4T
             */
            if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), "F4:06:A5")) {
                //敏狐手环
                device_code.setText(AppSharedPreferencesHelper.getMacAddress());
            } else {
                //其他
                device_code.setText(AppSharedPreferencesHelper.getBlueDeviceName());
            }
            device_status.setText("已连接");
            if (StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
                Log.e(tag, "mac address error");
                return;
            }
            bindDeviceNet("");

        }

        @Override
        public void onDeviceActionDataAvailabe() {
            LogUtil.i(tag, "onDeviceActionDataAvailabe");
        }

        @Override
        public void startScanDevice() {
            // TODO Auto-generated method stub
            LogUtil.i(tag, "startScanDevice");
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(tag, "onReceive: " + intent.getAction());
            if ("com.ACTION_SELFIE_DATA".equalsIgnoreCase(intent.getAction())) {
                showToast("自拍开启");
            } else if ("com.WRISTBAND_CONNECT".equals(intent.getAction())) {
                /**
                 * 设置为已连接
                 */
                if (mListener != null) {
                    mListener.onDeviceConnected();
                }
            } else if ("com.WRISTBAND_DISCONNECT".equals(intent.getAction())) {
                /**
                 * 设置为断开连接
                 */
                if (mListener != null) {
                    mListener.onDeviceDisConnected();
                }
            } else if ("com.ACTION_FIND_PHONE".equals(intent.getAction())) {
                // data.add("找手机功能手环反馈广播");
            } else if ("COM.ACTION.TIMESERVICE".equals(intent.getAction())) {
                // if (!getWristBand().isConnected()) {
                // getWristBand().stopLeScan(); 、、
                isCurScan = false;
                stopScanAnimation();
                // getWristBand().startLeScan();、、
                isCurScan = true;
                // }
            } else if ("COM.ACTION.UPMINEUI".equals(intent.getAction())) {
                mAdapter.notifyDataSetChanged();
            } else if ("COM.ACTION.UPMINEUIONDEVICE".equals(intent.getAction())) {
                if (mListener != null) {
                    mListener.onDeviceConnected();
                }
            } else if ("COM.ACTION.UPMINEUIONDISDEVICE".equals(intent.getAction())) {
                device_status.setText("已断开连接");
            }
        }
    };

    protected void onPause() {
        super.onPause();
    }

    ;

    /**
     * 后台同步绑定状态
     */
    protected void bindDeviceNet(final String gsmNumber) {
        if (haveBinded) {
            LogUtil.i(tag, "已经绑定成功");
            // Toast.makeText(mContext, "绑定成功", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<String, String>();

        // TODO网络绑定请求 gsm 设备 待确认与验证
        if (gsmNumber.trim().length() == 16 && org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(gsmNumber.trim(), deiverName)) {
            params.put("mac", gsmNumber);
            params.put("name", gsmNumber);
        } else {

            params.put("mac", AppSharedPreferencesHelper.getMacAddress());
            params.put("name", AppSharedPreferencesHelper.getBlueDeviceName());
        }
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        LogUtil.i(tag, "params: " + Constants.BindDevice + ">>" + params.toString());
        DcareRestClient.volleyPost(Constants.BindDevice, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                showToast(R.string.network_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        String status = response.getJSONObject("content").getString("status");
                        if (status.equals("1")) {
                            showToast("绑定成功");
                            mContext.sendBroadcast(new Intent(Preferences.ACTION_USER_EXIT));
                            if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(gsmNumber.trim(), deiverName)) {
                                AppSharedPreferencesHelper.setMacAddress(gsmNumber.trim()); // 保存903mac
                                searchbluedevices.setVisibility(View.GONE);
                                bindeddevice.setVisibility(View.VISIBLE);
                                /**
                                 * 判断是否是敏狐手环 F4T
                                 */
                                if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), "F4:06:A5")) {
                                    //敏狐手环
                                    device_code.setText(AppSharedPreferencesHelper.getMacAddress());
                                } else {
                                    //其他
                                    device_code.setText(AppSharedPreferencesHelper.getBlueDeviceName());
                                }
                                device_status.setText("已连接");
                                AppSettingPreferencesHelper.setIfSoftPedometerOn(false);

                            } else {
                                sendBroadcast(new Intent(Preferences.ActionBlueSysn)); // 同步艾薇蓝牙数据请求
                            }

                        } else {
                            String text = response.getJSONObject("content").getString("text");
                            if (StringUtils.isEmpty(text)) {
                                showToast("绑定失败");
                            } else {
                                showToast(text);
                            }
                        }
                    } else {// code不为0 发生异常

                        showToast(message);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });
    }

    /**
     * 后台同步解绑操作
     */
    protected void unBindDeviceNet() {
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
            Log.e(tag, "mac address error");
            return;
        }
        params.put("name", AppSharedPreferencesHelper.getBlueDeviceName());
        params.put("mac", AppSharedPreferencesHelper.getMacAddress());
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        LogUtil.i(tag, Constants.UnBindDevice + " >>>params: " + params.toString());
        DcareRestClient.volleyGet(Constants.UnBindDevice, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                showToast(R.string.network_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelmDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        String status = response.getJSONObject("content").getString("status");
                        if (status.equals("0")) {
                            unbindDeviceSuccess();
                            showToast("解绑成功");
                            saveTodayToDataSp(0, 0, 0, 0);
                            PedometerCounterService.initAppService(mContext);
                            AppSettingPreferencesHelper.setIfSoftPedometerOn(true);
                            mContext.sendBroadcast(new Intent(Preferences.OPEN_NEWS));
                        } else {
                            showToast("解绑失败");
                        }
                    } else {// code不为0 发生异常
                        unbindDeviceSuccess();
                        PedometerCounterService.initAppService(mContext);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 存储蓝牙计步及更新退出清空步数
     * @param bStep 步数
     * @param bMiles 距离
     * @param bCalor 卡路里
     * @param bSecondes
     */
    private void saveTodayToDataSp(float bStep, float bMiles, float bCalor, float bSecondes) {
        String dailydate = new DateTime().toString("yyyyMMdd");
        SharedPreferences datasp = mContext.getSharedPreferences(Preferences.DayData, Context.MODE_PRIVATE);
        SharedPreferences.Editor dataspEditor = datasp.edit();
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDaySteps, bStep);
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDayMile, bMiles);
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDayCalories, bCalor);
        dataspEditor.putFloat(dailydate + "-" + AppSharedPreferencesHelper.getUserId() + Preferences.BLUEDaySeconds, bSecondes);
        dataspEditor.commit();
    }

    /**
     * 解除绑定
     */
    protected void unbindDeviceSuccess() {
        /**
         * 判断是否是敏狐手环 F4T 如果是 进行假定的绑定或者解绑操作
         */
        if (!org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), "F4:06:A5")) {
            //敏狐手环
            WristBandDevice.getInstance(mContext).disconnect();
        }
        sendBroadcast(new Intent(Preferences.ActionBlueDisConn));
        AppSharedPreferencesHelper.setMacAddress("");
        AppSharedPreferencesHelper.setBlueDeviceName("");
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(checkIfBloothClosedWhenScan);
            mHandler = null;
        }
        devs.clear();
        mAdapter.notifyDataSetChanged();
        sendBroadcast(new Intent(Preferences.ActionBlueSysn));
        unregisterReceiver();
    }

    /**
     * 敏狐手环连接
     *
     * @param device
     */
    private void onFastFoxSelected(final WristBand device) {
        /**
         * 重新复制设备对象
         */
        this.mDevice = device;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 停止动画及添加标示
                 */
                isCurScan = false;
                stopScanAnimation();
                /**
                 * 保存手环硬件信息
                 */
                SharedPreferences mySharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("mac", device.getAddress());
                editor.putString("name", device.getName());
                editor.putInt("name", device.getRssi());
                AppSharedPreferencesHelper.setMacAddress(device.getAddress());
                AppSharedPreferencesHelper.setBlueDeviceName(device.getName());
                AppSharedPreferencesHelper.setBlueDeviceRssi(device.getRssi());
                editor.commit();
            }
        });

        finish();

    }


    /**
     * 扫描窗口点击扫描的设备回调
     */
    public void onDeviceSelected(final WristBand device, String name) {
        this.mDevice = device;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 连接方法 device 为bluetoothDevice 对象
                 */
                if (mListener != null) {
                    mListener.startConnectDevice();
                }
                WristBandDevice.getInstance(mContext).stopLeScan();
                isCurScan = false;
                stopScanAnimation();
                WristBandDevice.getInstance(mContext).connect(device);
                LogUtil.e(TAG, "WristBandDevice connect " + device.getName() + " " + device.getAddress());
                // device.getAddress()
                // WristBandDevice.getInstance(mContext).connectMac(mac);
                SharedPreferences mySharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("mac", device.getAddress());
                editor.putString("name", device.getName());
                editor.putInt("name", device.getRssi());
                editor.putString("paircode", device.getPairCode());
                editor.putString("paircodebyte", device.getPairCodeBytes().toString());
                AppSharedPreferencesHelper.setMacAddress(device.getAddress());
                AppSharedPreferencesHelper.setBlueDeviceName(device.getName());
                AppSharedPreferencesHelper.setBlueDeviceRssi(device.getRssi());
                editor.commit();
                LogUtil.i(TAG, "mac: " + device.getAddress() + "name:" + device.getName() + "getRssi:" + device.getRssi());
            }
        });
    }

    private Animation mAnimation;
    private ValueAnimator valueAnimator;
    /**
     * 设备已经被绑定,不再去请求绑定
     */
    protected boolean haveBinded = false;
    private boolean hasAskEnableBlueTooth = false;

    /**
     * 获取WristBandDevice 对象 get WristBandDevice
     *
     * @return
     */

    private void registerReceiver() {
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
        // UI更新广播
        filter.addAction("COM.ACTION.UPMINEUI");
        filter.addAction("COM.ACTION.UPMINEUIONDEVICE"); // mListener.onDeviceConnected();
        filter.addAction("COM.ACTION.UPMINEUIONDISDEVICE"); // mListener.onDeviceConnected();
        // dis
        mContext.registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        // TODO Auto-generated method stub
        unregisterReceiver(mReceiver);
    }

    /**
     * model to json 使用Gson工具
     *
     * @param obj
     * @return
     */
    public String toJson(Object obj) {
        Gson gson = new Gson();
        LogUtil.i(TAG, "gson.toJson(obj: " + gson.toJson(obj));
        return gson.toJson(obj);
    }

    /**
     * 连接手环
     *
     * @param device
     */
    public void getIfDeviceCanConnect(final WristBand device) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", device.getName());
        params.put("mac", device.getAddress());
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        showProgress(DIALOG_DEFAULT, true);
        LogUtil.i(tag, "params: " + Constants.BindDevice + ">>" + params.toString());
        DcareRestClient.volleyPost(Constants.BindDevice, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelmDialog();
                showToast(R.string.network_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        String status = response.getJSONObject("content").getString("status");
                        if (status.equals("1")) {
                            /**
                             * 判断是否是敏狐手环 F4T
                             */
                            if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(device.getAddress(), "F4:06:A5")) {
                                //敏狐手环
                                onFastFoxSelected(device);
                            } else {
                                //其他
                                onDeviceSelected(device, device.getName());
                            }
                        } else if (status.equals("-1")) {
                            String text = response.getJSONObject("content").getString("text");
                            if (StringUtils.isEmpty(text)) {
                                showToast("绑定失败");
                            } else {
                                showToast(text);
                            }
                        }
                    } else {// code不为0 发生异常
                        showToast(message);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final boolean mIsBluetoothOn = mBluetoothUtils.isBluetoothOn();
        final boolean mIsBluetoothLePresent = mBluetoothUtils.isBluetoothLeSupported();

        if (!hasAskEnableBlueTooth) {
            mBluetoothUtils.askUserToEnableBluetoothIfNeeded();
            hasAskEnableBlueTooth = true;
        }
        if (mIsBluetoothOn && mIsBluetoothLePresent) {
            if (!StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
                if (!WristBandDevice.getInstance(mContext).isConnected())
                    // ?????????????????????????????????????????????????????????????????????/
                    if (mDevice == null) {
                        mDevice = new WristBand();
                        mDevice.setAddress(AppSharedPreferencesHelper.getMacAddress());
                        mDevice.setName(AppSharedPreferencesHelper.getBlueDeviceName());
                        mDevice.setRssi(AppSharedPreferencesHelper.getBlueDeviceRssi());
                        LogUtil.i(TAG, "Create mDevice");
                    }
                WristBandDevice.getInstance(mContext).connect(mDevice);
                LogUtil.i(TAG, "known device connect");

            } else {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(StartScan);
                    }
                }).start();

            }
        }

    }

    // 打开查找手环
    protected void startscan() {

        isCurScan = true;
        if (mListener != null) {
            mListener.startScanDevice();
        }
        startScanAnimation();
    }

    // 查找动画
    private void startScanAnimation() {

        final int[] location = new int[2];
        search_device.getLocationOnScreen(location);
        final int width = search_device.getHeight();
        // location [0]--->x坐标,location [1]--->y坐标
        valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(Animation.INFINITE);
        valueAnimator.setRepeatMode(Animation.REVERSE);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            int radius = width / 2;
            int x0 = location[0], y0 = location[1] / 2 - 2 * width;

            // int x0=30*2,y0=30*2;
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                // 30
                // 角度：a0
                float a0 = 360 * fraction;
                // 则圆上任一点为：（x1,y1）
                // x方向200px/s ，则y方向0.5 * g * t (g = 100px / s*s)
                PointF point = new PointF();
                point.x = (float) (x0 + radius * Math.cos(a0 * Math.PI / 180));
                point.y = (float) (y0 + radius * Math.sin(a0 * Math.PI / 180));
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                search_device.setX(point.x);
                search_device.setY(point.y);

            }
        });
        // valueAnimator.
    }

    private void stopScanAnimation() {
        isCurScan = false;
        search_device.clearAnimation();
        if (valueAnimator != null) {
            valueAnimator.end();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == BluetoothUtils.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                LogUtil.i(tag, "REQUEST_ENABLE_BT RESULT_OK");
                if (StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
                    startscan();
                    startService(new Intent(mContext, BluetoothService.class));
                } else {
                    // getWristBand();
                    WristBandDevice.getInstance(mContext).connect(mDevice);
                }
            } else {
                finish();
                LogUtil.i(tag, "REQUEST_ENABLE_BT cancel");
            }
        }
    }

    public interface BluetoothActionListener {
        public void startScanDevice();

        public void startConnectDevice();

        public void onDeviceConnected();

        public void onDeviceDisConnected();

        public void onDeviceActionDataAvailabe();
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        devs.clear();
        mBluetoothUtils = new BluetoothUtils(this);
        registerReceiver();
        titlebar.setTitle("我的设备");
        titlebar.setLeftLl(backClickListener);
        isConn = SharePreferencesUtil.getBlueConn(mContext);
        search_device.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startscan();
                startService(new Intent(mContext, BluetoothService.class));
            }
        });
        unbinddevice.setOnClickListener(new OnClickListener() {
            private Dialog commonDialog;

            @Override
            public void onClick(View v) {
                // /
                LogUtil.i(tag, "unbinddevice");
                // getWristBand().disconnect();
                commonDialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定取消设备绑定吗？", "取消", "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        commonDialog.dismiss();
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        commonDialog.dismiss();
                        unBindDeviceNet();
                        // TODO 绑定服务 取消绑定

                    }
                }, Color.BLACK, Color.BLACK);

            }
        });
        // mScanner = new BluetoothLeScanner(mLeScanCallback, mBluetoothUtils);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WristBand device = mAdapter.getItem(position);
                getIfDeviceCanConnect(device);

            }
        });
        mAdapter = new BlueDeviceListAdapter(mContext, devs);
        listview.setAdapter(mAdapter);
        View mEmpty = getLayoutInflater().inflate(R.layout.empty_bluedevice, null);
        listview.setEmptyView(mEmpty);
        if (StringUtils.isEmpty(AppSharedPreferencesHelper.getMacAddress())) {
            searchbluedevices.setVisibility(View.VISIBLE);
            bindeddevice.setVisibility(View.GONE);
        } else {
            searchbluedevices.setVisibility(View.GONE);
            bindeddevice.setVisibility(View.VISIBLE);
            /**
             * 判断是否是敏狐手环 F4T
             */
            if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), "F4:06:A5")) {
                //敏狐手环
                device_code.setText(AppSharedPreferencesHelper.getMacAddress());
            } else {
                //其他
                device_code.setText(AppSharedPreferencesHelper.getBlueDeviceName());
            }
            if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(AppSharedPreferencesHelper.getMacAddress(), deiverName)) {
                // 903 设备一直显示已连接
                device_status.setText("已连接");
            } else {
                // 手环设备检测绑定状态
                if (!isConn) {
                    LogUtil.e(TAG, "isConn >>>" + isConn);
                    // TODO 断线重连
                    device_status.setText("已断开连接");

                } else {
                    device_status.setText("已连接");
                }
            }
        }

    }

    Runnable checkIfBloothClosedWhenScan = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (isCurScan) {
                if (mBluetoothUtils != null) {
                    if (!mBluetoothUtils.isBluetoothOn()) {
                        showToast("请检查蓝牙是否开启");
                    }
                }
            }
            if (mHandler != null) {
                mHandler.postDelayed(checkIfBloothClosedWhenScan, CHECK_TIME);
            }
        }

    };
    private Dialog bindDialog;
    private String gsmNum = "";// GSM序列号

    @OnClick({R.id.tv_conngsm})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_conngsm:
                gsmNum = etGsm.getText().toString();
                if (gsmNum.trim().length() == 16 && org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(gsmNum.trim(), deiverName)) {
                    bindDeviceNet(gsmNum.trim());
                    AppSharedPreferencesHelper.setBlueDeviceName(gsmNum); // 保存设备名称
                } else {
                    showToast("GSM设备号格式不正确,请检查");
                }
                break;

            default:
                break;
        }
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_minedevice;
    }
}

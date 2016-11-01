package com.etcomm.dcare.app.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.device.MineDeviceActivityNew;
import com.iwown.android_iwown_ble.bluetooth.BluetoothDataParseBiz;
import com.iwown.android_iwown_ble.bluetooth.WristBandDevice;
import com.iwown.android_iwown_ble.bluetooth.task.NewAgreementBackgroundThreadManager;
import com.iwown.android_iwown_ble.bluetooth.task.WriteOneDataTask;
import com.iwown.android_iwown_ble.model.CurrData_0x29;
import com.iwown.android_iwown_ble.model.FMdeviceInfo;
import com.iwown.android_iwown_ble.model.HeartRateDetial;
import com.iwown.android_iwown_ble.model.HeartRateParams;
import com.iwown.android_iwown_ble.model.KeyModel;
import com.iwown.android_iwown_ble.model.Power;
import com.iwown.android_iwown_ble.model.Result;
import com.iwown.android_iwown_ble.model.SportType;
import com.iwown.android_iwown_ble.model.WristBand;

import org.apache.commons.lang3.StringUtils;

public class BluetoothService extends Service  {

    final String TAG = "BluetoothService";
    // 3.1 device information 0x00
    public static final int CMD_DEVICE_MESSAGE = 0X00;
    // 3.2 battery electricity 0x01
    public static final int CMD_DEVICE_POWER = 0x01;
    // 3.25 switch to sync sport data in different time range of the day 0x28
    public static final int CMD_SEGMENT_DATA = 0x28;
    // 3.26 switch to sync data in total by day 0x29
    public static final int CMD_DIALY_CURR_DATA = 0x29;
    // 3.17 queyr sport type device support 0x1A
    public static final int CMD_DEVICE_SURPORT = 0x1a;
    // 3.34 bracelet mode control 0x40
    public static final int CMD_MANUAL_MODE_CONTROL = 0x40;
    // 3.37 heart rate parameters 0x50
    public static final int CMD_SETTING_HEARTRATE_PARAMS = 0x50;
    // 3.38 sync heart rate data in different time range 0x51
    public static final int CMD_HEARTRATE_DATA = 0x51;

    private BlueDataCallback myback; // 回调类
    byte[] data = null; // 蓝牙同步数据参数

    // private Context ctx;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        logI("onBind");
        // if (myCallback != null) {
        // sync0x29_data();
        // }
        if (devi == null) {
            devi = getWristBand();
        }
        devi.connect();
        return myBinder;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        logI("onCreate");
        devi = getWristBand();
        registerReceiver();
        // ctx = this;
        super.onCreate();
    }

    private WristBandDevice devi = null; // 蓝牙驱动对象

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // TODO Auto-generated method stub
        logI("onStartCommand");
        myback = new BlueDataCallback(DcareApplication.mCon);
        if (devi == null) {
            devi = getWristBand();
        }
        devi.startLeScan(); // 启动查找服务
        set0x29_data(""); // 初始化空字符
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        logI("onDestroy");
        devi.stopLeScan();
        unregisterReceiver();
        super.onDestroy();
    }

    private void unregisterReceiver() {
        // TODO Auto-generated method stub
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        logI("onUnbind");
        return super.onUnbind(intent);
    }

    void logI(String msg) {
        LogUtil.i(TAG, msg);
    }

    /**
     * 获取手环连接 对象 get WristBandDevice
     *
     * @return
     */
    private WristBandDevice getWristBand() {
        WristBandDevice device = WristBandDevice.getInstance(DcareApplication.mCon);
        device.setCallbackHandler(myback);
        return device;
    }

    /**
     * 同步日冻结蓝牙数据
     */
    void sync0x29_data() {

        data = devi.setWristBand_3BVersion_DialydataCurr(1);
        WriteOneDataTask task = new WriteOneDataTask(DcareApplication.mCon, data);
        NewAgreementBackgroundThreadManager.getInstance().addTask(task);
        logI("sync0x29_data>>" + data.length);
    }

    class BlueDataCallback extends BluetoothDataParseBiz {

        public BlueDataCallback(Context context) {
            super(context);
        }

        @Override
        public void connectStatue(boolean isConnect) {
            super.connectStatue(isConnect); // 连接状态
            // data.add(isConnect+""); //listview 适配器的data
            // Log.e("MainAtv connstate is ", isConnect+"");
            // scan.setText("conn is +"+isConnect);
            // mAdapter.notifyDataSetChanged();
            logI("blue conn statue >>" + devi.isConnected());
            if (isConnect) {
                logI("连接  已连接 " + isConnect);
                SharePreferencesUtil.saveBlueConn(DcareApplication.mCon, isConnect);
                sync0x29_data();
                sendBroadcast(new Intent("COM.ACTION.UPMINEUIONDEVICE")); // 更新绑定界面

            } else {
                logI("连接状态断开 " + isConnect);
                SharePreferencesUtil.saveBlueConn(DcareApplication.mCon, isConnect);
                sendBroadcast(new Intent("COM.ACTION.UPMINEUIONDISDEVICE")); // 更新绑定界面

            }
        }

        @Override
        public void onWristBandFindNewAgreement(WristBand dev) {
            super.onWristBandFindNewAgreement(dev);
            String name = dev.getName();
            String contains = dev.getAddress();
            /**
             * 判断 包含已知信息为True 包含已知信息为false
             */
            if (!MineDeviceActivityNew.devs.contains(dev) && StringUtils.startsWithIgnoreCase(dev.getName(), "Braceli5")) {
                LogUtil.e(TAG, TAG + dev.getName());
                // Braceli5
                // if (StringUtils.startsWithIgnoreCase(dev.getName(),
                // "Braceli5")) {
                LogUtil.e(TAG, TAG + ">>>" + dev.getName());
                /**
                 * 静态调用设备列表页面的列表数据并填充数据
                 */
                MineDeviceActivityNew.devs.add(dev);
                /**
                 * 通知设备列表页面ListView 刷新
                 */
                sendBroadcast(new Intent("COM.ACTION.UPMINEUI"));
                // }
                // mAdapter.notifyDataSetChanged();
            }else if(!MineDeviceActivityNew.devs.contains(dev) && StringUtils.startsWithIgnoreCase(dev.getAddress(), "F4:06:A5")) {
                /**
                 * 静态调用设备列表页面的列表数据并填充数据
                 */
                MineDeviceActivityNew.devs.add(dev);
                /**
                 * 通知设备列表页面ListView 刷新
                 */
                sendBroadcast(new Intent("COM.ACTION.UPMINEUI"));
            }

        }

        @Override
        public void onCharacteristicWriteData() {
            super.onCharacteristicWriteData();
            // data.add("when delegate invoked the data can sync");
            // mAdapter.notifyDataSetChanged();
        }

        @Override
        public void getData(int type, Result result) {
            super.getData(type, result);
            logI("getData result : " + result);
            // Log.e("MainAtv", "result:"+result.toJson());
            switch (type) {
                case CMD_DEVICE_MESSAGE:
                    FMdeviceInfo info = (FMdeviceInfo) result;
                    // data.add(info.toJson());
                    // mAdapter.notifyDataSetChanged();
                    break;
                case CMD_DEVICE_POWER:
                    Power power = (Power) result;
                    // data.add(power.toJson());
                    // Log.e("MainAtv", power.toJson());
                    // mAdapter.notifyDataSetChanged();
                    break;
                case CMD_DEVICE_SURPORT:
                    SportType sportType = (SportType) result;
                    // data.add(sportType.toJson());
                    // mAdapter.notifyDataSetChanged();
                    break;
                case CMD_DIALY_CURR_DATA:
                    CurrData_0x29 curr = (CurrData_0x29) result;
                    logI("0x29>>>" + curr.toJson());
                    // data.add(curr.toJson());
                    // DeviceDailyData dailySport =
                    // CurrData_0x29_Convert_DailyData(curr.toJson());
                    set0x29_data(curr.toJson());
//                    Intent data_0x29 = new Intent();
                    // data_0x29.putExtra(Preferences.ActionBlueStep,
                    // curr.toJson());
                    sendBroadcast(new Intent(Preferences.HOME_STEP_UPDATE));
                    // mAdapter.notifyDataSetChanged();
                    break;
                case CMD_HEARTRATE_DATA:
                    HeartRateDetial detial = (HeartRateDetial) result;
                    // data.add(detial.toJson());
                    // mAdapter.notifyDataSetChanged();
                    break;
                case CMD_MANUAL_MODE_CONTROL:
                    KeyModel keymode = (KeyModel) result;
                    // data.add(keymode.toJson());
                    // mAdapter.notifyDataSetChanged();
                    break;
                case CMD_SETTING_HEARTRATE_PARAMS:
                    HeartRateParams params = (HeartRateParams) result;
                    // data.add(params.toJson());
                    // mAdapter.notifyDataSetChanged();
                    break;
                case CMD_SEGMENT_DATA:
                    // Log.e("MainAtv",
                    // "result TB_v3_sport_data >>"+result.toJson()); //分段数据
                    // if(result instanceof TB_v3_sleep_data){
                    // // data.add(result.toJson());
                    // // mAdapter.notifyDataSetChanged();
                    // }else if(result instanceof TB_v3_sport_data){
                    // // data.add(result.toJson());
                    // }
                    break;

                default:
                    break;
            }
        }

    }

    boolean isSave = true;

    /**
     * 保存当日冻结回调数据数据
     *
     * @param str
     */
    void set0x29_data(String str) {
        logI("set0x29_data>>>" + str);
        SharePreferencesUtil.saveBlueData(getBaseContext(), str);
    }

    // public DeviceDailyData CurrData_0x29_Convert_DailyData(String json) {
    // // TODO Auto-generated method stub
    // DeviceDailyData dailyData = new DeviceDailyData();
    // // "sportSteps": "20",
    // // "sportDistances": "13.0",
    // // "sportCalories": "2",
    // // "month": 255,
    // // "uid": 0,
    // // "day": 255,
    // // "sportType": 1,
    // // "count": 0,
    // // "startTime": 0,
    // // "activityTime": 0,
    // // "endTime": 0,
    // // "year": 255
    // JSONObject jobject;
    // try {
    // jobject = JSON.parseObject(json);
    // } catch (Exception e) {
    // return null;
    // }
    // dailyData.setYear(jobject.getIntValue("year"));
    // dailyData.setMonth(jobject.getIntValue("month"));
    // dailyData.setDay(jobject.getIntValue("day"));
    // dailyData.setSteps(jobject.getIntValue("sportSteps"));
    // dailyData.setDistance(jobject.getFloatValue("sportDistances"));
    // dailyData.setCalorie(jobject.getFloatValue("sportCalories"));
    // dailyData.setBcc(jobject.getIntValue("uid"));
    // dailyData.set_converted(jobject.getIntValue("sportType"));
    // dailyData.set_uploaded(jobject.getIntValue("count"));
    // return dailyData;
    // }

    public class MyBinder extends Binder {

        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    private MyBinder myBinder = new MyBinder();

    // 处理内容
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO 接收调用数据后 重新调用连接方法
            // 1.首页调用 2.关联页调用
            if (intent.getAction().equals(Preferences.ActionBlueSysn)) {
                // 再次调用数据服务方法
                sync0x29_data();
                logI("ActionBlueSysn blue>>>");

            } else if (intent.getAction().equals(Preferences.ActionBlueDisConn)) {
                devi.disconnect(); // 关闭蓝牙连接
                logI("disconn blue>>>");
            }
        }
    };

    // 注册广播
    void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        // 同步当日手环步数广播 蓝牙计步数据
        filter.addAction(Preferences.ActionBlueSysn);
        registerReceiver(mReceiver, filter);
    }

}

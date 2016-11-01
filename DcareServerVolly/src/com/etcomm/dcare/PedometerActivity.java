package com.etcomm.dcare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.device.MineDeviceActivityNew;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.service.StepDataUploadService;
import com.etcomm.dcare.widget.DialogFactory;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.etcomm.dcare.widget.SwitchButton;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import me.chunyu.pedometerservice.PedometerCounterService;

public class PedometerActivity extends BaseActivity {
    boolean isCheck = false; // 软件计步状态
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.pedometer_switch)
    SwitchButton pedometer_switch;
    @Bind(R.id.screenlongon_switch)
    SwitchButton screenlongon_switch;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.pedometer_tv1)
    TextView pedometer_tv1;
    @Bind(R.id.pedometer_device_rl)
    RelativeLayout pedometer_device_rl;
    @Bind(R.id.setting_submit_rl)
    RelativeLayout setting_submit_rl;
    @Bind(R.id.pedometer_plus)
    ImageView pedometer_plus;
    @Bind(R.id.pedometer_sensitivity)
    TextView pedometer_sensitivity;
    @Bind(R.id.pedometer_minus)
    ImageView pedometer_minus;
    @Bind(R.id.pedometer_device_imageView1)
    ImageView pedometer_device_imageView1;
    @Bind(R.id.wrist_device_control_tv)
    TextView wrist_device_control_tv;
    @Bind(R.id.app_sensitivity_rl)
    RelativeLayout app_sensitivityrl;
    @Bind(R.id.device_imageView1)
    ImageView device_imageView1;
    @Bind(R.id.app_sensitivity_tv)
    TextView app_sensitivitytv;
    @Bind(R.id.setting_screenon_rl)
    RelativeLayout setting_screenon_rl;
    @Bind(R.id.setting_screenon_imageView1)
    ImageView setting_screenon_imageView1;
    @Bind(R.id.setting_pedometer_imageView1)
    ImageView setting_pedometer_imageView1;
    private Dialog coDialog;

    @Override
    public void onResume() {
        super.onResume();
        if (AppSharedPreferencesHelper.getMacAddress().length() < 5) {
            pedometer_switch.setChecked(true);
            AppSettingPreferencesHelper.setIfSoftPedometerOn(true);
            // showToast(""+AppSettingPreferencesHelper.isSoftPedometerOn());
        } else {
            pedometer_switch.setChecked(false);
            screenlongon_switch.setChecked(false);
            AppSettingPreferencesHelper.setIfSoftPedometerOn(false);
        }
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    @OnClick({R.id.pedometer_plus, R.id.pedometer_minus, R.id.setting_submit_rl, R.id.pedometer_device_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pedometer_plus:

                AppSettingPreferencesHelper.setSoftPedometerSensitivity(Integer.parseInt(pedometer_sensitivity.getText().toString()));
                pedometer_sensitivity.setText((AppSettingPreferencesHelper.getSoftPedometerSensitivity() + 1) + "");
                AppSettingPreferencesHelper.setSoftPedometerSensitivity(Integer.parseInt(pedometer_sensitivity.getText().toString()));
                if (Integer.parseInt(pedometer_sensitivity.getText().toString()) < 9 && Integer.parseInt(pedometer_sensitivity.getText().toString()) > 1) {
                    pedometer_plus.setEnabled(true);
                    pedometer_minus.setEnabled(true);
                } else if (Integer.parseInt(pedometer_sensitivity.getText().toString()) >= 9) {
                    pedometer_plus.setEnabled(false);
                    pedometer_minus.setEnabled(true);
                } else if (Integer.parseInt(pedometer_sensitivity.getText().toString()) <= 1) {
                    pedometer_plus.setEnabled(true);
                    pedometer_minus.setEnabled(false);
                }
                Intent intent = new Intent("changesensitivity");
                intent.putExtra("sensity", AppSettingPreferencesHelper.getSoftPedometerSensitivity());
                sendBroadcast(intent);
                break;
            case R.id.pedometer_minus:

                AppSettingPreferencesHelper.setSoftPedometerSensitivity(Integer.parseInt(pedometer_sensitivity.getText().toString()) - 2);
                pedometer_sensitivity.setText((AppSettingPreferencesHelper.getSoftPedometerSensitivity() + 1) + "");
                AppSettingPreferencesHelper.setSoftPedometerSensitivity(Integer.parseInt(pedometer_sensitivity.getText().toString()));
                Log.e(tag, "发送前灵敏度" + AppSettingPreferencesHelper.getSoftPedometerSensitivity());

                if (Integer.parseInt(pedometer_sensitivity.getText().toString()) < 9 && Integer.parseInt(pedometer_sensitivity.getText().toString()) > 1) {
                    pedometer_plus.setEnabled(true);
                    pedometer_minus.setEnabled(true);
                } else if (Integer.parseInt(pedometer_sensitivity.getText().toString()) >= 9) {
                    pedometer_plus.setEnabled(false);
                    pedometer_minus.setEnabled(true);
                } else if (Integer.parseInt(pedometer_sensitivity.getText().toString()) <= 1) {
                    pedometer_plus.setEnabled(true);
                    pedometer_minus.setEnabled(false);
                }
                Intent intent1 = new Intent("changesensitivity");
                intent1.putExtra("sensity", AppSettingPreferencesHelper.getSoftPedometerSensitivity());
                sendBroadcast(intent1);
                break;
            case R.id.setting_submit_rl:

                Intent service = new Intent(this, StepDataUploadService.class);
                service.putExtra("usersubmit", true);
                Toast.makeText(mContext, "正在上传", Toast.LENGTH_SHORT).show();
                startService(service);
                break;
            case R.id.pedometer_device_rl: // 计步设备管理
                if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    startAtvTask(MineDeviceActivityNew.class);
                } else {
                    showToast("您的安卓系统版本过低，不支持此功能！");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        titlebar.setTitle("计步设置");
        titlebar.setLeftLl(backClickListener);
        pedometer_sensitivity.setText((AppSettingPreferencesHelper.getSoftPedometerSensitivity()) + "");

        if (Integer.parseInt(pedometer_sensitivity.getText().toString()) < 9 && Integer.parseInt(pedometer_sensitivity.getText().toString()) > 1) {
            pedometer_plus.setEnabled(true);
            pedometer_minus.setEnabled(true);
        } else if (Integer.parseInt(pedometer_sensitivity.getText().toString()) >= 9) {
            pedometer_plus.setEnabled(false);
            pedometer_minus.setEnabled(true);
        } else if (Integer.parseInt(pedometer_sensitivity.getText().toString()) <= 1) {
            pedometer_plus.setEnabled(true);
            pedometer_minus.setEnabled(false);
        }

        if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
            // 软件计步方式
            // screenlongon_switch.setChecked(true);
            screenlongon_switch.setChecked(AppSettingPreferencesHelper.isScreenLongOn());


            if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
                PedometerCounterService.initAppService(mContext);
            } else {
                PedometerCounterService.releaseAppService();
            }
        } else {

            // 低版本的硬件计步
            if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                coDialog = showTipsDialog(mContext, "提示", "我知道了", "由于您系统版本过低，不支持手环绑定，APP开启软件计步！");
                // 屏蔽返回键按钮
                coDialog.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        } else {
                            return false; // 默认返回 false
                        }
                    }
                });
                pedometer_switch.setChecked(true);
                isCheck = true;
                AppSettingPreferencesHelper.setIfSoftPedometerOn(true);
            } else {
                // 蓝牙计步方式
                PedometerCounterService.releaseAppService();
                screenlongon_switch.setChecked(false);
                AppSettingPreferencesHelper.setIsScreenLongOn(false);
            }

        }

        if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
            pedometer_switch.setChecked(true);
            screenlongon_switch.setChecked(AppSettingPreferencesHelper.isScreenLongOn());
        } else {
            pedometer_switch.setChecked(false);
        }
        screenlongon_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (AppSharedPreferencesHelper.getMacAddress().length() >= 5) {
                    screenlongon_switch.setChecked(false);
                    showToast("硬件计步时不可打开屏幕常亮");
                } else {
                    AppSettingPreferencesHelper.setIsScreenLongOn(isChecked);
                    screenlongon_switch.setChecked(isChecked);
                }
            }

        });
        pedometer_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    AppSettingPreferencesHelper.setIfSoftPedometerOn(isChecked);
                    isCheck = isChecked;
                    if (isChecked) {
                        if (AppSharedPreferencesHelper.getMacAddress().length() >= 5) {

                            coDialog = DialogFactory.getDialogFactory().showSettingDialog(mContext, "无法启用软件计步", "请先取消绑定硬件", "取消", "取消绑定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    coDialog.dismiss();
                                    if (!coDialog.isShowing()) {
                                        pedometer_switch.setChecked(false);
                                    }
                                }
                            }, new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    coDialog.dismiss();
                                    startActivity(new Intent(mContext, MineDeviceActivityNew.class));
                                }
                            }, Color.BLACK, Color.BLACK);

                        } else {
                            Intent broadCast = new Intent(Preferences.ACTION_ENALBE_PEDOMETER);
                            broadCast.putExtra("ENABLEPEDOMETER", isChecked);
                            sendBroadcast(broadCast);

                            if (AppSettingPreferencesHelper.isSoftPedometerOn()) {
                                PedometerCounterService.initAppService(mContext);

                            } else {
                                PedometerCounterService.releaseAppService();
                            }

                        }

                    } else {
                        if (AppSharedPreferencesHelper.getMacAddress().length() < 5) {
                            coDialog = DialogFactory.getDialogFactory().showSettingDialog(mContext, "无法关闭", "请先连接手环再关闭", "取消", "搜索手环", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    coDialog.dismiss();
                                    if (!coDialog.isShowing()) {
                                        pedometer_switch.setChecked(true);
                                    }

                                }
                            }, new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    coDialog.dismiss();
                                    mHandler.sendMessage(mHandler.obtainMessage(0));

                                }
                            }, Color.BLACK, Color.BLACK);

                        }

                        Intent unablePodometer = new Intent(Preferences.ACTION_UNENALBE_PEDOMETER);
                        unablePodometer.putExtra("ENABLEPEDOMETER", isChecked);
                        sendBroadcast(unablePodometer);
                    }
                } else {
                    pedometer_switch.setChecked(true);
                    pedometer_switch.setEnabled(false);
                    showToast("您的安卓系统版本过低，只能使用软件计步！");

                }

            }
        });
    }


    // 解决方案
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0:
                    // showToast("000000000");
                    // pedometer_switch.setChecked(isCheck);
                    startAtvTask(MineDeviceActivityNew.class);
                    if (coDialog != null) {
                        coDialog.dismiss();
                    }

                    break;

                default:
                    break;
            }
            super.handleMessage(msg);

        }

    };

    @Override
    protected int setLayoutId() {
        return R.layout.pedometer_activity;
    }

    protected void unBindDeviceNet() {// 后台同步解绑操作
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
                            showToast("解绑成功");
                            PedometerCounterService.initAppService(mContext);
                            AppSettingPreferencesHelper.setIfSoftPedometerOn(true);
                            AppSharedPreferencesHelper.setMacAddress("");
                            AppSharedPreferencesHelper.setBlueDeviceName("");
                            pedometer_switch.setChecked(true);
                            coDialog.dismiss();
                        } else {
                            showToast("解绑失败");
                        }
                    } else {// code不为0 发生异常
                        PedometerCounterService.initAppService(mContext);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }

    public Dialog showTipsDialog(Context mContext, String title, String button, String content) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_tips, null);
        final Dialog customDialog = new Dialog(mContext, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView tips_title = (TextView) view.findViewById(R.id.tips_title);
        tips_title.setText(title);
        TextView tips_content = (TextView) view.findViewById(R.id.tips_content);
        tips_content.setText(content);
        TextView tips_button = (TextView) view.findViewById(R.id.tips_button);
        tips_button.setText(button);
        tips_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindDeviceNet();
            }
        });
        customDialog.show();
        return customDialog;
    }

}

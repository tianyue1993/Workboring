package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.login.ChangePasswordActivity;
import com.etcomm.dcare.app.activity.login.LoginActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
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

/**
 * 设置
 *
 * @author iexpressbox
 */
public class SettingActivity extends BaseActivity implements OnCheckedChangeListener {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.exit_btn)
    Button exit_btn;
    @Bind(R.id.setting_personal_rl)
    RelativeLayout setting_personal_rl;
    @Bind(R.id.setting_goal_rl)
    RelativeLayout setting_goal_rl;
    @Bind(R.id.setting_submit_rl)
    RelativeLayout setting_submit_rl;
    @Bind(R.id.setting_screenon_rl)
    RelativeLayout setting_screenon_rl;
    @Bind(R.id.sb_ios)
    SwitchButton sb_ios;
    @Bind(R.id.setting_changepassword_rl)
    RelativeLayout setting_changepassword_rl;
    @Bind(R.id.setting_relateworknumber_rl)
    RelativeLayout setting_relateworknumber_rl;
    @Bind(R.id.setting_msgsetting_rl)
    RelativeLayout setting_msgsetting_rl;
    @Bind(R.id.setting_aboutus_rl)
    RelativeLayout setting_aboutus_rl;
    private static final String dateformat = "yyyyMMdd";
    private Dialog delete;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equals("isexit")) {
                    exitFromNet();
                }
            }

        }
    };

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

    @OnClick({R.id.setting_personal_rl, R.id.setting_goal_rl, R.id.setting_changepassword_rl, R.id.setting_relateworknumber_rl, R.id.setting_submit_rl, R.id.setting_msgsetting_rl, R.id.setting_aboutus_rl, R.id.exit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_personal_rl:
                startAtvTask(SettingPersonalDataActivity.class);
                break;
            case R.id.setting_goal_rl:
                startAtvTask(SettingSportGoalActivity.class);
                break;
            case R.id.setting_changepassword_rl:
                startAtvTask(ChangePasswordActivity.class);
                break;
            case R.id.setting_relateworknumber_rl:
                startAtvTask(RelateWorkNumberActivity.class);
                break;
            case R.id.setting_submit_rl:
                Intent service = new Intent(this, StepDataUploadService.class);
                service.putExtra("usersubmit", true);
                Toast.makeText(mContext, "正在上传", Toast.LENGTH_SHORT).show();
                startService(service);
                break;
            case R.id.setting_msgsetting_rl:
                startAtvTask(MsgSettingActivity.class);
                break;
            case R.id.setting_aboutus_rl:
                startAtvTask(AboutUsActivity.class);

                break;
            case R.id.exit_btn:
                delete = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要退出账号吗？", "取消", "确定", new OnClickListener() {
                    @SuppressWarnings("unused")
                    @Override
                    public void onClick(View v) {
                        delete.dismiss();
                    }
                }, new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (delete != null && delete.isShowing()) {
                            delete.dismiss();
//                            Intent service = new Intent(mContext, StepDataUploadService.class);
//                            service.putExtra("isexit", true);
//                            startService(service);
                            exitFromNet();

                        }
                    }
                }, mContext.getResources().getColor(R.color.black), mContext.getResources().getColor(R.color.black));
                break;
            default:
                break;
        }
    }

    void exitFromNet() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.Exit, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode);// +
                // " response "+
                // response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        exit();
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });

    }

    void exit() {

        // 退出登陆清除用户相关数据
        SharedPreferences sp = getSharedPreferences(Preferences.UserInfo, Context.MODE_PRIVATE);
        SharedPreferences timeStep = getSharedPreferences(Preferences.SAVE_HOC_STEP, Context.MODE_PRIVATE);
        SharedPreferences stepOfCount = getSharedPreferences(SharePreferencesUtil.Prefe_step_data, Context.MODE_PRIVATE);
        SharePreferencesUtil.saveReData(mContext, true);
        stepOfCount.edit().clear();
        timeStep.edit().clear();
        sp.edit().clear();
        // 清除用户相关计步数据
        AppSharedPreferencesHelper.clear();
        AppSettingPreferencesHelper.clear();
        SharePreferencesUtil.clearEtc(mContext, "etc");
        SharePreferencesUtil.deleteUserData(mContext);
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        sendBroadcast(new Intent(Preferences.ACTION_USER_EXIT));
        DcareApplication.getInstance().finishActivity(this);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        AppSettingPreferencesHelper.setIsScreenLongOn(isChecked);
        if (isChecked) {
            Intent intent = new Intent(Preferences.ACTION_SCREENLONGON);
            sendBroadcast(intent);
        }
    }

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("isexit");
        registerReceiver(receiver, filter);
        titlebar.setTitle("基本设置");
        titlebar.setLeftLl(backClickListener);
        sb_ios.setOnCheckedChangeListener(this);
        if (AppSettingPreferencesHelper.isScreenLongOn()) {
            sb_ios.setChecked(true);
        } else {
            sb_ios.setChecked(false);
        }

//        if (SharePreferencesUtil.getIslevel(mContext)) {
//            setting_relateworknumber_rl.setVisibility(View.GONE);
//        } else {
//            setting_relateworknumber_rl.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }
}

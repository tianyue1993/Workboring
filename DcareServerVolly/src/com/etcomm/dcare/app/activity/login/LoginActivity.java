package com.etcomm.dcare.app.activity.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.etcomm.dcare.MainActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.setting.EnterUserNameChooseAvatarActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.app.utils.SystemInfoUtils;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.Login;
import com.etcomm.dcare.ormlite.bean.UserDao;
import com.etcomm.dcare.service.StepDataUploadService;
import com.etcomm.dcare.widget.CircleImageView;
import com.etcomm.dcare.widget.ExEditText;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_username)
    ExEditText et_username;
    @Bind(R.id.iv_photo_default)
    CircleImageView iv_photo_default;
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.et_passWord)
    ExEditText et_passWord;
    @Bind(R.id.bt_login_btn)
    Button bt_login_btn;
    @Bind(R.id.tv_forget_pwd)
    TextView tv_forget_pwd;
    @Bind(R.id.iv_del_username)
    ImageView iv_del_username;
    @Bind(R.id.iv_del_password)
    ImageView iv_del_password;
    private SharedPreferences defaultsp;
    // protected LoadingDialog dialog;
    private UserDao userDao;
    protected String device_id = "";
    private ImageLoader imageloader = ImageLoader.getInstance();
    // private String lastUsername;
    String lastUID = ""; // UserId

    @Override
    protected void onResume() {
        super.onResume();

        /**
         *   清除用户相关数据
         * 防止因异地登录强制退出之后进入登录页面，未清除上个账号本地数据
         * */
        SharedPreferences sp = getSharedPreferences(Preferences.UserInfo, Context.MODE_PRIVATE);
        SharedPreferences timeStep = getSharedPreferences(Preferences.SAVE_HOC_STEP, Context.MODE_PRIVATE);
        SharedPreferences stepOfCount = getSharedPreferences(SharePreferencesUtil.Prefe_step_data, Context.MODE_PRIVATE);
        SharePreferencesUtil.saveReData(mContext, true);
        stepOfCount.edit().clear();
        timeStep.edit().clear();
        sp.edit().clear();
        AppSharedPreferencesHelper.clear();
        AppSettingPreferencesHelper.clear();
        SharePreferencesUtil.clearEtc(mContext, "etc");
        SharePreferencesUtil.deleteUserData(mContext);
        if (!TextUtils.isEmpty(SharePreferencesUtil.getUid(mContext))) {
            lastUID = defaultsp.getString(Preferences.USER_UID, "");
        }
        LogUtil.d(TAG, "onResume " + lastUID);

    }

    @SuppressLint("NewApi")
    private void initview() {
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // TODO Auto-generated method stub
                if (source.equals(" "))
                    return "";
                else
                    return null;
            }
        };
        et_passWord.setFilters(new InputFilter[]{filter});

        String username = defaultsp.getString(Preferences.USER_NAME, "");
        if (!StringUtils.isEmpty(username)) {
            et_username.setText(username);
        }
        if (defaultsp.getString("islevel", "").equals("1")) {
            et_username.setHint("手机号/邮箱");
        } else {
            et_username.setHint("手机号/邮箱/工号");
        }
        String lastavator = defaultsp.getString(Preferences.Avator, "");
        if (!StringUtils.isEmpty(lastavator)) {
            Log.i(tag, "default image avator: " + lastavator);
            imageloader.displayImage(lastavator, iv_photo_default);
        } else {
            Log.e(tag, "lastavator null ");
            lastavator = defaultsp.getString(Preferences.Avator, "");
            if (!StringUtils.isEmpty(lastavator)) {
                Log.i(tag, "default image avator: " + lastavator);
                imageloader.displayImage(lastavator, iv_photo_default);
            } else {
                Log.e(tag, "lastavator null ");
            }
        }
    }

    @OnTextChanged(value = R.id.et_username, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged_username(Editable s) {
        if (s.length() > 0) {
            iv_del_username.setVisibility(View.VISIBLE);
            if (et_username.getText().toString().length() >= 1 && et_passWord.getText().toString().length() >= 1) {
                bt_login_btn.setEnabled(true);
            } else {
                bt_login_btn.setEnabled(false);
            }
        } else {
            bt_login_btn.setEnabled(false);
            iv_del_username.setVisibility(View.INVISIBLE);
        }
    }

    @OnTextChanged(value = R.id.et_passWord, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged_password(Editable s) {

        if (s.length() > 0) {

            iv_del_password.setVisibility(View.VISIBLE);
            if (et_username.getText().toString().length() >= 1 && et_passWord.getText().toString().length() >= 1) {
                bt_login_btn.setEnabled(true);
            } else {
                bt_login_btn.setEnabled(false);
            }
        } else {
            bt_login_btn.setEnabled(false);
            iv_del_password.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        device_id = SystemInfoUtils.readDeviceId(mContext);
        Log.i(tag, "client_id  : " + client_id);
        client_id = PushManager.getInstance().getClientid(DcareApplication.mCon);
        if (TextUtils.isEmpty(client_id)) {
            client_id = PushManager.getInstance().getClientid(this);
            Log.i(tag, "client_id2  : " + client_id);
        }

        defaultsp = getSharedPreferences(Preferences.CommonLoginInfo, MODE_PRIVATE);
        userDao = new UserDao(mContext);
        initview();
        bt_login_btn.setEnabled(false);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_register, R.id.iv_del_username, R.id.iv_del_password, R.id.tv_forget_pwd, R.id.bt_login_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startAtvTask(EnterpriseActivationActivity.class, Preferences.DEVICE_ID, device_id);
                break;
            case R.id.iv_del_username:
                et_username.setText("");

                break;
            case R.id.iv_del_password:
                et_passWord.setText("");

                break;
            case R.id.tv_forget_pwd:
                startAtvTask(ForgotPasswordActivity.class, Preferences.DEVICE_ID, device_id);
                break;
            case R.id.bt_login_btn:
                cancelmDialog();
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", et_username.getText().toString());
                params.put("password", et_passWord.getText().toString());
                params.put("device_id", device_id);
                if (TextUtils.isEmpty(client_id)) {
                    PushManager.getInstance().initialize(getApplicationContext());
                    client_id = PushManager.getInstance().getClientid(LoginActivity.this);
                    LogUtil.e(TAG, TAG + "btnLogin+ client_id：" + client_id);
                    if (TextUtils.isEmpty(client_id)) {
                        showToast(R.string.network_conn_error);
                        return;
                    }
                    return;
                }
                params.put("client_id", client_id);
                showProgress(DIALOG_DEFAULT, true);
                Log.i(tag, "params: " + params.toString() + " Username: " + et_username.getText().toString() + " Password: " + et_passWord.getText().toString());
                DcareRestClient.volleyPost(Constants.Login, params, new FastJsonHttpResponseHandler() {
                    @Override
                    public void onCancel() {
                        Log.w(tag, "post cancel" + this.getRequestURI());
                        // cancelDialog();
                        cancelmDialog();
                        super.onCancel();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, com.alibaba.fastjson.JSONObject errorResponse) {
                        Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                        cancelmDialog();
                        showToast(R.string.network_error);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, com.alibaba.fastjson.JSONObject response) {
                        cancelmDialog();
                        LogUtil.e(TAG, "response:>>" + response);
                        try {

                            int code = response.getInteger("code");
                            String message = response.getString("message");
                            Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                            if (code == -1) {
                                showToast(R.string.network_conn_error);
                                return;
                            }
                            if (code == 0) {
                                Intent service = new Intent(mContext, StepDataUploadService.class);
                                service.putExtra("isexit", true);
                                LogUtil.e(tag, "AppSharedPreferencesHelper>>>" + AppSharedPreferencesHelper.getUserId() + ">>>SharePreferencesUtils" + SharePreferencesUtil.getToken(mContext));
                                defaultsp.edit().putString(Preferences.USER_NAME, et_username.getText().toString()).commit();
                                Login login = JSON.parseObject(response.getJSONObject("content").toString(), Login.class);
                                LogUtil.d(tag, ">000>>lastUID==" + lastUID + ">000>>user_id==" + login.getUser_id());
                                if (!TextUtils.isEmpty(lastUID.trim()) && !lastUID.trim().equals(login.getUser_id())) {
                                    LogUtil.d(tag, "<<<<<<<<<<clear<<<<<<<<");
                                    //切换用户

                                } else {
                                }
                                if (login.getIslevel().equals("1")) {
                                    SharePreferencesUtil.saveIslevel(mContext, true);
                                    defaultsp.edit().putString("islevel", "1").commit();
                                }
                                MobclickAgent.onProfileSignIn(login.getId());
                                // -------utils start
                                SharePreferencesUtil.saveUserName(mContext, et_username.getText().toString().trim());
                                SharePreferencesUtil.saveAvatar(mContext, login.getAvatar());
                                SharePreferencesUtil.saveUid(mContext, login.getUser_id());
                                SharePreferencesUtil.savePhone(mContext, login.getMobile());
                                SharePreferencesUtil.saveEmail(mContext, login.getEmail());
                                // ----end
                                defaultsp.edit().putString(Preferences.USER_UID, login.getUser_id()).commit();
                                defaultsp.edit().putString(Preferences.Avator, login.getAvatar()).commit();
                                defaultsp.edit().putString(Preferences.USER_NAME, et_username.getText().toString()).commit();
                                defaultsp.edit().putString(Preferences.UserPassword, et_passWord.getText().toString()).commit();
                                defaultsp.edit().putString(Preferences.NICK_NAME, login.getNick_name()).commit();
                                Log.e(tag, "Avator: " + login.getAvatar());
                                defaultsp.edit().putString(Preferences.Avator, login.getAvatar()).commit();
                                SharePreferencesUtil.saveImage(mContext, login.getCustomer_image());
                                Log.i(tag, "customer_image== " + login.getCustomer_image());
                                Log.i(tag, "login.getUser_id(): " + login.getUser_id());
                                AppSharedPreferencesHelper.setUserId(login.getUser_id());
                                AppSharedPreferencesHelper.setJob_number(login.getJob_number());
                                AppSharedPreferencesHelper.setReal_name(login.getReal_name());
                                LogUtil.d(tag, "before>>>login.getAccess_token()" + login.getAccess_token());
                                LogUtil.d(tag, "before>>>getAccessToken" + SharePreferencesUtil.getToken(mContext));
                                AppSharedPreferencesHelper.setAccessToken(login.getAccess_token());
                                SharePreferencesUtil.saveToken(mContext, login.getAccess_token());
                                SharePreferencesUtil.saveJobNumber(mContext, login.job_number); // 保存工号
                                LogUtil.e(TAG, "job_number:+" + login.job_number);
                                LogUtil.d(tag, "after>>>login.getAccess_token()" + login.getAccess_token());
                                LogUtil.d(tag, "after>>>getAccessToken" + SharePreferencesUtil.getToken(mContext));
                                LogUtil.d(tag, "after>>>SharePreferencesUtil" + SharePreferencesUtil.getToken(DcareApplication.mCon));
                                AppSharedPreferencesHelper.setBirth_year(login.getBirth_year());
                                AppSharedPreferencesHelper.setAvatar(login.getAvatar());
                                AppSharedPreferencesHelper.setHeight(login.getHeight());
                                AppSharedPreferencesHelper.setWeight(login.getWeight());
                                AppSharedPreferencesHelper.setStructure(login.getStructure());
                                AppSharedPreferencesHelper.setGender(login.getGender());
                                AppSharedPreferencesHelper.setScore(login.getScore());
                                AppSharedPreferencesHelper.setTotalScore(login.getTotal_score());
                                AppSharedPreferencesHelper.setNick_name(login.getNick_name());
                                AppSharedPreferencesHelper.setPedometer_target(login.getPedometer_target());
                                AppSharedPreferencesHelper.setPedometer_distance(login.getPedometer_distance());
                                AppSharedPreferencesHelper.setPedometer_time(login.getPedometer_time());
                                AppSharedPreferencesHelper.setPedometer_consume(login.getPedometer_consume());
                                if (login.getIs_comment().equals("1")) {
                                    AppSettingPreferencesHelper.setIsPushMsg_Comment(true);
                                } else {
                                    AppSettingPreferencesHelper.setIsPushMsg_Comment(false);
                                }
                                if (login.getIs_like().equals("1")) {
                                    AppSettingPreferencesHelper.setIsPushMsg_Like(true);
                                } else {
                                    AppSettingPreferencesHelper.setIsPushMsg_Like(false);
                                }

                                defaultsp.edit().putString(Preferences.USER_NAME, et_username.getText().toString()).commit();
                                defaultsp.edit().putString(Preferences.UserPassword, et_passWord.getText().toString()).commit();
                                defaultsp.edit().putString(Preferences.Avator, login.getAvatar()).commit();

                                showToast(R.string.login_suc);
                                Log.i(tag, "Login : " + login.toString());
                                sendBroadcast(new Intent(Preferences.ACTION_USER_LOGIN));
                                sendBroadcast(new Intent(Preferences.BROADCAST_CHANGEIMAGE));// 发送广播刷新启动图标
                                // 此处添加是否完善用户信息判断
                                if (login.getInfo_status().equals("0")) {
                                    // 不完整
                                    SharePreferencesUtil.saveInfoState(mContext, false);
                                    Intent intent = new Intent(LoginActivity.this, EnterUserNameChooseAvatarActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // 完整
                                    SharePreferencesUtil.saveInfoState(mContext, true);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                showToast(message);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            // TODO: handle exception
                            LogUtil.e(TAG, "LoginActivity" + e.toString());
                        }

                    }

                });
                break;
            default:
                break;
        }

    }

}

package com.etcomm.dcare.app.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.setting.EnterUserNameChooseAvatarActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.Login;
import com.etcomm.dcare.ormlite.bean.User;
import com.etcomm.dcare.ormlite.bean.UserDao;
import com.etcomm.dcare.service.StepDataUploadService;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 修改密码页面 找回密码后的密码重新填写页面
 *
 * @author iexpressbox
 */
public class ChangePasswordActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.old_pwd)
    EditText old_pwd;
    @Bind(R.id.new_pwd)
    EditText new_pwd;
    @Bind(R.id.renew_pwd)
    EditText renew_pwd;
    @Bind(R.id.isShowPwd)
    CheckBox isShowPwd;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.old_psd_li)
    LinearLayout old_psd_li;
    @Bind(R.id.new_pwd_tv)
    TextView new_pwd_tv;
    @Bind(R.id.renew_pwd_tv)
    TextView renew_pwd_tv;
    private String device_id;
    private String user_id;
    private Intent intent;
    private boolean isRegisterEnterPassword;
    protected SharedPreferences defaultsp;
    protected Editor editor;
    private SharedPreferences sp;
    private UserDao userDao;
    private boolean isForgotPassword;
    private String forgotusername;
    private String forgotvalidcode;
    private String client_id;

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

    void readDeviceId() {
        // TODO Auto-generated method stub
        String login = getSharedPreferences(Preferences.UserInfo, MODE_PRIVATE).getString(Preferences.UserInfo, "");
        if (!StringUtils.isEmpty(login)) {
            Login l = JSON.parseObject(login, Login.class);
            user_id = l.getId();
        }

        device_id = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        if (device_id.length() < 5) {
            TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String tmdevice_id = "" + tm.getDeviceId();
            if (tmdevice_id.length() < 5) {
                String deviceSereisId = tm.getSimSerialNumber();
                if (deviceSereisId.length() < 5) {
                    String m_szDevIDShort = "35" + // we make this look like a
                            // valid IMEI
                            Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; // 13
                    device_id = "imei" + m_szDevIDShort;
                } else {
                    device_id = "tmSimSerialNumber" + deviceSereisId;
                }
            } else {
                device_id = "tmDeviceId" + tmdevice_id;
            }
        }
        Log.i(tag, "device_id: " + device_id);
    }

    @OnClick(R.id.btn_next)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (isRegisterEnterPassword) {// 注册
                    register();
                } else if (isForgotPassword) {
                    forgotpassword();

                } else {
                    String oldPass = old_pwd.getText().toString();
                    String newPass = new_pwd.getText().toString();
                    String renewPass = renew_pwd.getText().toString();
                    if (oldPass.length() < 6 || newPass.length() < 6 || newPass.length() < 6) {
                        showToast(R.string.password_length_error);
                    } else if (!newPass.trim().equalsIgnoreCase(renewPass.trim())) {
                        showToast(R.string.password_equals_error);
                    } else if (oldPass.trim().length() > 12 || newPass.trim().length() > 12 || renewPass.trim().length() > 12) {
                        showToast(R.string.password_length_error);
                    } else {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("access_token", SharePreferencesUtil.getToken(mContext));
                        if (TextUtils.isEmpty(user_id)) {
                            user_id = AppSharedPreferencesHelper.getUserId();
                            if (TextUtils.isEmpty(user_id)) {
                                user_id = SharePreferencesUtil.getUid(mContext);
                            }
                        }
                        params.put("user_id", user_id);
                        params.put("device_id", device_id);
                        params.put("old_password", old_pwd.getText().toString());
                        params.put("password", new_pwd.getText().toString());
                        params.put("repeat_password", renew_pwd.getText().toString());
                        showProgress(DIALOG_DEFAULT, true);
                        Log.i(tag, "params: " + params.toString());
                        DcareRestClient.volleyPost(Constants.ChangePwd, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
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
                                // TODO Auto-generated method stub
                                cancelmDialog();
                                try {
                                    int code = response.getInteger("code");
                                    String message = response.getString("message");
                                    Log.i(tag, "onSuccess  code: " + code + " message: " + message);// +
                                    if (code == 0) {
                                        showToast(message);
                                        startAtvTask(LoginActivity.class);
                                        finish();
                                    } else {// code不为0 发生异常
                                        showToast(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
                break;
        }
    }

    private void forgotpassword() {
        // TODO Auto-generated method stub
        if (new_pwd.getText().toString().length() >= 6 && new_pwd.getText().toString().equals(renew_pwd.getText().toString())) {
            // RequestParams params = new RequestParams();
            Map<String, String> params = new HashMap<String, String>();
            params.put("device_id", device_id);
            params.put("password", new_pwd.getText().toString());
            params.put("repeat_password", renew_pwd.getText().toString());
            showProgress(DIALOG_DEFAULT, true);
            Log.i(tag, "params: " + params.toString());
            String url;
            if (forgotusername.contains("@")) {
                url = Constants.ForgotPsdByEmail;
                params.put("email", forgotusername);
            } else {
                url = Constants.ForgotPsdBySMS;
                params.put("mobile", forgotusername);
            }
            DcareRestClient.volleyPost(url, params, new FastJsonHttpResponseHandler() {
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
                    // TODO Auto-generated method stub
                    cancelmDialog();
                    try {
                        int code = response.getInteger("code");
                        String message = response.getString("message");
                        Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// response.getJSONObject("content").toString());
                        if (code == 0) {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            DcareApplication.getInstance().finishActivity(ChangePasswordActivity.this);
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            // super.onSuccess(statusCode, headers, response);
                        } else {// code不为0 发生异常
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(mContext, "请检查密码", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册
     */
    private void register() {
        // TODO Auto-generated method stub
        final String username = intent.getStringExtra(Preferences.USER_NAME);
        if (username != null && username.length() > 0) {
            String url;
            Map<String, String> params = new HashMap<String, String>();
            if (username.contains("@")) {// 邮件
                url = Constants.RegisterbyEMAILCode;
                params.put("email", username);
            } else {// 手机号
                url = Constants.RegisterbySNSCode;
                params.put("mobile", username);

            }
            String password = new_pwd.getText().toString();
            final String repassword = renew_pwd.getText().toString();
            if (TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword) || password.trim().length() < 6 || repassword.trim().length() < 6 || !repassword.equalsIgnoreCase(password)) {
                showToast(R.string.password_error);
                return;
            }
            params.put("password", password);
            params.put("repeat_password", password);
            params.put("structure_id", intent.getStringExtra(Preferences.ActivitionStructureCode));
            params.put("sn", intent.getStringExtra(Preferences.ActivitionCode));
            params.put("device_id", intent.getStringExtra(Preferences.DEVICE_ID));
            params.put("client_id", client_id);
            cancelmDialog();
            showProgress(DIALOG_DEFAULT, true);
            Log.i(tag, "params: " + params.toString());
            DcareRestClient.volleyPost(url, params, new FastJsonHttpResponseHandler() {
                @Override
                public void onCancel() {
                    // TODO Auto-generated method stub
                    Log.w(tag, "post cancel" + this.getRequestURI());
                    cancelmDialog();
                    super.onCancel();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, com.alibaba.fastjson.JSONObject errorResponse) {
                    Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                    cancelmDialog();
                    Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, com.alibaba.fastjson.JSONObject response) {
                    Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                    cancelmDialog();
                    try {
                        int code = response.getInteger("code");
                        String message = response.getString("message");
                        Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                        if (code == 0) {
                            Log.i(tag, "发送数据清空广播");
                            Intent service = new Intent(mContext, StepDataUploadService.class);
                            service.putExtra("isexit", true);
                            mContext.sendBroadcast(new Intent(Preferences.ACTION_CLEAR_ALLDATA));
                            defaultsp.edit().putString(Preferences.USER_NAME, username).commit();
                            defaultsp.edit().putString(Preferences.UserPassword, repassword).commit();
                            Login login = JSON.parseObject(response.getJSONObject("content").toString(), Login.class);
                            editor.putString(Preferences.USER_NAME, username);
                            editor.putString(Preferences.UserId, login.getUser_id());
                            editor.putString(Preferences.NICK_NAME, login.getNick_name());
                            editor.putString(Preferences.Avator, login.getAvatar());
                            editor.putString(Preferences.ACCESS_TOKEN, login.getAccess_token());
                            editor.putString(Preferences.UserInfo, response.getJSONObject("content").toString());
                            editor.commit();
                            defaultsp.edit().putString(Preferences.Avator, login.getAvatar()).commit();
                            Log.i(tag, "login.getUser_id(): " + login.getUser_id());
                            AppSharedPreferencesHelper.setUserId(login.getUser_id());
                            SharePreferencesUtil.saveToken(mContext, login.getAccess_token());
                            AppSharedPreferencesHelper.setUserName(username);
                            AppSharedPreferencesHelper.setBirth_year(login.getBirth_year());
                            AppSharedPreferencesHelper.setAvatar(login.getAvatar());
                            AppSharedPreferencesHelper.setHeight(login.getHeight());
                            AppSharedPreferencesHelper.setWeight(login.getWeight());
                            AppSharedPreferencesHelper.setStructure(login.getStructure());
                            AppSharedPreferencesHelper.setGender(login.getGender());
                            AppSharedPreferencesHelper.setScore(login.getScore());
                            AppSharedPreferencesHelper.setNick_name(login.getNick_name());
                            AppSharedPreferencesHelper.setPedometer_target(login.getPedometer_target());
                            AppSharedPreferencesHelper.setPedometer_distance(login.getPedometer_distance());
                            AppSharedPreferencesHelper.setPedometer_time(login.getPedometer_time());
                            AppSharedPreferencesHelper.setPedometer_consume(login.getPedometer_consume());
                            SharePreferencesUtil.savePhone(mContext, login.getMobile());
                            SharePreferencesUtil.saveEmail(mContext, login.getEmail());
                            SharePreferencesUtil.saveAvatar(mContext, login.getAvatar());
                            SharePreferencesUtil.saveUid(mContext, login.getUser_id());
                            User user = new User();
                            user.setAccess_token(login.getAccess_token());
                            user.setAvatar(login.getAvatar());
                            user.setBirthday(login.getBirthday());
                            user.setUser_id(login.getUser_id());
                            user.setCustomer_id(login.getCustomer_id());
                            user.setHeight(login.getHeight());
                            user.setIs_leader(login.getIs_leader());
                            if (username.contains("@")) {
                                user.setEmail(username);
                            } else {
                                user.setMobile(username);
                            }
                            user.setNick_name(login.getNick_name());
                            user.setPedometer_consume(login.getPedometer_consume());
                            user.setPedometer_distance(login.getPedometer_distance());
                            user.setPedometer_target(login.getPedometer_target());
                            user.setPedometer_time(login.getPedometer_time());
                            // user.setReal_name(login.get);
                            user.setScore(login.getScore());
                            user.setSerial_number_id(login.getSerial_number_id());
                            user.setWeight(login.getWeight());
                            List<User> userlist = userDao.getUserList();

                            boolean isExist = false;

                            if (!isExist) {
                                userDao.add(user);
                            }

                            SharePreferencesUtil.saveReData(mContext, true);
                            Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                            Log.i(tag, "Login : " + login.toString());
                            DcareApplication.getInstance().finishActivity(ChangePasswordActivity.this);
                            Intent intent = new Intent(ChangePasswordActivity.this, EnterUserNameChooseAvatarActivity.class);
                            startActivity(intent);
                            finish();

                        } else {// code不为0 发生异常
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (com.alibaba.fastjson.JSONException e) {
                        Log.e(tag, "JSONException:");
                        e.printStackTrace();
                    }

                }
            });
        } else {
            Toast.makeText(mContext, "用户名读取失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {
        intent = getIntent();
        defaultsp = getSharedPreferences(Preferences.CommonLoginInfo, MODE_PRIVATE);
        isForgotPassword = intent.getBooleanExtra(Preferences.IsForgotPassword, false);
        isRegisterEnterPassword = intent.getBooleanExtra(Preferences.IsRegisterEnterPassword, false);
        sp = getSharedPreferences(Preferences.UserInfo, MODE_PRIVATE);
        defaultsp = getSharedPreferences(Preferences.CommonLoginInfo, MODE_PRIVATE);
        editor = sp.edit();
        userDao = new UserDao(mContext);
        client_id = PushManager.getInstance().getClientid(getApplicationContext());
        Log.i(tag, "client_id  : " + client_id);
        // TODO Auto-generated method stub
        if (isRegisterEnterPassword) {// 注册
            titlebar.setTitle("注册");
            titlebar.setLeftLl(backClickListener);
            old_psd_li.setVisibility(View.GONE);
            new_pwd_tv.setText("密码:");

        } else if (isForgotPassword) {// 忘记密码
            titlebar.setTitle("找回密码");
            titlebar.setLeftLl(backClickListener);
            old_psd_li.setVisibility(View.GONE);
            forgotusername = intent.getStringExtra(Preferences.USER_NAME);
            forgotvalidcode = intent.getStringExtra(Preferences.ValidCode);
        } else {// 修改密码

            titlebar.setTitle("修改密码");
            titlebar.setLeftLl(backClickListener);

        }
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
        renew_pwd.setFilters(new InputFilter[]{filter});
        new_pwd.setFilters(new InputFilter[]{filter});
        old_pwd.setFilters(new InputFilter[]{filter});
        isShowPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    // 显示密码
                    old_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Editable oldetable = old_pwd.getText();
                    Selection.setSelection(oldetable, oldetable.length());
                    renew_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Editable etable = renew_pwd.getText();
                    Selection.setSelection(etable, etable.length());
                    new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Editable newetable = new_pwd.getText();
                    Selection.setSelection(newetable, newetable.length());
                } else {
                    old_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // 下面两行代码实现: 输入框光标一直在输入文本后面
                    Editable oldetable = old_pwd.getText();
                    Selection.setSelection(oldetable, oldetable.length());
                    renew_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // 下面两行代码实现: 输入框光标一直在输入文本后面
                    Editable etable = renew_pwd.getText();
                    Selection.setSelection(etable, etable.length());
                    new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // 下面两行代码实现: 输入框光标一直在输入文本后面
                    Editable newetable = new_pwd.getText();
                    Selection.setSelection(newetable, newetable.length());

                }
            }
        });
        btn_next.setEnabled(false);
        old_pwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (isRegisterEnterPassword) {// 注册
                    if (new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                } else if (isForgotPassword) {
                    if (new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                } else {
                    if (old_pwd.getText().length() > 5 && new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                }
            }
        });
        new_pwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (isRegisterEnterPassword) {// 注册
                    if (new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                } else if (isForgotPassword) {
                    if (new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                } else {
                    if (old_pwd.getText().length() > 5 && new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                }
            }
        });
        renew_pwd.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (isRegisterEnterPassword) {// 注册
                    if (new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                } else if (isForgotPassword) {
                    if (new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                } else {
                    if (old_pwd.getText().length() > 5 && new_pwd.getText().length() > 5 && renew_pwd.getText().length() > 5) {
                        btn_next.setEnabled(true);
                    } else {
                        btn_next.setEnabled(false);
                    }
                }
            }
        });
        readDeviceId();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_changepassword;
    }
}

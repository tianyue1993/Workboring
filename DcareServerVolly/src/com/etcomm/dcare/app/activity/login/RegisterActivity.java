package com.etcomm.dcare.app.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.ExEditText;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;

/**
 * 输入密码信息，包含之前所有信息进行注册
 *
 * @author iexpressbox
 */
public class RegisterActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.et_username)
    ExEditText et_username;
    @Bind(R.id.et_passWord)
    ExEditText et_passWord;
    @Bind(R.id.iv_del_username)
    ImageView iv_del_username;
    @Bind(R.id.iv_del_password)
    ImageView iv_del_password;
    @Bind(R.id.getcode)
    Button getcode;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.username_tv)
    TextView username_tv;
    // 获取设备的各种信息
    private String unique_id;
    private String xinghao;
    private Intent intent;
    protected boolean isCountTime;
    protected boolean isEmail = false;

    @OnClick({R.id.next, R.id.getcode, R.id.iv_del_username, R.id.iv_del_password})
    public void onClick(View v) {
        String username = "";
        String code = "";
        Map<String, String> params;
        switch (v.getId()) {
            case R.id.next:
                cancelmDialog();
                username = et_username.getText().toString().trim();
                code = et_passWord.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    showToast("请输入用户名");
                }
                if (TextUtils.isEmpty(code)) {
                    showToast("请输入验证码");
                    return;
                }
                if (code.length() != 4) {
                    showToast("验证码格式错误");
                }
                if (username_tv.getText().toString().contains("邮箱")) {
                    if (!username.contains("@")) {
                        showToast(R.string.email_format_error);
                        return;
                    }
                }
                if (username_tv.getText().toString().contains("手机")) {
                    if (username.trim().length() != 11) {
                        showToast(R.string.mobile_format_error);
                        return;
                    }
                }
                params = new HashMap<String, String>();
                if (username != null && username.length() > 0) {
                    String url;
//				if (username.contains("@")) {// 邮件
                    if (isEmail) {
                        url = Constants.VilidateEMAILCode;
                        params.put("type", "email_sign_up");
                    } else {// 手机号
                        url = Constants.VilidateSNSCode;
                        params.put("type", "mobile_sign_up");

                    }
                    params.put("receiver", username);
                    params.put("verify_code", code);
                    cancelmDialog();
                    showProgress(DIALOG_DEFAULT, true);
                    Log.i(tag, tag + url + " params: " + params.toString());
                    DcareRestClient.volleyPost(url, params, new FastJsonHttpResponseHandler() {
                        @Override
                        public void onCancel() {
                            cancelmDialog();
                            Log.w(tag, "post cancel" + this.getRequestURI());
                            super.onCancel();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            cancelmDialog();
                            Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                            Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                            cancelmDialog();
                            try {
                                int code = response.getInteger("code");
                                String message = response.getString("message");
                                if (code != 0) {
                                    showToast(message);
                                    return;
                                }
                                Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: " + response.getString("content").toString());
                                if (code == 0) {
                                    // 验证码验证成功，下一步
                                    nextstep();
                                } else {// code不为0 发生异常
                                    showToast(message);
                                }
                            } catch (JSONException e) {
                                Log.e(tag, tag + " JSONException:");
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    showToast("请输入用户名");
                    return;
                }
                break;
            case R.id.getcode:
                username = et_username.getText().toString();
                if (username_tv.getText().toString().contains("邮箱")) {
                    if (!username.contains("@")) {
                        showToast(R.string.email_format_error);
                        return;
                    }
                }
                if (username_tv.getText().toString().contains("手机")) {
                    if (username.trim().length() != 11) {
                        showToast(R.string.mobile_format_error);
                        return;
                    }
                }

                if (username != null && username.length() > 0) {
                    params = new HashMap<String, String>();
                    // TODO //username_tv pan
                    String url;
                    if (isEmail) {
                        url = Constants.GetVilidateEMAILCode;
                        params.put("type", "email_sign_up");
                    } else {// 手机号
                        url = Constants.GetVilidateSNSCode;
                        params.put("type", "mobile_sign_up");

                    }
                    params.put("receiver", username);
                    cancelmDialog();
                    showProgress(DIALOG_DEFAULT, true);
                    Log.i(tag, "params: " + params.toString());
                    DcareRestClient.volleyPost(url, params, new FastJsonHttpResponseHandler() {
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
                                Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: " + response.getString("content").toString());
                                if (code == 0) {
                                    showToast(message);
                                    isCountTime = true;
                                    timer.start();
                                    getcode.setEnabled(false);
                                } else {// code不为0 发生异常
                                    showToast(message);
                                }
                            } catch (JSONException e) {
                                Log.e(tag, "JSONException:");
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    showToast("用户名输入不正确");
                }
                break;
            case R.id.iv_del_username:
                et_username.setText("");
                next.setEnabled(false);
                break;
            case R.id.iv_del_password:
                et_passWord.setText("");
                next.setEnabled(false);
                break;
            default:
                break;
        }
    }

    protected void nextstep() {
        Log.e(tag, "nextstep 验证码验证成功,进入 密码页面");
        intent.putExtra(Preferences.USER_NAME, et_username.getText().toString());
        intent.putExtra(Preferences.ValidCode, et_passWord.getText().toString());
        intent.putExtra(Preferences.IsRegisterEnterPassword, true);
        intent.setClass(mContext, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @OnTextChanged(value = R.id.et_username, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged_username(Editable s) {
        if (s.length() > 1) {
            iv_del_username.setVisibility(View.VISIBLE);
            if (et_username.getText().toString().length() >= 1 && et_passWord.getText().toString().length() >= 1) {
                next.setEnabled(true);
            } else {
                next.setEnabled(false);
            }
        } else {
            iv_del_username.setVisibility(View.INVISIBLE);
        }
    }

    @OnTextChanged(value = R.id.et_passWord, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged_password(Editable s) {
        if (s.length() > 1) {
            iv_del_password.setVisibility(View.VISIBLE);
            if (et_username.getText().toString().length() >= 1 && et_passWord.getText().toString().length() >= 1) {
                next.setEnabled(true);
            } else {
                next.setEnabled(false);
            }
        } else {
            iv_del_password.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    protected void initDatas() {
        intent = getIntent();
        titlebar.setLeftLl(backClickListener);
        titlebar.setTitle("注册");

        // 新增邮箱注册和手机号注册 初始为手机注册
        username_tv.setText("手机号:");
        titlebar.setRightText("邮箱注册");
        et_username.setHint("输入手机号");
        titlebar.setRightClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (titlebar.getRightText() != null) {
                    if (titlebar.getRightText().getText().toString().equals("邮箱注册")) {// 切换到邮箱注册
                        titlebar.setRightText("手机注册");
                        username_tv.setText("邮箱:");
                        isEmail = true;
                        // username_tv.setHint("输入手机号");
                        et_username.setHint("输入邮箱");
                    } else {
                        titlebar.setRightText("邮箱注册");
                        et_username.setHint("输入手机号");
                        isEmail = false;
                        // username_tv.setHint("输入邮箱");


                        username_tv.setText("手机号:");

                    }
                }
            }
        });
        next.setEnabled(false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_register;
    }

    // 注册 刷新验证码 倒计时
    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            getcode.setText(millisUntilFinished / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            getcode.setEnabled(true);
            getcode.setText(R.string.reget_check_code);
            isCountTime = false;
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

}

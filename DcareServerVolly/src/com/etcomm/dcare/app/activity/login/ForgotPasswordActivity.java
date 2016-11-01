package com.etcomm.dcare.app.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import cz.msebera.android.httpclient.Header;

/**
 * 忘记密码，使用手机号或者邮箱找回密码 右上角标题可以切换手机或者邮箱找回密码 获取验证码页面
 *
 * @author iexpressbox
 */
public class ForgotPasswordActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.et_username)
    ExEditText et_username;
    @Bind(R.id.et_passWord)
    ExEditText et_passWord;
    @Bind(R.id.getcode)
    Button getcode;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.tv_username)
    TextView tv_username;
    private Intent intent;
    private String deviceid;
    //	private Dialog dialog;
    private CountDownTimer mDownTimer;
    protected boolean isCountTime;
    private boolean isEmail = false;

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


    @OnClick({R.id.next, R.id.getcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:

                String username = et_username.getText().toString();
                String code = et_passWord.getText().toString();
                // nextstep();
                if (username != null && username.length() > 0) {

                } else {
                    Toast.makeText(mContext, "请输入手机号/邮箱/工号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code != null && code.length() > 0) {

                } else {
                    Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                // RequestParams params = new RequestParams();
                Map<String, String> params = new HashMap<String, String>();
                if (username != null && username.length() > 0) {
                    String url;
                    // if(username.contains("@")){//邮件
                    if (isEmail) {
                        url = Constants.VilidateEMAILCode;
                        // params.put("email", username);
                        params.put("type", "forgot_password");
                    } else {// 手机号
                        url = Constants.VilidateSNSCode;
                        // params.put("mobile", username);
                        params.put("type", "forgot_password");

                    }
                    params.put("receiver", username);
                    params.put("verify_code", code);
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
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                            cancelmDialog();

                            Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response "
                                    + response.toString());
                            cancelmDialog();

                            try {
                                int code = response.getInteger("code");
                                String message = response.getString("message");
                                Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
//      								+ response.getString("content").toString());
                                if (code == 0) {
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    // 验证码验证成功，下一步
                                    nextstep();
                                } else {// code不为0 发生异常
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Log.e(tag, "JSONException:");
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.getcode:

                String uname = et_username.getText().toString();
                if (uname != null && uname.length() > 0) {
                    String url;
//      			if (username.contains("@")) {// 邮件
                    if (isEmail) {
                        url = Constants.GetVilidateEMAILCode;
                    } else {// 手机号
                        url = Constants.GetVilidateSNSCode;

                    }
                    // RequestParams params = new RequestParams();
                    Map<String, String> paramss = new HashMap<String, String>();
                    paramss.put("receiver", uname);
                    paramss.put("type", "forgot_password");
                    cancelmDialog();
                    showProgress(DIALOG_DEFAULT, true);
                    Log.i(tag, "params: " + paramss.toString());
                    DcareRestClient.volleyPost(url, paramss, new FastJsonHttpResponseHandler() {
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
                            Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response "
                                    + response.toString());
                            cancelmDialog();

                            try {
                                int code = response.getInteger("code");
                                String message = response.getString("message");
                                Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                                // + response.getString("content").toString());
                                if (code == 0) {
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    isCountTime = true;
                                    mDownTimer.start();
                                    getcode.setEnabled(false);
                                } else {// code不为0 发生异常
                                    showToast(message);
//      							Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Log.e(tag, "JSONException:");
                                e.printStackTrace();
                            }

                        }
                    });

                } else {
                    Toast.makeText(mContext, "用户名输入不正确", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    protected void nextstep() {
        // TODO Auto-generated method stub
        Log.e(tag, "nextstep 验证码验证成功,进入 密码页面");
        intent.putExtra(Preferences.USER_NAME, et_username.getText().toString());
        intent.putExtra(Preferences.ValidCode, et_passWord.getText().toString());
        intent.putExtra(Preferences.IsForgotPassword, true);
        intent.setClass(mContext, ChangePasswordActivity.class);
        startActivity(intent);
    }


    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {
        intent = getIntent();
        deviceid = intent.getStringExtra(Preferences.DEVICE_ID);
        titlebar.setLeftLl(backClickListener);
        titlebar.setTitle("找回密码");
        titlebar.setRightText("邮箱找回");
        tv_username.setText("手机号");
        titlebar.setRightClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titlebar.getRightText() != null) {
                    if (titlebar.getRightText().getText().toString().equals("邮箱找回")) {// 切换到邮箱注册
                        titlebar.setRightText("手机找回");
                        tv_username.setText("邮箱");
                        if (et_username.getText().toString().isEmpty()) {
                            et_username.setHint("输入邮箱");
                        } else {
                            et_username.setText("");
                        }
                        if (!et_passWord.getText().toString().isEmpty()) {
                            et_passWord.setText("");
                        }
                        isEmail = true;
                    } else {
                        titlebar.setRightText("邮箱找回");
                        if (et_username.getText().toString().isEmpty()) {
                            et_username.setHint("输入手机号");
                        } else {
                            et_username.setText("");
                        }
                        if (!et_passWord.getText().toString().isEmpty()) {
                            et_passWord.setText("");
                        }
                        tv_username.setText("手机号");
                        isEmail = false;
                    }
                }
            }
        });
        mDownTimer = new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                getcode.setText(
                        Html.fromHtml("<font color=#5a5a5a>" + millisUntilFinished / 1000 + "秒后重新发送" + "</font>"));
                // getcode.setText("60秒后重新发送");
            }

            @Override
            public void onFinish() {
                getcode.setEnabled(true);
                getcode.setText(R.string.reget_check_code);
                isCountTime = false;
            }
        };
        next.setEnabled(false);
        et_passWord.addTextChangedListener(new TextWatcher() {
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
                if ((et_passWord.getText().toString().length() > 3) && et_username.getText().toString().length() > 1) {
                    next.setEnabled(true);
                } else {
                    next.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_forgotpassword;
    }
}

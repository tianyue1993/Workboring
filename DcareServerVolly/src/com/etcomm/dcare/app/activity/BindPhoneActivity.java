package com.etcomm.dcare.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
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
 * Created by Administrator on 2016/9/12.
 */
public class BindPhoneActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.getcode)
    Button getcode;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.et_username)
    ExEditText et_username;
    @Bind(R.id.et_passWord)
    ExEditText et_passWord;
    @Bind(R.id.iv_del_username)
    ImageView iv_del_username;
    @Bind(R.id.iv_del_password)
    ImageView iv_del_password;
    protected boolean isCountTime;
    protected boolean isEmail = false;
    protected boolean isChange = false;
    private Intent intent;
    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.li)
    LinearLayout li;
    @Bind(R.id.ll_ar)
    LinearLayout ll_ar;
    @Bind(R.id.alreadyentered_tv)
    TextView alreadyentered_tv;
    @Bind(R.id.change)
    Button change;
    private String username;
    @Bind(R.id.number)
    TextView number;
    @Bind(R.id.title)
    TextView title;

    @OnClick({R.id.next, R.id.getcode, R.id.iv_del_username, R.id.iv_del_password})
    public void onClick(View v) {
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
                                    if (isEmail) {
                                        editUserInfo("email", username);
                                    } else {
                                        editUserInfo("mobile", username);
                                    }

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
                    if (username.equals("")) {
                        showToast("请输入邮箱");
                    }
                    if (!username.contains("@")) {
                        showToast(R.string.email_format_error);
                        return;
                    }

                }
                if (username_tv.getText().toString().contains("手机")) {
                    if (username.equals("")) {
                        showToast("请输入手机号");
                    }
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

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        titlebar.setLeftLl(backClickListener);
        intent = getIntent();
        titlebar.setLeftLl(backClickListener);
        titlebar.setTitle("注册");
        // 新增邮箱注册和手机号注册 初始为手机注册
        username_tv.setText("手机号:");
        et_username.setHint("输入手机号");
        isEmail = intent.getBooleanExtra("email", false);
        isChange = intent.getBooleanExtra("change", false);
        if (isEmail) {
            username_tv.setText("邮箱：");
            titlebar.setTitle("绑定邮箱");
            et_username.setHint("输入邮箱");
            change.setText("更换邮箱");
            alreadyentered_tv.setText("已关联邮箱:");
            if (isChange) {
                change.setText("更换");
                titlebar.setTitle("更换邮箱");
                title.setText("更换邮箱后，下次可使用新邮箱登录");
            } else {
                title.setText("绑定邮箱后，下次可使用邮箱登录");
            }

            number.setText(SharePreferencesUtil.getEmail(mContext));
            if (isChange) {
                li.setVisibility(View.VISIBLE);
                ll_ar.setVisibility(View.GONE);
            } else {
                if (SharePreferencesUtil.getEmail(mContext).equals("")) {
                    li.setVisibility(View.VISIBLE);
                    ll_ar.setVisibility(View.GONE);
                } else {
                    li.setVisibility(View.GONE);
                    ll_ar.setVisibility(View.VISIBLE);
                }
            }


        } else {
            username_tv.setText("手机号：");
            titlebar.setTitle("绑定手机号");
            et_username.setHint("输入手机号");
            change.setText("更换手机号");
            alreadyentered_tv.setText("已关联手机号:");
            if (isChange) {
                titlebar.setTitle("更换手机号");
                title.setText("更换手机号后，下次可使用新手机号登录");
                change.setText("更换");
            } else {
                title.setText("绑定手机号后，下次可使用手机号登录");
            }
            number.setText(SharePreferencesUtil.getPhone(mContext));
            if (isChange) {
                li.setVisibility(View.VISIBLE);
                ll_ar.setVisibility(View.GONE);
            } else {
                if (SharePreferencesUtil.getPhone(mContext).equals("")) {
                    li.setVisibility(View.VISIBLE);
                    ll_ar.setVisibility(View.GONE);
                } else {
                    li.setVisibility(View.GONE);
                    ll_ar.setVisibility(View.VISIBLE);
                }
            }

        }
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BindPhoneActivity.class);
                if (isEmail) {
                    intent.putExtra("email", true);
                }
                intent.putExtra("change", true);
                startActivity(intent);
                finish();
            }
        });
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
    protected int setLayoutId() {
        return R.layout.activity_bind_phone;
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

    private void editUserInfo(final String field, final String value) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("field", field);
        params.put("value", value);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyPost(Constants.EditUserInfo, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                cancelmDialog();
                Log.w(tag, "post cancel" + this.getRequestURI());

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
                    if (code != 0) {
                        exceptionCode(code);
                    } else {
                        showToast("绑定成功");
                        if (isEmail) {
                            SharePreferencesUtil.saveEmail(mContext, username);
                        } else {
                            SharePreferencesUtil.savePhone(mContext, username);
                        }
                        sendBroadcast(new Intent("changeinfo"));
                        finish();
                    }
                    showToast(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}

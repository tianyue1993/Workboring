package com.etcomm.dcare.app.activity.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.ExEditText;
import com.etcomm.dcare.widget.ExEditText.OnRightDrawableClickListener;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @author etc
 * @ClassName: ChangeNickNameActivity
 * @Description: 设置-修改姓名
 * @date 8 Apr, 2016 10:12:57 AM
 */
@SuppressLint("NewApi")
public class ChangeUserNameActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.nickname_et)
    ExEditText nickname_et;
    @Bind(R.id.btn_next)
    Button btn_next;
    private Intent intent;
    private String nickname;
    int code = 0;
    String message = null;

    @OnClick(R.id.titlebar)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar:
                break;
        }
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
                    code = response.getInteger("code");
                    message = response.getString("message");
                    if (code != 0) {

                        return;
                    } else {
                        if (field.equals("real_name")) {
                            AppSharedPreferencesHelper.setReal_name(value);
                        }
                        backWithData(Preferences.SelectNickName, nickname_et.getText().toString());
                        finish();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                } finally {
                    showToast(message);
                }

            }
        });
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        intent = getIntent();
        nickname = intent.getStringExtra(Preferences.USER_NAME);
        titlebar.setTitle("修改姓名");// 姓名 昵称
        titlebar.setLeftLl(backClickListener);
        titlebar.setRightText("保存");
        nickname_et.setSingleLine();
        nickname_et.setText(nickname);
        nickname_et.requestFocus();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            nickname_et.setRightDrawable(null, getDrawable(R.drawable.nickname_delete));
        } else {
            nickname_et.setRightDrawable(null, getResources().getDrawable(R.drawable.nickname_delete));
        }
        nickname_et.setRightDrawableOnClickListener(new OnRightDrawableClickListener() {
            @Override
            public void onRightDrawableClick() {
                // TODO Auto-generated method stub
                nickname_et.setText("");
            }
        });
        titlebar.setRightClick(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nickname_et.getText().toString())) {
                    showToast(R.string.nick_name_nonull);
                    return;
                }
                if (nickname_et.getText().toString().trim().length() < 2) {
                    showToast(R.string.nick_name_error);
                    return;
                }
                editUserInfo("real_name", nickname_et.getText().toString());
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_nickname;
    }

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

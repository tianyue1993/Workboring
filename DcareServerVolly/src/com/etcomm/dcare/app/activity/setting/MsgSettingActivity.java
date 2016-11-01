package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.etcomm.dcare.widget.SwitchButton;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import cz.msebera.android.httpclient.Header;

public class MsgSettingActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.msgsetting_msg_switch)
    SwitchButton msgsetting_msg_switch;
    @Bind(R.id.msgsetting_msgdianzan_switch)
    SwitchButton msgsetting_msgdianzan_switch;
    @Bind(R.id.msgsetting_msgpinglun_switch)
    SwitchButton msgsetting_msgpinglun_switch;

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


    private void setPush(final String type, final boolean status) {
        // TODO Auto-generated method stub
//		RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("type", type);
        if (status) {
            params.put("status", "1");
        } else {
            params.put("status", "0");
        }
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.UserPush, params, new FastJsonHttpResponseHandler() {
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
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
//							+ response.getString("content").toString());
                    if (code == 0) {
                        if (type.equals("is_push")) {
                            AppSettingPreferencesHelper.setIsPushMsg(status);
                            if (status) {
                                msgsetting_msg_switch.setChecked(true);
                                msgsetting_msgdianzan_switch.setChecked(true);
                                msgsetting_msgpinglun_switch.setChecked(true);
                                msgsetting_msgdianzan_switch.setEnabled(true);
                                msgsetting_msgpinglun_switch.setEnabled(true);
                            } else {
                                msgsetting_msg_switch.setChecked(false);
                                msgsetting_msgdianzan_switch.setChecked(false);
                                msgsetting_msgpinglun_switch.setChecked(false);
                                msgsetting_msgdianzan_switch.setEnabled(false);
                                msgsetting_msgpinglun_switch.setEnabled(false);
                            }
                        } else if (type.equals("is_like")) {
                            AppSettingPreferencesHelper.setIsPushMsg_Like(status);
                            if (status) {
                                msgsetting_msgdianzan_switch.setChecked(true);
                            } else {
                                msgsetting_msgdianzan_switch.setChecked(false);
                            }
                        } else if (type.equals("is_comment")) {
                            AppSettingPreferencesHelper.setIsPushMsg_Comment(status);
                            if (status) {
                                msgsetting_msgpinglun_switch.setChecked(true);
                            } else {
                                msgsetting_msgpinglun_switch.setChecked(false);
                            }
                        }

                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        if (type.equals("is_push")) {
                            msgsetting_msg_switch.setChecked(!status);
                        } else if (type.equals("is_like")) {
                            msgsetting_msgdianzan_switch.setChecked(!status);
                        } else if (type.equals("is_comment")) {
                            msgsetting_msgpinglun_switch.setChecked(!status);
                        }
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
    protected Activity atvBind() {
        return this;
    }

    @OnCheckedChanged({R.id.msgsetting_msgdianzan_switch, R.id.msgsetting_msgpinglun_switch})
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        switch (v.getId()) {
            case R.id.msgsetting_msgdianzan_switch:
                setPush("is_like", isChecked);
                break;
            case R.id.msgsetting_msgpinglun_switch:
                setPush("is_comment", isChecked);
                break;

            default:
                break;
        }
    }


    @Override
    protected void initDatas() {

        titlebar.setTitle("设置");
        titlebar.setLeftLl(backClickListener);
        if (AppSettingPreferencesHelper.isPushMsg()) {
            msgsetting_msg_switch.setChecked(true);
        } else {
            msgsetting_msg_switch.setChecked(false);
        }
        if (AppSettingPreferencesHelper.isPushMsg_Comment()) {
            msgsetting_msgpinglun_switch.setChecked(true);
        } else {
            msgsetting_msgpinglun_switch.setChecked(false);
        }
        if (AppSettingPreferencesHelper.isPushMsg_Like()) {
            msgsetting_msgdianzan_switch.setChecked(true);
        } else {
            msgsetting_msgdianzan_switch.setChecked(false);
        }
        msgsetting_msg_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                setPush("is_push", isChecked);
            }
        });
        msgsetting_msgdianzan_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                setPush("is_like", isChecked);
            }
        });
        msgsetting_msgpinglun_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                setPush("is_comment", isChecked);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_msgsetting;
    }
}

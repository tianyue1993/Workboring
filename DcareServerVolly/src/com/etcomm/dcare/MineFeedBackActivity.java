package com.etcomm.dcare;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 意见反馈
 *
 * @author iexpressbox
 */
public class MineFeedBackActivity extends BaseActivity {
    @Bind(R.id.feedback_commit)
    Button feedback_commit;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.feedback_contact_tv)
    EditText feedback_contact_tv;
    @Bind(R.id.feedback_tv)
    EditText feedback_tv;
    @Bind(R.id.contact_info_tv)
    TextView contact_info_tv;
    @Bind(R.id.btn1)
    RadioButton btn1;
    @Bind(R.id.btn2)
    RadioButton btn2;
    @Bind(R.id.contact_tv)
    TextView contact_tv;
    protected String device_info;
    private String type = "0";

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


    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        device_info = android.os.Build.MODEL + "_"
                + android.os.Build.VERSION.SDK + "_"
                + android.os.Build.VERSION.RELEASE;
        Log.i(tag, "device_info:" + device_info);
        btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "0";
                } else {
                    type = "1";
                }
            }
        });
        feedback_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 200 - s.length();
                contact_tv.setText("您还可以输入" + i + "个字");
            }
        });

        feedback_tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (feedback_tv.getText().length() >= 1) {
                    feedback_commit.setClickable(true);
                    feedback_commit.setEnabled(true);
                } else {
                    feedback_commit.setClickable(false);
                    feedback_commit.setEnabled(false);
                }
            }
        });
        titlebar.setTitle("意见反馈");
        titlebar.setLeftLl(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        feedback_commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (feedback_tv.getText().toString().length() < 1) {
                    Toast.makeText(mContext, "请输入您要吐槽的内容!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", SharePreferencesUtil.getToken(mContext));
                params.put("contact", feedback_contact_tv.getText().toString());
                params.put("content", feedback_tv.getText().toString());
                params.put("device_info", device_info);
                params.put("type", type);
                showProgress(DIALOG_DEFAULT, true);
                Log.i(tag, "params: " + params.toString());
                DcareRestClient.volleyPost(Constants.FeedBack, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {

                    @Override
                    public void onCancel() {
                        // TODO Auto-generated method stub
                        Log.w(tag, "post cancel" + this.getRequestURI());
                        cancelmDialog();
                        super.onCancel();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
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
                            Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");//+ response.getJSONObject("content").toString()
                            if (code == 0) {
                                Toast.makeText(mContext, "提交成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {// code不为0 发生异常
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activiity_minefeedback;
    }
}

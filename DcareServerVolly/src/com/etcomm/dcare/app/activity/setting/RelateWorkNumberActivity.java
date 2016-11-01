package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @author etc
 * @ClassName: RelateWorkNumberActivity
 * @Description: 用户设置-关联工号
 * @date 14 Apr, 2016 1:55:10 PM
 */
public class RelateWorkNumberActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.enternumber_li)
    LinearLayout enternumber_li;
    @Bind(R.id.enterworknumber_et)
    EditText enterworknumber_et;
    @Bind(R.id.alreadyentered_tv)
    TextView alreadyentered_tv;
    @Bind(R.id.prompt)
    TextView prompt;
    @Bind(R.id.commit_btn)
    Button commit_btn;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.job_number)
    TextView job_number;
    private SharedPreferences sp;

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

    @OnClick(R.id.commit_btn)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_btn:
                String job_number = enterworknumber_et.getText().toString();
                if (TextUtils.isEmpty(job_number) || job_number.trim().length() < 3) {
                    showToast(R.string.job_number_error);
                    return;
                }
                editUserInfo("job_number", job_number);

                break;

            default:
                break;
        }
    }

    private void editUserInfo(String field, final String value) {
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("field", field);
        params.put("value", value);
        params.put(Preferences.ACCESS_TOKEN, SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyPost(Constants.EditUserInfo, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
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
                showToast(R.string.network_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        showToast(message);
                        SharePreferencesUtil.saveJobNumber(mContext, value);
                        AppSharedPreferencesHelper.setJob_number(value);
                        finish();
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

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {

        titlebar.setTitle("关联员工号");
        titlebar.setLeftLl(backClickListener);
        String workNumber = AppSharedPreferencesHelper.getJob_number();
        LogUtil.d(TAG, "WorkNumber is >>>" + workNumber);
        if (TextUtils.isEmpty(workNumber)) {// 未关联工号
            alreadyentered_tv.setVisibility(View.INVISIBLE);
            job_number.setVisibility(View.INVISIBLE);
            enterworknumber_et.setVisibility(View.VISIBLE);
            prompt.setVisibility(View.INVISIBLE);
        } else {
            alreadyentered_tv.setVisibility(View.VISIBLE);
            job_number.setVisibility(View.VISIBLE);
            job_number.setText(workNumber);
            enterworknumber_et.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            commit_btn.setVisibility(View.INVISIBLE);
            prompt.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_relateworknumber;
    }

}

package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.MainActivity;
import com.etcomm.dcare.R;
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
import com.etcomm.dcare.widget.CircleImageView;
import com.etcomm.dcare.widget.ProgressSeekBar;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


/**
 * 设置运动量
 *
 * @author iexpressbox
 */
public class SettingSportGoalActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.goal_avator)
    CircleImageView goal_avator;
    @Bind(R.id.goal_age_tv)
    TextView goal_age_tv;
    @Bind(R.id.nickname)
    TextView nickname;
    @Bind(R.id.goal_height_tv)
    TextView goal_height_tv;
    @Bind(R.id.goal_weight_tv)
    TextView goal_weight_tv;
    @Bind(R.id.progressbar)
    ProgressSeekBar progressbar;
    @Bind(R.id.btn_next)
    Button btn_next;
    private Intent intent;
    private boolean isFirstSetUserInfo;
    private SharedPreferences sp;
    private Editor editor;
    ImageLoader imageloader = ImageLoader.getInstance();
    private Login login;

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

    // progress 5000 20000 1000
    @OnClick(R.id.btn_next)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                if (isFirstSetUserInfo) {// 注册完成
                    Map<String, String> params = new HashMap<String, String>();
                    cancelmDialog();
                    showProgress(DIALOG_DEFAULT, true);
                    params.put("user_id", AppSharedPreferencesHelper.getUserId());
                    params.put("birth_year", intent.getStringExtra(Preferences.UserAge));
                    params.put("avatar", intent.getStringExtra(Preferences.Avator));
                    params.put("height", intent.getStringExtra(Preferences.UserHeight));
                    params.put("weight", intent.getStringExtra(Preferences.UserWeight));
                    params.put("nick_name", intent.getStringExtra(Preferences.UserNickName));
                    params.put("gender", String.valueOf(intent.getIntExtra(Preferences.UserSex, 1)));// 1男，2女
                    params.put("job_number", intent.getStringExtra(Preferences.JOB_NUMBER).trim());// 工号
                    params.put("real_name", intent.getStringExtra(Preferences.USER_NAME).trim());// 姓名
                    params.put("pedometer_target", String.valueOf(progressbar.getProgress() * 1000 + 1000));// 最高2w步
                    AppSharedPreferencesHelper.setBirth_year(intent.getStringExtra(Preferences.UserAge));
                    AppSharedPreferencesHelper.setAvatar(intent.getStringExtra(Preferences.Avator));
                    AppSharedPreferencesHelper.setHeight(intent.getStringExtra(Preferences.UserHeight));
                    AppSharedPreferencesHelper.setWeight(intent.getStringExtra(Preferences.UserWeight));
                    AppSharedPreferencesHelper.setGender("" + intent.getIntExtra(Preferences.UserSex, 1));
                    AppSharedPreferencesHelper.setNick_name(intent.getStringExtra(Preferences.UserNickName).trim());
                    AppSharedPreferencesHelper.setReal_name(intent.getStringExtra(Preferences.USER_NAME).trim());
                    if (intent.getStringExtra(Preferences.JOB_NUMBER) == null) {
                        AppSharedPreferencesHelper.setJob_number(intent.getStringExtra(Preferences.JOB_NUMBER));
                    }
                    AppSharedPreferencesHelper.setScore("20");
                    AppSharedPreferencesHelper.setPedometer_target("" + (int) (progressbar.getProgress() * 1000 + 1000));
                    Log.i(tag, "params: " + params.toString());
                    DcareRestClient.volleyPost(Constants.RegisterUpdateInfo, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
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
                            cancelmDialog();
                            try {
                                int code = response.getInteger("code");
                                String message = response.getString("message");
                                Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                                // + response.getString("content").toString()
                                if (code == 0) {
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    mContext.sendBroadcast(new Intent(Preferences.ACTION_CLEAR_ALLDATA));
                                    DcareApplication.getInstance().finishActivity(SettingSportGoalActivity.this);
                                    editor.putString(Preferences.UserAge, intent.getStringExtra(Preferences.UserAge));
                                    editor.putString(Preferences.UserHeight, intent.getStringExtra(Preferences.UserWeight));
                                    editor.putString(Preferences.UserSex, "" + intent.getIntExtra(Preferences.UserSex, 1));// //1男，2女
                                    editor.putString(Preferences.UserWeight, intent.getStringExtra(Preferences.UserWeight));
                                    editor.putString(Preferences.NICK_NAME, intent.getStringExtra(Preferences.UserNickName));
                                    editor.putString(Preferences.PedometerTarget, "" + progressbar.getProgress() * 1000 + 1000);
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    SharePreferencesUtil.saveInfoState(mContext, true);
                                    sendBroadcast(new Intent(Preferences.ACTION_USER_REGISTER));
                                    startActivity(intent);
                                    finish();
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

                    String pedometer_target = "" + (int) (progressbar.getProgress() * 1000 + 1000);// +
                    AppSharedPreferencesHelper.setPedometer_target(pedometer_target);
                    editUserInfo("pedometer_target", pedometer_target);
                }
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
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
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
                Toast.makeText(mContext, "修改失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        AppSharedPreferencesHelper.setPedometer_target(value);
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
        intent = getIntent();
        isFirstSetUserInfo = intent.getBooleanExtra(Preferences.FirstSetUserInfo, false);
        sp = getSharedPreferences(Preferences.SignIn, MODE_PRIVATE);
        editor = sp.edit();

        String loginstr = sp.getString(Preferences.MineCurrent, "");// minecurrent
        login = null;
        if (!StringUtils.isEmpty(loginstr)) {
            login = JSON.parseObject(loginstr, Login.class);
            Log.i(tag, "login str: " + login.toString());
        } else {
            String str = sp.getString(Preferences.UserInfo, "");
            if (!StringUtils.isEmpty(str)) {
                login = JSON.parseObject(str, Login.class);
                Log.i(tag, "login str: " + login.toString());
            }
        }
        if (isFirstSetUserInfo) {
            titlebar.setTitle("目标设置");
            titlebar.setLeftLl(backClickListener);
            btn_next.setText("完成");
            String age = intent.getStringExtra(Preferences.UserAge);
            String height = intent.getStringExtra(Preferences.UserHeight);
            String weight = intent.getStringExtra(Preferences.UserWeight);
            String avator = intent.getStringExtra(Preferences.Avator);
            String nick_name = intent.getStringExtra(Preferences.NICK_NAME);
            nickname.setText(nick_name);
            goal_height_tv.setText(height);
            goal_weight_tv.setText(weight);
            DateTime d = new DateTime();
            int a = d.getYear() - Integer.valueOf(age);
            goal_age_tv.setText(a + "");
            // 29岁以下，10000步
            // 30-39岁，8000步
            // 40-49岁，6000步
            // 50岁以上，5000步

            int curpro = 4;
            if (a <= 29) {
                curpro = 9;
            } else if (a >= 30 && a <= 39) {
                curpro = 7;
            } else if (a >= 40 && a <= 49) {
                curpro = 5;
            } else {
                curpro = 4;
            }
            progressbar.setProgress(curpro);
            imageloader.displayImage(avator, goal_avator);

        } else {
            titlebar.setTitle("目标设置");
            titlebar.setLeftLl(backClickListener);
            if (login != null) {
                String avator = AppSharedPreferencesHelper.getAvatar();
                // String avator = login.getAvatar();

                if (!StringUtils.isEmpty(avator)) {
                    imageloader.displayImage(avator, goal_avator);
                } else {
                    avator = sp.getString(Preferences.Avator, "");
                    if (!StringUtils.isEmpty(avator)) {
                        imageloader.displayImage(avator, goal_avator);
                    }
                }
                goal_height_tv.setText(AppSharedPreferencesHelper.getHeight() + "");
                goal_weight_tv.setText(AppSharedPreferencesHelper.getWeight() + "");
                nickname.setText(AppSharedPreferencesHelper.getNick_name());
                DateTime time = new DateTime();
                goal_age_tv.setText("" + (time.getYear() - Integer.parseInt(AppSharedPreferencesHelper.getBirth_year())));
                int progress = (Integer.parseInt(AppSharedPreferencesHelper.getPedometer_target())) / 1000 - 1;
                progressbar.setProgress(progress);
            } else {
                Log.e(tag, "error login null");
            }
        }

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_settingsportgoal;
    }
}

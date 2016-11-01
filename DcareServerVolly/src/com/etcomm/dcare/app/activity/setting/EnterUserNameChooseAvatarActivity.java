package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.etcomm.dcare.netresponse.AvatarContent;
import com.etcomm.dcare.widget.ScrollHorizontalScrollView;
import com.etcomm.dcare.widget.ScrollHorizontalScrollView.OnItemClickListener;
import com.etcomm.dcare.widget.ScrollHorizontalScrollViewAdapter;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;

/**
 * 输入用户名，选择头像
 *
 * @author iexpressbox
 */

public class EnterUserNameChooseAvatarActivity extends BaseActivity {

    private String nickname = "";
    private String username = "";
    private String worknumber = "";

    @Bind(R.id.et_username)
    com.etcomm.dcare.widget.ExEditText et_username;
    @Bind(R.id.et_nickyname)
    com.etcomm.dcare.widget.ExEditText et_nickyname;
    @Bind(R.id.et_worknumber)
    com.etcomm.dcare.widget.ExEditText et_worknumber;
    @Bind(R.id.sex_switch)
    CheckBox sex_switch;
    @Bind(R.id.btn_enterusernamechooseavatarnext)
    Button btn_enterusernamechooseavatarnext;
    @Bind(R.id.iv_del_username1)
    ImageView iv_del_username1;
    @Bind(R.id.iv_del_username2)
    ImageView iv_del_username2;
    @Bind(R.id.iv_del_username3)
    ImageView iv_del_username3;
    @Bind(R.id.ll_jobnumber)
    LinearLayout ll_jobnumber;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    private Intent intent;
    private ArrayList<String> mMan = new ArrayList<String>();
    private ArrayList<String> mWeMen = new ArrayList<String>();
    private boolean isMan = true;
    private int selected = 0;
    private ScrollHorizontalScrollView scrollhsv;
    private ScrollHorizontalScrollViewAdapter mAdapter;

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

    @OnClick({R.id.iv_del_username1, R.id.iv_del_username2, R.id.iv_del_username3})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_del_username1:
                et_username.setText("");
                break;
            case R.id.iv_del_username2:
                et_nickyname.setText("");
                break;
            case R.id.iv_del_username3:
                et_worknumber.setText("");
                break;
        }
    }

    @OnTextChanged(value = R.id.et_username, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged_username(Editable s) {
        // TODO Auto-generated method stub
        if (s.length() > 0) {
            iv_del_username1.setVisibility(View.VISIBLE);
//            btn_enterusernamechooseavatarnext.setEnabled(true);
        } else {
//            btn_enterusernamechooseavatarnext.setEnabled(false);
            iv_del_username1.setVisibility(View.INVISIBLE);
        }
//        if (et_username.getText().toString().length() > 0 && et_nickyname.getText().toString().length() > 0) {
//            btn_enterusernamechooseavatarnext.setEnabled(true);
//        } else {
//            btn_enterusernamechooseavatarnext.setEnabled(false);
//        }
    }

    @OnTextChanged(value = R.id.et_nickyname, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged_et_nickyname(Editable s) {
        if (s.length() > 0) {
//            btn_enterusernamechooseavatarnext.setEnabled(true);
            iv_del_username2.setVisibility(View.VISIBLE);
        } else {
//            btn_enterusernamechooseavatarnext.setEnabled(false);
            iv_del_username2.setVisibility(View.INVISIBLE);
        }
//        if (et_username.getText().toString().length() > 0 && et_nickyname.getText().toString().length() > 0) {
//            btn_enterusernamechooseavatarnext.setEnabled(true);
//        } else {
//            btn_enterusernamechooseavatarnext.setEnabled(false);
//        }

    }

    @OnTextChanged(value = R.id.et_worknumber, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged_et_worknumber(Editable s) {
        if (et_username.getText().toString().length() == 0 && et_nickyname.getText().toString().length() == 0) {
//            btn_enterusernamechooseavatarnext.setEnabled(false);
        }
        if (s.length() > 0) {
            iv_del_username3.setVisibility(View.VISIBLE);
        } else {
            iv_del_username3.setVisibility(View.INVISIBLE);
        }
//        if (et_username.getText().toString().length() > 0 && et_nickyname.getText().toString().length() > 0) {
//            btn_enterusernamechooseavatarnext.setEnabled(true);
//        } else {
//            btn_enterusernamechooseavatarnext.setEnabled(false);
//        }
    }


    private void getDefaultAvator() {
        Map<String, String> params = new HashMap<String, String>();
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);

        Log.i(TAG, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetDefaultAvator, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(TAG, "post cancel" + this.getRequestURI());
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
                    // + response.getString("content").toString());
                    if (code == 0) {
                        List<AvatarContent> ava = JSON.parseArray(response.getString("content"), AvatarContent.class);
                        mMan.clear();
                        mWeMen.clear();
                        for (Iterator iterator = ava.iterator(); iterator.hasNext(); ) {
                            AvatarContent avatarContent = (AvatarContent) iterator.next();
                            if (avatarContent != null) {
                                if (avatarContent.getGender() == 1) {// 性别： 0：女
                                    // 1：男
                                    mMan.add(avatarContent.getAvatar());
                                    mAdapter = new ScrollHorizontalScrollViewAdapter(mContext, mMan);
                                    scrollhsv.initData(mAdapter);
                                } else {
                                    mWeMen.add(avatarContent.getAvatar());
                                }
                            }
                        }
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

    private void initview() {
        titlebar.setTitle("个人资料");
        titlebar.setLeftLl(backClickListener);
        if (SharePreferencesUtil.getIslevel(mContext)) {
            ll_jobnumber.setVisibility(View.GONE);
        } else {
            ll_jobnumber.setVisibility(View.VISIBLE);
        }
        scrollhsv = (ScrollHorizontalScrollView) findViewById(R.id.scrollhsv);
        scrollhsv.setHorizontalFadingEdgeEnabled(false);
        scrollhsv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onClick(int index) {
                if (isMan) {
                    intent.putExtra(Preferences.UserSex, 1);// //1男，2女
                    intent.putExtra(Preferences.Avator, mMan.get(index));
                    AppSharedPreferencesHelper.setAvatar(mMan.get(index));
                    AppSharedPreferencesHelper.setGender("1");
                } else {
                    intent.putExtra(Preferences.UserSex, 2);
                    intent.putExtra(Preferences.Avator, mWeMen.get(index));
                    AppSharedPreferencesHelper.setAvatar(mWeMen.get(index));
                    AppSharedPreferencesHelper.setGender("2");
                }

            }
        });
        btn_enterusernamechooseavatarnext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                nickname = et_nickyname.getText().toString();
                username = et_username.getText().toString();
                worknumber = et_worknumber.getText().toString();
//                String reg = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
//
//                Pattern pattern = Pattern.compile(reg);
//                pattern.matcher(reg);
//                if (!username.isEmpty()) {
//                    if (username.length() >= 2 && username.length() <= 10) {
//                    } else {
//                        showToast("姓名长度为2至10个字，请核实姓名");
//                        return;
//                    }
//                    if (!username.matches(reg)) {
//                        showToast("用户姓名只能为中英文、数字");
//                        return;
//                    }
//                } else {
//
//                }

                if (!username.isEmpty()) {
                    if (!checkName(username)) {
                        showToast("用户姓名只能为中英文、数字、下划线、中杠线、星号");
                        return;
                    }
                    if (username.length() >= 1 && username.length() <= 20) {
                    } else {
                        showToast("姓名长度为1至20个字，请核实姓名");
                        return;
                    }
                }


                if (!nickname.isEmpty()) {
                    if (!checkName(nickname)) {
                        showToast("用户昵称只能为中英文、数字、下划线、中杠线、星号");
                        return;
                    }
                    if (nickname.length() >= 1 && nickname.length() <= 10) {
                    } else {
                        showToast("昵称长度为1至10个字，请核实昵称");
                        return;
                    }
                }

                // 校验昵称是否重复
                if (nickname != null && nickname != "") {
                    editNickName("nick_name", nickname);
                }

            }
        });

        sex_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) { // 男
                    mAdapter = new ScrollHorizontalScrollViewAdapter(mContext, mMan);
                    scrollhsv.initData(mAdapter);
                    isMan = true;
                } else {
                    mAdapter = new ScrollHorizontalScrollViewAdapter(mContext, mWeMen);
                    scrollhsv.initData(mAdapter);
                    isMan = false;
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (scrollhsv != null) {
            scrollhsv.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        SharePreferencesUtil.saveInfoState(mContext, false);
        intent = getIntent();
        initview();
        getDefaultAvator();
        /*
        * 如果后台返回不为空，则显示后台返回值
        * */
        if (!AppSharedPreferencesHelper.getReal_name().isEmpty()) {
            et_username.setText(AppSharedPreferencesHelper.getReal_name());
        }
        if (!AppSharedPreferencesHelper.getNick_name().isEmpty()) {
            et_nickyname.setText(AppSharedPreferencesHelper.getNick_name());
        }
        if (!AppSharedPreferencesHelper.getJob_number().isEmpty()) {
            et_worknumber.setText(AppSharedPreferencesHelper.getJob_number());
        }

    }

    @Override
    protected int setLayoutId() {
        return R.layout.enterusernamechooseavatar;
    }

    private void editNickName(final String field, String value) {
        Map<String, String> params = new HashMap<String, String>();
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        params.put("type", "check");
        params.put(field, value);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyPost(Constants.RegisterUpdateInfo, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
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
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                cancelmDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.d(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        if (!worknumber.isEmpty()) {
                            if (worknumber.length() < 11) {
                                editJobNumnber("job_number", worknumber);
                            } else {
                                showToast("工号长度最多为10位");
                            }

                        } else {
                            intent.putExtra(Preferences.FirstSetUserInfo, true);
                            intent.putExtra(Preferences.UserNickName, nickname);
                            intent.putExtra(Preferences.USER_NAME, username);
                            intent.putExtra(Preferences.JOB_NUMBER, worknumber);
                            intent.setClass(mContext, SettingNewsActivity.class);
                            startActivity(intent);
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        exceptionCode(code);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
            }
        });

    }

    /*
    * 检验工号是否重复
    * */
    private void editJobNumnber(String field, String value) {
        Map<String, String> params = new HashMap<String, String>();
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        params.put("type", "check");
        params.put(field, value);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyPost(Constants.RegisterUpdateInfo, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
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
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                cancelmDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.d(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code != 0) {
                        exceptionCode(code);
                        finish();
                    }
                    if (code == 0) {
                        intent.putExtra(Preferences.FirstSetUserInfo, true);
                        intent.putExtra(Preferences.UserNickName, nickname);
                        intent.putExtra(Preferences.USER_NAME, username);
                        intent.putExtra(Preferences.JOB_NUMBER, worknumber);
                        intent.setClass(mContext, SettingNewsActivity.class);
                        startActivity(intent);
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
            }
        });

    }

    public static boolean checkName(String username) {
        boolean isMaches = true;
        for (int i = 0; i < username.length(); i++) {
            String reg = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
            Pattern pattern = Pattern.compile(reg);
            pattern.matcher(reg);
            String s = username.charAt(i) + "";
            if (s.matches(reg) || s.equals("*") || s.equals("_") || s.equals("-")) {
                isMaches = true;
            } else {
                isMaches = false;
                break;
            }
        }
        return isMaches;
    }
}

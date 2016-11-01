package com.etcomm.dcare.app.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.Login;
import com.etcomm.dcare.netresponse.StructureContent;
import com.etcomm.dcare.netresponse.StructureItems;
import com.etcomm.dcare.ormlite.bean.User;
import com.etcomm.dcare.ormlite.bean.UserDao;
import com.etcomm.dcare.service.StepDataUploadService;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * Enterprise Activation 企业激活码
 *
 * @author iexpressbox
 */
public class EnterpriseActivationActivity extends BaseActivity implements OnClickListener {

    /**
     * 邀请码
     */
    private TextInputLayout invitationCode;
    /**
     * 部门
     */
    private TextView regDepartmentId;
    /**
     * 手机号
     */
    private TextInputLayout registeredPhone;
    /**
     * 获取验证码
     */
    private Button getCode;
    /**
     * 填写验证码
     */
    private TextInputLayout verificationCode;
    /**
     * 密码
     */
    private TextInputLayout registerPassword;
    /**
     * 注册方式 true 为邮箱注册
     */
    protected boolean isEmail = false;
    /**
     * 传递信息
     */
    private Map<String, String> params;
    /***
     * 用户注册部门信息ID
     */
    private String department_info_id;


    // SystemTitle systemtitle;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    Button btn_enteractivationnext;
    // private Dialog dialog;
    protected String structurename;
    protected List<StructureItems> lists;
    private String deviceid;
    protected SharedPreferences defaultsp;

    @Override
    public void onResume() {
        super.onResume();
        /**
         * 非空判断证实有无选择部门
         */
        if (!SharePreferencesUtil.getDepartmentInfo(this).equals("")) {
            regDepartmentId.setText(SharePreferencesUtil.getDepartmentInfo(this));
            department_info_id = SharePreferencesUtil.getDepartmentInfoId(this);
            SharePreferencesUtil.saveDepartmentInfoId(this, "");
            SharePreferencesUtil.saveDepartmentInfo(this, "");
        }
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    private void initview() {
        titlebar.setTitle("注册");
        titlebar.setLeftLl(backClickListener);
        titlebar.setRightTextView(rightClickListener, "邮箱注册");
        invitationCode = (TextInputLayout) findViewById(R.id.invitationCode);
        regDepartmentId = (TextView) findViewById(R.id.regDepartmentId);
        registeredPhone = (TextInputLayout) findViewById(R.id.registeredPhone);
        verificationCode = (TextInputLayout) findViewById(R.id.verificationCode);
        registerPassword = (TextInputLayout) findViewById(R.id.registerPassword);
        getCode = (Button) findViewById(R.id.getCode);
        btn_enteractivationnext = (Button) findViewById(R.id.btn_enteractivationnext);
        initOnClickListener();
        initData();

    }

    /**
     * 多点击监听注册
     */
    private void initOnClickListener() {
        regDepartmentId.setOnClickListener(this);
        regDepartmentId.setClickable(false);
        btn_enteractivationnext.setOnClickListener(this);
        getCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_enteractivationnext:
                //完成注册
                nextToInfo();
                break;
            case R.id.getCode:
                //获取验证码
                getVerificationCode();
                break;
            case R.id.regDepartmentId:
                //选择部门
                Intent intent = new Intent(EnterpriseActivationActivity.this, ChooseComStructureActivity.class);
                intent.putExtra(Preferences.DEVICE_ID, deviceid);
                intent.putExtra(Preferences.ActivitionCode, invitationCode.getEditText().getText().toString());
                intent.putExtra(Preferences.ActivitionStructureCode, "0");
                intent.putExtra(Preferences.LastStructureName, structurename);
                intent.putExtra(Preferences.LastStructureItems, (Serializable) lists);
                startActivityForResult(intent, 0);
                break;
        }

    }

    /**
     * 填充数据及监听
     */
    private void initData() {
        initInvitationCode();
    }

    /**
     * 邀请码监听及错误提示
     */
    private void initInvitationCode() {
        final EditText editText = invitationCode.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //监听满6位进行说明邀请码输入完毕进行网络请求
                if (s.length() >= 6) {
                    getSubStructure();
                }

            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        String userCode = registeredPhone.getEditText().getText().toString();
        if (registeredPhone.getHint().toString().contains("邮箱")) {
            if (!userCode.contains("@")) {
                registeredPhone.setError("请输入有效的邮箱号");
                registeredPhone.setErrorEnabled(true);
                return;
            }else {
                registeredPhone.setError(null);
                registeredPhone.setErrorEnabled(false);
            }
        }
        if (registeredPhone.getHint().toString().contains("手机")) {
            if (userCode.trim().length() != 11) {
                registeredPhone.setError("请输入有效的手机号");
                registeredPhone.setErrorEnabled(true);
                return;
            }else {
                registeredPhone.setError(null);
                registeredPhone.setErrorEnabled(false);
            }
        }

        if (userCode != null && userCode.length() > 0) {
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
            params.put("receiver", userCode);
            cancelmDialog();
            showProgress(DIALOG_DEFAULT, true);
            Log.i(tag, "params: " + params.toString());
            DcareRestClient.volleyPost(url, params, new FastJsonHttpResponseHandler() {
                @Override
                public void onCancel() {
                    Log.w(tag, "post cancel" + this.getRequestURI());
                    cancelmDialog();
                    inputErrorShow(verificationCode, false, "请输入有效的验证码");
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
                            inputErrorShow(verificationCode, true, null);
//                            isCountTime = true;
//                            timer.start();
//                            getcode.setEnabled(false);
                        } else {// code不为0 发生异常
                            showToast(message);
                            inputErrorShow(verificationCode, false, "请输入有效的验证码");
                        }
                    } catch (JSONException e) {
                        Log.e(tag, "JSONException:");
                        e.printStackTrace();
                        inputErrorShow(verificationCode, false, "请输入有效的验证码");
                    }
                }
            });

        } else {
            showToast("用户名输入不正确");
        }
    }

    /**
     * 注册完成
     */
    private void nextToInfo(){
        String username = registeredPhone.getEditText().getText().toString().trim();
        String code = verificationCode.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            inputErrorShow(registeredPhone,false ,"请输入有效的手机号");
        }
        if (TextUtils.isEmpty(code)) {
            inputErrorShow(verificationCode, false, "请输入验证码");
            return;
        }
        if (code.length() != 4) {
            inputErrorShow(verificationCode, false, "验证码格式错误");
        }

        if (regDepartmentId.getText().equals("部门")) {
            showToast("请选择部门");
            return;
        }
        if (registeredPhone.getHint().toString().contains("邮箱")) {
            if (!username.contains("@")) {
                registeredPhone.setError("请输入有效的邮箱号");
                registeredPhone.setErrorEnabled(true);
                return;
            }else {
                registeredPhone.setError(null);
                registeredPhone.setErrorEnabled(false);
            }
        }
        if (registeredPhone.getHint().toString().contains("手机")) {
            if (username.trim().length() != 11) {
                registeredPhone.setError("请输入有效的手机号");
                registeredPhone.setErrorEnabled(true);
                return;
            }else {
                registeredPhone.setError(null);
                registeredPhone.setErrorEnabled(false);
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
                            inputErrorShow(verificationCode, true, null);
                            nextStep();
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
            return;
        }
    }

    /**
     * 综合验证 通过→个人信息完善
     */
    private void nextStep(){
        // TODO Auto-generated method stub
        final String username = registeredPhone.getEditText().getText().toString().trim();
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
            final String password = registerPassword.getEditText().getText().toString();
            if (TextUtils.isEmpty(password) || password.trim().length() < 6) {
                inputErrorShow(registerPassword, false, "请输入6-12位密码");
                return;
            }
            params.put("password", password);
            params.put("repeat_password", password);
            params.put("structure_id", department_info_id);
            params.put("sn", invitationCode.getEditText().getText().toString().trim());
            params.put("device_id", "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
            params.put("client_id", PushManager.getInstance().getClientid(getApplicationContext()));
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
                            defaultsp.edit().putString(Preferences.UserPassword, password).commit();
                            Login login = JSON.parseObject(response.getJSONObject("content").toString(), Login.class);
                            SharedPreferences sp = getSharedPreferences(Preferences.UserInfo, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
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
                            UserDao userDao = new UserDao(mContext);
                            List<User> userlist = userDao.getUserList();

                            boolean isExist = false;

                            if (!isExist) {
                                userDao.add(user);
                            }

                            SharePreferencesUtil.saveReData(mContext, true);
                            Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                            Log.i(tag, "Login : " + login.toString());
                            DcareApplication.getInstance().finishActivity(EnterpriseActivationActivity.this);
                            Intent intent = new Intent(EnterpriseActivationActivity.this, EnterUserNameChooseAvatarActivity.class);
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

    /**
     * 是否显示错误信息
     * 组件，true不显示 false 显示 ， 内容
     *
     */
    private void inputErrorShow(TextInputLayout textInputLayout,Boolean boo, String content) {
        if (textInputLayout != null) {
            if (boo) {
                textInputLayout.setError(content);
                textInputLayout.setErrorEnabled(false);
            }else {
                textInputLayout.setError(content);
                textInputLayout.setErrorEnabled(true);

            }
        }
    }

    /**
     * 企业邀请码效验
     */
    private void getSubStructure() {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(invitationCode.getEditText().getText().toString()) || invitationCode.getEditText().getText().toString().length() < 6) {
            inputErrorShow(invitationCode, false, "请输入6位有效的邀请码");
            return;
        }
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("serial_number", invitationCode.getEditText().getText().toString());
        params.put("parent_id", "0");
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.ActiveStructure, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                inputErrorShow(invitationCode, false, "请输入6位有效的邀请码");
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                inputErrorShow(invitationCode, false, "请输入6位有效的邀请码");
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelmDialog();

                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    StructureContent disscomment = JSON.parseObject(response.getString("content"), StructureContent.class);
                    if (code == 0) {
                        inputErrorShow(invitationCode, true, null);
                        if (disscomment.getIslevel().equals("1")) {
                            /**
                             * 部门信息写死在UI上 不允许更改
                             */
                            defaultsp.edit().putString("islevel", "1").commit();
                            SharePreferencesUtil.saveIslevel(mContext, true);
//                            Intent intent = new Intent(EnterpriseActivationActivity.this, RegisterActivity.class);
//                            intent.putExtra(Preferences.ActivitionStructureCode, );
//                            intent.putExtra(Preferences.DEVICE_ID, deviceid);
//                            intent.putExtra(Preferences.ActivitionCode, invitationCode.getEditText().getText().toString());
                            department_info_id = disscomment.getStructure().get(0).getStructure_id();
                            regDepartmentId.setText(disscomment.getStructure().get(0).getStructure());
                            regDepartmentId.setClickable(false);
//                            startActivity(intent);
                        } else {
                            /**
                             * 可供选择的二级或者多级部门分类
                             */
                            structurename = disscomment.getCustomer();
                            lists = disscomment.getStructure();
                            regDepartmentId.setClickable(true);
                        }


                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        inputErrorShow(invitationCode, false, "请输入6位有效的邀请码");
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                    inputErrorShow(invitationCode ,false ,"请输入6位有效的邀请码");
                }

            }
        });
    }


    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }


    @Override
    protected void initDatas() {
        defaultsp = getSharedPreferences(Preferences.CommonLoginInfo, MODE_PRIVATE);
        Intent intent = getIntent();
        deviceid = intent.getStringExtra(Preferences.DEVICE_ID);
        LogUtil.e("LogUtil", ">>>>>>>>>>>>>>>>>>>>>" + deviceid);
        initview();

    }

    /**
     * 邮箱注册
     */
    private OnClickListener rightClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (registeredPhone.getHint().toString().contains("邮箱")) {
                isEmail = true;
                registeredPhone.setHint("手机号");
                titlebar.setRightTextContent("手机注册");
            }else {
                isEmail = false;
                registeredPhone.setHint("邮箱号");
                titlebar.setRightTextContent("邮箱注册");
            }
        }
    };


    @Override
    protected int setLayoutId() {
        return R.layout.activity_enteractivation;
    }
}

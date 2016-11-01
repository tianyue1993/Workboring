package com.etcomm.dcare.app.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.etcomm.dcare.MyCollectionActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.login.LoginActivity;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.widget.ProgressDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public abstract class BaseActivity extends FragmentActivity {
    protected String TAG = getClass().getSimpleName();
    public String client_id = ""; // 推送SDK
    Intent intent;
    protected final int DIALOG_DEFAULT = 0;
    public static final int MSG_CLOSE_PROGRESS = 1;
    public static final int MSG_SHOW_TOAST = 2;
    protected String RES_CODE = "code";
    protected String RES_MESSAGE = "message";
    protected ProgressDialog mProgress;
    protected Gson gson = new Gson();
    public Handler baseHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_CLOSE_PROGRESS:
                    cancelmDialog();
                    break;
                case MSG_SHOW_TOAST:
                    int resid = msg.arg1;
                    if (resid > 0) {
                        showToast(resid);
                    } else if (msg.obj != null) {
                        String mes = (String) msg.obj;
                        if (!TextUtils.isEmpty(mes)) {
                            showToast(mes);
                        }
                    }
                    break;
            }
        }

        ;
    };

    Toast toast = null;

    protected void showToast(String message) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(this, (!TextUtils.isEmpty(message)) ? message : this.getString(R.string.network_error), Toast.LENGTH_SHORT);
        toast.show();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 2000);
    }

    protected void showToast(int resid) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(this, resid, Toast.LENGTH_SHORT);
        toast.show();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 2000);
    }

    /**
     * @param @param message
     * @param @param length 设定文件
     * @return void 返回类型
     * @throws
     * @Title: showToast
     * @Description: 居中Toast
     */
    protected void showToast(String message, int length) {
        Toast toast = Toast.makeText(this, message, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void showToast(int resid, int length) {
        Toast toast = Toast.makeText(this, resid, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void startAtvTask(Class cls) {
        DcareApplication.mCon.startActivity(setAtvTaskParameters(cls));
    }

    protected void startAtvTask(Class cls, String... str) {
        if (str.length < 2) {
            // Log.e(TAG, StringUtils.id2String(R.string.atv_parameter));
            showToast(R.string.atv_parameter);
        }
        DcareApplication.mCon.startActivity(setAtvTaskParameters(cls, str));
    }

    protected Intent setAtvTaskParameters(Class cl) {
        intent = new Intent(DcareApplication.mCon, cl);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        return intent;
    }

    protected Intent setAtvTaskParameters(Class cl, String... str) {
        // intent = new Intent(DcareApplication.mCon, cl);

        intent = setAtvTaskParameters(cl);
        for (int i = 0; i < str.length; i += 2) {
            intent.putExtra(str[i], str[i + 1]);
        }
        return intent;
    }

    /**
     * @param @param  atv
     * @param @return 设定文件
     * @return Activity 返回类型
     * @throws
     * @Title: setAtv
     * @Description:初始化绑定
     */
    protected abstract Activity atvBind();

    protected abstract void initDatas();

    protected abstract int setLayoutId();

    protected void initWiget() {
        ButterKnife.bind(atvBind());

    }

    ;

    private void _init() {
        setContentView(setLayoutId());
        initWiget();
        initDatas();
    }

    public void showProgress(int resId, boolean cancel) {
        mProgress = new ProgressDialog(this);
        if (resId <= 0) {
            mProgress.setMessage(R.string.loading_data, cancel);
        } else {
            mProgress.setMessage(resId, cancel);
        }
        mProgress.show();
    }

    public void cancelmDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    // -----------------------我是改动分割线------------------------------
    protected final String tag = getClass().getSimpleName();
    protected Context mContext;

    // protected String REQUEST_RESULT = "request_result";
    // protected String REQUEST_ERROR_CODE = "request_error_code";
    // protected String REQUEST_ERROR_MSG = "request_error_msg";
    // protected int LOAD_DATA_DELAY_TIME = 300;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DcareApplication.getInstance().addActivity(this);
        mContext = this;

        // //-----
        _init();
    }


    protected void backWithData(String k, String v) {
        Intent data = new Intent();
        data.putExtra(k, v);
        setResult(RESULT_OK, data);
    }

    protected void backToActivityBase(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null)
            intent.putExtras(bundle);
        intent.setClass(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    protected void showSoftKeyBoard(final View v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(v, 0);
            }
        }, 500);
    }


    protected View.OnClickListener backClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            backEvent();
        }
    };

    protected void backEvent() {
        Intent intent = new Intent(mContext, MyCollectionActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    protected synchronized void exceptionCode(int code) {
        switch (code) {
            case 10000:
                showToast(R.string.token_error);
                mContext.sendBroadcast(new Intent(Preferences.ACTION_USER_EXIT));
                finish();
//                startAtvTask(LoginActivity.class);
                Intent intent = new Intent(mContext, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:

                break;
        }
    }

    // 本方法判断自己些的一个Service-->com.android.controlAddFunctions.PhoneService是否已经运行
    public boolean isWorked() {
        ActivityManager myManager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals("com.etcomm.dcare.service.DcareService")) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BaseActivity.this.finish();
        }

        return false;

    }
}

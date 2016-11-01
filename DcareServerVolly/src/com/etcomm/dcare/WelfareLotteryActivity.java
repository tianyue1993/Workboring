package com.etcomm.dcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class WelfareLotteryActivity extends BaseActivity {
    String token = "";
    protected static final int GetUrl = 0;
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.webview)
    WebView webview;
    private String userurl;
    //	private LoadingDialog dialog;
    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GetUrl:
                    if (userurl == null) {
                        // geturlFromNet(0);//0user排名 1 部门排名
                        Toast.makeText(mContext, "抽奖地址获取失败", Toast.LENGTH_SHORT).show();
                    } else {
                        webview.loadUrl(userurl);
                    }
                    break;
                default:
                    break;
            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.i(tag, "福利抽奖 onCreate");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(tag, "福利抽奖 onStart");
    }


    private void geturlFromNet(int i) {
        // TODO Auto-generated method stub
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", token);
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//			dialog = null;
//		}
//		dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
//		dialog.setCancelable(false);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetWelfareLotteryUrl, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                // Toast.makeText(mContext, "修改失败，请检查网络连接",
                // Toast.LENGTH_SHORT).show();
                // super.onFailure(statusCode, headers, throwable,
                // errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response "
                        + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    // Log.i(tag, "onSuccess code: " + code + " message: " +
                    // message + "content: "
                    // + response.getString("content").toString());
                    if (code == 0) {
                        String content = response.getString("content");
                        userurl = content;
                        mHandler.sendEmptyMessage(GetUrl);
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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initDatas() {
        token = getIntent().getStringExtra("Token");
        titlebar.setTitle("福利抽奖");
        titlebar.setLeftLl(backClickListener);
        titlebar.setRightText("我的抽奖");
        titlebar.setRightClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(mContext, MyLotteryActivity.class);
//				startActivity(intent);
                startAtvTask(MyLotteryActivity.class, "Token", SharePreferencesUtil.getToken(mContext));
            }
        });
        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(tag, "url=" + url);
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // TODO Auto-generated method stub
                // super.onReceivedError(view, errorCode, description,
                // failingUrl);
                Log.e(tag, "onReceivedError errorCode:" + errorCode + " description:" + description + " failingUrl : "
                        + failingUrl);
                Toast.makeText(mContext, "网络异常，请求数据失败,请检查网络，稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(tag, "onAttachedToWindow");
        if (userurl == null) {
            geturlFromNet(0);// 0user排名 1 部门排名
        } else {
            webview.loadUrl(userurl);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_welfarelottery;
    }

}

package com.etcomm.dcare;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 记录趋势 包含每日2记录趋势以及每周计步趋势，可以选择日期，可以左右选择切换
 *
 * @author iexpressbox
 */
public class TrendRecordActivity extends BaseWebViewActivity {
    String token = "";

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
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


    @Override
    protected void initDatas() {
        token = getIntent().getStringExtra("Token");
        titlebar.setTitle("记录趋势");
        titlebar.setLeftLl(backClickListener);
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
                Log.e(tag, "onReceivedError errorCode:" + errorCode + " description:" + description + " failingUrl : " + failingUrl);
                Toast.makeText(mContext, "网络异常，请求数据失败,请检查网络，稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int setLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_trend;
    }

    protected static final int GetTrendUrl = 0;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    private String userurl;
    // private LoadingDialog dialog;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GetTrendUrl:
                    if (userurl == null) {
                        // geturlFromNet(0);//0user排名 1 部门排名
                        Toast.makeText(mContext, "地址获取失败", Toast.LENGTH_SHORT).show();
                    } else {
                        webview.loadUrl(userurl);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };


    @Override
    public void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        Log.i(tag, "onAttachedToWindow");
        if (userurl == null) {
            geturlFromNet(0);// 0user排名 1 部门排名
        } else {
            webview.loadUrl(userurl);
        }
    }

    private void geturlFromNet(int i) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", token);
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetTrendUrl, params, new FastJsonHttpResponseHandler() {
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
                Toast.makeText(mContext, R.string.network_conn_error, Toast.LENGTH_SHORT).show();
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
                        return;
                    }
                    if (code == 0) {
                        String content = response.getString("content");
                        userurl = content;
                        mHandler.sendEmptyMessage(GetTrendUrl);
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
}

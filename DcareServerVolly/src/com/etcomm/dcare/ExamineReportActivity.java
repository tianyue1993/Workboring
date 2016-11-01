package com.etcomm.dcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.common.DcareApplication;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 体检报告
 *
 * @author iexpressbox
 */
public class ExamineReportActivity extends BaseWebViewActivity {
    String token = "";


    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }


    @Override
    protected void initDatas() {
        token = getIntent().getStringExtra("Token");
        // TODO Auto-generated method stub
        titlebar.setTitle("体检报告");
        titlebar.setLeftLl(backClickListener);
        WebSettings webSettings = webview.getSettings();
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
                neterrorloadLocalPage(webview);
                Toast.makeText(mContext, "网络异常，请求数据失败,请检查网络，稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });
        if (url != null) {
            if (!StringUtils.isEmpty(url)) {
                webview.loadUrl(url);
            }
        } else {
            getUrl();
        }
    }


    @Override
    protected int setLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_examinereport;
    }

    protected static final int GetUrl = 1;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.webview)
    WebView webview;
    private String url;
    //	private LoadingDialog dialog;
    @SuppressLint("HandlerLeak")
    protected Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GetUrl:
                    if (url == null) {
                        Toast.makeText(mContext, "用户排名地址获取失败", Toast.LENGTH_SHORT).show();
                    } else {
                        webview.loadUrl(url);
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };


    private void getUrl() {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<String, String>();
        LogUtil.e(tag, "SharePreferencesUtil>>>" + SharePreferencesUtil.getToken(DcareApplication.mCon) + "token>>>" + token);
        params.put("access_token", token);

        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetPhysicalReportUrl, params, new FastJsonHttpResponseHandler() {
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
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response "
                        + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(TAG, response.toString());
                    if (code == 0) {
                        String content = response.getString("content");
                        url = content;
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }
}

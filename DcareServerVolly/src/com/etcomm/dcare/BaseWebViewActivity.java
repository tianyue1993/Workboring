package com.etcomm.dcare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.etcomm.dcare.app.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("JavascriptInterface")
public abstract class BaseWebViewActivity extends BaseActivity {

    WebView webview;


    public class DcareJavaScriptInterface {
        private Context mContext;

        public DcareJavaScriptInterface(Context mContext) {
            this.mContext = mContext;
        }

        public void onpicclick() {
            Log.i(tag, "onpicclick");
            if (webview != null) {
                webview.reload();
            } else {

            }
            Log.i(tag, "onpicclick");
        }
    }

    public void neterrorloadLocalPage(WebView webview) {

        webview.loadUrl("file:///android_asset/html/tips.html");//, html, mimeType, encoding, "");
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                // TODO Auto-generated method stub
                Log.i(tag, message);
                result.confirm();
                return true;
            }
        });
        webview.addJavascriptInterface(new DcareJavaScriptInterface(mContext), "dcare");
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

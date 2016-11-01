package com.etcomm.dcare;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.netresponse.CompanyWelfareItems;
import com.etcomm.dcare.netresponse.SuggestItems;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * 公司福利
 *
 * @author iexpressbox
 */
public class CompanyWelfareActivity extends BaseWebViewActivity {

    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.iv_rocket)
    ImageView iv_rocket;
    private Bundle bundle;
    private SuggestItems mInfo;
    private CompanyWelfareItems mWelfare;
    private boolean isFromFindWelfare;
    TranslateAnimation animation;

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }


    @Override
    protected void initDatas() {
        animation = new TranslateAnimation(0, 0, 500, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(600);//设置动画持续时间
        animation.setRepeatCount(60);//重复次数
        animation.setRepeatMode(Animation.REVERSE);//反向执行
        animation.setStartOffset(0);//设置启动时间
        Intent intent = getIntent();
        if (intent != null) {
            isFromFindWelfare = intent.getBooleanExtra(Preferences.FindWelfareDetail, false);
            bundle = intent.getExtras();
            if (bundle != null) {
                if (isFromFindWelfare) {
                    mWelfare = (CompanyWelfareItems) bundle.getSerializable(Preferences.HealthNewsDetail);
                } else {
                    mInfo = (SuggestItems) bundle.getSerializable(Preferences.SuggestSportDetail);
                }
                if (mInfo != null) {
                    Log.i(tag, "mInfo:  " + mInfo.toString());
                }
                if (mWelfare != null) {
                    Log.i(tag, "mWelfare:  " + mWelfare.toString());
                }
            }
        }
        // TODO Auto-generated method stub
        titlebar.setTitle("公司福利");
        titlebar.setLeftLl(backClickListener);
        Log.i(tag, "公司福利公司福利公司福利公司福利");
        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webview.getSettings().setAppCacheEnabled(true);//是否使用缓存
        webview.getSettings().setDomStorageEnabled(true);//DOM Storage
        webview.setWebViewClient(new MyWebview());
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        if (mInfo != null) {
            if (!StringUtils.isEmpty(mInfo.getDetail_url())) {

                webview.loadUrl(mInfo.getDetail_url());
            }
        }
        if (mWelfare != null) {
            if (!StringUtils.isEmpty(mWelfare.getDetail_url())) {

                webview.loadUrl(mWelfare.getDetail_url());
            }
        }
        // sport_detail
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_companywelfare;
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

    public class MyWebview extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            iv_rocket.setVisibility(View.GONE);
            iv_rocket.clearAnimation();
            iv_rocket.invalidate();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            iv_rocket.setVisibility(View.VISIBLE);
            iv_rocket.startAnimation(animation);

        }

    }
}

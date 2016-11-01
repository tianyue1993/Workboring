package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

public class ActivityRanktActivity extends BaseWebViewActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.iv_rocket)
    ImageView iv_rocket;

    TranslateAnimation animation;
    String url;

    @Override
    protected Activity atvBind() {
        return this;
    }


    @Override
    protected void initDatas() {
        animation = new TranslateAnimation(0, 0, 500, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(6000);//设置动画持续时间
        animation.setRepeatCount(60);//重复次数
        animation.setRepeatMode(Animation.REVERSE);//反向执行
        animation.setStartOffset(0);//设置启动时间
        Intent intent = getIntent();
        url = intent.getStringExtra("activity_rank");
        titlebar.setTitle("活动排名");
        titlebar.setLeftLl(backClickListener);

        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webview.setWebViewClient(new MyWebview());
        if (url != null) {
            webview.loadUrl(url);
        }


    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_healthconsult;
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
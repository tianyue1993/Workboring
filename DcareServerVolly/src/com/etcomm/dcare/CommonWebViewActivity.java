package com.etcomm.dcare;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.activity.SharetoGroupActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class CommonWebViewActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.iv_rocket)
    ImageView iv_rocket;
    private String url;
    private Intent intent;
    private String title;
    TranslateAnimation animation;
    boolean isShare = true;

    @Override
    protected Activity atvBind() {
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
        intent = getIntent();
        title = intent.getStringExtra(Preferences.CommonWebViewTitle);
        url = intent.getStringExtra(Preferences.CommonWebViewUrl);
        titlebar.setTitle(title);
        titlebar.setLeftLl(backClickListener);
        titlebar.setRightImage(R.drawable.ic_more);
        titlebar.setRightClick(new View.OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
                                       if (isShare) {
                                           new ShareAction(CommonWebViewActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).addButton("sharetogroup", "sharetogroup", "logo", "logo").
                                                   setShareboardclickCallback(new ShareBoardlistener() {
                                                                                  @Override
                                                                                  public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {


                                                                                      if (snsPlatform.mShowWord.equals("sharetogroup")) {
                                                                                          // 分享
                                                                                          intent.setClass(mContext, SharetoGroupActivity.class);
                                                                                          intent.putExtra("type", "activity");
                                                                                          startActivity(intent);
                                                                                      } else {
                                                                                          UMImage image = new UMImage(mContext, intent.getStringExtra("image"));
                                                                                          new ShareAction(CommonWebViewActivity.this).withTitle(intent.getStringExtra("topic_name")).withText(intent.getStringExtra("discuse")).withTargetUrl(intent.getStringExtra("url")).withMedia(image).setPlatform(share_media).setCallback(umShareListener).share();
                                                                                      }

                                                                                  }

                                                                              }

                                                   ).open();
                                       }


                                   }
                               }

        );
        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        Log.i(tag, "公司福利公司福利公司福利公司福利");
//        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webview.getSettings().setAppCacheEnabled(true);//是否使用缓存
        webview.getSettings().setDomStorageEnabled(true);//DOM Storage
        webview.setWebViewClient(new MyWebview());

        if (url != null) {
            webview.loadUrl(url);
        } else {
            Toast.makeText(mContext, "链接错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_commonwebview;
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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // name : id Url拦截处理
            String str = url;
            if (StringUtils.startsWithIgnoreCase(url, "etcomm")) {
                try {
                    String[] flag = url.split("\\:");
                    String[] content = flag[1].split("\\|");
                    Intent intent = new Intent(CommonWebViewActivity.this, TopicDisscussListActivity.class);
                    intent.putExtra("topic_id", content[1]);
                    intent.putExtra("topic_name", URLDecoder.decode(content[0], "utf-8"));
                    intent.putExtra("user_id", "");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(CommonWebViewActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommonWebViewActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(CommonWebViewActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(CommonWebViewActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 判断资讯是否存在，是否可分享
     */
    private void getIfcanshare() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("activity_id", intent.getStringExtra("topic_id"));
        params.put("type", "check");
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.ACTIVITY_SHARE_DROUP, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_conn_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                cancelmDialog();
                int code = response.getInteger("code");
                String message = response.getString("message");
                Log.i(tag, "onSuccess  code: " + code + " message: " + message);
                if (code != 0) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    isShare = false;
                }
            }

        });
    }

}

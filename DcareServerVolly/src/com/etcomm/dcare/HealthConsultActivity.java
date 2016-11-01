package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.DisscussItems;
import com.etcomm.dcare.netresponse.HealthNewsItems;
import com.etcomm.dcare.netresponse.SuggestItems;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 健康咨讯详情页面 两个进入途径 1 从推荐进来 2 从发现 的健康咨讯进入 使用isFromSuggest 区分
 * 点赞和收藏都是H5界面的，无需自己做接口，做界面
 *
 * @author iexpressbox
 */
public class HealthConsultActivity extends BaseWebViewActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.iv_rocket)
    ImageView iv_rocket;

    private Bundle bundle;
    private SuggestItems mSuggestInfo;
    private HealthNewsItems mSuggestFromCollectInfo;
    private boolean isFromSuggest;
    private boolean isFromMyCollect;
    private boolean isFromTopic;
    private HealthNewsItems mHealthNewsInfo;
    private DisscussItems mTopicInfo;
    private String id = "";
    TranslateAnimation animation;

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
        if (intent != null) {
            isFromSuggest = intent.getBooleanExtra("IsFromSuggest", false);
            isFromMyCollect = intent.getBooleanExtra("isFromMyCollect", false);
            isFromTopic = intent.getBooleanExtra("IsFromTopic", false);
            bundle = intent.getExtras();
            if (bundle != null) {
                if (isFromSuggest) {
                    mSuggestInfo = (SuggestItems) bundle.getSerializable(Preferences.SuggestSportDetail);
                    id = mSuggestInfo.getType_id();
                    Log.e(TAG, "mSuggestInfo====" + mSuggestInfo.toString());
                } else if (isFromMyCollect) {
                    mSuggestFromCollectInfo = (HealthNewsItems) bundle.getSerializable(Preferences.HealthNewsDetail);
                    id = mSuggestFromCollectInfo.getNews_id();
                } else if (isFromTopic) {
                    mTopicInfo = (DisscussItems) bundle.getSerializable(Preferences.HealthNewsDetail);
                    id = mTopicInfo.getShare_id();
                } else {
                    mHealthNewsInfo = (HealthNewsItems) bundle.getSerializable(Preferences.HealthNewsDetail);
                    id = mHealthNewsInfo.getNews_id();
                }


            }
        }
        titlebar.setTitle("资讯中心");
        titlebar.setLeftLl(backClickListener);
        titlebar.setRightImage(R.drawable.ic_more);
        titlebar.setRightClick(new OnClickListener() {

                                   @Override
                                   public void onClick(View v) {
                                       getIfcanshare();
                                       if (SharePreferencesUtil.getIfShare(mContext)) {
                                           new ShareAction(HealthConsultActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).addButton("sharetogroup", "sharetogroup", "logo", "logo").
                                                   setShareboardclickCallback(new ShareBoardlistener() {
                                                                                  @Override
                                                                                  public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {


                                                                                      if (snsPlatform.mShowWord.equals("sharetogroup")) {
                                                                                          // 分享
                                                                                          Intent intent = new Intent(mContext, SharetoGroupActivity.class);
                                                                                          intent.putExtras(bundle);
                                                                                          intent.putExtra("type", tag);
                                                                                          startActivity(intent);
                                                                                      } else {

                                                                                          if (mSuggestInfo != null) {
                                                                                              UMImage image = new UMImage(mContext, mSuggestInfo.getImage());
                                                                                              new ShareAction(HealthConsultActivity.this).withMedia(image).withTitle(mSuggestInfo.getTitle()).withText(mSuggestInfo.getDesc()).withTargetUrl(mSuggestInfo.getShare_url()).setPlatform(share_media).setCallback(umShareListener).share();

                                                                                          } else if (mHealthNewsInfo != null) {
                                                                                              UMImage image = new UMImage(mContext, mHealthNewsInfo.getImage());
                                                                                              new ShareAction(HealthConsultActivity.this).withMedia(image).withTitle(mHealthNewsInfo.getTitle()).withText(mHealthNewsInfo.getDesc()).withTargetUrl(mHealthNewsInfo.getShare_url()).setPlatform(share_media).setCallback(umShareListener).share();

                                                                                          } else if (mSuggestFromCollectInfo != null) {
                                                                                              UMImage image = new UMImage(mContext, mSuggestFromCollectInfo.getImage());
                                                                                              new ShareAction(HealthConsultActivity.this).withTitle(mSuggestFromCollectInfo.getTitle()).withText(mSuggestFromCollectInfo.getDesc()).withTargetUrl(mSuggestFromCollectInfo.getShare_url()).withMedia(image).setPlatform(share_media).setCallback(umShareListener).share();
                                                                                          } else {
                                                                                              UMImage image = new UMImage(mContext, mTopicInfo.getPhotos().get(0).getThumb_image());
                                                                                              new ShareAction(HealthConsultActivity.this).withTitle(mTopicInfo.getTitle()).withText(mTopicInfo.getContent()).withTargetUrl(mTopicInfo.getShare_url()).withMedia(image).setPlatform(share_media).setCallback(umShareListener).share();

                                                                                          }
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
        webview.setWebViewClient(new MyWebview());
        if (mSuggestInfo != null)

        {
            webview.loadUrl(mSuggestInfo.getDetail_url());

        } else if (mHealthNewsInfo != null)

        {
            webview.loadUrl(mHealthNewsInfo.getDetail_url());
        } else if (mSuggestFromCollectInfo != null)

        {
            webview.loadUrl(mSuggestFromCollectInfo.getDetail_url());
        } else

        {
            webview.loadUrl(mTopicInfo.getDetail_url());
        }


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

    @Override
    protected int setLayoutId() {
        return R.layout.activity_healthconsult;
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(HealthConsultActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HealthConsultActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(HealthConsultActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(HealthConsultActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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

    /**
     * 判断资讯是否存在，是否可分享
     */
    private void getIfcanshare() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("news_id", id + "");
        params.put("type", "check");
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.SHARE_GROUP, params, new FastJsonHttpResponseHandler() {
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
                } else {
                    SharePreferencesUtil.saveIfShare(mContext, true);
                }
            }

        });
    }
}
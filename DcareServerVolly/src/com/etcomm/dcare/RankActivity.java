package com.etcomm.dcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.util.DensityUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class RankActivity extends BaseWebViewActivity {
    protected static final int GetUserRankUrl = 0;
    protected static final int GetStructureRankUrl = 1;
    @Bind(R.id.leftimage)
    ImageView leftimage;
    @Bind(R.id.title_right_iv)
    ImageView title_right_iv;
    @Bind(R.id.leftindicator)
    View leftindicator;
    @Bind(R.id.rightindicator)
    View rightindicator;
    @Bind(R.id.around_tab_attationed)
    TextView around_tab_attationed;
    @Bind(R.id.around_tab_notattationed)
    TextView around_tab_notattationed;
    @Bind(R.id.webview)
    WebView webview;
    @Bind(R.id.person_rank)
    RelativeLayout person_rank;
    @Bind(R.id.whole_rank)
    RelativeLayout whole_rank;

    private String token;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_rank;
    }

    @Override
    protected Activity atvBind() {
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
        Intent intent = getIntent();
        if (intent.getBooleanExtra("islevel", false)) {
            whole_rank.setVisibility(View.GONE);
            person_rank.setVisibility(View.VISIBLE);
        } else {
            whole_rank.setVisibility(View.VISIBLE);
            person_rank.setVisibility(View.GONE);
        }
        token = intent.getStringExtra("token");
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
        title_right_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startAtvTask(PointsColleketDetailActivity.class, "Token", SharePreferencesUtil.getToken(mContext));
            }
        });
        around_tab_attationed.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                around_tab_notattationed.setBackground(null);
                around_tab_notattationed.setTextColor(Color.parseColor("#aba8a8"));
                around_tab_attationed.setTextColor(Color.parseColor("#ffffff"));
                if (curTab == 0) {

                } else {
                    if (animation != null) {
                        animation.cancel();
                        animation = null;
                    }
                    animation = new TranslateAnimation(0, -DensityUtil.dip2px(mContext, 80), 0, 0);
                    animation.setInterpolator(new AccelerateInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            leftindicator.setVisibility(View.VISIBLE);
                            rightindicator.setVisibility(View.INVISIBLE);
                            around_tab_attationed.setBackground(null);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            around_tab_attationed.setBackgroundResource(R.drawable.arout_tab_choosed_bg);
                            leftindicator.setVisibility(View.INVISIBLE);
                            rightindicator.setVisibility(View.INVISIBLE);
                        }
                    });
                    rightindicator.startAnimation(animation);
                }
                curTab = 0;
                if (userurl == null) {
                    geturlFromNet(0);
                } else {
                    mHandler.sendEmptyMessage(GetUserRankUrl);
                }
            }
        });
        around_tab_notattationed.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                around_tab_attationed.setBackground(null);
                around_tab_notattationed.setTextColor(Color.parseColor("#ffffff"));
                around_tab_attationed.setTextColor(Color.parseColor("#aba8a8"));// /aba8a8
                // //656363
                if (curTab == 0) {
                    if (animation != null) {
                        animation.cancel();
                        animation = null;
                    }
                    animation = new TranslateAnimation(0, DensityUtil.dip2px(mContext, 80), 0, 0);
                    ;
                    animation.setInterpolator(new AccelerateInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            rightindicator.setVisibility(View.VISIBLE);
                            leftindicator.setVisibility(View.INVISIBLE);
                            around_tab_notattationed.setBackground(null);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            around_tab_notattationed.setBackgroundResource(R.drawable.arout_tab_choosed_bg);
                            leftindicator.setVisibility(View.INVISIBLE);
                            rightindicator.setVisibility(View.INVISIBLE);
                        }
                    });
                    leftindicator.startAnimation(animation);
                } else {

                }
                curTab = 1;
                if (structureurl == null) {
                    geturlFromNet(1);
                } else {
                    mHandler.sendEmptyMessage(GetStructureRankUrl);
                }
            }
        });

    }


    /**
     * tab 指示器，默认是已关注为0
     */
    protected int curTab = 0;
    String userurl;
    String structureurl;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GetUserRankUrl:
                    if (userurl == null) {
                        Toast.makeText(mContext, "用户排名地址获取失败", Toast.LENGTH_SHORT).show();
                    } else {
                        webview.loadUrl(userurl);
                    }
                    break;
                case GetStructureRankUrl:
                    if (userurl == null) {
                        Toast.makeText(mContext, "组织机构排名地址获取失败", Toast.LENGTH_SHORT).show();
                    } else {
                        webview.loadUrl(structureurl);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };
    protected TranslateAnimation animation;

    @SuppressLint("SetJavaScriptEnabled")
    @OnClick(R.id.leftimage)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftimage:
                backEvent();
                break;

            default:
                break;
        }
    }

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

    private void geturlFromNet(final int usrstructure) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", token);
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        String httpurl;
        if (usrstructure == 0) {
            httpurl = Constants.GetUserRankUrl;
        } else {
            httpurl = Constants.GetStructureRankUrl;
        }
        DcareRestClient.volleyGet(httpurl, params, new FastJsonHttpResponseHandler() {
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
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                cancelmDialog();
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    if (code == 0) {
                        String content = response.getString("content");
                        if (usrstructure == 0) {
                            userurl = content;
                            mHandler.sendEmptyMessage(GetUserRankUrl);
                        } else {
                            structureurl = content;
                            mHandler.sendEmptyMessage(GetStructureRankUrl);
                        }
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

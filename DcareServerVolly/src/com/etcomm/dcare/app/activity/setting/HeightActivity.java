package com.etcomm.dcare.app.activity.setting;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.util.DensityUtil;
import com.etcomm.dcare.widget.ObservableScrollView;
import com.etcomm.dcare.widget.ObservableScrollView.OnScrollStopListner;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择身高
 *
 * @author iexpressbox
 */
@SuppressLint("NewApi")
public class HeightActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.height_value)
    TextView height_value;
    @Bind(R.id.userimage)
    ImageView userimage;
    @Bind(R.id.height_scrollview)
    ObservableScrollView height_scrollview;
    @Bind(R.id.height_scrollview_ll)
    RelativeLayout height_scrollview_ll;
    @Bind(R.id.btn_next)
    Button btn_next;
    private Intent intent;
    private boolean isFirstSetUserInfo;
    private String height = "160";
    private int usersex = 0;


    @Override
    public void onResume() {
        super.onResume();
        final int h;
        if (!StringUtils.isEmpty(height)) {
            h = Integer.parseInt(height);

        } else {
            h = 160;
        }
        height_value.setText("" + h);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                height_scrollview.smoothScrollTo(0, DensityUtil.dip2px(mContext, (float) ((230 - h) * 7)));
            }
        }, 1 * 500);
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    private Handler handler = new Handler() {

    };

    @OnClick(R.id.btn_next)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:

                if (isFirstSetUserInfo) {
                    intent.putExtra(Preferences.UserHeight, height_value.getText().toString());
                    intent.setClass(mContext, SettingSportGoalActivity.class);
                    startActivity(intent);
                } else {
                    backWithData(Preferences.SelectHeight, height_value.getText().toString());
                    finish();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    ////115   ---  235     初始232    总长1200dp
    @Override
    protected void initDatas() {
        intent = getIntent();
        height = intent.getStringExtra(Preferences.UserHeight);
//		height = AppSharedPreferencesHelper.getHeight();
        isFirstSetUserInfo = intent.getBooleanExtra(Preferences.FirstSetUserInfo, false);
        usersex = intent.getIntExtra(Preferences.UserSex, 1);////1男，2女)  0默认
        if (AppSharedPreferencesHelper.getGender().equals("1")) {
            if (Build.VERSION.SDK_INT >= 21) {
                userimage.setImageDrawable(getDrawable(R.drawable.staffman));
            } else {
                userimage.setImageDrawable(getResources().getDrawable(R.drawable.staffman));
            }
            if (isFirstSetUserInfo)
                height = "170";
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                userimage.setImageDrawable(getDrawable(R.drawable.staffweman));
            } else {
                userimage.setImageDrawable(getResources().getDrawable(R.drawable.staffweman));
            }
            if (isFirstSetUserInfo)
                height = "160";
        }
        if (isFirstSetUserInfo) {
            titlebar.setTitle("个人资料");
            titlebar.setLeftLl(backClickListener);
            btn_next.setText("下一步");
        } else {
            titlebar.setTitle("身高");
            titlebar.setLeftLl(backClickListener);
        }
        height_scrollview.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    height_scrollview.startScrollerTask();
                }
                return false;
            }
        });
        height_scrollview.setOnScrollStopListner(new OnScrollStopListner() {
            public void onScrollChange(int index) {//0  --3500   1000dp   10dp=1cm
                Log.i(tag, "index: " + index + " dp " + DensityUtil.px2dip(mContext, index));
                if (index <= 0) {
                    int value = DensityUtil.px2dip(mContext, index);
                    height_value.setText((230 - (int) (value / 7)) + "");
                } else {
                    int value = DensityUtil.px2dip(mContext, index);
                    height_value.setText((230 - (int) (value / 7)) + "");
                }
            }
        });
        Log.i(tag, "height_scrollview  Height: " + height_scrollview.getHeight());


//		smoothScrollTo

    }

    @Override
    protected int setLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_height;
    }
}

package com.etcomm.dcare.app.activity.setting;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.util.DensityUtil;
import com.etcomm.dcare.widget.ObservableHorizontalScrollView;
import com.etcomm.dcare.widget.ObservableHorizontalScrollView.OnScrollStopListner;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 选择体重
 *
 * @author iexpressbox
 */
//@EActivity(R.layout.activityweight)
public class WeightActivity extends BaseActivity {
    ObservableHorizontalScrollView weight_scrollview;
    LinearLayout weight_scrollview_ll;
    RelativeLayout weight_container;
    ImageView userinfo_staff;
    TextView weight_value;
    SimpleTitleBar titlebar;
    Button btn_next;
    private Intent intent;
    private boolean isFirstSetUserInfo;
    private int usersex = 0;
    private String weight = "65";

    @Override
    public void onResume() {
        super.onResume();
        final int w;
        if (!StringUtils.isEmpty(weight)) {
            w = Integer.parseInt(weight);

        } else {
            w = 60;
        }
        weight_value.setText("" + w);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                weight_scrollview.smoothScrollTo(DensityUtil.dip2px(mContext, (float) ((w - 25) * 11.28 + 6)), 0);
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

    @Override
    protected Activity atvBind() {
        return this;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initDatas() {

        intent = getIntent();
        isFirstSetUserInfo = intent.getBooleanExtra(Preferences.FirstSetUserInfo, false);
        weight_value = (TextView) findViewById(R.id.weight_value);
        userinfo_staff = (ImageView) findViewById(R.id.userinfo_staff);
        btn_next = (Button) findViewById(R.id.btn_next);
        titlebar = (SimpleTitleBar) findViewById(R.id.titlebar);
        weight_scrollview = (ObservableHorizontalScrollView) findViewById(R.id.weight_scrollview);
        /////??????/////
//		AppSharedPreferencesHelper.get
//		AppSharedPreferencesHelper.getGender().equals("1") ? "男" : "女"
//		usersex = intent.getIntExtra(Constant.UserSex, 0);////1男，2女)  0默认
        if (AppSharedPreferencesHelper.getGender().equals("1")) {
//			userinfo_staff.setImageResource(R.drawable.staffman);
            if (Build.VERSION.SDK_INT >= 21) {
                userinfo_staff.setImageDrawable(getDrawable(R.drawable.staffman));
            } else {
                userinfo_staff.setImageDrawable(getResources().getDrawable(R.drawable.staffman));
            }
            if (isFirstSetUserInfo)
                weight = "65";
        } else {
//			userinfo_staff.setImageResource(R.drawable.staffweman);
            if (Build.VERSION.SDK_INT >= 21) {
                userinfo_staff.setImageDrawable(getDrawable(R.drawable.staffweman));
            } else {
                userinfo_staff.setImageDrawable(getResources().getDrawable(R.drawable.staffweman));
            }
            if (isFirstSetUserInfo)
                weight = "50";
        }
        if (isFirstSetUserInfo) {
            titlebar.setTitle("个人资料");
            btn_next.setText("下一步");
            titlebar.setLeftLl(backClickListener);
        } else {
            titlebar.setTitle("体重");
            titlebar.setLeftLl(backClickListener);
            if (intent.hasExtra(Preferences.UserWeight)) {
                weight = intent.getStringExtra(Preferences.UserWeight);
            }
        }
        weight_scrollview.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    weight_scrollview.startScrollerTask();
                }
                return false;
            }
        });
        weight_scrollview.setOnScrollStopListner(new OnScrollStopListner() {
            public void onScrollChange(int index) {
                Log.i(tag, "index: " + index + " dp " + DensityUtil.px2dip(mContext, index));
                if (index <= 0) {
//					weight_value.setText("2");
                    int value = DensityUtil.px2dip(mContext, index);
                    weight_value.setText((int) (value / 11.28 + 25) + ""); //33
                } else {
                    int value = DensityUtil.px2dip(mContext, index);
                    weight_value.setText((int) (value / 11.28 + 25) + ""); //33
                }
            }
        });
        btn_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isFirstSetUserInfo) {
                    intent.setClass(mContext, HeightActivity.class);
                    intent.putExtra(Preferences.UserWeight, weight_value.getText().toString());
                    startActivity(intent);
                } else {
                    backWithData(Preferences.SelectWeight, weight_value.getText().toString());
                    finish();
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_weight;
    }
}

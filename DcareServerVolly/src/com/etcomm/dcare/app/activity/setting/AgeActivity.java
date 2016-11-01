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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.util.DensityUtil;
import com.etcomm.dcare.widget.ObservableHorizontalScrollView;
import com.etcomm.dcare.widget.ObservableHorizontalScrollView.OnScrollStopListner;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择年龄
 *
 * @author iexpressbox
 */
public class AgeActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.age_value)
    TextView age_value;
    @Bind(R.id.userinfo_staff)
    ImageView userinfo_staff;
    @Bind(R.id.age_scrollview)
    ObservableHorizontalScrollView age_scrollview;
    @Bind(R.id.age_scrollview_ll)
    LinearLayout age_scrollview_ll;
    @Bind(R.id.btn_next)
    Button btn_next;
    private Intent intent;
    private boolean isFirstSetUserInfo;
    private String agestr = "1990";
    private int age = 1990;
    private Handler handler = new Handler() {
    };
    private int usersex = 0;

    @OnClick({R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (isFirstSetUserInfo) {
                    intent.putExtra(Preferences.UserAge, age_value.getText().toString());
                    intent.setClass(mContext, WeightActivity.class);
                    startActivity(intent);
                } else {
                    backWithData(Preferences.SelectAge, age_value.getText().toString());
                    finish();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                int scr = DensityUtil.dip2px(mContext, (float) ((age - 1930) * 9.5));
                age_scrollview.smoothScrollTo(scr, 0);/// DensityUtil.dip2px(mContext,
                /// (232-h)*10)
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


    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initDatas() {
        intent = getIntent();
        isFirstSetUserInfo = intent.getBooleanExtra(Preferences.FirstSetUserInfo, false);
        usersex = intent.getIntExtra(Preferences.UserSex, 0);////1男，2女)  0默认
        if (!isFirstSetUserInfo) {
            agestr = intent.getStringExtra(Preferences.UserAge);
            if (agestr != null && agestr.contains("-")) {
                String[] ags = agestr.split("-");
                age = Integer.parseInt(ags[0]);
                if (age == 0) {
                    age = 1990;
                }
            } else {
                age = Integer.parseInt(agestr);
            }
            Log.i(tag, "age:" + age);
        }
        if (AppSharedPreferencesHelper.getGender().equals("1")) {
            if (Build.VERSION.SDK_INT >= 21) {
                userinfo_staff.setImageDrawable(getDrawable(R.drawable.staffman));
            } else {
                userinfo_staff.setImageDrawable(getResources().getDrawable(R.drawable.staffman));
            }
            if (isFirstSetUserInfo) {
                age = 1980;
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                userinfo_staff.setImageDrawable(getDrawable(R.drawable.staffweman));
            } else {
                userinfo_staff.setImageDrawable(getResources().getDrawable(R.drawable.staffweman));
            }
            if (isFirstSetUserInfo) {
                age = 1980;
            }
        }
        if (isFirstSetUserInfo) {
            titlebar.setTitle("个人资料");
            titlebar.setLeftLl(backClickListener);
            btn_next.setText("下一步");

        } else {
            titlebar.setTitle("年龄");
            titlebar.setLeftLl(backClickListener);
        }
        age_scrollview.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    age_scrollview.startScrollerTask();
                }
                return false;
            }
        });// 0 3283 262dp 1200dp 1946:400 1950:580 1960:1000 10岁相关120dp
        age_scrollview.setOnScrollStopListner(new OnScrollStopListner() {
            public void onScrollChange(int index) {
                Log.i(tag, "index: " + index + " dp " + DensityUtil.px2dip(mContext, index));
                int indexcut = index;//index - 575;
                if (indexcut <= 0) {
//					age_value.setText("1930");
//				} else if (indexcut > 2883) {
                    int value = DensityUtil.px2dip(mContext, indexcut);
                    age_value.setText((int) (value / 9.5 + 1930) + "");
//					age_value.setText("2005");
                } else {
                    int value = DensityUtil.px2dip(mContext, indexcut);
                    age_value.setText((int) (value / 9.5 + 1930) + "");
                }
            }
        });
        age_value.setText(age + "");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_age;
    }
}

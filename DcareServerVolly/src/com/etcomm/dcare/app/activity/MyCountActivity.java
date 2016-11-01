package com.etcomm.dcare.app.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.setting.RelateWorkNumberActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MyCountActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.view_line)
    View view_line;
    @Bind(R.id.bind_jobnumber)
    Button bind_jobnumber;
    @Bind(R.id.bind_email)
    Button bind_email;
    @Bind(R.id.bind_phone)
    Button bind_phone;
    @Bind(R.id.job_indicator)
    ImageView job_indicator;
    @Bind(R.id.phone_indicator)
    ImageView phone_indicator;
    @Bind(R.id.email_indicator)
    ImageView email_indicator;
    @Bind(R.id.et_jobnumber)
    TextView et_jobnumber;
    @Bind(R.id.et_phone)
    TextView et_phone;
    @Bind(R.id.et_email)
    TextView et_email;

    @Override
    protected Activity atvBind() {
        return this;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("changeinfo")) {
                if (SharePreferencesUtil.getJobNumber(mContext).equals("")) {
                    bind_jobnumber.setVisibility(View.VISIBLE);
                    job_indicator.setVisibility(View.GONE);
                } else {
                    et_jobnumber.setText(SharePreferencesUtil.getJobNumber(mContext));
                    bind_jobnumber.setVisibility(View.GONE);
                    job_indicator.setVisibility(View.VISIBLE);

                }

                if (SharePreferencesUtil.getPhone(mContext).equals("")) {
                    bind_phone.setVisibility(View.VISIBLE);
                    phone_indicator.setVisibility(View.GONE);
                } else {
                    et_phone.setText(SharePreferencesUtil.getPhone(mContext));
                    bind_phone.setVisibility(View.GONE);
                    phone_indicator.setVisibility(View.VISIBLE);

                }

                if (SharePreferencesUtil.getEmail(mContext).equals("")) {
                    bind_email.setVisibility(View.VISIBLE);
                    email_indicator.setVisibility(View.GONE);
                } else {
                    et_email.setText(SharePreferencesUtil.getEmail(mContext));
                    bind_email.setVisibility(View.GONE);
                    email_indicator.setVisibility(View.VISIBLE);

                }
            }
        }
    };

    @Override
    protected void initDatas() {
        titlebar.setLeftLl(backClickListener);
        titlebar.setTitle("我的账号");
        IntentFilter filter = new IntentFilter();
        filter.addAction("changeinfo");
        registerReceiver(receiver, filter);
      /*
      * 是否是权限分级管理账户
      * */
        if (SharePreferencesUtil.getIslevel(mContext)) {
            ll1.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
        } else {
            ll1.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
        }
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAtvTask(RelateWorkNumberActivity.class);
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAtvTask(BindPhoneActivity.class);
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BindPhoneActivity.class);
                intent.putExtra("email", true);
                startActivity(intent);
            }
        });
        if (SharePreferencesUtil.getJobNumber(mContext).equals("")) {
            bind_jobnumber.setVisibility(View.VISIBLE);
            job_indicator.setVisibility(View.GONE);
        } else {
            et_jobnumber.setText(SharePreferencesUtil.getJobNumber(mContext));
            bind_jobnumber.setVisibility(View.GONE);
            job_indicator.setVisibility(View.VISIBLE);

        }

        if (SharePreferencesUtil.getPhone(mContext).equals("")) {
            bind_phone.setVisibility(View.VISIBLE);
            phone_indicator.setVisibility(View.GONE);
        } else {
            et_phone.setText(SharePreferencesUtil.getPhone(mContext));
            bind_phone.setVisibility(View.GONE);
            phone_indicator.setVisibility(View.VISIBLE);

        }

        if (SharePreferencesUtil.getEmail(mContext).equals("")) {
            bind_email.setVisibility(View.VISIBLE);
            email_indicator.setVisibility(View.GONE);
        } else {
            et_email.setText(SharePreferencesUtil.getEmail(mContext));
            bind_email.setVisibility(View.GONE);
            email_indicator.setVisibility(View.VISIBLE);

        }
        bind_jobnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAtvTask(RelateWorkNumberActivity.class);
            }
        });
        bind_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BindPhoneActivity.class);
                intent.putExtra("email", true);
                startActivity(intent);
            }
        });
        bind_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAtvTask(BindPhoneActivity.class);
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_mine_count;
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
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

    }
}

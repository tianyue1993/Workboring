package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.TopicDisscussReportActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 话题讨论页面点击右上角，显示举报/取关
 *
 * @author iexpressbox
 */
public class TopicReportPopActivity extends BaseActivity implements
        OnClickListener {
    @Bind(R.id.btn_report)
    Button btn_report;
    @Bind(R.id.btn_cancel)
    Button btn_cancel;
    @Bind(R.id.pop_layout)
    LinearLayout layout;

    Intent intent;

    @Override
    public void onResume() {
        super.onResume();
        intent = getIntent();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activ
    // ity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @OnClick({R.id.btn_report, R.id.btn_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_report: // 举报
                intent.setClass(mContext, TopicDisscussReportActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cancel:
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        Intent intent = getIntent();

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_topic_report_dialog;
    }
}

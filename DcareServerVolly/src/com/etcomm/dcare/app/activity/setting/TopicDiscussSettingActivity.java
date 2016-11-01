package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.SharetoGroupActivity;
import com.etcomm.dcare.app.activity.TopicDisscussReportActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 话题讨论页面点击右上角，显示举报/取关
 *
 * @author iexpressbox
 */
public class TopicDiscussSettingActivity extends BaseActivity implements
        OnClickListener {
    @Bind(R.id.btn_report)
    Button btn_report;
    @Bind(R.id.btn_attention)
    Button btn_attention;
    @Bind(R.id.btn_cancel)
    Button btn_cancel;
    @Bind(R.id.pop_layout)
    LinearLayout layout;
    String topic_id;
    @Bind(R.id.btn_share)
    Button btn_share;

    @Override
    public void onResume() {
        super.onResume();
        topic_id = getIntent().getStringExtra("topic_id");
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @OnClick({R.id.btn_report, R.id.btn_share, R.id.btn_attention, R.id.btn_cancel})
    public void onClick(View v) {
        Intent data = new Intent();
        switch (v.getId()) {
            case R.id.btn_attention: // 取关
                data.putExtra(Preferences.TOPICSET, "1");
                setResult(RESULT_OK, data);
                break;
            case R.id.btn_report: // 举报小组
                Intent intent = new Intent(mContext, TopicDisscussReportActivity.class);
                intent.putExtra("topic_id", topic_id);
                intent.putExtra("type", "topic");
                startActivity(intent);
                break;
            case R.id.btn_share:
                Intent shareintent = new Intent(mContext, SharetoGroupActivity.class);
                shareintent.putExtra("topic_id", topic_id);
                shareintent.putExtra("type", "topic");
                shareintent.putExtra("discuse", getIntent().getStringExtra("discuse"));
                shareintent.putExtra("image", getIntent().getStringExtra("image"));
                shareintent.putExtra("topic_name", getIntent().getStringExtra("topic_name"));
                startActivity(shareintent);
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
        boolean isAttentioned = intent.getBooleanExtra("isAttentioned", false);
        String id = intent.getStringExtra("id");
        if (id.equals(AppSharedPreferencesHelper.getUserId())) {
            btn_attention.setText("删除");
        } else {
            if (isAttentioned) {
                btn_attention.setText("取消关注");
            } else {
                btn_attention.setText("关注");
            }

        }

        btn_report.setText("举报");

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_topic_dialog;
    }
}

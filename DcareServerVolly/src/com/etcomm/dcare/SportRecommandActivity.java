package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.etcomm.dcare.app.activity.login.RegisterActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.widget.CircleImageView;
import com.etcomm.dcare.widget.SystemTitle;
import com.umeng.analytics.MobclickAgent;

/**
 * 运动目标推荐
 *
 * @author iexpressbox
 */
public class SportRecommandActivity extends BaseActivity {
    SystemTitle systemtitle;
    CircleImageView useravatar;
    TextView tv_age;
    TextView tv_height;
    TextView tv_weight;
    TextView tv_recommandsteps;
    Button btn_recommandstepsnext;

    private void initview() {
        // TODO Auto-generated method stub
        systemtitle = (SystemTitle) findViewById(R.id.systemtitle);
        useravatar = (CircleImageView) findViewById(R.id.useravatar);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_height = (TextView) findViewById(R.id.tv_height);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_recommandsteps = (TextView) findViewById(R.id.tv_recommandsteps);
        btn_recommandstepsnext = (Button) findViewById(R.id.btn_recommandstepsnext);
        btn_recommandstepsnext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SportRecommandActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {
        initview();

    }

    @Override
    protected int setLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_sportrecommand;
    }
}

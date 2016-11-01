package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.widget.ObservableHorizontalScrollView;
import com.etcomm.dcare.widget.SlideSwitch;
import com.umeng.analytics.MobclickAgent;

/**
 * 输入身高体重性别年龄信息
 *
 * @author iexpressbox
 */
public class EnterAgeWeightHeightActivity extends BaseActivity {
    TextView tv_info;
    SlideSwitch swith_sex;
    ObservableHorizontalScrollView age_scrollview;
    LinearLayout age_scrollview_ll;
    ImageView iv_age_scrollview;
    ImageView userimage;
    ObservableHorizontalScrollView height_scrollview;
    LinearLayout height_scrollview_ll;
    ImageView iv_height_scrollview;
    ObservableHorizontalScrollView weight_scrollview;
    LinearLayout weight_scrollview_ll;
    ImageView iv_weight_scrollview;
    Button btn_enteruserinofnext;

    private void initview() {
        // TODO Auto-generated method stub
        tv_info = (TextView) findViewById(R.id.tv_info);
        swith_sex = (SlideSwitch) findViewById(R.id.swith_sex);
        age_scrollview = (ObservableHorizontalScrollView) findViewById(R.id.age_scrollview);
        age_scrollview_ll = (LinearLayout) findViewById(R.id.age_scrollview_ll);
        iv_age_scrollview = (ImageView) findViewById(R.id.iv_age_scrollview);
        userimage = (ImageView) findViewById(R.id.userimage);
        height_scrollview = (ObservableHorizontalScrollView) findViewById(R.id.height_scrollview);
        height_scrollview_ll = (LinearLayout) findViewById(R.id.height_scrollview_ll);
        iv_height_scrollview = (ImageView) findViewById(R.id.iv_height_scrollview);
        weight_scrollview = (ObservableHorizontalScrollView) findViewById(R.id.weight_scrollview);
        weight_scrollview_ll = (LinearLayout) findViewById(R.id.weight_scrollview_ll);
        iv_weight_scrollview = (ImageView) findViewById(R.id.iv_weight_scrollview);
        btn_enteruserinofnext = (Button) findViewById(R.id.btn_enteruserinofnext);
        btn_enteruserinofnext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(EnterAgeWeightHeightActivity.this, SportRecommandActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        initview();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_enterageweightheight;
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

}

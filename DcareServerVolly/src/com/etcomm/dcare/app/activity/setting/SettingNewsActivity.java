package com.etcomm.dcare.app.activity.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.R.string;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.widget.WheelView;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择年龄
 *
 * @author iexpressbox
 */
public class SettingNewsActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.iv_avator)
    ImageView iv_avator;
    @Bind(R.id.age)
    TextView age;
    @Bind(R.id.weight)
    TextView weight;
    @Bind(R.id.height)
    TextView height;
    @Bind(R.id.wl_pickerage)
    WheelView wl_pickerage;
    @Bind(R.id.wl_pickerweight)
    WheelView wl_pickerweight;
    @Bind(R.id.wl_pickerheight)
    WheelView wl_pickerheight;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.sure)
    TextView sure;
    @Bind(R.id.layout_wl)
    LinearLayout layout_wl;
    @Bind(R.id.choosetext)
    TextView choosetext;
    @Bind(R.id.ll_age)
    LinearLayout ll_age;
    @Bind(R.id.ll_height)
    LinearLayout ll_height;
    @Bind(R.id.ll_weight)
    LinearLayout ll_weight;

    private Intent intent;
    private boolean isShow = true;
    private int usersex = 0;

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

    @OnClick({R.id.btn_next, R.id.ll_age, R.id.ll_weight, R.id.ll_height, R.id.cancel, R.id.sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                intent.putExtra(Preferences.UserAge, age.getText().toString());
                intent.putExtra(Preferences.UserHeight, height.getText().toString());
                intent.putExtra(Preferences.UserWeight, weight.getText().toString());
                intent.setClass(mContext, SettingSportGoalActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_age:
                personal_age_rl();

                break;
            case R.id.ll_weight:
                personal_weight_rl();

                break;
            case R.id.ll_height:
                personal_height_rl();

                break;
            case R.id.cancel:
                cancel();
                break;
            case R.id.sure:
                sure();
                break;
        }
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
        usersex = intent.getIntExtra(Preferences.UserSex, 0);// //1男，2女) 0默认
        // 头像设置
        if (intent.getStringExtra(Preferences.Avator) != null && intent.getStringExtra(Preferences.Avator) != "") {
            ImageLoader.getInstance().displayImage(intent.getStringExtra(Preferences.Avator), iv_avator);
        }

        titlebar.setTitle("个人资料");
        titlebar.setLeftLl(backClickListener);
        age.setText("1980");
        if (usersex == 1) {
            // 男
            weight.setText("65");
            height.setText("170");
        } else if (usersex == 2) {
            // 女
            weight.setText("50");
            height.setText("160");

        }

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_settingnews;
    }

    // 身高设置
    void personal_height_rl() {
        choosetext.setText(string.chooseheight);
        if (isShow) {
            layout_wl.setVisibility(View.VISIBLE);
            wl_pickerage.setVisibility(View.GONE);
            wl_pickerheight.setVisibility(View.VISIBLE);
            wl_pickerweight.setVisibility(View.GONE);
            isShow = false;

            ArrayList<String> heightList = new ArrayList<String>();
            for (int i = 0; i < 111; i++) {
                // 120--230
                int weight = i + 120;
                heightList.add(weight + "");
            }
            wl_pickerheight.setOffset(1);
            wl_pickerheight.setItems(heightList);
            int height1 = Integer.parseInt(height.getText().toString());
            if (height1 != 0) {
                wl_pickerheight.setSeletion(height1 - 120);

            } else {
                if (usersex == 1) {
                    wl_pickerheight.setSeletion(50);
                } else if (usersex == 2) {
                    wl_pickerheight.setSeletion(40);
                }
            }

            wl_pickerheight.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                    height.setText(item);
                }
            });
        } else {
            isShow = true;
            wl_pickerweight.setSeletion(0);
            wl_pickerage.setSeletion(0);
            wl_pickerheight.setSeletion(0);
            layout_wl.setVisibility(View.GONE);
        }
    }

    // 体重设置
    void personal_weight_rl() {
        choosetext.setText(string.chooseweight);
        if (isShow) {
            layout_wl.setVisibility(View.VISIBLE);
            wl_pickerage.setVisibility(View.GONE);
            wl_pickerheight.setVisibility(View.GONE);
            wl_pickerweight.setVisibility(View.VISIBLE);
            isShow = false;

            ArrayList<String> weightList = new ArrayList<String>();
            for (int i = 0; i < 276; i++) {
                int weight = i + 25;
                weightList.add(weight + "");
            }
            wl_pickerweight.setOffset(1);
            wl_pickerweight.setItems(weightList);
            int weight1 = Integer.parseInt(weight.getText().toString());
            if (weight1 != 0) {
                wl_pickerweight.setSeletion(weight1 - 25);
            } else {

                if (usersex == 1) {
                    wl_pickerweight.setSeletion(40);
                } else if (usersex == 2) {
                    wl_pickerweight.setSeletion(25);
                }

            }

            wl_pickerweight.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                    weight.setText(item);
                }
            });
        } else {
            isShow = true;
            wl_pickerweight.setSeletion(0);
            wl_pickerage.setSeletion(0);
            wl_pickerheight.setSeletion(0);
            layout_wl.setVisibility(View.GONE);
        }
    }

    // 年龄设置
    void personal_age_rl() {
        choosetext.setText(string.chooseage);
        if (isShow) {
            layout_wl.setVisibility(View.VISIBLE);
            wl_pickerage.setVisibility(View.VISIBLE);
            wl_pickerheight.setVisibility(View.GONE);
            wl_pickerweight.setVisibility(View.GONE);
            isShow = false;
            ArrayList<String> ageList = new ArrayList<String>();
            for (int i = 0; i < 71; i++) {
                int age = i + 1930;
                ageList.add(age + "");
            }
            wl_pickerage.setOffset(1);
            wl_pickerage.setItems(ageList);

            int age1 = Integer.parseInt(age.getText().toString());
            if (age1 != 0) {
                wl_pickerage.setSeletion(age1 - 1930);
            } else {
                wl_pickerage.setSeletion(50);
            }

            wl_pickerage.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                    age.setText(item);
                }
            });

        } else {
            isShow = true;
            wl_pickerweight.setSeletion(0);
            wl_pickerage.setSeletion(0);
            wl_pickerheight.setSeletion(0);
            layout_wl.setVisibility(View.GONE);
        }
    }

    private void cancel() {
        isShow = true;
        layout_wl.setVisibility(View.GONE);
        wl_pickerweight.setSeletion(0);
        wl_pickerage.setSeletion(0);
        wl_pickerheight.setSeletion(0);

    }

    private void sure() {
        wl_pickerweight.setSeletion(0);
        wl_pickerage.setSeletion(0);
        wl_pickerheight.setSeletion(0);
        isShow = true;
        layout_wl.setVisibility(View.GONE);
    }
}

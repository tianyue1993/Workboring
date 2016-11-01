package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择性别
 *
 * @author iexpressbox
 */
public class SelectSexPopupWindowActivity extends BaseActivity implements
        OnClickListener {
    @Bind(R.id.btn_take_photo)
    Button btn_take_photo;
    @Bind(R.id.btn_pick_photo)
    Button btn_pick_photo;
    @Bind(R.id.btn_cancel)
    Button btn_cancel;
    @Bind(R.id.pop_layout)
    LinearLayout layout;

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

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @OnClick({R.id.btn_take_photo, R.id.btn_pick_photo, R.id.btn_cancel})
    public void onClick(View v) {
        Intent data = new Intent();
        switch (v.getId()) {
            case R.id.btn_take_photo: // 男
                data.putExtra(Preferences.SelectSex, "男");
                setResult(RESULT_OK, data);
                break;
            case R.id.btn_pick_photo: // 女
                data.putExtra(Preferences.SelectSex, "女");
                setResult(RESULT_OK, data);
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
        btn_take_photo.setText("男");
        btn_pick_photo.setText("女");

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_picpop_dialog;
    }
}

package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Preferences;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectPicPopupWindowActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "SelectPicPopupWindowActivity";
    @Bind(R.id.btn_take_photo)
    Button btn_take_photo;
    @Bind(R.id.btn_pick_photo)
    Button btn_pick_photo;
    @Bind(R.id.btn_cancel)
    Button btn_cancel;
    @Bind(R.id.pop_layout)
    LinearLayout layout;
    protected Bitmap photo;
    protected File mCurrentPhotoFile;
    private String filename;

    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @OnClick({R.id.btn_take_photo, R.id.btn_pick_photo, R.id.btn_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                backWithData(Preferences.PICMethod, "TAKEPHOTO");
                break;
            case R.id.btn_pick_photo:
                backWithData(Preferences.PICMethod, "PICKPHOTO");
                break;
            case R.id.btn_cancel:
                break;
            default:
                break;
        }
        finish();

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
        return this;
    }

    @Override
    protected void initDatas() {
        filename = getIntent().getStringExtra(Preferences.TakePhotoPath);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_picpop_dialog;
    }

}

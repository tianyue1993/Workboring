package com.etcomm.dcare.app.activity;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.OnClick;

public abstract class CommonTitleActivity extends BaseActivity {

    @Bind(R.id.title_content) //头部文字
            TextView mTitle;
    @Bind(R.id.title_right)
    ImageView titleRight; //title 右边icon
    @Bind(R.id.title_content_view) //主布局
            LinearLayout mllView;
    @Bind(R.id.title_left_iv_back) //返回图标
            ImageView mLeftBack;
    @Bind(R.id.title_left_iv_search) //左边查找图标
            ImageView ivSearch;
    @Bind(R.id.title_left_iv_edit) //左边编辑图标
            ImageView ivEdit;
    @Bind(R.id.title_left_rlview) //左边布局(查找图标|编辑图标)
            RelativeLayout mLeftView;
    @Bind(R.id.title_left_button) //返回 暂不用
            TextView mTvBack;
    @Bind(R.id.icon) //右边图标
            ImageView icon;
    @Bind(R.id.text) //右边文字
            TextView text;
    @Bind(R.id.title_right_view) //右边布局(右边图标|右边文字)
            LinearLayout mRightView;


    //头部文字
    public void setHeaderTitle(String s) {
        mTitle.setText(s);
    }

    //头部文字
    public void setHeaderTitle(int resId) {
        mTitle.setText(resId);
    }

    //右边文字
    public void setRightText(int resId) {
        setRightRlView(true);
        text.setVisibility(View.VISIBLE);
        text.setText(resId);
    }

    //右边文字
    public void setRightText(String str) {
        setRightRlView(true);
        text.setVisibility(View.VISIBLE);
        text.setText(str);
    }
    //右边文字
    public String getRightText() {
    	setRightRlView(true);
    	text.setVisibility(View.VISIBLE);
    	
    	return text.getText().toString().trim();
    }

    //右边图标
    public void setRightIconBackgroud(int resId) {
        setRightRlView(true);
        icon.setVisibility(View.VISIBLE);
        if (resId == 0) {
            icon.setBackgroundDrawable(null);
        } else {
            icon.setImageDrawable(null);
//            icon.setImageResource(resId);
            icon.setBackgroundResource(resId);
        }
    }

    //右边布局
    public void setRightRlView(boolean b) {
        mRightView.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    //左边查找
    public void setLeftIconSearch(int resId) {
        setRightllView(true);
        if (resId == 0) {
            ivSearch.setBackgroundDrawable(null);
        } else {
            ivSearch.setVisibility(View.VISIBLE);
            ivSearch.setBackgroundResource(resId);
        }
    }

    //左边编辑
    public void setLeftIconEdit(int resId) {
        setRightllView(true);
        if (resId == 0) {
            ivEdit.setBackgroundDrawable(null);
        } else {
            ivEdit.setVisibility(View.VISIBLE);
            ivEdit.setBackgroundResource(resId);
        }
    }

    //左边布局
    public void setRightllView(boolean b) {
        mLeftView.setVisibility(b ? View.VISIBLE : View.GONE);
        mLeftBack.setVisibility(b ? View.GONE : View.VISIBLE);
    }


    @OnClick({R.id.title_left_iv_back, R.id.icon, R.id.text, R.id.title_left_iv_search, R.id.title_left_iv_edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_iv_back:
                onLeftBackClickListener(v);
                break;
            case R.id.icon:
                onRightIconClickListener(v);
                break;
            case R.id.text:
                onRighTextClickListener(v);
                break;
            case R.id.title_left_iv_search:
                onLeftSearchClickListener(v);
                break;
            case R.id.title_left_iv_edit:
                onLeftEditClickListener(v);
                break;
            default:
                break;
        }
    }

    /**
     * @param v 左边编辑
     */
    protected void onLeftEditClickListener(View v) {
    }

    /**
     * @param v 左边查询
     */
    protected void onLeftSearchClickListener(View v) {
    }

    /**
     * @param v 右边文字
     */
    protected void onRighTextClickListener(View v) {
    }

    /**
     * @param v 右边图标
     */
    protected void onRightIconClickListener(View v) {
    }


    //返回按钮
    public void onLeftBackClickListener(View v) {
        // SystemInfoUtils.closeSoftKeyBoard(this);
        finish();
    }

}

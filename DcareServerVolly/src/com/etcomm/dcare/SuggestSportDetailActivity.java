package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.netresponse.SuggestItems;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SuggestSportDetailActivity extends BaseActivity {

    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.sugget_sports_image_iv)
    ImageView sugget_sports_image_iv;
    @Bind(R.id.sport_signup)
    Button sport_signup;
    @Bind(R.id.sport_time_tv)
    TextView sport_time_tv;
    @Bind(R.id.sport_deadline)
    TextView sport_deadline;
    @Bind(R.id.sport_entrynumber)
    TextView sport_entrynumber;
    @Bind(R.id.sport_detail)
    TextView sport_detail;

    private Bundle bundle;
    private SuggestItems mInfo;

    //	private LoadingDialog dialog;
    @Override
    public void onResume() {
        super.onResume();
        updateTextWithDate();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }


    void initviewwithSuggestItems() {
        // TODO Auto-generated method stub
        titlebar.setTitle("活动详情");
        titlebar.setLeftLl(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        ImageLoader.getInstance().displayImage(mInfo.getImage(), sugget_sports_image_iv);

        sport_time_tv.setText(mInfo.getStart_at() + "-" + mInfo.getEnd_at());
        sport_deadline.setText(mInfo.getDeadline());
        sport_entrynumber.setText(mInfo.getNumber());
        // sport_detail
    }

    void updateTextWithDate() {

        if (mInfo != null) {
            Log.i(tag, "mInfo:  " + mInfo.toString());
            initviewwithSuggestItems();
        }
    }

    @OnClick(R.id.sport_signup)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sport_signup:

//			 RequestParams params = new RequestParams();
                Map<String, String> params = new HashMap<String, String>();
                params.put("activityid", mInfo.getRecommend_id());
                params.put("access_token", SharePreferencesUtil.getToken(mContext));
                cancelmDialog();
                showProgress(DIALOG_DEFAULT, true);
//				dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
//				dialog.setCancelable(false);
//				dialog.setCanceledOnTouchOutside(false);
//				dialog.show();
                Log.i(tag, "params: " + params.toString());
                DcareRestClient.volleyGet(Constants.SuggestSportsSignUp, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onCancel() {
                        // TODO Auto-generated method stub
                        Log.w(tag, "post cancel" + this.getRequestURI());
                        cancelmDialog();
//						if (dialog != null && dialog.isShowing()) {
//							dialog.dismiss();
//						}
                        super.onCancel();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                        cancelmDialog();
//						if (dialog != null && dialog.isShowing()) {
//							dialog.dismiss();
//						}
                        Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // TODO Auto-generated method stub
                        cancelmDialog();
                        try {
                            int code = response.getInt("code");
                            String message = response.getString("message");
                            Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// response.getJSONObject("content").toString());
                            if (code == 0) {
                                Toast.makeText(mContext, "报名成功", Toast.LENGTH_SHORT).show();
                                ;
                                finish();
                                super.onSuccess(statusCode, headers, response);
                            } else {// code不为0 发生异常
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//					if (dialog != null && dialog.isShowing()) {
//						dialog.dismiss();
//					}
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            if (bundle != null) {

                mInfo = (SuggestItems) bundle.getSerializable(Preferences.SuggestSportDetail);
                if (mInfo != null) {
                    Log.i(tag, "mInfo:  " + mInfo.toString());
//					updateTextWithDate();
                }
            }
        }
        initviewwithSuggestItems();
        updateTextWithDate();

    }

    @Override
    protected int setLayoutId() {
        return R.layout.suggest_sports_detail;
    }
}

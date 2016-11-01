package com.etcomm.dcare.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class TopicDisscussReportActivity extends BaseActivity {
    //举报页面
    private String topic_id;
    private String discussion_id;
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.gview)
    GridView gview;
    @Bind(R.id.report_tv)
    EditText report_tv;
    @Bind(R.id.contact_tv)
    TextView contact_tv;
    @Bind(R.id.commit)
    Button commit;
    StringBuffer type = new StringBuffer("");
    String report_type;
    String url;
    String comment_id;

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
        titlebar.setTitle("举报");
        titlebar.setLeftLl(backClickListener);
        Intent intent = getIntent();
        report_type = intent.getStringExtra("type");
        if (intent != null) {
            topic_id = intent.getStringExtra("topic_id");
            discussion_id = intent.getStringExtra("discussion_id");
            comment_id = intent.getStringExtra("comment_id");
        }

        report_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 100 - s.length();
                contact_tv.setText("您还可以输入" + i + "个字");
            }
        });

        gview.setAdapter(new ReportGridviewAdapterprivate(mContext));

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.toString() != "") {
                    Report();
                } else {
                    showToast("请选择举报类型");
                }

            }
        });
    }

    private void Report() {
        String Type = type.toString();//去掉最后一个分割逗号
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("content", report_tv.getText().toString());
        params.put("type", Type.substring(0, Type.length() - 1));
        showProgress(DIALOG_DEFAULT, true);
        if (report_type.contains("discussion")) {
            //举报帖子
            params.put("discussion_id", discussion_id);
            url = Constants.REPORT_DISCUSSION;
        } else if (report_type.contains("topic")) {
            //举报小组
            params.put("topic_id", topic_id);
            url = Constants.REPORT_TOPIC;
        } else if (report_type.contains("conment")) {
            //举报评论
            params.put("comment_id", comment_id);
            url = Constants.REPORT_COMMENT;
        }
        Log.e(tag, "  params: " + params.toString());
        DcareRestClient.volleyPost(url, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, com.alibaba.fastjson.JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                if (errorResponse != null) {
                    Log.e(tag, "  errorResponse: " + errorResponse.toString());
                }
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, com.alibaba.fastjson.JSONObject response) {
                cancelmDialog();
                int code = response.getInteger("code");
                String message = response.getString("message");
                Log.e(tag, "  response: " + response.toString());

                if (code == 0) {
                    showToast(message);
                    finish();
                } else {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected int setLayoutId() {
        return R.layout.acitivity_report;
    }


    /**
     * Created by ${tianyue} on 2016/10/10.
     * 适配器
     */
    class ReportGridviewAdapterprivate extends BaseAdapter {
        private String[] imageId;

        public ReportGridviewAdapterprivate(Context context) {
            imageId = new String[]{"广告类", "恐怖类", "赌博类", "诈骗类", "政治有害类", "淫秽色情类", "损害他人类型", "其他类"}; // 定义并初始化的数组

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CheckBox imageview;
            if (convertView == null) {
                imageview = new CheckBox(mContext); // 实例化的对象
                imageview.setPadding(20, 0, 20, 0); // 设置内边距
                imageview.setButtonDrawable(R.drawable.unchecked);
                imageview.setTextColor(getResources().getColor(R.color.grey));
                imageview.setTextSize(16);

            } else {
                imageview = (CheckBox) convertView;
            }

            imageview.setText(imageId[position]); // 为ImageView设置要显示的wenben
            imageview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        type.append(position + 1 + ",");
                        imageview.setButtonDrawable(R.drawable.checked);
                    } else {
                        String string = type.toString().replace(position + 1 + ",", "");
                        type = new StringBuffer(string);
                        imageview.setButtonDrawable(R.drawable.unchecked);
                    }


                }
            });
            return imageview;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return imageId.length;
        }
    }
}

package com.etcomm.dcare.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.etcomm.dcare.R;
import com.etcomm.dcare.adapter.MemberListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.model.TopicMemberJson;
import com.etcomm.dcare.app.model.TopicUser;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class TopicMemberActivity extends BaseActivity {
    @Bind(R.id.memberpulllistview)
    ListView memberpulllistview;
    @Bind(R.id.member_count)
    TextView member_count;
    @Bind(R.id.leftimage)
    ImageView leftimage;

    private MemberListAdapter mAttentionedAdapter;
    private List<TopicUser> UserList;
    protected static final String tag = TopicMemberActivity.class.getSimpleName();

    private String topic_id;
    private int attion;

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
        Intent intent = getIntent();
        if (intent != null) {
            topic_id = intent.getStringExtra("topic_id");
            attion = intent.getIntExtra("attion", 0);
        }
        member_count.setText("小组成员（" + attion + "人）");
        getMemberList();
        leftimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getMemberList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("topic_id", topic_id);
        Log.i(tag, "params: " + params.toString());
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        DcareRestClient.volleyGet(Constants.GetAroundTopicMemberList, params, new FastJsonHttpResponseHandler() {
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
                if (code == 0) {
                    String jsonString = JSON.toJSONString(response);
                    Log.i(tag, "jsonString" + jsonString);
                    TopicMemberJson contents = JSON.parseObject(jsonString, TopicMemberJson.class);
                    UserList = contents.content;
                    mAttentionedAdapter = new MemberListAdapter(mContext, UserList);
                    memberpulllistview.setAdapter(mAttentionedAdapter);
                    Log.i(tag, "UserList" + UserList.size() + UserList.toString());

                } else {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_topic_member;
    }


}

package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.widget.ExEditText;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 新增加话题Topic  每个人每周最多一次  需要审核
 *
 * @author iexpressbox
 */

/**
 * 身边-创建小组
 *
 * @author etc
 */
public class AddNewTopicActivity extends BaseActivity {
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:// 已经发布过
                    newtopic_already_tv.setVisibility(View.VISIBLE);
                    newtopic_sucess_tv.setVisibility(View.INVISIBLE);
                    li.setVisibility(View.INVISIBLE);
                    btn_next.setEnabled(true);
                    btn_next.setVisibility(View.GONE);
                    btn_next.setText("返回身边");
                    break;
                case 1:
                    newtopic_already_tv.setVisibility(View.INVISIBLE);
                    newtopic_sucess_tv.setVisibility(View.INVISIBLE);
                    li.setVisibility(View.VISIBLE);
                    btn_next.setEnabled(false);
                    // btn_next.setVisibility(View.GONE);
                    btn_next.setText("提交审核");
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.li)
    LinearLayout li;
    @Bind(R.id.newtopic_sucess_tv)
    TextView newtopic_sucess_tv;
    @Bind(R.id.newtopic_already_tv)
    TextView newtopic_already_tv;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.topic_discuss)
    ExEditText topic_discuss;
    @Bind(R.id.topic_name)
    ExEditText topic_name;
    @Bind(R.id.iv_del_name)
    ImageView iv_del_name;
    @Bind(R.id.iv_del_discuss)
    ImageView iv_del_discuss;
    private SharedPreferences sp;

    @OnClick({R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (btn_next.getText().toString().equals("提交审核")) {

                } else if (btn_next.getText().toString().equals("返回身边")) {
                    backEvent();
                    return;
                }
                String comment = topic_name.getText().toString();
                if (StringUtils.isEmpty(comment) || comment.length() < 1) {
                    Toast.makeText(mContext, "" +
                            "话题字数太少", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", SharePreferencesUtil.getToken(mContext));
                params.put("topic", comment);
                params.put("description", topic_discuss.getText().toString());
                cancelmDialog();
                showProgress(DIALOG_DEFAULT, true);
                Log.i(tag, "params: " + params.toString());
                DcareRestClient.volleyPost(Constants.AddTopic, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
                    @Override
                    public void onCancel() {
                        // TODO Auto-generated method stub
                        Log.w(tag, "post cancel" + this.getRequestURI());
                        cancelmDialog();
                        super.onCancel();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                        cancelmDialog();
                        Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                        cancelmDialog();
                        try {
                            int code = response.getInteger("code");
                            String message = response.getString("message");
                            Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                            if (code == 0) {
                                publishtopicsuccess();
                                sp.edit().putLong("lastcreate", System.currentTimeMillis()).commit();
                                sp.edit().putString(Preferences.ACCESS_TOKEN, SharePreferencesUtil.getToken(mContext)).commit();
                                // if()考虑一下是不是要显示刚才评论的问题，如果刚好在下一页面，怎么考虑
                                // 评论列表避免重复显示
                            } else {// code不为0 发生异常
                                if (message.equals("一周内只允许发布一次话题")) {
                                    sp.edit().putLong("lastcreate", System.currentTimeMillis()).commit();
                                    sp.edit().putString(Preferences.ACCESS_TOKEN, SharePreferencesUtil.getToken(mContext)).commit();
                                }
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(tag, "JSONException:");
                            e.printStackTrace();
                        }
                        cancelmDialog();

                    }
                });
                break;
        }
    }

    protected void publishtopicsuccess() {
        // TODO Auto-generated method stub
        btn_next.setText("返回身边");
        btn_next.setEnabled(true);
        newtopic_sucess_tv.setVisibility(View.VISIBLE);
        li.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        titlebar.setTitle(R.string.add_topic);
        titlebar.setLeftLl(backClickListener);
        sp = getPreferences(MODE_PRIVATE);
        Intent addIntent = getIntent();
        // 是否可以创建话题
        if (!addIntent.getBooleanExtra(Preferences.ADD_TOPIC_CHECK, false)) {
            newtopic_already_tv.setVisibility(View.VISIBLE);
            newtopic_sucess_tv.setVisibility(View.INVISIBLE);
            li.setVisibility(View.INVISIBLE);
            btn_next.setVisibility(View.GONE);
            return;
        }
        String token = sp.getString(Preferences.ACCESS_TOKEN, "");
        if (StringUtils.isEmpty(token) || token.equals(SharePreferencesUtil.getToken(mContext))) {
            long lastcreatetime = sp.getLong("lastcreate", 0);
            // LocalDate now = SystemFactory.getClock().getLocalDate();
            DateTime now = DateTime.now();
            DateTime dateTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0);

            TimeZone tz = TimeZone.getDefault();
            String s = "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID();
            System.out.println(s);
            LocalTime locatime = dateTime.toLocalTime();
            long maxtime = dateTime.dayOfWeek().withMaximumValue().getMillis();
            long mintime = dateTime.dayOfWeek().withMinimumValue().getMillis();
            Log.i(tag, "Week datetime.getMillis:" + dateTime.getMillis() + " max:" + maxtime + " min: " + mintime + "  lastcreatetime :" + lastcreatetime);
            if (lastcreatetime < maxtime && lastcreatetime > mintime) {// 本周内创建过任务，不可再创建任务
                Log.i(tag, "本周内创建过任务，不可再创建任务");
                newtopic_already_tv.setVisibility(View.VISIBLE);
                newtopic_sucess_tv.setVisibility(View.INVISIBLE);
                li.setVisibility(View.INVISIBLE);
                btn_next.setEnabled(true);
                btn_next.setVisibility(View.GONE);
                btn_next.setText("返回身边");
            } else {
                Log.i(tag, "本周内未创建过");
            }
        } else {
            btn_next.setEnabled(false);
        }
        topic_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.toString().length() > 0 && topic_discuss.getText().length() > 0) {
                    btn_next.setEnabled(true);
                    btn_next.setClickable(true);
                    btn_next.setFocusable(true);
                } else {
                    btn_next.setEnabled(false);
                    btn_next.setClickable(false);
                    btn_next.setFocusable(false);
                }
                if (s.toString().length() > 0) {
                    iv_del_name.setVisibility(View.VISIBLE);
                } else {
                    iv_del_name.setVisibility(View.GONE);
                }
            }
        });


        topic_discuss.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && topic_name.getText().length() > 0) {
                    btn_next.setEnabled(true);
                    btn_next.setClickable(true);
                    btn_next.setFocusable(true);

                } else {
                    btn_next.setEnabled(false);
                    btn_next.setClickable(false);
                    btn_next.setFocusable(false);

                }

                if (s.toString().length() > 0) {
                    iv_del_discuss.setVisibility(View.VISIBLE);
                } else {
                    iv_del_discuss.setVisibility(View.GONE);
                }
            }
        });

        iv_del_discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topic_discuss.setText("");
            }
        });
        iv_del_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topic_name.setText("");
            }
        });
        if (li.getVisibility() == View.INVISIBLE) {
            btn_next.setEnabled(true);
        } else {
            btn_next.setEnabled(false);
        }

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_addnewtopic;
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

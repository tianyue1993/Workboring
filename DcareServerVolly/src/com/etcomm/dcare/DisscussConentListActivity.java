package com.etcomm.dcare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.DisscussCommentListAdapter;
import com.etcomm.dcare.adapter.DisscussCommentListAdapter.DisscussCommentResponseClickListener;
import com.etcomm.dcare.app.activity.setting.TopicReportPopActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.DisscussCommentContent;
import com.etcomm.dcare.netresponse.DisscussCommentItems;
import com.etcomm.dcare.widget.DialogFactory;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * @author iexpressbox 评论界面
 */
public class DisscussConentListActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.comment_et)
    EditText comment_et;
    @Bind(R.id.bottom_bar)
    LinearLayout bottom_bar;
    @Bind(R.id.comment_send)
    Button comment_send;
    @Bind(R.id.commmentpulllist)
    PullToRefreshListView commmentpulllist;
    @Bind(R.id.emptyview)
    View emptyview;
    private ListView commmentlist;
    private String disscuss_id;
    protected List<DisscussCommentItems> list = new ArrayList<DisscussCommentItems>();
    private DisscussCommentListAdapter adapter;
    protected int page_number = 1;
    protected int page_size = 6;
    private String response_comment_id = "";
    private String topic_id;
    protected Dialog deletecommentDialog;
    private Dialog commonDialog;


    private void deleteShowDialog(final DisscussCommentItems item) {
        // TODO Auto-generated method stub
        commonDialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定删除该评论吗？", "取消", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                commonDialog.dismiss();
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                commonDialog.dismiss();
                deleteComment(item);

            }
        }, Color.BLACK, Color.BLACK);

    }

    void deleteComment(final DisscussCommentItems item) {
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("comment_id", item.getComment_id());// 只有回复 某个人时才有
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);

        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DeleteDisscussComment, params, new FastJsonHttpResponseHandler() {
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
                showToast(R.string.network_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    // + response.getString("content").toString());
                    if (code == 0) {
                        comment_et.setText("");
                        list.remove(item);
                        adapter.notifyDataSetChanged();
                        // if()考虑一下是不是要显示刚才评论的问题，如果刚好在下一页面，怎么考虑
                        // 评论列表避免重复显示
                    }
                    showToast(message);
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });
    }

    @OnClick(R.id.comment_send)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_send:
                String comment = comment_et.getText().toString();
                if (!StringUtils.isEmpty(comment) && comment.startsWith("回复") && comment.contains(":")) {
                    String[] strs = comment.split(":");
                    if (strs.length > 1 && !StringUtils.isEmpty(strs[1])) {
                        comment = strs[1];
                    } else {
                        comment = "";
                    }
                } else {
                }
                if (StringUtils.isEmpty(comment) || comment.length() < 1) {
                    showToast(R.string.disscuss_null);
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("discussion_id", disscuss_id);
                params.put("topic_id", topic_id);
                params.put("access_token", SharePreferencesUtil.getToken(mContext));
                params.put("comment", comment);
                if (!StringUtils.isEmpty(response_comment_id)) {
                    params.put("comment_id", response_comment_id);// 只有回复 某个人时才有
                }
                cancelmDialog();
                showProgress(DIALOG_DEFAULT, true);
                Log.i(tag, "params: " + params.toString());
                DcareRestClient.volleyPost(Constants.ADDDisscussComment, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
                    @Override
                    public void onCancel() {
                        Log.w(tag, "post cancel" + this.getRequestURI());
                        cancelmDialog();
                        super.onCancel();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                        cancelmDialog();
                        showToast(R.string.network_error);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                        try {
                            int code = response.getInteger("code");
                            String message = response.getString("message");
                            Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                            // + response.getString("content").toString());
                            if (code == 0) {
                                comment_et.setText("");
                                // if()考虑一下是不是要显示刚才评论的问题，如果刚好在下一页面，怎么考虑
                                // 评论列表避免重复显示
                                page_number = 1;
                                getDisscussConmentList(true, page_size, page_number);
                            }
                            showToast(message);
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

    void getDisscussConmentList(final boolean isRefresh, int page_size, int page_number) {
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("discussion_id", disscuss_id);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", page_size + "");
        params.put("page_number", page_number + "");
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DisscussComment, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                commmentpulllist.onRefreshComplete();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                showToast(R.string.network_error);
                commmentpulllist.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        DisscussCommentContent disscomment = JSON.parseObject(response.getString("content"), DisscussCommentContent.class);
                        List<DisscussCommentItems> lists = disscomment.getItems();
                        if (isRefresh) {
                            list.clear();
                            list.addAll(lists);
                        } else {
                            // list.addAll(lists);
                            for (Iterator<DisscussCommentItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                DisscussCommentItems disscussCommentItems = (DisscussCommentItems) iterator.next();
                                if (!list.contains(disscussCommentItems)) {
                                    list.add(disscussCommentItems);
                                }
                            }
                        }

                        if (list.size() == 0) {
                            emptyview.setVisibility(View.VISIBLE);
                        } else {
                            emptyview.setVisibility(View.INVISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    } else {// code不为0 发生异常
                        showToast(message);
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();
                commmentpulllist.onRefreshComplete();

            }
        });
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        Intent intent = getIntent();
        disscuss_id = intent.getStringExtra("disscuss_id");
        topic_id = intent.getStringExtra("topic_id");
        titlebar.setTitle("评论");
        titlebar.setLeftLl(backClickListener);
        commmentlist = commmentpulllist.getRefreshableView();
        adapter = new DisscussCommentListAdapter(mContext, list, new DisscussCommentResponseClickListener() {

            @Override
            public void onClick(DisscussCommentItems mInfo) {
                // TODO Auto-generated method stub
                String comment = comment_et.getText().toString();
                if (!StringUtils.isEmpty(comment) && comment.startsWith("回复")) {
                    String[] strs = comment.split(":");
                    if (!StringUtils.isEmpty(strs[1])) {
                        comment_et.setHint("回复" + mInfo.getNick_name() + ":" + strs[1]);
                        response_comment_id = mInfo.getComment_id();
                    } else {
                        comment_et.setHint("回复" + mInfo.getNick_name() + ":");
                        response_comment_id = mInfo.getComment_id();
                    }
                } else {
                    comment_et.setHint("回复" + mInfo.getNick_name() + ":");
                    response_comment_id = mInfo.getComment_id();
                }
            }
        });
        commmentlist.setAdapter(adapter);
        commmentlist.setDividerHeight(5);
        commmentlist.setHeaderDividersEnabled(false);
        commmentlist.setFooterDividersEnabled(false);
        commmentlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final DisscussCommentItems item = adapter.getItem(position - 1);
                OnClickListener upBtnClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (deletecommentDialog != null && deletecommentDialog.isShowing()) {
                            deletecommentDialog.dismiss();
                        }
                        String comment = comment_et.getText().toString();
                        if (!StringUtils.isEmpty(comment) && comment.startsWith("回复")) {
                            String[] strs = comment.split(":");
                            if (strs.length >= 2 && !StringUtils.isEmpty(strs[1])) {
                                comment_et.setHint("回复" + item.getNick_name() + ":" + strs[1]);
                                response_comment_id = item.getComment_id();
                            } else {
                                comment_et.setHint("回复" + item.getNick_name() + ":");
                                response_comment_id = item.getComment_id();
                            }
                        } else {
                            comment_et.setHint("回复" + item.getNick_name() + ":");
                            response_comment_id = item.getComment_id();
                        }
                    }
                };
                OnClickListener bottomBtnClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (deletecommentDialog != null && deletecommentDialog.isShowing()) {
                            deletecommentDialog.dismiss();
                        }
                        deleteShowDialog(item);
                    }

                };
                if (item.getUser_id().equals(AppSharedPreferencesHelper.getUserId())) {
                    deletecommentDialog = DialogFactory.getDialogFactory().showCommentOptDialog(mContext, "删除", null, bottomBtnClickListener, null);
                } else {
                    deletecommentDialog = DialogFactory.getDialogFactory().showCommentOptDialog(mContext, "回复", null, upBtnClickListener, null);

                }
            }
        });
        commmentpulllist.setMode(Mode.BOTH);
        commmentpulllist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number = 1;
                getDisscussConmentList(true, page_size, page_number);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getDisscussConmentList(false, page_size, ++page_number);

            }
        });

        commmentlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DisscussCommentItems item = adapter.getItem(position - 1);
                Intent intent = new Intent(mContext, TopicReportPopActivity.class);
                intent.putExtra("comment_id", item.getComment_id());
                intent.putExtra("type", "conment");
                startActivity(intent);
                return true;
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_disscussconment;
    }

    public void onResume() {
        super.onResume();
        getDisscussConmentList(true, page_size, page_number);
        hideSoftKeyBoard();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }
}

package com.etcomm.dcare.app.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.etcomm.dcare.R;
import com.etcomm.dcare.adapter.ShareAttentionedListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.ACache;
import com.etcomm.dcare.common.CacheConstant;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.netresponse.AttentionedContent;
import com.etcomm.dcare.netresponse.AttentionedItems;
import com.etcomm.dcare.widget.ExEditText;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class SharetoGroupActivity extends BaseActivity {
    @Bind(R.id.attentionpulllistview)
    PullToRefreshListView attentionpulllistview;
    @Bind(R.id.search_healthnew_et)
    ExEditText search_healthnew_et;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.attention)
    TextView attention;

    private ShareAttentionedListAdapter mAttentionedAdapter;
    private ListView mAttentionedListView;
    private int page_size = 10;
    private int attentionpage_number = 1;
    protected int page_number;
    private ACache acache;
    private ArrayList<AttentionedItems> mAttentionList = new ArrayList<AttentionedItems>();
    protected static final String tag = SharetoGroupActivity.class.getSimpleName();

    private Bundle bundle;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equals("finish")) {
                    SharetoGroupActivity.this.finish();
                }
            }
        }
    };

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
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        registerReceiver(receiver, filter);
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
        }
        acache = ACache.get(mContext);
        getAttentionedList(true, page_size, 1);
        mAttentionedListView = attentionpulllistview.getRefreshableView();
        mAttentionedListView.setFooterDividersEnabled(false);
        mAttentionedListView.setHeaderDividersEnabled(false);
        mAttentionedAdapter = new ShareAttentionedListAdapter(mContext, mAttentionList, bundle, intent);
        mAttentionedListView.setAdapter(mAttentionedAdapter);
        mAttentionedListView.setDividerHeight(2);
        attentionpulllistview.setMode(Mode.BOTH);

        attentionpulllistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                attentionpage_number = 1;
                getAttentionedList(true, page_size, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getAttentionedList(false, page_size, ++attentionpage_number);
            }
        });

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search_healthnew_et.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    page_number = 1;
                    searchTopic(true, page_size, page_number);
                }
                return false;
            }
        });

    }

    private void getAttentionedList(final boolean isRefresh, final int page_size, final int page_number) {
        attentionpulllistview.setRefreshing();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
        params.put("page_number", String.valueOf(page_number));
        params.put("remove_id", getIntent().getStringExtra("topic_id"));
        Log.i(tag, "params: " + params.toString());
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        DcareRestClient.volleyGet(Constants.GetAroundAttentionTopicList, params, new JsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                super.onCancel();
                cancelmDialog();
                attentionpulllistview.onRefreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelmDialog();
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                if (errorResponse != null) {
                    Log.e(tag, "  errorResponse: " + errorResponse.toString());
                }
                AttentionedContent content = (AttentionedContent) acache.getAsObject(CacheConstant.AttationList);

                List<AttentionedItems> list = null;
                try {
                    list = content.getItems();
                } catch (NullPointerException e) {
                    LogUtil.e(tag, "data is null");
                } catch (Exception e) {

                }
                if (list != null) {
                    if (isRefresh) {
                        mAttentionList.clear();
                    }
                    for (Iterator<AttentionedItems> iterator = list.iterator(); iterator.hasNext(); ) {
                        AttentionedItems notAttentionedItems = (AttentionedItems) iterator.next();
                        if (mAttentionList.add(notAttentionedItems)) {
                        } else {
                            Log.e(tag, "mAttentionList add error");
                        }
                    }
                    Log.i(tag, "list.size: " + list.size());

                    Log.i(tag, "mAttentionList size: " + mAttentionList.size());
                    if (isRefresh && mAttentionList.size() == 0) {
                        mAttentionList.addAll(list);
                    }
                }
                attentionpulllistview.onRefreshComplete();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                cancelmDialog();
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Log.i(tag, "getAttentionedList" + response.toString());
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// +
                    if (code == 0) {
                        Log.i(tag, "content:  " + response.getJSONObject("content").toString());
                        AttentionedContent content = JSON.parseObject(response.getJSONObject("content").toString(), AttentionedContent.class);
                        acache.put(CacheConstant.AttationList, content);
                        List<AttentionedItems> list = content.getItems();
                        if (list.size() == 0) {
                            showToast("已无更多关注的小组");
                        }
                        if (isRefresh) {
                            mAttentionList.clear();
                        }
                        if (list != null) {
                            mAttentionList.addAll(list);
                        }
                        Log.i(tag, "mAttentionList size: " + mAttentionList.size());

                        super.onSuccess(statusCode, headers, response);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                attentionpulllistview.onRefreshComplete();
            }
        });
    }

    protected void searchTopic(final boolean isRefresh, int page_size, int page_number) {
        Map<String, String> params = new HashMap<String, String>();
        String keyword = search_healthnew_et.getText().toString();
        if (StringUtils.isEmpty(keyword)) {// || keyword.length() < 2
            Toast.makeText(mContext, "关键字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("type", "1");
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
        params.put("page_number", String.valueOf(page_number));
        params.put("keyword", keyword);
        params.put("remove_id", getIntent().getStringExtra("topic_id"));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetAroundAttentionTopicList, params, new JsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                super.onCancel();
                cancelmDialog();
                attentionpulllistview.onRefreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelmDialog();
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                if (errorResponse != null) {
                    Log.e(tag, "  errorResponse: " + errorResponse.toString());
                }
                AttentionedContent content = (AttentionedContent) acache.getAsObject(CacheConstant.AttationList);

                List<AttentionedItems> list = null;
                try {
                    list = content.getItems();
                } catch (NullPointerException e) {
                    LogUtil.e(tag, "data is null");
                } catch (Exception e) {

                }
                if (list != null) {
                    if (isRefresh) {
                        mAttentionList.clear();
                    }
                    for (Iterator<AttentionedItems> iterator = list.iterator(); iterator.hasNext(); ) {
                        AttentionedItems notAttentionedItems = (AttentionedItems) iterator.next();
                        if (mAttentionList.add(notAttentionedItems)) {
                        } else {
                            Log.e(tag, "mAttentionList add error");
                        }
                    }
                    Log.i(tag, "list.size: " + list.size());

                    Log.i(tag, "mAttentionList size: " + mAttentionList.size());
                    if (isRefresh && mAttentionList.size() == 0) {
                        mAttentionList.addAll(list);
                    }
                }
                attentionpulllistview.onRefreshComplete();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                cancelmDialog();
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Log.i(tag, "getAttentionedList" + response.toString());
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// +
                    if (code == 0) {
                        Log.i(tag, "content:  " + response.getJSONObject("content").toString());
                        AttentionedContent content = JSON.parseObject(response.getJSONObject("content").toString(), AttentionedContent.class);
                        acache.put(CacheConstant.AttationList, content);
                        List<AttentionedItems> list = content.getItems();
                        if (list.size() == 0) {
                            showToast("没有搜索到相关小组");
                        }
                        if (isRefresh) {
                            mAttentionList.clear();
                        }
                        if (list != null) {
                            mAttentionList.addAll(list);
                        }
                        Log.i(tag, "mAttentionList size: " + mAttentionList.size());

                        super.onSuccess(statusCode, headers, response);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                attentionpulllistview.onRefreshComplete();
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search_share_group;
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

}

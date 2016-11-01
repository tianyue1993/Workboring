package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.SearchTopicListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.SearchTopicContent;
import com.etcomm.dcare.netresponse.SearchTopicItems;
import com.etcomm.dcare.widget.ExEditText;
import com.etcomm.dcare.widget.ExEditText.OnRightDrawableClickListener;
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
import cz.msebera.android.httpclient.Header;

/**
 * 话题搜索
 *
 * @author iexpressbox
 */
public class SearchTopicActivity extends BaseActivity {
    @Bind(R.id.pulllistview)
    PullToRefreshListView pulllistview;
    ListView listview;
    @Bind(R.id.search_topic_et)
    ExEditText search_topic_et;
    @Bind(R.id.back)
    ImageView back;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

        }

    };
    protected int page_number;
    protected int page_size = 10;
    private SearchTopicListAdapter mAdapter;
    private ArrayList<SearchTopicItems> mList = new ArrayList<SearchTopicItems>();

    
    protected synchronized void searchTopic(final boolean isRefresh, int page_size, int page_number) {
        // TODO Auto-generated method stub
        String topic = search_topic_et.getText().toString();
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", page_size + "");
        params.put("page_number", page_number + "");
        String keyword = search_topic_et.getText().toString();
        if (StringUtils.isEmpty(keyword)) {// || keyword.length() < 2
            Toast.makeText(mContext, "关键字不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("keyword", keyword);
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.SearchTopic, params, new FastJsonHttpResponseHandler() {
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
                try {
                    cancelmDialog();
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        SearchTopicContent disscomment = JSON.parseObject(response.getString("content"), SearchTopicContent.class);
                        List<SearchTopicItems> lists = disscomment.getItems();
                        if (lists != null && lists.size() > 0) {
                            if (isRefresh) {
                                mList.clear();
                                mList.addAll(lists);
                            } else {
                                for (Iterator<SearchTopicItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    SearchTopicItems disscussCommentItems = (SearchTopicItems) iterator.next();
                                    if (!mList.contains(disscussCommentItems)) {
                                        mList.add(disscussCommentItems);
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "没有搜索到相关的小组", Toast.LENGTH_SHORT).show();
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                pulllistview.onRefreshComplete();

            }
        });
    }

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {

        back.setOnClickListener(backClickListener);
        search_topic_et.setRightDrawable(null, getResources().getDrawable(R.drawable.around_search));
        search_topic_et.setRightDrawableOnClickListener(new OnRightDrawableClickListener() {
            @Override
            public void onRightDrawableClick() {
                // TODO Auto-generated method stub
                page_number = 1;
                searchTopic(true, 10, page_number);
            }
        });
        search_topic_et.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    page_number = 1;
                    searchTopic(true, 10, page_number);
                    // search pressed and perform your functionality.
                }
                return false;
            }
        });
        search_topic_et.addTextChangedListener(new TextWatcher() {

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
                if (s.length() == 0) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        listview = pulllistview.getRefreshableView();
        mAdapter = new SearchTopicListAdapter(mContext, mList, null);
        // mAttentionedAdapter = new AroundAttentionedListAdapter(getContext());
        listview.setAdapter(mAdapter);
        listview.setDividerHeight(5);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchTopicItems item = mList.get(position - 1);
                Intent intent = new Intent(mContext, TopicDisscussListActivity.class);
                Log.i(tag, "mAttentionedListView onItemClick position " + position);
                intent.putExtra("topic_id", item.getTopic_id());
                intent.putExtra("user_id", "0");
                intent.putExtra("topic_name", item.getName());
                if (item.getIs_followed().equals("1")) {
                    intent.putExtra("isAttentioned", true);
                } else {
                    intent.putExtra("isAttentioned", false);
                }
                startActivity(intent);
            }
        });
        pulllistview.setMode(Mode.BOTH);
        pulllistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number = 1;
                searchTopic(true, 10, page_number);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number++;
                searchTopic(false, 10, page_number);

            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.acitivity_seach_topic;
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

}

package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.MineSportsListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.MineSportsContent;
import com.etcomm.dcare.netresponse.MineSportsItem;
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
import cz.msebera.android.httpclient.Header;

/**
 * 我的活动
 *
 * @author iexpressbox
 */
public class MineSportsActivity extends BaseActivity {
    @Bind(R.id.listview)
    PullToRefreshListView listview;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    private ListView mListview;
    protected int page_size = 6;
    protected int page = 1;
    private MineSportsListAdapter<MineSportsItem> mAdapter;
    private ArrayList<MineSportsItem> mList = new ArrayList<MineSportsItem>();

    @Override
    public void onResume() {
        super.onResume();
        getList(true, page_size, page);
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }


    protected void getList(final boolean isRefresh, int page_size, int page_number) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
        params.put("page", String.valueOf(page_number));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.MineSportsList, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                listview.onRefreshComplete();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                listview.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        MineSportsContent disscomment = JSON.parseObject(response.getString("content"), MineSportsContent.class);
                        List<MineSportsItem> lists = disscomment.getItems();
                        if (lists != null && lists.size() > 0) {
                            if (isRefresh) {
                                mList.clear();
                                mList.addAll(lists);
                            } else {
                                for (Iterator<MineSportsItem> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    MineSportsItem disscussCommentItems = (MineSportsItem) iterator.next();
                                    if (!mList.contains(disscussCommentItems)) {
                                        mList.add(disscussCommentItems);
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if (isRefresh) {

                            } else {

                            }
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();
                listview.onRefreshComplete();

            }
        });
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        titlebar.setTitle("我的活动");
        titlebar.setLeftLl(backClickListener);
        mListview = listview.getRefreshableView();
        mAdapter = new MineSportsListAdapter<MineSportsItem>(mContext, mList);
        mListview.setAdapter(mAdapter);
        mListview.setDivider(getResources().getDrawable(R.color.suggest_item_text_color));
        mListview.setDividerHeight(10);
        mListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MineSportsItem mInfo = mAdapter.getItem(position - 1);
                Intent intent3 = new Intent(mContext, CommonWebViewActivity.class);
                intent3.putExtra(Preferences.CommonWebViewUrl, mInfo.getDetail_url());
                intent3.putExtra(Preferences.CommonWebViewTitle, "活动详情");
                intent3.putExtra("topic_name", mInfo.getTitle());
                intent3.putExtra("image", mInfo.getImage());
                intent3.putExtra("discuse", mInfo.getDesc());
                intent3.putExtra("topic_id", mInfo.getActivity_id());
                intent3.putExtra("url", mInfo.getShare_url());
                startActivity(intent3);
            }
        });
        View emptyView = getLayoutInflater().inflate(R.layout.empty_minesport_view, null);
        mListview.setEmptyView(emptyView);
        mListview.setHeaderDividersEnabled(false);
        mListview.setFooterDividersEnabled(false);
        listview.setMode(Mode.BOTH);
        listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getList(true, page_size, page);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList(false, page_size, ++page);

            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_minesports;
    }
}

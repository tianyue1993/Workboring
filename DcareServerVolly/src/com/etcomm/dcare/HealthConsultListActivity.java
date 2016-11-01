package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.HealthNewsListAdapter;
import com.etcomm.dcare.app.activity.SearchHealthNewsActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.HealthNewsContent;
import com.etcomm.dcare.netresponse.HealthNewsItems;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
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
 * HealthNewsItems 发现，健康咨讯 列表页面
 *
 * @author iexpressbox
 */
public class HealthConsultListActivity extends BaseActivity {
    protected static final int GetSuggestList_END = 0;
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.healthlist)
    PullToRefreshListView healthlist;
    // private
    protected int page_size = 6;
    protected int page_number = 1;
    private ListView healthlistview;

    private HealthNewsListAdapter mHealthAdapter;
    protected ArrayList<HealthNewsItems> list = new ArrayList<HealthNewsItems>();

    private void getHealthList(final boolean isRefresh, int page_size, int page_number) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
        params.put("page_number", String.valueOf(page_number));
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.HealthNewsList, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                healthlist.onRefreshComplete();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                healthlist.onRefreshComplete();
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
                        HealthNewsContent disscomment = JSON.parseObject(response.getString("content"), HealthNewsContent.class);
                        List<HealthNewsItems> lists = disscomment.getModel();
                        if (lists != null && lists.size() > 0) {
                            if (isRefresh) {
                                list.clear();
                                list.addAll(lists);
                            } else {
                                // list.addAll(lists);
                                for (Iterator<HealthNewsItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    HealthNewsItems disscussCommentItems = (HealthNewsItems) iterator.next();
                                    if (!list.contains(disscussCommentItems)) {
                                        list.add(disscussCommentItems);
                                    }
                                }
                            }
                            mHealthAdapter.notifyDataSetChanged();
                        } else {
                            showToast("暂无资讯");
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();
                healthlist.onRefreshComplete();

            }
        });
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {

        titlebar.setLeftLl(backClickListener);
        titlebar.setTitle("资讯中心");
        titlebar.setRightImage(R.drawable.arout_search);
        titlebar.setRightClick(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 我的咨讯
                Intent intent = new Intent(mContext, SearchHealthNewsActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        healthlistview = healthlist.getRefreshableView();
        mHealthAdapter = new HealthNewsListAdapter(HealthConsultListActivity.this, list);
        healthlistview.setAdapter(mHealthAdapter);
        healthlistview.setDividerHeight(5);
        healthlistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HealthNewsItems m = mHealthAdapter.getItem(position - 1);
                Log.i(tag, "mInfo  itemClick  : " + m.toString());
                Bundle extras = new Bundle();
                extras.putSerializable(Preferences.HealthNewsDetail, m);
                Intent intent = new Intent(HealthConsultListActivity.this, HealthConsultActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        healthlist.setMode(Mode.BOTH);
        healthlist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number = 1;
                getHealthList(true, page_size, page_number);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number++;
                getHealthList(false, page_size, page_number);

            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_healthconsultlist;
    }

    @Override
    public void onResume() {
        super.onResume();
        getHealthList(true, page_size, page_number);
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

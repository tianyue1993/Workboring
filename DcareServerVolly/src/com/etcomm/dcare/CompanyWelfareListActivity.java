package com.etcomm.dcare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.CompanyWelfareListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.CompanyWelfareContent;
import com.etcomm.dcare.netresponse.CompanyWelfareItems;
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

public class CompanyWelfareListActivity extends BaseActivity {
    protected static final int GetCompanyWelfareEnd = 0;
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.companywelfarelist)
    PullToRefreshListView companywelfarelist;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GetCompanyWelfareEnd:
                    mHealthAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private CompanyWelfareListAdapter mHealthAdapter;
    private Dialog dialog;
    protected ArrayList<CompanyWelfareItems> list = new ArrayList<CompanyWelfareItems>();
    private int page_size = 6;
    private int page_number = 1;
    private ListView listview;


    private void getList(final boolean isRefresh, int page_size, int page_number) {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
        params.put("page", String.valueOf(page_number));
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.CompanyWelfareList, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                companywelfarelist.onRefreshComplete();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                companywelfarelist.onRefreshComplete();
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
                        CompanyWelfareContent disscomment = JSON.parseObject(response.getString("content"), CompanyWelfareContent.class);
                        List<CompanyWelfareItems> lists = disscomment.getItems();
                        if (isRefresh) {
                            list.clear();
                            list.addAll(lists);
                        } else {
                            for (Iterator<CompanyWelfareItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                CompanyWelfareItems disscussCommentItems = (CompanyWelfareItems) iterator.next();
                                if (!list.contains(disscussCommentItems)) {
                                    list.add(disscussCommentItems);
                                }
                            }
                        }
                        mHealthAdapter.notifyDataSetChanged();

                        if (isRefresh && lists.size() == 0) {
                            showToast(R.string.no_welfare);
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                companywelfarelist.onRefreshComplete();

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
        titlebar.setLeftLl(backClickListener);
        titlebar.setTitle("公司福利");
        listview = companywelfarelist.getRefreshableView();
        mHealthAdapter = new CompanyWelfareListAdapter(mContext, list);
        listview.setAdapter(mHealthAdapter);
        listview.setDivider(getResources().getDrawable(R.color.suggest_item_text_color));
        listview.setDividerHeight(10);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompanyWelfareItems m = mHealthAdapter.getItem(position - 1);
                Log.i(tag, "mInfo  itemClick  : " + m.toString());
                Bundle extras = new Bundle();
                extras.putSerializable(Preferences.HealthNewsDetail, m);
                Intent intent = new Intent(mContext, CompanyWelfareActivity.class);
                intent.putExtra(Preferences.FindWelfareDetail, true);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        companywelfarelist.setMode(Mode.BOTH);
        companywelfarelist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number = 1;
                getList(true, page_size, page_number);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number++;
                getList(false, page_size, page_number);

            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_companywelfarelist;
    }

    public void onResume() {
        super.onResume();
        getList(true, page_size, page_number);
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }
}

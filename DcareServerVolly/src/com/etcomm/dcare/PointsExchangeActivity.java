package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.PointsExchangeListAdapter;
import com.etcomm.dcare.adapter.PointsExchangeListAdapter.ExchangeGift;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.PointsExchangeContent;
import com.etcomm.dcare.netresponse.PointsExchangeItems;
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
 * 积分兑换
 * 可以兑换物品，显示当前自己的积分，可进入铁 兑换中
 *
 * @author iexpressbox
 */
public class PointsExchangeActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.mypoints)
    TextView mypoints;
    @Bind(R.id.myexchange)
    TextView myexchange;
    @Bind(R.id.refreshlist)
    PullToRefreshListView refreshlist;
    private PointsExchangeListAdapter mAdapter;
    protected ArrayList<PointsExchangeItems> list = new ArrayList<PointsExchangeItems>();
    private int page_size = 6;
    private int page_number = 1;
    private ListView listview;
    private int totalpoints;

    @Override
    public void onResume() {
        super.onResume();
        getList(true, page_size, page_number);
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    private void getList(final boolean isRefresh, int page_size, int page_number) {
        // TODO Auto-generated method stub
//		RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
//		params.put("page",page_number);
        params.put("page", String.valueOf(page_number));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//			dialog = null;
//		}
//		dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
//		dialog.setCancelable(false);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.PointsExchangeList, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                refreshlist.onRefreshComplete();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                refreshlist.onRefreshComplete();
//				super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelmDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
//							+ response.getString("content").toString());
                    if (code == 0) {
                        PointsExchangeContent disscomment = JSON.parseObject(response.getString("content"), PointsExchangeContent.class);
                        List<PointsExchangeItems> lists = disscomment.getModel();
                        totalpoints = disscomment.getMy_score();
                        if (!StringUtils.isEmpty(AppSharedPreferencesHelper.getScore()) && Integer.valueOf(AppSharedPreferencesHelper.getScore()) == totalpoints) {

                        } else {
                            AppSharedPreferencesHelper.setScore(String.valueOf(totalpoints));
                        }
                        if (lists != null && lists.size() > 0) {
                            if (isRefresh) {
                                list.clear();
                                list.addAll(lists);
                            } else {
//								list.addAll(lists);
                                for (Iterator<PointsExchangeItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    PointsExchangeItems disscussCommentItems = (PointsExchangeItems) iterator.next();
                                    if (!list.contains(disscussCommentItems)) {
                                        list.add(disscussCommentItems);
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
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                refreshlist.onRefreshComplete();
//				super.onSuccess(statusCode, headers, response);

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

        titlebar.setTitle("积分兑换");
        titlebar.setLeftLl(backClickListener);
        myexchange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, MyExchangeActivity.class);
                startActivity(intent);
            }
        });
        mypoints.setText("可用积分：" + AppSharedPreferencesHelper.getScore());
        mypoints.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext, MyPointsDetailActivity.class);
                startActivity(intent);
            }
        });
        listview = refreshlist.getRefreshableView();
        mAdapter = new PointsExchangeListAdapter(mContext, list);
        mAdapter.setmPointExchanged(new ExchangeGift() {
            @Override
            public void exchangegift(int score) {
                // TODO Auto-generated method stub
                mypoints.setText("我的积分：" + (int) (Integer.valueOf(AppSharedPreferencesHelper.getScore()) - score));
                AppSharedPreferencesHelper.setScore((int) (Integer.valueOf(AppSharedPreferencesHelper.getScore()) - score) + "");
            }
        });
        listview.setAdapter(mAdapter);
        listview.setFooterDividersEnabled(false);
        listview.setHeaderDividersEnabled(false);
        listview.setDividerHeight(4);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PointsExchangeItems m = mAdapter.getItem(position - 1);
                Log.i(tag, "mInfo  itemClick  : " + m.toString());
            }
        });
        View emptyView = getLayoutInflater().inflate(
                R.layout.empty_pointsexchange_view, null);
        listview.setEmptyView(emptyView);
        refreshlist.setMode(Mode.BOTH);
        refreshlist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
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
        return R.layout.activity_pointsexchange;
    }
}

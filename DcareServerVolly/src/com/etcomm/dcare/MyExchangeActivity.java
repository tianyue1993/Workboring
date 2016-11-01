package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.MyExchangeListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.MyExchangeContent;
import com.etcomm.dcare.netresponse.MyExchangeItems;
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
 * 我的兑换  我已经兑换的和未兑换的列表
 *
 * @author iexpressbox
 */
public class MyExchangeActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.myexchangelist)
    PullToRefreshListView myexchangelist;
    private ListView listview;
    private ArrayList<MyExchangeItems> list = new ArrayList<>();
    private MyExchangeListAdapter mAdapter;
    //	private Dialog dialog;
    private int page_size = 6;
    private int page_number = 1;
    private View emptyView;

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
    public void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

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
        DcareRestClient.volleyGet(Constants.MyExchangeList, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                cancelmDialog();
                myexchangelist.onRefreshComplete();
                listview.setEmptyView(emptyView);
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                myexchangelist.onRefreshComplete();
                listview.setEmptyView(emptyView);
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
                        MyExchangeContent disscomment = JSON.parseObject(response.getString("content"), MyExchangeContent.class);
                        List<MyExchangeItems> lists = disscomment.getModel();
                        if (lists != null && lists.size() > 0) {
                            if (isRefresh) {
                                list.clear();
                                list.addAll(lists);
                            } else {
//								list.addAll(lists);
                                for (Iterator<MyExchangeItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    MyExchangeItems disscussCommentItems = (MyExchangeItems) iterator.next();
                                    if (!list.contains(disscussCommentItems)) {
                                        list.add(disscussCommentItems);
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                            listview.setEmptyView(emptyView);
                        } else {
                            if (isRefresh) {
                                listview.setEmptyView(emptyView);
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
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                if (list != null & list.size() == 0) {
                    listview.setEmptyView(emptyView);
                }
                myexchangelist.onRefreshComplete();
//				super.onSuccess(statusCode, headers, response);

            }
        });
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {

        titlebar.setTitle("我的兑换");
        titlebar.setLeftLl(backClickListener);
        listview = myexchangelist.getRefreshableView();
        mAdapter = new MyExchangeListAdapter(mContext, list);
        listview.setAdapter(mAdapter);
        listview.setDividerHeight(10);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyExchangeItems m = mAdapter.getItem(position - 1);
                Log.i(tag, "mInfo  itemClick  : " + m.toString());
            }
        });
        emptyView = getLayoutInflater().inflate(
                R.layout.empty_myexchange, null);
        TextView emptydec = (TextView) emptyView.findViewById(R.id.empty_dec);
        emptydec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PointsExchangeActivity.class);
                mContext.startActivity(intent);
                finish();
            }
        });
        String html = "暂时没有兑换任何商品\n <br/>&#10;赶紧去【<font color=\"#f37f32\">兑换</font>】吧！";
        //		 emptydec.setText(Html.fromHtml(getString(R.string.activityinfo_person_nick)));
//		 emptydec.setText(Html.fromHtml(getString(R.string.emptymyexchange)));
        emptydec.setText(Html.fromHtml(html));

        listview.setFooterDividersEnabled(false);
        listview.setHeaderDividersEnabled(false);
        myexchangelist.setMode(Mode.BOTH);
        myexchangelist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
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
        getList(true, page_size, page_number);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_myexchange;
    }
}

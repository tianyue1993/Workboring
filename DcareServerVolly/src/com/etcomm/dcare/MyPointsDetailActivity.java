package com.etcomm.dcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.MyPointsDetailListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.MyPointsDetailContent;
import com.etcomm.dcare.netresponse.MyPointsDetailItems;
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
 * 我的积分详情，显示积分来源
 *
 * @author iexpressbox
 */
public class MyPointsDetailActivity extends BaseActivity {
    @Bind(R.id.leftimage)
    ImageView leftimage;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.title_right_iv)
    ImageView title_right_iv;
    @Bind(R.id.mytotalpoints)
    TextView mytotalpoints;
    @Bind(R.id.howtoearnpoints)
    TextView howtoearnpoints;
    @Bind(R.id.refreshlist)
    PullToRefreshListView refreshlist;


    private ListView listview;
    private ArrayList<MyPointsDetailItems> list = new ArrayList<>();
    private MyPointsDetailListAdapter mAdapter;
    //	private Dialog dialog;
    private int page_size = 12;
    private int page_number = 1;
    protected int page_count = 0;

    @SuppressLint("NewApi")

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
        getList(true, page_size, page_number);
    }

    private void getList(final boolean isRefresh, int page_size, int page_number) {
        // TODO Auto-generated method stub
//		RequestParams params = new RequestParams();
        refreshlist.setRefreshing();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
//		params.put("page",page_number);
//		if(page_count!=0&&page_number>page_count){
//			Toast.makeText(mContext, "已经没有更多内容了", Toast.LENGTH_SHORT).show();
//			refreshlist.onRefreshComplete();
//			return;
//		}
        params.put("page", String.valueOf(page_number));
//		if (dialog != null && dialog.isShowing()) {
//			dialog.dismiss();
//			dialog = null;
//		}
//		dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
//		dialog.setCancelable(false);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetMyPointsDetail, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                refreshlist.onRefreshComplete();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
//				if (dialog != null && dialog.isShowing()) {
//					dialog.dismiss();
//				}
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                refreshlist.onRefreshComplete();
//				super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "pointsDetail: onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                refreshlist.onRefreshComplete();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
//							+ response.getString("content").toString());
                    if (code == 0) {
                        MyPointsDetailContent disscomment = JSON.parseObject(response.getString("content"), MyPointsDetailContent.class);
                        page_count = disscomment.getPage_count();
                        List<MyPointsDetailItems> lists = disscomment.getScore();
                        if (lists != null && lists.size() > 0) {
                            if (isRefresh) {
                                list.clear();
                                list.addAll(lists);
                            } else {
//								list.addAll(lists);
                                for (Iterator<MyPointsDetailItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    MyPointsDetailItems disscussCommentItems = (MyPointsDetailItems) iterator.next();
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
                View emptyView = getLayoutInflater().inflate(
                        R.layout.empty_myexchange, null);
                listview.setEmptyView(emptyView);
                refreshlist.onRefreshComplete();
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

        tv_title.setText("我的积分");
        leftimage.setOnClickListener(backClickListener);
        listview = refreshlist.getRefreshableView();
        //累计积分的
        mytotalpoints.setText("累计积分:" + AppSharedPreferencesHelper.getTotalScore());//
        howtoearnpoints.setText("可用积分:" + AppSharedPreferencesHelper.getScore());
        title_right_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				Intent intent = new Intent(mContext,PointsColleketDetailActivity.class);
//				startActivity(intent);
                startAtvTask(PointsColleketDetailActivity.class, "Token", SharePreferencesUtil.getToken(mContext));
//			    overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }
        });
        mAdapter = new MyPointsDetailListAdapter(mContext, list);
        listview.setAdapter(mAdapter);
        listview.setDividerHeight(2);
//		howtoearnpoints.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			}
//		});
//		listview.setFooterDividersEnabled(false);
        listview.setHeaderDividersEnabled(false);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyPointsDetailItems m = mAdapter.getItem(position - 1);
                Log.i(tag, "mInfo  itemClick  : " + m.toString());
            }
        });

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
        return R.layout.activity_mypointsdetail;
    }


}

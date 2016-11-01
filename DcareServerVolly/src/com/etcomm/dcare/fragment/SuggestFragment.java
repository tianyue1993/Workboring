package com.etcomm.dcare.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.CommonWebViewActivity;
import com.etcomm.dcare.CompanyWelfareActivity;
import com.etcomm.dcare.HealthConsultActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.adapter.SuggestListAdapter;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.SuggestContent;
import com.etcomm.dcare.netresponse.SuggestItems;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * @author etc
 * @ClassName: SuggestFragment
 * @Description: 头条
 * @date 8 Apr, 2016 1:30:59 PM
 */
public class SuggestFragment extends BaseFragment implements View.OnClickListener {
    private static final String tag = "SuggestFragment";
    PullToRefreshListView suggestlist;
    private ImageView msg_iv;
    private TextView tv_title;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GetSuggestList_END:
                    mSuggestAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ListView suggestlistview;
    private ArrayList<SuggestItems> mSuggestItems = new ArrayList<SuggestItems>();
    private SuggestListAdapter mSuggestAdapter;
    protected int page_number = 1;
    protected int page_size = 6;
    protected Dialog dialog;
    protected static final int GetSuggestList_END = 1;

    /**
     * 活动
     */
    private TextView homePagerActivity;
    /**
     * 资讯
     */
    private TextView homePagerInformation;
    /**
     * →跳转
     */
    private ImageButton homePagerSearch;
    /**
     * 保留上次筛选条件
     */
    private String searchKey = "最新";
    private boolean saveKey = true;
    /**
     * 是否进入筛选接口
     */
    private boolean isRecommend = false;
    /**
     * 排序类型 late最新、hot最热 activity活动、health资讯、welfare福利
     */
    private String sort = "late";

    @Override
    public String initContent() {
        return "SuggestFragment";
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
            msg_iv.setImageResource(R.drawable.icon_msg_unread);
        } else {
            msg_iv.setImageResource(R.drawable.icon_msg);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.homePagerActivity:
                // 活动 最新
                page_number = 1;
                if (homePagerActivity.getText().equals("活动")) {
                    sort = "activity";
                    isRecommend = true;
                    getSuggestList(true, page_size, page_number, isRecommend);
                } else {
                    sort = "late";
                    isRecommend = false;
                    getSuggestList(true, page_size, page_number, isRecommend);
                }
                selectTitleColor(homePagerActivity, homePagerInformation, homePagerActivity.getText().toString().trim());
                break;
            case R.id.homePagerInformation:
                // 资讯 最热
                page_number = 1;
                if (homePagerInformation.getText().equals("资讯")) {
                    sort = "health";
                    isRecommend = true;
                    getSuggestList(true, page_size, page_number, isRecommend);
                } else {
                    sort = "hot";
                    isRecommend = false;
                    getSuggestList(true, page_size, page_number, isRecommend);
                }
                selectTitleColor(homePagerInformation, homePagerActivity, homePagerInformation.getText().toString().trim());
                break;
            case R.id.homePagerSearch:
                //跳转筛选
                if (homePagerActivity.getText().equals("活动") && homePagerInformation.getText().equals("资讯")) {
                    homePagerActivity.setText("最新");
                    homePagerInformation.setText("最热");
                    homePagerSearch.setImageResource(R.drawable.home_page_screen_org);
                } else {
                    homePagerActivity.setText("活动");
                    homePagerInformation.setText("资讯");
                    homePagerSearch.setImageResource(R.drawable.home_page_arrows_org);
                }
                selectTitleColor(homePagerInformation, homePagerActivity, "search");
                break;
        }
    }

    /**
     * 变更点击字体颜色
     * @param textView      变色的字体
     * @param otherTextView 还原的字体
     * @param title         标题
     */
    private void selectTitleColor(TextView textView, TextView otherTextView, String title) {

        if (title.equals("活动") || title.equals("最新")) {
            searchKey = title;
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setBackgroundResource(R.drawable.title_bac);
            otherTextView.setTextColor(Color.parseColor("#999999"));
            otherTextView.setBackground(null);
        } else if (title.equals("search")) {
            //二次变更选择颜色及背景
            if (homePagerActivity.getText().equals(searchKey)) {
                homePagerActivity.setTextColor(Color.parseColor("#666666"));
                homePagerActivity.setBackgroundResource(R.drawable.title_bac);
                homePagerInformation.setTextColor(Color.parseColor("#999999"));
                homePagerInformation.setBackground(null);
                saveKey = true;
            } else if (homePagerInformation.getText().equals(searchKey)) {
                homePagerInformation.setTextColor(Color.parseColor("#666666"));
                homePagerInformation.setBackgroundResource(R.drawable.title_bac);
                homePagerActivity.setTextColor(Color.parseColor("#999999"));
                homePagerActivity.setBackground(null);
                saveKey = true;
            } else {
                textView.setTextColor(Color.parseColor("#999999"));
                otherTextView.setTextColor(Color.parseColor("#999999"));
                otherTextView.setBackground(null);
                textView.setBackground(null);
            }
        } else {
            searchKey = title;
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setBackgroundResource(R.drawable.title_bac);
            otherTextView.setTextColor(Color.parseColor("#999999"));
            otherTextView.setBackground(null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.fragment_page_suggest, null);
        homePagerActivity = (TextView) layout.findViewById(R.id.homePagerActivity);
        homePagerInformation = (TextView) layout.findViewById(R.id.homePagerInformation);
        homePagerSearch = (ImageButton) layout.findViewById(R.id.homePagerSearch);
        suggestlist = (PullToRefreshListView) layout
                .findViewById(R.id.suggestlist);
        RelativeLayout rLayout = (RelativeLayout) layout
                .findViewById(R.id.rltop);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        rLayout.measure(w, h);
        int height = rLayout.getMeasuredHeight();  //头部状态栏高度
        Rect outRect = new Rect();
        mContext.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        int invisibleheight = outRect.height();//view绘制域高度
        SharePreferencesUtil.saveHeight(mContext, invisibleheight - height);
        msg_iv = (ImageView) layout.findViewById(R.id.msg_iv);
        if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
            msg_iv.setImageResource(R.drawable.icon_msg_unread);
        } else {
            msg_iv.setImageResource(R.drawable.icon_msg);
        }
        tv_title = (TextView) layout.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.home_suggest));
        msg_iv.setOnClickListener(msgClickListener);
        suggestlistview = suggestlist.getRefreshableView();
        mSuggestAdapter = new SuggestListAdapter(getContext(), mSuggestItems, invisibleheight - height * 2);
        suggestlistview.setAdapter(mSuggestAdapter);
        suggestlistview.setHeaderDividersEnabled(false);
        suggestlistview.setFooterDividersEnabled(false);
        suggestlistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SuggestItems mInfo = mSuggestAdapter.getItem(position - 1);
                Log.i(tag, "mInfo  itemClick  : " + mInfo.toString());
                Bundle extras = new Bundle();
                extras.putSerializable(Preferences.SuggestSportDetail, mInfo);
                switch (Integer.parseInt(mInfo.getType())) {
                    // 类型 （1:健康资讯 2:企业福利 3：活动）
                    // "type_id": "1", //推荐类型id （type:health-健康资讯 welfare-企业福利
                    // activity-活动）
                    case 1:
                        Intent intent = new Intent(getActivity(),
                                HealthConsultActivity.class);
                        intent.putExtra("IsFromSuggest", true);
                        intent.putExtras(extras);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(),
                                CompanyWelfareActivity.class);
                        intent2.putExtras(extras);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(),
                                CommonWebViewActivity.class);
                        intent3.putExtra(Preferences.CommonWebViewUrl,
                                mInfo.getDetail_url());
                        intent3.putExtra(Preferences.CommonWebViewTitle, "活动详情");
                        intent3.putExtra("topic_name", mInfo.getTitle());
                        intent3.putExtra("image", mInfo.getImage());
                        intent3.putExtra("discuse", mInfo.getDesc());
                        intent3.putExtra("topic_id", mInfo.getType_id());
                        intent3.putExtra("url", mInfo.getShare_url());
                        startActivity(intent3);
                        break;
                }
            }
        });
        suggestlist.setMode(Mode.BOTH);
        suggestlist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                page_number = 1;
                getSuggestList(true, page_size, page_number, isRecommend);
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                page_number++;
                getSuggestList(false, page_size, page_number, isRecommend);

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSuggestList(true, page_size, page_number, isRecommend);
            }
        }, Preferences.LOAD_DATA_DELAY_TIME);
        suggestlistview.setDivider(getResources().getDrawable(R.color.suggest_item_text_color));
        suggestlistview.setDividerHeight(10);

        initOnClickListener();
        return layout;
    }

    /**
     * 点击监听注册
     */
    private void initOnClickListener() {
        homePagerActivity.setOnClickListener(this);
        homePagerInformation.setOnClickListener(this);
        homePagerSearch.setOnClickListener(this);
    }

    /**
     * 获取头条列表数据
     *
     * @param isRefresh    是否下拉刷新
     * @param page_size2   列表长度
     * @param page_number2 分页页数
     * @param isRecommend  时候加载筛选列表 true 筛选后 false 筛选前
     */
    protected void getSuggestList(final boolean isRefresh, int page_size2,
                                  int page_number2, boolean isRecommend) {
        // TODO Auto-generated method stub
        suggestlist.setRefreshing();
        String url;
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
        params.put("page", String.valueOf(page_number));
        if (isRecommend) {
            params.put("type", sort);
            url = Constants.GetSuggestTopicList + Constants.getRecommendFilter;
        } else {
            params.put("sort", sort);
            url = Constants.GetSuggestTopicList + Constants.getRecommend;
        }
        Log.i(tag, "getSuggestList params: " + params.toString());
        DcareRestClient.volleyGet(url, params,
                new FastJsonHttpResponseHandler() {
                    @Override
                    public void onCancel() {
                        // TODO Auto-generated method stub
                        cancelDialog();
                        Log.w(tag, "post cancel" + this.getRequestURI());

                        super.onCancel();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        cancelDialog();
                        Log.e(tag, "OnFailure:" + this.getRequestURI()
                                + " statusCode" + statusCode);
                        if (errorResponse != null) {
                            Log.e(tag,
                                    "  errorResponse: "
                                            + errorResponse.toString());
                        }

                        showToast(R.string.network_error);
                        // super.onFailure(statusCode, headers, throwable,
                        // errorResponse);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        // TODO Auto-generated method stub
                        cancelDialog();
                        try {
                            int code = response.getInteger("code");
                            String message = response.getString("message");
                            if (code != 0) {

                                exceptionCode(code);
                                return;
                            }
                            Log.i(tag, "getSuggestList:" + response.toString());
//							Log.i(tag, "onSuccess  code: " + code
//									+ " message: " + message + "content: "+ response.getJSONObject("content").toString());
                            if (code == 0) {
                                SuggestContent content = JSON.parseObject(
                                        response.getJSONObject("content")
                                                .toString(),
                                        SuggestContent.class);
                                List<SuggestItems> list = content.getItems();
                                if (isRefresh) {
                                    mSuggestItems.clear();
                                }
                                if (list != null) {
                                    for (Iterator iterator = list.iterator(); iterator
                                            .hasNext(); ) {
                                        SuggestItems suggestItems = (SuggestItems) iterator
                                                .next();
                                        if (!mSuggestItems
                                                .contains(suggestItems)) {
                                            mSuggestItems.add(suggestItems);
                                        }
                                    }
                                    // mSuggestItems.addAll(list);
                                }
                                Log.i(tag, "mAttentionList size: "
                                        + mSuggestItems.size());
                                handler.sendEmptyMessage(GetSuggestList_END);
                                suggestlist.onRefreshComplete();
                                // super.onSuccess(statusCode, headers,
                                // response);
                            } else {
                                // code不为0 发生异常
                                showToast(message);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void receive_msg_data() {
        // TODO Auto-generated method stub
        msg_iv.setImageResource(R.drawable.icon_msg_unread);
    }

    void cancelDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

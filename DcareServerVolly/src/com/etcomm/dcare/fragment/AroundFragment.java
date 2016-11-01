package com.etcomm.dcare.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.etcomm.dcare.AddNewTopicActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.SearchTopicActivity;
import com.etcomm.dcare.TopicDisscussListActivity;
import com.etcomm.dcare.adapter.AroundAttentioned2ListAdapter;
import com.etcomm.dcare.adapter.AroundNotAttentioned2ListAdapter;
import com.etcomm.dcare.adapter.AroundNotAttentioned2ListAdapter.NotAttentionAttentioned;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.ACache;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.common.CacheConstant;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.netresponse.AttentionedContent;
import com.etcomm.dcare.netresponse.AttentionedItems;
import com.etcomm.dcare.netresponse.NotAttentionedContent;
import com.etcomm.dcare.netresponse.NotAttentionedItems;
import com.etcomm.dcare.util.DensityUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * @author etc
 * @ClassName: AroundFragment
 * @Description: 主页-身边
 * @date 8 Apr, 2016 1:34:20 PM
 */
public class AroundFragment extends BaseFragment {
    private static final long LOAD_DATA_DELAyTIME = 100;
    private ImageView around_search_iv;
    private ImageView msg_iv;
    private ImageView around_editnewtopic_iv;
    private View leftindicator;
    private View rightindicator;
    private TextView around_tab_attationed;
    private TextView around_tab_notattationed;
    private PullToRefreshListView around_attentionedlist;
    private PullToRefreshListView around_notattentionedlist;
    protected Dialog dialog;
    private View notattention_emptyview;
    private View attention_emptyview;
    private ViewPager viewpager;
    private AroundAttentioned2ListAdapter mAttentionedAdapter;
    private ListView mAttentionedListView;
    private Context mContext;
    private ListView mNotAttentionListView;
    private AroundNotAttentioned2ListAdapter mNotAttentionedAdapter;
    private ArrayList<NotAttentionedItems> mNotAttentionList = new ArrayList<NotAttentionedItems>();

    int attentiontype = 1;
    private int page_size = 10;
    private int attentionpage_number = 1;
    private int notattentiontype = 0;
    private int notattentionpage_number = 1;
    private ArrayList<AttentionedItems> mAttentionList = new ArrayList<AttentionedItems>();
    protected static final String tag = AroundFragment.class.getSimpleName();
    protected static final int GetAttentionList_END = 0;
    protected static final int GetNOTAttentionList_END = 1;
    private RelativeLayout around_notattentionedlist_rl;
    private RelativeLayout around_attentionedlist_rl;
    private boolean isFirstOpen;
    private SharedPreferences sp;
    private ACache acache;
    protected TranslateAnimation animation;
    ArrayList<View> viewContainter = new ArrayList<View>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (action.equals(Preferences.REFRESH_NOTATTENTION)) {
                    getNotAttentionList(true, attentiontype, page_size, 1);
                }
            }

        }
    };
    /**
     * tab 指示器，默认是已关注为0
     */
    protected int curTab = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GetAttentionList_END:
                    // mAttentionedAdapter.alterData(mAttentionList);
                    mAttentionedAdapter = null;
                    Log.e(tag, "change view ");
                    mAttentionedAdapter = null;
                    around_attentionedlist.onRefreshComplete();
                    if (mAttentionList.size() == 0) {
                        attention_emptyview.setVisibility(View.VISIBLE);
                    } else {
                        attention_emptyview.setVisibility(View.INVISIBLE);
                    }
                    mAttentionedListView.setAdapter(null);
                    mAttentionedAdapter = new AroundAttentioned2ListAdapter(mContext, mAttentionList);
                    mAttentionedListView.setAdapter(mAttentionedAdapter);
                    mAttentionedAdapter.notifyDataSetChanged();
                    around_attentionedlist.onRefreshComplete();
                    break;
                case GetNOTAttentionList_END:
                    mNotAttentionedAdapter = null;
                    mNotAttentionListView.setAdapter(null);
                    mNotAttentionedAdapter = new AroundNotAttentioned2ListAdapter(mContext, mNotAttentionList, new NotAttentionAttentioned() {

                        @Override
                        public void onAttentioned(NotAttentionedItems item) {
                            // TODO Auto-generated method stub
                            mNotAttentionList.remove(item);
                            handler.sendEmptyMessage(GetNOTAttentionList_END);
                        }
                    });
                    mNotAttentionListView.setAdapter(mNotAttentionedAdapter);
                    mNotAttentionedAdapter.notifyDataSetChanged();
                    around_notattentionedlist.onRefreshComplete();
                    if (mNotAttentionList.size() == 0) {
                        notattention_emptyview.setVisibility(View.VISIBLE);
                    } else {
                        notattention_emptyview.setVisibility(View.INVISIBLE);
                    }
                default:
                    break;
            }
        }

    };


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        mContext = activity;
        super.onAttach(activity);
        sp = activity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        acache = ACache.get(mContext);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Preferences.REFRESH_NOTATTENTION);
        mContext.registerReceiver(receiver, filter);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_page_around, null);
        around_search_iv = (ImageView) layout.findViewById(R.id.around_search_iv);
        msg_iv = (ImageView) layout.findViewById(R.id.msg_iv);
        if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
            msg_iv.setImageResource(R.drawable.icon_msg_unread);
        } else {
            msg_iv.setImageResource(R.drawable.icon_msg);
        }

        msg_iv.setOnClickListener(msgClickListener);
        around_editnewtopic_iv = (ImageView) layout.findViewById(R.id.around_editnewtopic_iv);
        around_tab_attationed = (TextView) layout.findViewById(R.id.around_tab_attationed_li);
        around_tab_notattationed = (TextView) layout.findViewById(R.id.around_tab_notattationed_li);
        leftindicator = (View) layout.findViewById(R.id.leftindicator);
        rightindicator = (View) layout.findViewById(R.id.rightindicator);
        viewpager = (ViewPager) layout.findViewById(R.id.viewpager);
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        isFirstOpen = sp.getBoolean("IsFirstOpen", true);
        //viewpager开始添加view
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.tab1, null);
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.tab2, null);
        around_notattentionedlist_rl = (RelativeLayout) view2.findViewById(R.id.around_notattentionedlist_rl);
        around_attentionedlist_rl = (RelativeLayout) view1.findViewById(R.id.around_attentionedlist_rl);
        notattention_emptyview = view2.findViewById(R.id.notattention_emptyview);
        attention_emptyview = view1.findViewById(R.id.attention_emptyview);
        around_attentionedlist = (PullToRefreshListView) view1.findViewById(R.id.around_attentionedlist);
        around_notattentionedlist = (PullToRefreshListView) view2.findViewById(R.id.around_notattentionedlist);
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewpager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

        });

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    around_tab_notattationed.setBackground(null);
                    around_tab_notattationed.setTextColor(Color.parseColor("#656363"));
                    around_tab_attationed.setTextColor(Color.parseColor("#ffffff"));
                    if (curTab == 0) {

                    } else {
                        if (animation != null) {
                            animation.cancel();
                            animation = null;
                        }
                        animation = new TranslateAnimation(0, -DensityUtil.dip2px(mContext, 60), 0, 0);
                        animation.setInterpolator(new AccelerateInterpolator());
                        animation.setDuration(100);
                        animation.setFillAfter(true);
                        animation.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                leftindicator.setVisibility(View.VISIBLE);
                                rightindicator.setVisibility(View.INVISIBLE);
                                around_tab_attationed.setBackground(null);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                around_tab_attationed.setBackgroundResource(R.drawable.arout_tab_choosed_bg);
                                leftindicator.setVisibility(View.INVISIBLE);
                                rightindicator.setVisibility(View.INVISIBLE);
                            }
                        });
                        rightindicator.startAnimation(animation);
                    }
                    curTab = 0;
                    around_notattentionedlist_rl.setVisibility(View.GONE);
                    around_attentionedlist_rl.setVisibility(View.VISIBLE);
                    attentionpage_number = 1;
                    getAttentionedList(true, attentiontype, page_size, 1);

                } else {
                    around_tab_attationed.setBackground(null);
                    around_tab_notattationed.setTextColor(Color.parseColor("#ffffff"));
                    around_tab_attationed.setTextColor(Color.parseColor("#656363"));
                    if (curTab == 0) {
                        if (animation != null) {
                            animation.cancel();
                            animation = null;
                        }
                        animation = new TranslateAnimation(0, DensityUtil.dip2px(mContext, 60), 0, 0);
                        animation.setInterpolator(new AccelerateInterpolator());
                        animation.setDuration(100);
                        animation.setFillAfter(true);
                        animation.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                rightindicator.setVisibility(View.VISIBLE);
                                leftindicator.setVisibility(View.INVISIBLE);
                                around_tab_notattationed.setBackground(null);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                around_tab_notattationed.setBackgroundResource(R.drawable.arout_tab_choosed_bg);
                                leftindicator.setVisibility(View.INVISIBLE);
                                rightindicator.setVisibility(View.INVISIBLE);
                            }
                        });
                        leftindicator.startAnimation(animation);
                    } else {

                    }
                    curTab = 1;
                    around_attentionedlist_rl.setVisibility(View.GONE);
                    around_notattentionedlist_rl.setVisibility(View.VISIBLE);
                    notattentionpage_number = 1;
                    getNotAttentionList(true, notattentiontype, page_size, notattentionpage_number);

                }
            }
        });

        around_search_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchTopicActivity.class);
                startActivity(intent);
            }
        });
        around_editnewtopic_iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkifcreatetopicthisweek(); // 验证是否可以创建话题
            }
        });
        around_tab_attationed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //显示已关注
                viewpager.setCurrentItem(0);
            }
        });
        around_tab_notattationed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //显示未关注
                viewpager.setCurrentItem(1);
            }
        });
        mAttentionedListView = around_attentionedlist.getRefreshableView();
        mAttentionedListView.setFooterDividersEnabled(false);
        mAttentionedListView.setHeaderDividersEnabled(false);
        mAttentionedAdapter = new AroundAttentioned2ListAdapter(getContext(), mAttentionList);
        mAttentionedListView.setAdapter(mAttentionedAdapter);
        mAttentionedListView.setDividerHeight(2);
        mAttentionedListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, TopicDisscussListActivity.class);
                Log.i(tag, "mAttentionedListView onItemClick position " + position);
                intent.putExtra("topic_id", mAttentionList.get(position - 1).getTopic_id());
                intent.putExtra("user_id", mAttentionedAdapter.getItem(position - 1).getUser_id());
                intent.putExtra("topic_name", mAttentionedAdapter.getItem(position - 1).getName());
                intent.putExtra("isAttentioned", true);
                intent.putExtra("Activity_id", mAttentionList.get(position - 1).getActivity_id());
                intent.putExtra("activity_rank", mAttentionList.get(position - 1).getActivity_rank());
                startActivity(intent);
            }
        });
        around_attentionedlist.setMode(Mode.BOTH);
        around_attentionedlist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                attentionpage_number = 1;
                getAttentionedList(true, attentiontype, page_size, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getAttentionedList(false, attentiontype, page_size, ++attentionpage_number);

            }
        });
        mNotAttentionListView = around_notattentionedlist.getRefreshableView();
        mNotAttentionListView.setFooterDividersEnabled(false);
        mNotAttentionListView.setHeaderDividersEnabled(false);
        mNotAttentionListView.setBackgroundColor(Color.parseColor("#33f4f3f3"));
        mNotAttentionedAdapter = new AroundNotAttentioned2ListAdapter(getContext(), mNotAttentionList, new NotAttentionAttentioned() {

            @Override
            public void onAttentioned(NotAttentionedItems item) {
                // TODO Auto-generated method stub
                mNotAttentionList.remove(item);
                handler.sendEmptyMessage(GetNOTAttentionList_END);
            }
        });
        mNotAttentionListView.setAdapter(mNotAttentionedAdapter);
        mNotAttentionListView.setDividerHeight(2);
        mNotAttentionListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(tag, "onItemClick");
                Intent intent = new Intent(mContext, TopicDisscussListActivity.class);
                NotAttentionedItems m = mNotAttentionList.get(position - 1);
                intent.putExtra("topic_id", m.getTopic_id());
                intent.putExtra("user_id", m.getUser_id());
                intent.putExtra("topic_name", m.getName());
                intent.putExtra("isAttentioned", false);
                intent.putExtra("Activity_id", m.getActivity_id());
                intent.putExtra("activity_rank", m.getActivity_rank());
                startActivity(intent);
            }
        });
        around_notattentionedlist.setMode(Mode.BOTH);
        around_notattentionedlist.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                notattentionpage_number = 1;
                getNotAttentionList(true, attentiontype, page_size, notattentionpage_number);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getNotAttentionList(false, attentiontype, page_size, ++notattentionpage_number);

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getIfuserHaveFollowedTopic();
            }
        }, LOAD_DATA_DELAyTIME);
        return layout;
    }

    protected void getIfuserHaveFollowedTopic() {
        // TODO Auto-generated method stub
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();

        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        around_attentionedlist.setRefreshing();
        DcareRestClient.volleyGet(Constants.GetIfUserHaveFollowedTopic, params, new JsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                around_tab_notattationed.performClick();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                cancelDialog();
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// +
                    // response.getJSONObject("content").toString());
                    if (code == 0) {
                        around_tab_attationed.performClick();
                    } else {// code不为0 发生异常
                        around_tab_notattationed.performClick();
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void getNotAttentionList(final boolean b, int notattentiontype2, int notattentionpage_size2, int notattentionpage_number2) {
        around_notattentionedlist.setRefreshing();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "0");
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(notattentionpage_size2));
        params.put("page_number", String.valueOf(notattentionpage_number2));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetAroundAttentionTopicList, params, new JsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                cancelDialog();
                Log.w(tag, "post cancel" + this.getRequestURI());

                super.onCancel();
                around_notattentionedlist.onRefreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelDialog();
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                if (errorResponse != null) {
                    Log.e(tag, "  errorResponse: " + errorResponse.toString());
                }
                NotAttentionedContent content = (NotAttentionedContent) acache.getAsObject(CacheConstant.NotAttationList);
                if (content != null) {
                    List<NotAttentionedItems> list = content.getItems();

                    if (list != null) {
                        if (b) {
                            mNotAttentionList.clear();
                        }
                        for (Iterator<NotAttentionedItems> iterator = list.iterator(); iterator.hasNext(); ) {
                            NotAttentionedItems notAttentionedItems = (NotAttentionedItems) iterator.next();
                            if (mNotAttentionList.add(notAttentionedItems)) {
                                Log.i(tag, "notAttentionedItems : " + notAttentionedItems.toString());
                            } else {
                                Log.e(tag, "mNotAttentionList add error");
                            }
                        }
                        Log.i(tag, "list.size: " + list.size());

                        Log.i(tag, "mNotAttentionList size: " + mNotAttentionList.size());
                        if (b && mNotAttentionList.size() == 0) {
                            mNotAttentionList.addAll(list);
                        }
                    }
                }

                around_notattentionedlist.onRefreshComplete();
                handler.sendEmptyMessage(GetNOTAttentionList_END);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// +
                    // response.getJSONObject("content").toString());
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    if (code == 0) {
                        Log.i(tag, "content:  " + response.getJSONObject("content").toString());
                        NotAttentionedContent content = JSON.parseObject(response.getJSONObject("content").toString(), NotAttentionedContent.class);
                        List<NotAttentionedItems> lists = content.getItems();
//                        if (list.size() == 0) {
//                            showToast("已无最新内容");
//                        }
//                        if (b) {
//                            mNotAttentionList.clear();
//                        }
//                        if (list != null) {
//                            if (b) {
//                                acache.put(CacheConstant.NotAttationList, content);
//                            }
//                            for (Iterator<NotAttentionedItems> iterator = list.iterator(); iterator.hasNext(); ) {
//                                NotAttentionedItems notAttentionedItems = (NotAttentionedItems) iterator.next();
//                                if (mNotAttentionList.add(notAttentionedItems)) {
//                                    Log.i(tag, "notAttentionedItems : " + notAttentionedItems.toString());
//                                } else {
//                                    Log.e(tag, "mNotAttentionList add error");
//                                }
//                            }
//                            Log.i(tag, "list.size: " + list.size());
//
//                            Log.i(tag, "mNotAttentionList size: " + mNotAttentionList.size());
//                            if (b && mNotAttentionList.size() == 0) {
//                                mNotAttentionList.addAll(list);
//                            }
//                        }
//
//                        handler.sendEmptyMessage(GetNOTAttentionList_END);
//                        around_notattentionedlist.onRefreshComplete();
//                        super.onSuccess(statusCode, headers, response);


                        if (lists != null && lists.size() > 0) {
                            if (b) {
                                mNotAttentionList.clear();
                                mNotAttentionList.addAll(lists);
                            } else {
                                // list.addAll(lists);
                                for (Iterator<NotAttentionedItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    NotAttentionedItems disscussCommentItems = (NotAttentionedItems) iterator.next();
                                    if (!mAttentionList.contains(disscussCommentItems)) {
                                        mNotAttentionList.add(disscussCommentItems);
                                    }
                                }
                            }
                            mNotAttentionedAdapter.notifyDataSetChanged();
                        } else {
                            showToast("已无最新内容");
                        }
                        around_notattentionedlist.onRefreshComplete();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param isRefresh
     * @param type
     * @param page_size
     * @param page_number
     */
    private void getAttentionedList(final boolean isRefresh, final int type, final int page_size, final int page_number) {

        around_attentionedlist.setRefreshing();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(page_size));
        params.put("page_number", String.valueOf(page_number));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetAroundAttentionTopicList, params, new JsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                cancelDialog();
                Log.w(tag, "post cancel" + this.getRequestURI());
                super.onCancel();
                around_attentionedlist.onRefreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                cancelDialog();
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
                around_attentionedlist.onRefreshComplete();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Log.i(tag, "getAttentionedList" + response.toString());
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// +
                    // response.getJSONObject("content").toString());

                    if (code == 0) {
                        Log.i(tag, "content:  " + response.getJSONObject("content").toString());
                        AttentionedContent content = JSON.parseObject(response.getJSONObject("content").toString(), AttentionedContent.class);
                        acache.put(CacheConstant.AttationList, content);
                        List<AttentionedItems> lists = content.getItems();

                        if (lists != null && lists.size() > 0) {
                            if (isRefresh) {
                                mAttentionList.clear();
                                mAttentionList.addAll(lists);
                            } else {
                                // list.addAll(lists);
                                for (Iterator<AttentionedItems> iterator = lists.iterator(); iterator.hasNext(); ) {
                                    AttentionedItems disscussCommentItems = (AttentionedItems) iterator.next();
                                    if (!mAttentionList.contains(disscussCommentItems)) {
                                        mAttentionList.add(disscussCommentItems);
                                    }
                                }
                            }
                            mAttentionedAdapter.notifyDataSetChanged();
                        } else {
                            showToast("已无最新内容");
                        }

                    } else {
                        showToast(message);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                around_attentionedlist.onRefreshComplete();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (curTab == 0) {
            attentionpage_number = 1;
            getAttentionedList(true, attentiontype, page_size, 1);
        } else {
            notattentionpage_number = 1;
            getNotAttentionList(true, notattentiontype, page_size, notattentionpage_number);
        }
        if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
            msg_iv.setImageResource(R.drawable.icon_msg_unread);
        } else {
            msg_iv.setImageResource(R.drawable.icon_msg);
        }
    }

    @Override
    public String initContent() {
        return "AroundFragment";
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

    private void checkifcreatetopicthisweek() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        DcareRestClient.volleyGet(Constants.CheckCreateTopic, params, new JsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + "response" + response);
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message);

                    Intent it = new Intent(mContext, AddNewTopicActivity.class);
                    if (code == 0) {
                        it.putExtra(Preferences.ADD_TOPIC_CHECK, true);
                    } else if (code == 30005) {
                        it.putExtra(Preferences.ADD_TOPIC_CHECK, false);

                    }
                    startActivity(it);

                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    // TODO: handle exception
                    Log.e(tag, "NullPointerException:");
                }

                cancelDialog();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }


}

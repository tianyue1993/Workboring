package com.etcomm.dcare;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.MsgListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.MsgListItems;
import com.etcomm.dcare.util.StringUtils;
import com.etcomm.dcare.widget.DialogFactory;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.umeng.analytics.MobclickAgent;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class MsgListActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.emptyview)
    View emptyview;
    @Bind(R.id.sdlistview)
    SlideAndDragListView sdlistview;
    private MsgListAdapter adapter;
    /**
     * 保存上一次的推送信息
     */
    private SharedPreferences sp;
    // private LoadingDialog dialog;
    protected List<MsgListItems> mMsgList = new ArrayList<>();
    private int mScreenWidth;
    private Dialog commonDialog;

    @Override
    public void onResume() {
        super.onResume();
        AppSharedPreferencesHelper.setHaveReceiveUnReadData(false);
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
    protected void onStart() {
        // TODO Auto-generated method stub
        getMSGList();
        super.onStart();
    }

    private void getMSGList() {
        // TODO Auto-generated method stub
        // private void getList(final boolean isRefresh,int page_size,int
        // page_number) {
        // TODO Auto-generated method stub
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        // params.put("page",page_number);
        // if (dialog != null && dialog.isShowing()) {
        // dialog.dismiss();
        // dialog = null;
        // }
        cancelmDialog();
        // dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
        // dialog.setCancelable(false);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.MSGList, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                // super.onFailure(statusCode, headers, throwable,
                // errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG, "getMSGList+ " + response.toString());
                // Log.e(tag, "MSGList onSuccess:" + this.getRequestURI() +
                // " statusCode" + statusCode + " response "
                // + response.toString());
                cancelmDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    // + response.getString("content").toString());
                    if (code == 0) {
                        JSONArray jsonarray = response.getJSONArray("content");
                        if (jsonarray.size() > 0) {
                            mMsgList.clear();
                        }
                        for (int index = 0; index < jsonarray.size(); index++) {
                            MsgListItems item = new MsgListItems();
                            JSONObject jsonobject = (JSONObject) jsonarray.get(index);
                            String news_id = jsonobject.getString("news_id");
                            String time = jsonobject.getString("time");
                            String title = jsonobject.getString("title");
                            String topic_name = jsonobject.getString("topic_name");
                            String detail = jsonobject.getString("detail");
                            String topic_list_type = jsonobject.getString("topic_list_type");
                            String topic_id = jsonobject.getString("topic_id");
                            // jsonobject.containsKey("detail_id");
                            // String detail_str =
                            // jsonobject.getString("detail_id");
                            String detail_id = jsonobject.getString("detail_id");
                            String picNamesArray = jsonobject.getString("picNamesArray");
                            StringBuilder sb = new StringBuilder(picNamesArray);
                            sb.deleteCharAt(0);
                            sb.deleteCharAt(sb.length() - 1);
                            picNamesArray = sb.toString().replaceAll("\"", "");
                            String[] strs = picNamesArray.split(",");
                            List<String> list = new ArrayList<>();
                            if (strs != null && strs.length > 0) {
                                for (int i = 0; i < strs.length; i++) {
                                    if (!StringUtils.isEmpty(strs[i]))
                                        list.add(strs[i]);
                                }
                                Log.i(tag, "picNamesArray size: " + list.size());
                            }
                            String is_like = jsonobject.getString("is_like");
                            String user_id = jsonobject.getString("user_id");
                            String avatar = jsonobject.getString("avatar");
                            String type = jsonobject.getString("type");
                            item.setTopic_id(topic_id);
                            item.setTopic_list_type(topic_list_type);
                            item.setAvatar(avatar);
                            item.setDetail(detail);
                            item.setDetail_id(detail_id);
                            item.setIs_like(is_like);
                            item.setNews_id(news_id);
                            item.setPicNamesArray(list);
                            item.setTime(time);
                            item.setTitle(title);
                            item.setType(type);
                            item.setTopic_name(topic_name);
                            item.setUser_id(user_id);
                            Log.i(tag, "MsgListItems item:" + item.toString());
                            mMsgList.add(mMsgList.size(), item);
                        }
                        // MsgListContent disscomment =
                        // JSON.parseObject(response.getString("content"),MsgListContent.class);
                        // List<MsgListItems> lists = disscomment.getContent();
                        // if (lists != null && lists.size() > 0) {
                        // for(int i = 0 ;i<lists.size();i++){
                        // MsgListItems item = lists.get(i);
                        // Log.i(tag, "MsgListItems item:"+item.toString());
                        //
                        // }
                        if (mMsgList.size() < 1) {
                            emptyview.setVisibility(View.VISIBLE);
                        } else {
                            emptyview.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        // }else{
                        // }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }

                // super.onSuccess(statusCode, headers, response);

            }
        });
    }

    protected void deleteAllMsg() {
        // TODO Auto-generated method stub
        commonDialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定删除该消息", "取消", "确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                commonDialog.dismiss();
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                commonDialog.dismiss();
                deleteAllMsgFromNet();
            }
        }, Color.BLACK, Color.BLACK);
    }

    protected void deleteAllMsgFromNet() {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        // if (dialog != null && dialog.isShowing()) {
        // dialog.dismiss();
        // dialog = null;
        // }
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        // dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
        // dialog.setCancelable(false);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DeleteMsgByUserid, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                // Toast.makeText(mContext, "修改失败，请检查网络连接",
                // Toast.LENGTH_SHORT).show();
                // super.onFailure(statusCode, headers, throwable,
                // errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "deleteAllMsgFromNet onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    // Log.i(tag, "onSuccess code: " + code + " message: " +
                    // message + "content: "
                    // + response.getString("content").toString());
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        mMsgList.clear();
                        emptyview.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                // super.onSuccess(statusCode, headers, response);

            }
        });
    }

    protected void deleteThisMSG(final MsgListItems item) {
        // TODO Auto-generated method stub
        commonDialog = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定删除该消息", "取消", "确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                commonDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                commonDialog.dismiss();
                deleteMsgFromNet(item);
            }
        }, Color.BLACK, Color.BLACK);
    }

    protected void deleteMsgFromNet(final MsgListItems item) {
        // TODO Auto-generated method stub
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("news_id", String.valueOf(item.getNews_id()));
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        // if (dialog != null && dialog.isShowing()) {
        // dialog.dismiss();
        // dialog = null;
        // }
        cancelmDialog();
        // dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
        // dialog.setCancelable(false);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DeleteMsgByUserid, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                // Toast.makeText(mContext, "修改失败，请检查网络连接",
                // Toast.LENGTH_SHORT).show();
                // super.onFailure(statusCode, headers, throwable,
                // errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "DeleteMsgByUserid onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    // Log.i(tag, "onSuccess code: " + code + " message: " +
                    // message + "content: "
                    // + response.getString("content").toString());
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        mMsgList.remove(item);
                        adapter.notifyDataSetChanged();
                        if (mMsgList.size() < 1) {
                            emptyview.setVisibility(View.VISIBLE);
                        } else {
                            emptyview.setVisibility(View.GONE);
                        }
                    } else {// code不为0 发生异常
                        // mMsgList.add(0, item);
                        if (mMsgList.size() < 1) {
                            emptyview.setVisibility(View.VISIBLE);
                        } else {
                            emptyview.setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                // super.onSuccess(statusCode, headers, response);

            }
        });
    }

    protected void deleteALLMsgFromNet() {
        // TODO Auto-generated method stub
        // RequestParams params = new RequestParams();
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        // if (dialog != null && dialog.isShowing()) {
        // dialog.dismiss();
        // dialog = null;
        // }
        cancelmDialog();
        // dialog = new LoadingDialog(mContext, R.layout.view_tips_loading2);
        // dialog.setCancelable(false);
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.show();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DeleteAllMsg, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                // Toast.makeText(mContext, "修改失败，请检查网络连接",
                // Toast.LENGTH_SHORT).show();
                // super.onFailure(statusCode, headers, throwable,
                // errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "DeleteAllMsg onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    // Log.i(tag, "onSuccess code: " + code + " message: " +
                    // message + "content: "
                    // + response.getString("content").toString());
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();
                // if (dialog != null && dialog.isShowing()) {
                // dialog.dismiss();
                // }
                // super.onSuccess(statusCode, headers, response);

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
        sp = getPreferences(MODE_PRIVATE);
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        mScreenWidth = width;
        titlebar.setTitle("消息");
        titlebar.setLeftLl(backClickListener);
        titlebar.setRightText("清空消息");

        titlebar.setRightClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mMsgList.size() > 0) {

                    deleteAllMsg();
                } else {
                    showToast("当前没有消息");
                    // showToast(mContext, "当前没有消息", Toast.LENGTH_SHORT);
                }
            }
        });
        // Menu leftmenu = new Menu(new ColorDrawable(Color.WHITE), true,
        // 0);//the second parameter is whether can slide over
        Menu menu = new Menu(new ColorDrawable(Color.WHITE), true, 0);// the
        // second
        // parameter
        // is
        // whether
        // can
        // slide
        // over
        menu.addItem(new MenuItem.Builder().setWidth(200)// set Width
                .setBackground(new ColorDrawable(Color.RED))// set background
                .setText("删除")// set text string
                .setDirection(MenuItem.DIRECTION_RIGHT).setTextColor(Color.WHITE)// set
                        // text
                        // color
                .setTextSize(20)// set text size
                .build());
        // set in sdlv
        // sdlistview.set
        sdlistview.setMenu(menu);
        sdlistview.setDividerHeight(1);
        sdlistview.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {
            @Override
            public void onSlideOpen(View view, View parentView, int position, int direction) {
                Log.i(tag, "onSlideOpen position:" + position + "  direction: " + direction);
            }

            @Override
            public void onSlideClose(View view, View parentView, int position, int direction) {
                Log.i(tag, "onSlideClose position:" + position + "  direction: " + direction);
            }
        });
        sdlistview.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                Log.i(tag, "onMenuItemClick itemPosition:" + itemPosition + " buttonPosition: " + buttonPosition + " direction: " + direction);
                switch (direction) {
                    case MenuItem.DIRECTION_LEFT:
                        switch (buttonPosition) {
                            case 0:// One
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 0:// icon
                                MsgListItems item = adapter.getItem(itemPosition);
                                deleteThisMSG(item);
                                // sdlistview
                                return Menu.ITEM_NOTHING;
                            // return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                        }
                        break;
                    default:
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_NOTHING;
            }
        });
        sdlistview.setOnListItemClickListener(new SlideAndDragListView.OnListItemClickListener() {
            @Override
            public void onListItemClick(View v, int position) {
                Log.i(tag, "onListItemClick position:" + position);
                MsgListItems obj = adapter.getItem(position);

                if (obj.getType().equals("2")) {
                    if (obj.getTopic_list_type().equals("1")) {// 点赞
                        Intent notifyintent = new Intent(MsgListActivity.this, TopicDisscussListActivity.class);
                        notifyintent.putExtra("topic_id", String.valueOf(obj.getDetail_id()));
                        notifyintent.putExtra("user_id", String.valueOf(obj.getUser_id()));
                        notifyintent.putExtra("isAttentioned", obj.getIs_like());
                        // notifyintent.putExtra("topic_name",
                        // obj.getTopic_name());/// ??????????????????????????
                        notifyintent.putExtra("topic_name", obj.getDetail());// /
                        notifyintent.putExtra("isFromMSG", true);
                        startActivity(notifyintent);
                    } else if (obj.getTopic_list_type().equals("2")) {
                        Intent notifyintent = new Intent(MsgListActivity.this, DisscussConentListActivity.class);
                        notifyintent.putExtra("topic_id", String.valueOf(obj.getTopic_id()));
                        notifyintent.putExtra("disscuss_id", obj.getDetail_id());
                        notifyintent.putExtra("isAttentioned", obj.getIs_like());
                        notifyintent.putExtra("topic_name", obj.getTopic_name());// /
                        notifyintent.putExtra("isFromMSG", true);
                        startActivity(notifyintent);

                    }

                } else {
                    Log.e(TAG, "点击后台通知信息");

                }

            }
        });
        sdlistview.setOnItemDeleteListener(new SlideAndDragListView.OnItemDeleteListener() {
            @Override
            public void onItemDelete(View view, int position) {
                // Log.i(tag, "onItemDelete position:" + position);
                // MsgListItems item = adapter.getItem(position);
                // mMsgList.remove(item);
                // adapter.notifyDataSetChanged();
                // deleteThisMSG(item);
            }
        });
        adapter = new MsgListAdapter(mContext, mMsgList, mScreenWidth);
        sdlistview.setAdapter(adapter);
        // View emptyView = getLayoutInflater().inflate(R.layout.emptymsglist,
        // null);
        // sdlistview.setEmptyView(emptyView);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_msglist;
    }
}

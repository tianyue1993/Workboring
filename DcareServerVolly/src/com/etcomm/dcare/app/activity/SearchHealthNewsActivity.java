package com.etcomm.dcare.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.HealthConsultActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.adapter.HealthNewsListAdapter;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.HealthNewsContent;
import com.etcomm.dcare.netresponse.HealthNewsItems;
import com.etcomm.dcare.netresponse.SuggestItems;
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

public class SearchHealthNewsActivity extends BaseActivity {

	@Bind(R.id.search_healthnew_et)
	com.etcomm.dcare.widget.ExEditText search_healthnew_et;
	@Bind(R.id.pulllistview)
	PullToRefreshListView pulllistview;
	@Bind(R.id.cancel)
	TextView cancel;

	protected int page_size = 6;
	protected int page_number = 1;
	private HealthNewsListAdapter mHealthAdapter;
	ListView listView;
	protected ArrayList<HealthNewsItems> list = new ArrayList<HealthNewsItems>();
	int searchType;

	@Override
	protected Activity atvBind() {
		return this;
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
	private void searchHealthnews(final boolean isRefresh, int page_size, int page_number) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", SharePreferencesUtil.getToken(mContext));
		params.put("page_size", String.valueOf(page_size));
		params.put("page_number", String.valueOf(page_number));
		params.put("keyword", search_healthnew_et.getText().toString());
		showProgress(DIALOG_DEFAULT, true);
		Log.i(tag, "params: " + params.toString());
		DcareRestClient.volleyGet(Constants.HealthNewsSearch, params, new FastJsonHttpResponseHandler() {
			@Override
			public void onCancel() {
				Log.w(tag, "post cancel" + this.getRequestURI());
				cancelmDialog();
				pulllistview.onRefreshComplete();
				super.onCancel();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
				cancelmDialog();
				Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
				pulllistview.onRefreshComplete();
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
								for (Iterator<HealthNewsItems> iterator = lists.iterator(); iterator.hasNext();) {
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
					Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				cancelmDialog();
				pulllistview.onRefreshComplete();

			}
		});
	}

	private void searcCollecthHealthnews(final boolean isRefresh, int page_size, int page_number) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", SharePreferencesUtil.getToken(mContext));
		params.put("page_size", String.valueOf(page_size));
		params.put("page_number", String.valueOf(page_number));
		params.put("keyword", search_healthnew_et.getText().toString());
		params.put("type", "1");
		showProgress(DIALOG_DEFAULT, true);
		Log.i(tag, "params: " + params.toString());
		DcareRestClient.volleyGet(Constants.HealthNewsSearch, params, new FastJsonHttpResponseHandler() {
			@Override
			public void onCancel() {
				Log.w(tag, "post cancel" + this.getRequestURI());
				cancelmDialog();
				pulllistview.onRefreshComplete();
				super.onCancel();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
				cancelmDialog();
				Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
				pulllistview.onRefreshComplete();
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
								for (Iterator<HealthNewsItems> iterator = lists.iterator(); iterator.hasNext();) {
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
				pulllistview.onRefreshComplete();

			}
		});
	}

	@Override
	protected void initDatas() {
		Intent intent = getIntent();
		searchType = intent.getIntExtra("type", 0);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		search_healthnew_et.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					page_number = 1;
					if (searchType == 1) {
						searchHealthnews(true, page_size, page_number);
					} else if (searchType == 2) {
						searcCollecthHealthnews(true, page_size, page_number);
					}

				}
				return false;
			}
		});

		listView = pulllistview.getRefreshableView();
		mHealthAdapter = new HealthNewsListAdapter(mContext, list);
		listView.setAdapter(mHealthAdapter);
		listView.setDividerHeight(5);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HealthNewsItems m = mHealthAdapter.getItem(position - 1);
				Log.i(tag, "mInfo  itemClick  : " + m.toString());
				Bundle extras = new Bundle();
				SuggestItems mInfo = new SuggestItems();
				mInfo.setCreated_at(m.getCreated_at());
				mInfo.setCustomer_id(m.getCustomer_id());
				mInfo.setDetail_url(m.getDetail_url());
				mInfo.setImage(m.getRectangle_image());
				mInfo.setPv(m.getPv());
				mInfo.setStatus(m.getLike_status());
				mInfo.setTitle(m.getTitle());
				extras.putSerializable(Preferences.HealthNewsDetail, m);
				Intent intent = new Intent(mContext, HealthConsultActivity.class);
				intent.putExtras(extras);
				startActivity(intent);
			}
		});
		pulllistview.setMode(Mode.BOTH);
		pulllistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				page_number = 1;
				if (searchType == 1) {
					searchHealthnews(true, page_size, page_number);
				} else if (searchType == 2) {
					searcCollecthHealthnews(true, page_size, page_number);
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				page_number++;
				if (searchType == 1) {
					searchHealthnews(false, page_size, page_number);
				} else if (searchType == 2) {
					searcCollecthHealthnews(false, page_size, page_number);
				}

			}
		});

	}

	@Override
	protected int setLayoutId() {
		return R.layout.activity_search_healthnewa;
	}

}

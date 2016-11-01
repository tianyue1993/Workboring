package com.etcomm.dcare.fragment;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.CompanyWelfareListActivity;
import com.etcomm.dcare.ExamineReportActivity;
import com.etcomm.dcare.HealthConsultListActivity;
import com.etcomm.dcare.PointsExchangeActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 *
 * @ClassName: FindFragment
 * @Description: 主页-发现
 * @author etc
 * @date 8 Apr, 2016 1:48:05 PM
 */
public class FindFragment extends BaseFragment {
	LinearLayout find_examine_report_li;
	LinearLayout find_healthconsult_li;
	LinearLayout find_companywelfare_li;
	LinearLayout find_pointsexchange_li;
	LinearLayout find_welfarelotteryli;

	LinearLayout find_healthconsult_li2;
	LinearLayout find_companywelfare_li2;
	LinearLayout find_pointsexchange_li2;
	LinearLayout find_welfarelotteryli2;

	LinearLayout layout1;
	LinearLayout layout2;

	private ImageView msg_iv;
	private TextView tv_title;
	private int type;

	// SimpleTitleBar titlebar;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (LinearLayout) inflater.inflate(R.layout.fragment_page_find, null);
		RelativeLayout rLayout = (RelativeLayout) layout.findViewById(R.id.rltop);
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		rLayout.measure(w, h);
		int height = rLayout.getMeasuredHeight(); // 头部状态栏高度
		Rect outRect = new Rect();
		mContext.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
		int invisibleheight = outRect.height();// view绘制域高度
		SharePreferencesUtil.saveHeight(mContext, invisibleheight - height);
		find_examine_report_li = (LinearLayout) layout.findViewById(R.id.find_examine_report_li);
		find_healthconsult_li = (LinearLayout) layout.findViewById(R.id.find_healthconsult_li);
		find_companywelfare_li = (LinearLayout) layout.findViewById(R.id.find_compynysaliry_li);
		find_pointsexchange_li = (LinearLayout) layout.findViewById(R.id.find_pointsexchange_li);
		find_welfarelotteryli = (LinearLayout) layout.findViewById(R.id.find_welfarelottery_li);

		find_healthconsult_li2 = (LinearLayout) layout.findViewById(R.id.find_healthconsult_li2);
		find_companywelfare_li2 = (LinearLayout) layout.findViewById(R.id.find_compynysaliry_li2);
		find_pointsexchange_li2 = (LinearLayout) layout.findViewById(R.id.find_pointsexchange_li2);
		find_welfarelotteryli2 = (LinearLayout) layout.findViewById(R.id.find_welfarelottery_li2);

		layout1 = (LinearLayout) layout.findViewById(R.id.layout1);// 全显示
		layout2 = (LinearLayout) layout.findViewById(R.id.layout2);// 去掉体检报告

		msg_iv = (ImageView) layout.findViewById(R.id.msg_iv);
		if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
			msg_iv.setImageResource(R.drawable.icon_msg_unread);
		} else {
			msg_iv.setImageResource(R.drawable.icon_msg);
		}
		tv_title = (TextView) layout.findViewById(R.id.tv_title);
		tv_title.setText(R.string.home_find);
		msg_iv.setOnClickListener(msgClickListener);
		find_examine_report_li.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtil.e(tag, ">>>>>" + SharePreferencesUtil.getToken(mContext));
				startAtvTask(ExamineReportActivity.class, "Token", SharePreferencesUtil.getToken(mContext));
				// Intent intent = new
				// Intent(getActivity(),ExamineReportActivity.class);
				// getActivity().startActivity(intent);
			}
		});
		find_healthconsult_li.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// PRD-ETCOM-APP-企业健康需求规格说明书V1.0-2016052701 需求 暂时不显示
				Intent intent = new Intent(getActivity(), HealthConsultListActivity.class);
				getActivity().startActivity(intent);
			}
		});
		find_companywelfare_li.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CompanyWelfareListActivity.class);
				getActivity().startActivity(intent);
			}
		});
		find_pointsexchange_li.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PointsExchangeActivity.class);
				getActivity().startActivity(intent);
				// getActivity().overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_right_out);
			}
		});
		/**
		 * 福利抽奖
		 */
		find_welfarelotteryli.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// PRD-ETCOM-APP-企业健康需求规格说明书V1.0-2016052701 需求 暂时不显示
				showToast(R.string.dev_ing);
				// LogUtil.e(tag,">>>>>>WelfareLotteryActivity>>>"+
				// SharePreferencesUtil.getToken(mContext));
				// startAtvTask(WelfareLotteryActivity.class,
				// "Token",SharePreferencesUtil.getToken(mContext));
				// // Intent intent = new
				// Intent(getActivity(),WelfareLotteryActivity.class);
				// // getActivity().startActivity(intent);

			}
		});

		find_healthconsult_li2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// PRD-ETCOM-APP-企业健康需求规格说明书V1.0-2016052701 需求 暂时不显示
				Intent intent = new Intent(getActivity(), HealthConsultListActivity.class);
				getActivity().startActivity(intent);
			}
		});
		find_companywelfare_li2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CompanyWelfareListActivity.class);
				getActivity().startActivity(intent);
			}
		});
		find_pointsexchange_li2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), PointsExchangeActivity.class);
				getActivity().startActivity(intent);
				// getActivity().overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_right_out);
			}
		});
		/**
		 * 福利抽奖
		 */
		find_welfarelotteryli2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// PRD-ETCOM-APP-企业健康需求规格说明书V1.0-2016052701 需求 暂时不显示
				showToast(R.string.dev_ing);
				// LogUtil.e(tag,">>>>>>WelfareLotteryActivity>>>"+
				// SharePreferencesUtil.getToken(mContext));
				// startAtvTask(WelfareLotteryActivity.class,
				// "Token",SharePreferencesUtil.getToken(mContext));
				// // Intent intent = new
				// Intent(getActivity(),WelfareLotteryActivity.class);
				// // getActivity().startActivity(intent);

			}
		});
		getModle();
		return layout;
	}

	@Override
	public String initContent() {
		return "FindFragment";
	}

	@Override
	public void receive_msg_data() {
		// TODO Auto-generated method stub
		msg_iv.setImageResource(R.drawable.icon_msg_unread);
	}

	// 获取公司定制展示模块
	public void getModle() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", SharePreferencesUtil.getToken(mContext));
		showProgress(DIALOG_DEFAULT, true);
		Log.i(tag, "params: " + params.toString());
		DcareRestClient.volleyGet(Constants.USER_MODULE, params, new FastJsonHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, Throwable arg3, JSONObject jsonobject) {
				Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + arg0);
				cancelmDialog();
				showToast(R.string.network_error);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					int code = response.getInteger("code");
					String message = response.getString("message");
					String content = response.getString("content");
					Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: " + response.getString("content"));
					// + response.getString("content").toString());
					if (code == 0 && content != null) {
						if (content.contains("体检报告")) {
							layout1.setVisibility(View.VISIBLE);
							layout2.setVisibility(View.GONE);// 全显示
						} else {
							layout1.setVisibility(View.GONE);
							layout2.setVisibility(View.VISIBLE);
						}
					} else {
					}
				} catch (JSONException e) {
					Log.e(tag, "JSONException:");
					e.printStackTrace();
				}
				cancelmDialog();
			}

		});

	}
}

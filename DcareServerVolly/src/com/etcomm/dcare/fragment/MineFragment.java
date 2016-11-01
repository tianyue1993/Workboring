package com.etcomm.dcare.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.MineFeedBackActivity;
import com.etcomm.dcare.MineSportsActivity;
import com.etcomm.dcare.MyCollectionActivity;
import com.etcomm.dcare.MyPointsDetailActivity;
import com.etcomm.dcare.PedometerActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.activity.MyCountActivity;
import com.etcomm.dcare.app.activity.setting.SettingActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.common.AppSettingPreferencesHelper;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.Login;
import com.etcomm.dcare.util.LogUtil;
import com.etcomm.dcare.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * @author etc
 * @ClassName: MineFragment
 * @Description: 主页-我的
 * @date 8 Apr, 2016 1:48:20 PM
 */
public class MineFragment extends BaseFragment {
    int code = 0;
    String message = "";
    TextView mine_name_tv;
    LinearLayout mine_name_department_li;
    TextView mine_department_tv;
    TextView total_tv_mileage;
    TextView total_tv_motiontimes;
    TextView total_iv_caliries;
    TextView mine_signin_tv;
    RelativeLayout mine_minesport_rl;
    RelativeLayout mine_minedevice_rl;
    RelativeLayout mine_minefeedback_rl;
    RelativeLayout mine_minesetting_rl;
    RelativeLayout mine_minecollect_rl;
    RelativeLayout mine_minecount_rl;
    private ImageView msg_iv;
    private TextView tv_title;
    private SharedPreferences sp;
    CircleImageView mine_avator_circleiv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = (View) inflater.inflate(R.layout.fragment_page_mine, null);
        RelativeLayout rLayout = (RelativeLayout) layout.findViewById(R.id.rltop);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        rLayout.measure(w, h);
        int height = rLayout.getMeasuredHeight(); // 头部状态栏高度
        Rect outRect = new Rect();
        mContext.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        int invisibleheight = outRect.height();// view绘制域高度
        SharePreferencesUtil.saveHeight(mContext, invisibleheight - height);
        mine_name_tv = (TextView) layout.findViewById(R.id.mine_name_tv);
        sp = getActivity().getSharedPreferences(Preferences.SignIn, Context.MODE_PRIVATE);
        mine_department_tv = (TextView) layout.findViewById(R.id.mine_department_tv);
        mine_name_department_li = (LinearLayout) layout.findViewById(R.id.mine_name_department_li);
        total_tv_mileage = (TextView) layout.findViewById(R.id.total_tv_mileage);
        total_tv_motiontimes = (TextView) layout.findViewById(R.id.total_tv_motiontimes);
        total_iv_caliries = (TextView) layout.findViewById(R.id.total_iv_caliries);
        mine_signin_tv = (TextView) layout.findViewById(R.id.mine_signin_tv);
        mine_minecount_rl = (RelativeLayout) layout.findViewById(R.id.mine_minecount_rl);

        mine_avator_circleiv = (CircleImageView) layout.findViewById(R.id.mine_avator_circleiv);
        mine_minesport_rl = (RelativeLayout) layout.findViewById(R.id.mine_minesport_rl);
        mine_minedevice_rl = (RelativeLayout) layout.findViewById(R.id.mine_minedevice_rl);
        mine_minefeedback_rl = (RelativeLayout) layout.findViewById(R.id.mine_minefeedback_rl);
        mine_minesetting_rl = (RelativeLayout) layout.findViewById(R.id.mine_minesetting_rl);
        mine_minecollect_rl = (RelativeLayout) layout.findViewById(R.id.mine_minecollect_rl);
        /** 当前已经签到的时间 如果今天已经签到 已签到不可点击 灰色 */
        long time = sp.getLong(AppSharedPreferencesHelper.getUserId() + "-" + Preferences.SignInTime, 0);
        DateTime datetime = new DateTime();
        if (time != 0 && time < datetime.millisOfDay().withMaximumValue().getMillis() && time > datetime.millisOfDay().withMinimumValue().getMillis()) {
            // 已经签到
            // mine_signin_ib.setBackgroundResource(R.drawable.mine_signined);
            // mine_signin_ib.setClickable(false);
            // mine_signin_ib.setFocusable(false);
        }
        msg_iv = (ImageView) layout.findViewById(R.id.msg_iv);
        tv_title = (TextView) layout.findViewById(R.id.tv_title);
        // titlebar = (SimpleTitleBar) layout.findViewById(R.id.titlebar);
        tv_title.setText(getString(R.string.home_mine));
        msg_iv.setOnClickListener(msgClickListener);
        if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
            msg_iv.setImageResource(R.drawable.icon_msg_unread);
        } else {
            msg_iv.setImageResource(R.drawable.icon_msg);
        }

        mine_signin_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), MyPointsDetailActivity.class);
                getActivity().startActivity(intent);
            }
        });
        mine_minedevice_rl.setOnClickListener(new OnClickListener() {// 我的计步设置

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PedometerActivity.class);
                getActivity().startActivity(intent);
            }
        });
        mine_minesport_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MineSportsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        mine_minefeedback_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), MineFeedBackActivity.class);
                getActivity().startActivity(intent);
            }
        });
        mine_minesetting_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
            }
        });

        mine_minecollect_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
                getActivity().startActivity(intent);

            }
        });
        mine_minecount_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCountActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return layout;
    }

    @Override
    public void onResume() {
        getIfDeviceCanConnect();
        if (!TextUtils.isEmpty(AppSharedPreferencesHelper.getPedometer_distance()) || !TextUtils.isEmpty(AppSharedPreferencesHelper.getScore())) {
            ImageLoader.getInstance().displayImage(AppSharedPreferencesHelper.getAvatar(), mine_avator_circleiv);
            mine_name_tv.setText(AppSharedPreferencesHelper.getNick_name());
            mine_department_tv.setText(AppSharedPreferencesHelper.getStructure());// 部门
            mine_signin_tv.setText("可用积分:" + AppSharedPreferencesHelper.getScore());

            DecimalFormat decimalFormat = new DecimalFormat("0.0");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
            if (!TextUtils.isEmpty(AppSharedPreferencesHelper.getPedometer_distance())) {
                total_tv_mileage.setText(decimalFormat.format(Float.parseFloat(AppSharedPreferencesHelper.getPedometer_distance())));
            }
            if (!TextUtils.isEmpty(AppSharedPreferencesHelper.getPedometer_time())) {
                total_tv_motiontimes.setText(decimalFormat.format(Float.parseFloat(AppSharedPreferencesHelper.getPedometer_time())));
            }
            if (!TextUtils.isEmpty(AppSharedPreferencesHelper.getPedometer_consume())) {
                total_iv_caliries.setText(decimalFormat.format(Float.parseFloat(AppSharedPreferencesHelper.getPedometer_consume())));
            }
        }
        if (AppSharedPreferencesHelper.getHaveReceiveUnReadData()) {
            msg_iv.setImageResource(R.drawable.icon_msg_unread);
        } else {
            msg_iv.setImageResource(R.drawable.icon_msg);
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserInfo();
    }

    private void getUserInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetuserInfo, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                showToast(R.string.network_error);
                cancelmDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                // cancelDialog();
                cancelmDialog();
                try {
                    code = response.getInteger("code");
                    message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        Login login = JSON.parseObject(response.getJSONObject("content").toString(), Login.class);
                        // 暂存数据，后续方便使用
                        Log.i(tag, "保存到mineCurrent: " + response.getString("content"));
                        AppSharedPreferencesHelper.setBirth_year(login.getBirth_year());
                        AppSharedPreferencesHelper.setAvatar(login.getAvatar());
                        AppSharedPreferencesHelper.setHeight(login.getHeight());
                        AppSharedPreferencesHelper.setWeight(login.getWeight());
                        AppSharedPreferencesHelper.setStructure(login.getStructure());
                        AppSharedPreferencesHelper.setGender(login.getGender());
                        AppSharedPreferencesHelper.setScore(login.getScore());
                        AppSharedPreferencesHelper.setTotalScore(login.getTotal_score());
                        AppSharedPreferencesHelper.setNick_name(login.getNick_name());
                        AppSharedPreferencesHelper.setPedometer_target(login.getPedometer_target());
                        AppSharedPreferencesHelper.setPedometer_distance(login.getPedometer_distance());
                        AppSharedPreferencesHelper.setPedometer_time(login.getPedometer_time());
                        AppSharedPreferencesHelper.setPedometer_consume(login.getPedometer_consume());
                        sp.edit().putString(Preferences.MineCurrent, response.getString("content")).commit();
                        ImageLoader.getInstance().displayImage(login.getAvatar(), mine_avator_circleiv);
                        mine_name_tv.setText(login.getNick_name());
                        mine_department_tv.setText(login.getStructure());// 部门
                        mine_signin_tv.setText("可用积分:" + AppSharedPreferencesHelper.getScore() + "");
                        DecimalFormat decimalFormat = new DecimalFormat("0.0");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
                        total_tv_mileage.setText(decimalFormat.format(Float.parseFloat(login.getPedometer_distance())));
                        total_tv_motiontimes.setText(decimalFormat.format(Float.parseFloat(login.getPedometer_time())));
                        total_iv_caliries.setText(decimalFormat.format(Float.parseFloat(login.getPedometer_consume())));
                        return;
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                } finally {
                    LogUtil.e("获取个人资料成功>>>>>" + message);

                }
            }
        });
    }

    @Override
    public String initContent() {
        return "MineFragment";
    }

    @Override
    public void receive_msg_data() {
        // TODO Auto-generated method stub
        msg_iv.setImageResource(R.drawable.icon_msg_unread);
    }

    // 判断计步方式
    protected void getIfDeviceCanConnect() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        LogUtil.i(tag, Constants.IsDeviceCanBind + ">>params: " + params.toString());
        DcareRestClient.volleyPost(Constants.IsDeviceCanBind, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                showToast(R.string.network_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    if (code == 0) {
                        String status = response.getJSONObject("content").getString("status");

                        if (status.equals("1")) {
                            // 已绑定
                            String mac = response.getJSONObject("content").getString("bracelet_mac");
                            String bracelet_name = response.getJSONObject("content").getString("bracelet_name");
                            AppSharedPreferencesHelper.setBlueDeviceName(bracelet_name);
                            AppSharedPreferencesHelper.setMacAddress(mac);
                            AppSettingPreferencesHelper.setIfSoftPedometerOn(false);

                        } else {
                            AppSharedPreferencesHelper.setBlueDeviceName("");
                            AppSharedPreferencesHelper.setMacAddress("");
                            AppSettingPreferencesHelper.setIfSoftPedometerOn(true);

                        }
                    } else {// code不为0 发生异常
                        showToast(message);
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

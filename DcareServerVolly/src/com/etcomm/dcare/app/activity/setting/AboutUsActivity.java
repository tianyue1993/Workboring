package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.UpdateObj;
import com.etcomm.dcare.widget.DialogFactory;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

//import com.etcomm.dcare.view.LoadingDialog;

public class AboutUsActivity extends BaseActivity {
    @Bind(R.id.titlebar)
    SimpleTitleBar titlebar;
    @Bind(R.id.app_version)
    TextView app_version;
    @Bind(R.id.app_name)
    TextView app_name;
    @Bind(R.id.version_update)
    RelativeLayout version_update;
    protected String apkurl;
    private Dialog noticeDialog;
    private BroadcastReceiver receiver;
    private boolean flag = true;

    @OnClick({R.id.version_update, R.id.titlebar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.version_update:
                checkUpdate(getVersion());
                break;
            case R.id.titlebar:
                finish();
                break;
        }
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

    private void checkUpdate(String version) {
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("version", version);

        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.CheckUpdate, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelmDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (message.equals("已是最新版本")) {
                        Toast.makeText(mContext, "已是最新版本!", Toast.LENGTH_SHORT).show();
                        Log.i(tag, "message " + message);
                        return;
                    }
                    if (code == 45000) {
                        UpdateObj content = JSON.parseObject(response.getString("content"), UpdateObj.class);
                        String versiononServer = content.getVersion();// ("version");
                        String dec = content.getDescription();// getString("versiononServer");
                        apkurl = content.getFile();// getString("file");
                        // String versionOnServer = content;
                        Long[] longVersionOnServer = stringVersionToLong(versiononServer);
                        Long[] longVersionOnClient = stringVersionToLong(getVersion().replaceAll("v", ""));
                        System.out.println(longVersionOnServer[0] + ",,,," + longVersionOnClient[0] + ",,," + longVersionOnServer[1] + ",,,," + longVersionOnClient[1]);
                        if (longVersionOnServer[0] > 0 && longVersionOnClient[0] > 0) {// 粗略检测version值合法性
                            boolean neadUpgrade = false;
                            if (longVersionOnServer[0] > longVersionOnClient[0]) {
                                neadUpgrade = true;
                                showNoticeDialog(dec);
                                return;
                            } else if (longVersionOnServer[0] == longVersionOnClient[0] && longVersionOnServer[1] > longVersionOnClient[1]) {
                                neadUpgrade = true;
                                showNoticeDialog(dec);
                                return;
                            } else {
                                Log.i(tag, "版本没有更新 ");
                                Toast.makeText(mContext, "已是最新版本!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.i(tag, "版本号有问题 ");
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver();
        super.onDestroy();
    }

    void registerReceiver() {
        receiver = new DownLoadBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.DOWNLOAD_COMPLETE");
        ;
        registerReceiver(receiver, filter);
    }

    void unregisterReceiver() {
        unregisterReceiver(receiver);
    }

    /**
     * 根据版本号返回new long[3]。用于比较版本新旧。
     *
     * @param version
     * @return
     */
    public Long[] stringVersionToLong(String version) {
        Long[] longVersion = new Long[2];
        try {
            if (!StringUtils.isEmpty(version)) {
                String[] temp = version.split("\\.");
                if (temp != null && temp.length == 2) {
                    longVersion[0] = Long.parseLong(temp[0]);
                    longVersion[1] = Long.parseLong(temp[1]);
                }
            }
        } catch (NumberFormatException e) {
            Log.e(tag, "readLocalVersion " + e.toString());
            e.printStackTrace();
        }
        return longVersion;
    }

    private void showNoticeDialog(String updatecontent) {

        noticeDialog = DialogFactory.getDialogFactory().showUpdateVersionDialog(this, getString(R.string.update_title), updatecontent, getString(R.string.update_later), getString(R.string.update_now), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "建议现在更新", Toast.LENGTH_SHORT).show();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    intoDownloadManager();
                }
                Toast.makeText(mContext, "后台正在下载安装包", Toast.LENGTH_LONG).show();
            }
        }, R.color.switch_texton_color, R.color.switch_texton_color);
        noticeDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false; // 默认返回 false
                }
            }
        });
    }

    private void downloadApk() {
        Log.i(tag, "apkurl:  " + apkurl);
        intoDownloadManager();
    }

    private void intoDownloadManager() {
        flag = false;
        DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Log.i(tag, "intoDownloadManager");
        Uri uri = Uri.parse(apkurl);
        Request request = new Request(uri);
        // 设置下载路径和文件名
        request.setDestinationInExternalPublicDir("dcare", "dcare.apk");
        request.setDescription(getString(R.string.app_name) + "更新");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
        // 把当前下载的ID保存起来
        SharedPreferences sPreferences = getSharedPreferences("downloadapp", 0);
        sPreferences.edit().putLong("plato", refernece).commit();

    }

    public class DownLoadBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            SharedPreferences sPreferences = context.getSharedPreferences("downloadapp", 0);
            long refernece = sPreferences.getLong("plato", 0);
            if (refernece == myDwonloadID) {
                String serviceString = Context.DOWNLOAD_SERVICE;
                DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {
        registerReceiver();
        titlebar.setTitle("关于我们");
        app_version.setText("V" + getVersion());
        app_name.setText(getApplicationName());
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_aboutus;
    }


}

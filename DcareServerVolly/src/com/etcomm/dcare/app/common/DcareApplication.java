package com.etcomm.dcare.app.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.config.TimeZoneChangedReceiver;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.service.BootBroadcastReceiver;
import com.etcomm.dcare.service.Receiver1;
import com.etcomm.dcare.service.Service2;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import me.chunyu.pedometerservice.PedometerCounterService;

//import com.iwown.android_iwown_ble.bluetooth.BluetoothDataParseBiz;

public class DcareApplication extends DaemonApplication {
    public static Context mCon;

    // -----------优化分割线--------------
    private static DcareApplication mInstance = null;
    private List<Activity> mActivityLists = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        mCon = this.getApplicationContext();
        PlatformConfig.setWeixin("wx8d48744b331cf979", "8b83f56633c08f675eaf79ab0d9d0fc4");
        // BluetoothDataParseBiz.getInstance(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "900028182", false);
        if (Constants.log2File) {
            PropertyConfigurator.getConfigurator(this).configure();
            LogUtil.logger = LoggerFactory.getLogger(DcareApplication.class);
        }

        SharedPreferences preference = getSharedPreferences(Preferences.LOG_ON_KEY_TAG, Context.MODE_PRIVATE);
        if (Constants.enableEngineerMode) {
            LogUtil.IS_LOG = preference.getBoolean("isLog", true);
        } else {
            LogUtil.IS_LOG = false;
        }
        SharePreferencesUtil.saveEtcBoolean(this, SharePreferencesUtil.Prefe_Forever, Preferences.USER_LOGING, true);
        // ------------分割------------------
        mInstance = this;
        initImageLoader();
        init(this);
        DcareRestClient.initVolley(this);
        // JodaTimeAndroid.init(this);
        // 异常处理，不需要处理时注释掉这两句即可！
        /*********************************************/
        /*********************************************/
        /*********************************************/
        // /部分手机这部分会出错，发新版本，务必注释掉这句话
        // CrashHandler crashHandler = CrashHandler.getInstance();
        // // 注册crashHandler
        // crashHandler.init(getApplicationContext());
        super.onCreate();
        PedometerCounterService.initAppService(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PedometerCounterService.releaseAppService();
    }

    /**
     * Whether the JodaTimeAndroid.init() method has been called.
     */
    private static boolean sInitCalled = false;

    /**
     * Initializes ResourceZoneInfoProvider and registers an instance of
     * {@link TimeZoneChangedReceiver} to receive
     * android.intent.action.TIMEZONE_CHANGED broadcasts. This method does
     * nothing if previously called.
     */
    public static void init(Context context) {
        if (sInitCalled) {
            return;
        }

        sInitCalled = true;
        context.getApplicationContext().registerReceiver(new TimeZoneChangedReceiver(), new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));
    }

    public synchronized void addActivity(Activity activity) {
        mActivityLists.add(activity);
    }

    public synchronized void finishActivity(Activity currentActivity) {
        Iterator<Activity> it = mActivityLists.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (activity != null && !activity.isFinishing() && !activity.getClass().isInstance(currentActivity)) {
                activity.finish();
            }
        }
    }

    private void initImageLoader() {

        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultDisplayImageOptions).threadPriority(Thread.NORM_PRIORITY).discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO).memoryCache(new WeakMemoryCache()).writeDebugLogs().build();

        ImageLoader.getInstance().init(config);
    }

    public static DcareApplication getInstance() {
        return mInstance;
    }

    /**
     * you can override this method instead of {@link android.app.Application
     * attachBaseContext}
     *
     * @param base
     */
    @Override
    public void attachBaseContextByDaemon(Context base) {
        super.attachBaseContextByDaemon(base);
    }

    /**
     * give the configuration to lib in this callback
     *
     * @return
     */
    @Override
    protected DaemonConfigurations getDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration("com.etcomm.dcare:process1", PedometerCounterService.class.getCanonicalName(), Receiver1.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration("com.etcomm.dcare:process2", Service2.class.getCanonicalName(), BootBroadcastReceiver.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }

}

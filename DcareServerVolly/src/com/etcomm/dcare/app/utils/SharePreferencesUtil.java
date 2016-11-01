package com.etcomm.dcare.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;

import org.joda.time.DateTime;

import java.io.File;

public class SharePreferencesUtil {
    private static SharePreferencesUtil mSpInstance;
    static final String TAG = "SharePreferencesUtil";
    public static String Prefe_Etc = "etc";
    public static String Prefe_step_data = "step_data";// 临时方案存数软件计步数据
    public static String Prefe_blueData = "bluedata";

    public static String Prefe_Forever = "forever"; // 用于一直存放变量，除非应用被卸载
    public static String KEY_LOCATION_NAME = "location_name";
    public static String KEY_LOCATION_NO = "location_no";
    public static String KEY_RUN_TIME = "run_time";// 运行时长
    public static String KEY_FIRST_ENTER = "first_enter";// 第一次进入时间

    public static String FIRST_OPEN = "first_open";// 第一次进入时间
    private static String INFO_STATE = "info_state";
    private static String FIRST_GETSTEP = "first_getstep";

    public static void saveImage(Context context, String uid) {
        deleteNoSameData(context, uid);
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.CUSTOMER_IMAGE, uid).commit();
    }

    public static void saveOpen(Context context, Boolean uid) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putBoolean(FIRST_OPEN, uid).commit();
    }

    /**
     * 是否从新填充首页网络数据
     *
     * @param context
     * @param uid
     */
    public static void saveReData(Context context, Boolean uid) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putBoolean(Preferences.SAVE_RE_DATA, uid).commit();
    }

    /**
     * 获取是否从新填充首页网络数据
     *
     * @param context
     * @return
     */
    public static boolean getReData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getBoolean(Preferences.SAVE_RE_DATA, false);
    }

    /**
     * 保存新老版本版本号 用于方便比较
     *
     * @param context
     * @return
     */
    public static void saveVersionInfo(Context context, String version) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.VERSION_INFO, version).commit();
    }

    /**
     * 获取新老版本号用于比较
     *
     * @param context
     * @return
     */
    public static String getVersionInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.VERSION_INFO, "");
    }

    /**
     * 存储当前用户注册部门信息
     * @param context
     * @return
     */
    public static void saveDepartmentInfo(Context context,String info){
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.DEPARTMENTINFO, info).commit();
    }

    /**
     * 获取当前用户注册部门信息
     * @param context
     * @return
     */
    public static String getDepartmentInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.DEPARTMENTINFO, "");
    }
    /**
     * 存储当前用户注册部门信息
     * @param context
     * @return
     */
    public static void saveDepartmentInfoId(Context context,String info){
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.DEPARTMENTINFOID, info).commit();
    }

    /**
     * 获取当前用户注册部门信息
     * @param context
     * @return
     */
    public static String getDepartmentInfoId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.DEPARTMENTINFOID, "");
    }


    // 是否是第一次启动splash
    public static boolean getOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getBoolean(FIRST_OPEN, true);
    }

    // 用户信息是否完整
    public static void saveInfoState(Context context, Boolean uid) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putBoolean(INFO_STATE, uid).commit();
    }

    public static boolean getInfoState(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getBoolean(INFO_STATE, false);// 不完整
    }

    // 用户是否是第一次请求获取网络长传的步数
    public static void saveFirstGetstep(Context context, Boolean uid) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putBoolean(FIRST_GETSTEP, uid).commit();
    }

    public static boolean getFirstGetstep(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getBoolean(FIRST_GETSTEP, true);// 默认为第一次请求需要存储本地步数，存储结束后，置为false
    }

    // 用户是否是第一次请求获取网络长传的步数
    public static void saveFirstGet(Context context, Boolean uid) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putBoolean("get", uid).commit();
    }

    public static boolean getFirstGet(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getBoolean("get", true);// 默认为第一次请求需要存储本地步数，存储结束后，置为false
    }

    public static String getImage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.CUSTOMER_IMAGE, null);
    }

    // 保存uid
    public static void saveUid(Context context, String uid) {
        deleteNoSameData(context, uid);
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.USER_UID, uid).commit();
    }

    // 获取uid
    public static String getUid(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.USER_UID, "0");
    }

    // 获取用户名
    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.USER_NAME, "");
    }

    // 保存用户名
    public static void saveUserName(Context context, String mobileNum) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.USER_NAME, mobileNum).commit();

    }


    // 获取号码
    public static String getPhone(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString("phone", "");
    }

    public static void savePhone(Context context, String mobileNum) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString("phone", mobileNum).commit();

    }


    // 获取邮件
    public static String getEmail(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString("email", "");
    }

    public static void saveEmail(Context context, String mobileNum) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString("email", mobileNum).commit();

    }


    // 保存用户昵称
    public static void saveNickName(Context context, String realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.NICK_NAME, realName).commit();
    }

    // 获取用户昵称
    public static String getNickName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.NICK_NAME, "");
    }

    // 保存token
    public static void saveToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.ACCESS_TOKEN, token).commit();

    }

    // 获取token
    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.ACCESS_TOKEN, "");
    }

    // public String pedometer_target; //运动目标
    // 保存运动目标
    public static void savePedometerTarget(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.PEDOMETER_TARGET, token).commit();

    }

    // 获取运动目标
    public static String getsavePedometerTarget(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.PEDOMETER_TARGET, "");
    }

    // public String pedometer_distance; //运动积累距离
    // 保存运动积累距离
    public static void savePedometerDistance(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.PEDOMETER_DISTANCE, token).commit();
    }

    // 获取运动积累距离
    public static String getPedometerDistance(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.PEDOMETER_DISTANCE, "");
    }

    // public String pedometer_time;//运动积累时间
    // 保存运动积累时间
    public static void savePedometerTime(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.PEDOMETER_TIME, token).commit();
    }

    // 获取运动积累时间
    public static String getPedometerTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.PEDOMETER_TIME, "");
    }

    // public String pedometer_consume;//运动积累消耗
    // 保存运动积累消耗
    public static void savePedometerConsume(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.PEDOMETER_CONSUME, token).commit();
    }

    // 获取运动积累消耗
    public static String getPedometerConsume(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.PEDOMETER_CONSUME, "");
    }

    // public String avatar; //用户头像
    // 获取用户头像
    public static String getAvatar(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.USER_AVATOR, "");
    }

    // 保存用户头像
    public static void saveAvatar(Context context, String realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.USER_AVATOR, realName).commit();
    }

    // 保存密码
    public static void savePassWord(Context context, String pwd) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.PASS_WORD, pwd).commit();

    }

    // 保存登录状态
    public static void saveIslevel(Context context, boolean pwd) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean("islevel", pwd).commit();

    }

    // 获取登录状态
    public static boolean getIslevel(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc,
                Context.MODE_PRIVATE);
        return sp.getBoolean("islevel", false);
    }

    // 获取密码
    public static String getPassWord(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.PASS_WORD, "");
    }

    // 获取工号
    public static String getJobNumber(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString(Preferences.JOB_NUMBER, "");
    }

    // 保存工号
    public static void saveJobNumber(Context context, String mobileNum) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.JOB_NUMBER, mobileNum).commit();
    }

    /**
     * 存储临时当前计步
     */
    public static void saveHocStep(Context context, int stepCount) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_step_data, Context.MODE_PRIVATE);
        sp.edit().putInt(Preferences.SAVE_HOC_STEP, stepCount).commit();
    }

    /**
     * 获取当前临时计步
     */
    public static int getHocStep(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_step_data, Context.MODE_PRIVATE);
        return sp.getInt(Preferences.SAVE_HOC_STEP, 0);
    }


    // 临时存储软件计步数据
    public static void saveTmpStep(Context context, float todayTotalSteps, float todayTotalMiles, float todayTotalSeconds, float todayTotalCaliries) {
        String nowTime = new DateTime().toString("yyyyMMdd");
        String uid = AppSharedPreferencesHelper.getUserId();
        SharedPreferences sp = context.getSharedPreferences(Prefe_step_data, Context.MODE_PRIVATE);
        sp.edit().putFloat(nowTime + "-" + uid + Preferences.DaySteps, todayTotalSteps).commit();
        sp.edit().putFloat(nowTime + "-" + uid + Preferences.DayMile, todayTotalMiles).commit();
        sp.edit().putFloat(nowTime + "-" + uid + Preferences.DaySeconds, todayTotalSeconds).commit();
        sp.edit().putFloat(nowTime + "-" + uid + Preferences.DayCalories, todayTotalCaliries).commit();

    }

    // 临时获取软件计步数据
    public static float getTmpStep(Context context, String date) {
        // String nowTime = new DateTime().toString("yyyyMMdd");
        String uid = AppSharedPreferencesHelper.getUserId();
        SharedPreferences sp = context.getSharedPreferences(Prefe_step_data, Context.MODE_PRIVATE);
        return sp.getFloat(date + "-" + uid + Preferences.DaySteps, 0);
        // return sp.getFloat("TotalSteps", 0);
    }

    // 临时获取软件计步里程
    public static float getTmpMiles(Context context, String date) {
        // String nowTime = new DateTime().toString("yyyyMMdd");
        String uid = AppSharedPreferencesHelper.getUserId();
        SharedPreferences sp = context.getSharedPreferences(Prefe_step_data, Context.MODE_PRIVATE);
        return sp.getFloat(date + "-" + uid + Preferences.DayMile, 0);
    }

    // 临时获取软件计步秒数
    public static float getTmpSeconds(Context context, String date) {
        // String nowTime = new DateTime().toString("yyyyMMdd");
        String uid = AppSharedPreferencesHelper.getUserId();
        SharedPreferences sp = context.getSharedPreferences(Prefe_step_data, Context.MODE_PRIVATE);
        return sp.getFloat(date + "-" + uid + Preferences.DaySeconds, 0);
    }

    // 临时获取软件计步卡路里
    public static float getTmpCaliries(Context context, String date) {
        // String nowTime = new DateTime().toString("yyyyMMdd");
        String uid = AppSharedPreferencesHelper.getUserId();
        SharedPreferences sp = context.getSharedPreferences(Prefe_step_data, Context.MODE_PRIVATE);
        return sp.getFloat(date + "-" + uid + Preferences.DayCalories, 0);
    }

    // 获取用户对象json
    public static String getLoginInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString("LoginInfo", "");
    }

    // 保存用户对象json
    public static void saveLoginInfo(Context context, String realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString("LoginInfo", realName).commit();
    }

    // public String is_sign; ////签到状态 1:已签到 0：未签到
    // 获取签到状态
    public static String getIsSign(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString("isSign", "");
    }

    // 保存签到状态
    public static void saveIsSign(Context context, String realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString("isSign", realName).commit();

    }

    // 获取高度值
    public static int getHeight(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getInt("height", 0);
    }

    // 保存高度值
    public static void saveHeight(Context context, int realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putInt("height", realName).commit();

    }


    // 获取高度值
    public static int getHeigh(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getInt("heigh", 0);
    }

    // 保存高度值
    public static void saveHeigh(Context context, int realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putInt("heigh", realName).commit();

    }


    // 获取是否分享
    public static boolean getIfShare(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getBoolean("getIfShare", false);
    }

    // 保存是否分享
    public static void saveIfShare(Context context, boolean b) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putBoolean("getIfShare", b).commit();

    }

    // 获取蓝牙数据
    public static String getBlueData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_blueData, Context.MODE_PRIVATE);
        return sp.getString(Preferences.ActionBlueStep, "");
    }

    // 保存蓝牙数据
    public static void saveBlueData(Context context, String realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_blueData, Context.MODE_PRIVATE);
        sp.edit().putString(Preferences.ActionBlueStep, realName).commit();

    }

    // 获取蓝牙连接状态
    public static boolean getBlueConn(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_blueData, Context.MODE_PRIVATE);
        return sp.getBoolean(Preferences.ActionBlueConn, false);
    }

    // 保存蓝牙连接状态
    public static void saveBlueConn(Context context, Boolean realName) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_blueData, Context.MODE_PRIVATE);
        sp.edit().putBoolean(Preferences.ActionBlueConn, realName).commit();

    }


    // username 用户名称 邮箱
    // /////// public String birthday; //生日
    // 获取生日
    public static String getBirthday(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getString("birthday", "");
    }

    // 保存生日
    public static void saveBirthday(Context context, String birthday) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putString("birthday", birthday).commit();

    }

    /**
     * 设置字符值
     *
     * @param context
     * @param name
     * @param value
     */
    public static void saveEtcString(Context context, String prefeName, String name, String value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putString(name, value).commit();
    }

    /**
     * 获取字符值
     *
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static String getEtcString(Context context, String prefeName, String name, String def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getString(name, def);
    }

    /**
     * 设置字符值
     *
     * @param context
     * @param name
     * @param value
     */
    public static void saveEtcLong(Context context, String prefeName, String name, long value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putLong(name, value).commit();
    }

    /**
     * 获取字符值
     *
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static long getEtcLong(Context context, String prefeName, String name, long def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getLong(name, def);
    }

    /**
     * 设置int值
     *
     * @param context
     * @param name
     * @param value
     */
    public static void saveEtcInt(Context context, String prefeName, String name, int value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putInt(name, value).commit();
    }

    /**
     * 设置int值,在之前的值上加
     *
     * @param context
     * @param name
     * @param value
     */
    public static void saveEtcIntAdd(Context context, String prefeName, String name, int value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putInt(name, value + getEtcInt(context, prefeName, name, 0)).commit();
    }

    /**
     * 获取int值
     *
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static int getEtcInt(Context context, String prefeName, String name, int def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getInt(name, def);
    }

    /**
     * 设置boolean值
     *
     * @param context
     * @param name
     * @param value
     */
    public static void saveEtcBoolean(Context context, String prefeName, String name, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(name, value).commit();
    }

    /**
     * 获取boolean值
     *
     * @param context
     * @param name
     * @param def
     * @return
     */
    public static boolean getEtcBoolean(Context context, String prefeName, String name, boolean def) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        return sp.getBoolean(name, def);
    }

    /**
     * 删除name
     *
     * @param context
     * @param name
     */
    public static void removeEtcName(Context context, String prefeName, String name) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().remove(name).commit();
    }

    /**
     * 清除数据
     *
     * @param context
     * @param prefeName
     */
    public static void clearEtc(Context context, String prefeName) {
        SharedPreferences sp = context.getSharedPreferences(prefeName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    /**
     * 删除Prefe
     *
     * @param context
     */
    public static void deletePrefe(Context context, String prefeName) {
        File file = new File("/data/data/" + context.getPackageName() + "/shared_prefs", prefeName);
        LogUtil.d(TAG, "---deletePrefe=" + file.getAbsolutePath() + ";exists=" + file.exists() + ";prefeName=" + prefeName);
        if (file.exists()) {
            clearEtc(context, prefeName.replace(".xml", ""));
            file.delete();
        }
    }

    /**
     * 清除登陆用户相关数据
     *
     * @param con
     */
    public static void deleteUserData(Context con) {
        SharePreferencesUtil.saveToken(con, "");
        SharePreferencesUtil.savePassWord(con, "");
        SharePreferencesUtil.saveUserName(con, "");
        SharePreferencesUtil.savePassWord(con, "");
        SharePreferencesUtil.saveAvatar(con, "");
        SharePreferencesUtil.saveNickName(con, "");
        SharePreferencesUtil.saveEmail(con, "");
        SharePreferencesUtil.saveLoginInfo(con, "");
        SharePreferencesUtil.saveNickName(con, "");
        SharePreferencesUtil.savePedometerConsume(con, "");
        SharePreferencesUtil.savePedometerDistance(con, "");
        SharePreferencesUtil.savePedometerTarget(con, "");
        SharePreferencesUtil.savePedometerTime(con, "");

    }

    /**
     * 用户登陆时，只有不同用户才清除的数据
     */
    public static void deleteNoSameData(Context con, String uid) {
        String uidLast = getUid(con);
        LogUtil.d(TAG, "-------deleteNoSameData----uidLast=" + uidLast + ";uid=" + uid);
        if (!uidLast.equalsIgnoreCase(uid)) {
            LogUtil.d(TAG, "-------deleteNoSameData----");
        }
    }


    /**
     * 判断是否进入后台或者黑屏重置
     */
    public static boolean getIsCallBackPause(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        return sp.getBoolean(Preferences.IS_CALL_BACK_PAUSE, false);
    }

    /**
     * 同上存储
     */
    public static void saveIsCallBackPause(Context context, Boolean uid) {
        SharedPreferences sp = context.getSharedPreferences(Prefe_Etc, Context.MODE_PRIVATE);
        sp.edit().putBoolean(Preferences.IS_CALL_BACK_PAUSE, uid).commit();
    }


}

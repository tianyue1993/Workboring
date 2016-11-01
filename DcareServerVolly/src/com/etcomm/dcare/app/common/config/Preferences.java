package com.etcomm.dcare.app.common.config;

/**
 * @author etc
 * @ClassName: Preferences
 * @Description: 常量字符, 偏好设置
 * @date 2 Apr, 2016 2:25:21 PM
 */
public class Preferences {
    //是否打开log开关
    public static final String LOG_ON_KEY_TAG = "log_on_key_tag";
    public static final String MOBILE_NUM = "MobileNum"; //手机号码
    public static final String USER_LOGING = "user_login"; //登陆状态


    public static final String USER_UID = "uid"; //保存UID
    public static final String ACCESS_TOKEN = "access_token"; //保存token
    public static final String REFRESH_NOTATTENTION = "refresh_notattention"; //保存token
    public static final String DEVICE_ID = "device_id"; //客户端ID
    public static final String CLIENT_ID = "client_id"; //推送ID
    public static final String USER_NAME = "username"; //用户名
    public static final String NICK_NAME = "nickname";//用户昵称
    public static final String PASS_WORD = "password";//用户密码
    public static final String USER_AVATOR = "avator"; //用户头像
    public static final String PEDOMETER_TARGET = "pedometerTarget";//运动目标
    public static final String PEDOMETER_DISTANCE = "pedometerDistance";//运动累积距离
    public static final String PEDOMETER_TIME = "pedometerTime";//运动累积时间
    public static final String PEDOMETER_CONSUME = "pedometerConsume";//运动积累消耗
    public static final String JOB_NUMBER = "jobNumber"; //关联工号
    public static final String CUSTOMER_IMAGE = "customer_image";//app启动图标
    /**
     * 判断是否进入过统计页面
     */
    public static final String IS_CALL_BACK_PAUSE = "is_call_back_pause";
    /**
     * 为了重新登录清零临时创建的flag
     */
    public static final String SAVE_HOC_STEP = "save_hoc_step";
    /**
     * 是否从新填充网络数据
     */
    public static final String SAVE_RE_DATA = "save_re_data";
    /**
     * 存储的版本号
     */
    public static final String VERSION_INFO = "version_info";
    /**
     * 存储的部门信息
     */
    public static final String DEPARTMENTINFO = "department_info";
    /**
     * 存储的部门信息ID
     */
    public static final String DEPARTMENTINFOID = "department_info_id";

    public static final String ADD_TOPIC_CHECK = "topic_check"; //是否可以创建话题
    public static final String TOPIC_PHOTO_ID = "position_id"; //选择照片ID
    public static final String User_NAME = "User_name";


    public static final String ActionGetBlueStepCount = "com.etcomm.dcare.ActionGetBlueSteps";//获取蓝牙计步数据

    public static final String ActionBlueSysn = "com.etcomm.dcare.ActionBlueSync";//调用蓝牙服务同步数据方法
    public static final String ActionBlueStep = "com.etcomm.dcare.ActionBlueSteps";//获取蓝牙计步数据
    public static final String ActionBlueConn = "com.etcomm.dcare.ActionBlueConn";//获取蓝牙连接状态
    public static final String ActionBlueDisConn = "com.etcomm.dcare.ActionBlueDisConn";//蓝牙取消绑定
    // --------优化分割线--------------
    public final static String WalkSharePreferences = "walk";
    public static final String IsHaveSleepDevice = "ishavesleepdevice";
    public static final String StartSecondBroadCastStepCount = "startpersecondstep";
    public static final String ActionGetCurStepCount = "com.etcomm.dcare.ActionGetSteps";
    public static final String BroadCastCurSteps = "broadcastcursteps";
    public static final String BroadcastMiles = "broadcastmiles";
    public static final String BroadcastSeconds = "broadcastseconds";
    public static final String BroadcastCaliries = "broadcastcaliries";
    public static final String BROADCAST_CHANGEIMAGE = "broadcastchangeimage";


    /**
     * 是否是在开发调试阶段,可以打印部分信息
     */
//		public static final boolean isDebag = true;

    /**
     * 登陆页面名字和头像
     */
    public static final String Avator = "avator";
    public static final String UserInfo = "userinfo";
    //		public static final String Token = "access_token";
    public final static int DEFAULT_ROUND_PIX = 6;
    public static final long LOAD_DATA_DELAY_TIME = 1000;
    public static final String SuggestSportDetail = "suggest_sport_detail";
    public static final String HealthNewsDetail = "healthnews_detail";
    public static final String UserWeight = "user_weight";
    public static final String UserHeight = "user_height";
    public static final String UserStepLeight = "user_steplenght";
    public static final String DayData = "daydata";
    /**
     *
     */
    public static final String TotalScore = "totalscore";
    /**
     * 上次累加的当日 的数据值 时间戳
     */
    public static final String LastClearTime = "daystarttime";
    public static final String DayMile = "daymile";
    public static final String DayCalories = "daycalories";
    public static final String DaySeconds = "dayseconds";
    public static final String DaySteps = "daysteps";
    public static final String BLUEDayMile = "bluedaymile";
    public static final String BLUEDayCalories = "bluedaycalories";
    public static final String BLUEDaySeconds = "bluedayseconds";
    public static final String BLUEDaySteps = "bluedaysteps";
    public static final String SignIn = "signin";
    public static final String SignInTime = "signintime";
    public static final String TakePhotoPath = "takephotopath";
    public static final String SelectSex = "selectsex";
    public static final String SelectAge = "selectage";
    public static final String SelectWeight = "selectweight";
    public static final String SelectNickName = "selectnickname";
    public static final String SelectHeight = "selectheight";
    public static final String SelectDepart = "selectdepart";
    public static final String PICMethod = "picmethod";
    public static final String TOPICSET = "topicset";
    public static final String UserId = "user_id";
    public static final String ActivitionCode = "activitioncode";
    public static final String ActivitionStructureCode = "activitionstructurecode";
    public static final String ActivitionStructureList = "activitionstructurelist";
    public static final String ValidCode = "validcode";
    public static final String IsRegisterEnterPassword = "isregisterenterpassword";
    public static final String UserPassword = "userpassword";
    public static final String UserNickName = "usernickname";
    public static final String LastStructureName = "laststructurename";
    public static final String LastStructureItems = "laststructureitems";
    public static final String FirstSetUserInfo = "isfirstsetuserinfo";
    public static final String UserAge = "userage";
    public static final String UserSex = "usergender";
    public static final String IsForgotPassword = "isforgotpassword";
    public static final String CommonLoginInfo = "commonlogin";
    public static final String PedometerTarget = "podemetertarget";
    public static final String MineCurrent = "minecurrent";
    public static final String IsSettingPersonalData = "issettingpersonaldata";
    public static final String SelectDepart_Id = "selectdepartid";
    public static final String FindWelfareDetail = "findcompanywelfare";
    /**
     * 数据上传相关sp信息
     */
    public static final String DataUpload = "dataupload";
    // 昨天 的时间
    public static final String AppLastDayTime = "lastdaytime";
    // 今天第一次打开app的时间
    public static final String AppThisDayTime = "thisdaytime";
    // 今天本次的时间
    // public static final String AppThisTime = "thistime";
    public static final String CommonWebViewTitle = "common_webview_title";
    public static final String CommonWebViewUrl = "common_webview_url";
    public static final String StructureName = "structurename";
    public static final String Score = "userscore";
    public static final String Pedometer_Distance = "pedometerdistance";
    public static final String Pedometer_Time = "pedometertime";
    public static final String Pedometer_Consume = "pedometerconsume";
    public static final String Is_Leader = "is_leader";
    /**
     * 启动延时
     */
    public static final long BROADCAST_ELAPSED_TIME_DELAY = 30 * 1000;
    /**
     * 调度计划
     */
    public static final String Dispatch_SERVICE = "com.etcomm.dcare.service.DispatchService";
    /**
     *
     */
    public static final String DcareService = "com.etcomm.dcare.service.DcareService";
    ;
    /**
     *
     */
    public static final String Dispatch_SERVICE_ACTION = "com.etcomm.dcare.service.DispatchService.Action";
    /**
     *
     */
    public static final long ELAPSED_TIME = 3000;
    /**
     * 首页动态同步数据
     */
    public static final String HOME_STEP_UPDATE = "home_step_update";
    /**
     * 日期改变
     */
    public static final String ACTION_DATE_CHANGED = "com.etcomm.dcare.service.ACTION_DATE_CHANGED";
    public static final String ACTION_NOTIFY_DATE_CHANGED = "com.etcomm.dcare.service.ACTION_NOTIFY_DATE_CHANGED";
    /**
     *
     */
    public static final String LastDateChangedMillis = "lastdatechangedmillis";
    /**
     * 用户更改注册，或者用其他账号登陆，应该清空当前 的数据，重新计步
     */
    public static final String ACTION_CLEAR_ALLDATA = "com.etcomm.dcare.service.ACTION_CLEAR_ALLDATA";

    /**
     *
     */
    public static final String MACAddress = "bluetoothmacaddress";

    /**
     *
     */
    public static final String OPEN_NEWS = "opennews";
    /**
     *
     */
    public static final String BlueDeviceRssi = "bluetoothrssi";
    /**
     *
     */
    public static final String BlueDeviceName = "bindedbluetoothname";
    /**
     *
     */
    public static final String ACTION_MSG_DATA = "com.etcomm.dcare.ACTION_MSG_DATA";
    /**
     *
     */
    public static final String IsHaveReceiveUnReadData = "isHaveReceiveUnReadData";
    /**
     * app设置数据信息
     */
    public static final String SPSetting = "dcaresetting";
    /**
     * 运动时，屏幕常量
     */
    public static final String SPSetting_ScreenLongOn = "isscreenlongon";
    public static final String SPSetting_PushMsg = "ispushmsg";
    public static final String SPSetting_PushMsg_Like = "ispushmsglike";
    public static final String SPSetting_PushMsg_Comment = "ispushmsgcomment";
    /**
     * 手机屏幕常亮
     */
    public static final String ACTION_SCREENLONGON = "com.etcomm.dcare.service.ACTION_SCREEN_LONGON";
    /**
     * 用户退出登陆
     */
    public static final String ACTION_USER_EXIT = "com.etcomm.dcare.service.ACTION_USER_EXIT";
    /**
     * 用户注册成功
     */
    public static final String ACTION_USER_REGISTER = "com.etcomm.dcare.service.ACTION_USER_REGISTER";
    /**
     * 用户登陆
     */
    public static final String ACTION_USER_LOGIN = "com.etcomm.dcare.service.ACTION_USER_LOGIN";

    public static final String DayIsBlue = "daydataisblue";
    /**
     * 是否停止计步服务，从后台直接停止
     */
    public static final String isFinishWork = "isfinishwork";

    /**
     * - 1.97 2.96 4.44 6.66 10.00 15.00 22.50 33.75 50.62
     */
    public static final float pedometer_sensitivity[] = {1.97f, 2.96f, 4.44f,
            6.66f, 10.00f, 15.00f, 22.50f, 33.75f, 50.62f};
    /**
     * app计步是否开启
     */
    public static final String IfSoftPedometerOn = "isSoftpedometerOn";
    /**
     * app计步灵敏度
     */
    public static final String SoftPedometerSensitivity = "softPedometerSensitivity";
    /**
     * 开启或者关闭手机计步的常量
     */
    public static final String ACTION_ENALBE_PEDOMETER = "com.etcomm.dcare.ACTION_ENALBE_PEDOMETER";
    public static final String ACTION_UNENALBE_PEDOMETER = "com.etcomm.dcare.ACTION_UNENALBE_PEDOMETER";


}
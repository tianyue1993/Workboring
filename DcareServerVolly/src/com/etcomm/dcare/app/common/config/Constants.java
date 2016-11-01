package com.etcomm.dcare.app.common.config;

/**
 * @author etc
 * @ClassName: Constants
 * @Description: 全局配置
 * @date 2 Apr, 2016 2:25:47 PM
 */
public class Constants {

    // ETC product
    // public static final boolean log2File = false;//写日志到ＳＤ卡文件
    // public static final boolean enableEngineerMode = false;
    //    public static String BASE_URL = "http://365happywork.com/dcare/api/web/index.php?r="; // 生产
//        public static String BASE_URL = "http://113.59.227.10:84/api/web/index.php?r=";

    public static final boolean log2File = true; // 写日志到ＳＤ卡文件
    public static final boolean enableEngineerMode = true; // 调试模式

    public static final String BASE_URL = "http://113.59.227.10:82/dcare/api/web/index.php?r="; // 测试
    // public static String BASE_URL =
    // "http://113.59.227.10:81/dcare/api/web/index.php?r=";// 测试

    public static final String Login = BASE_URL + "sign-in"; // 登陆
    public static final String GetAroundAttentionTopicList = BASE_URL + "topic";// 身边

    public static final String GetAroundTopicMemberList = BASE_URL + "topic/topic-user";// 身边
    // 获取话题列表
    // 获取话题列表
    public static final String GetSuggestTopicList = BASE_URL;// 获取推荐列表
    /**
     * 获取推荐列表
     */
    public static final String getRecommend = "recommend";
    /**
     * 获取筛选列表
     */
    public static final String getRecommendFilter = "recommend/filter";

    public static final String REPORT_COMMENT = BASE_URL + "discussion-comment/create-report";// 举报评论
    public static final String REPORT_TOPIC = BASE_URL + "topic/create-report";// 举报小组
    public static final String REPORT_DISCUSSION = BASE_URL + "discussion/create-report";//举报帖子
    public static final String AroundAttention = BASE_URL + "topic/follow"; // 关注话题
    public static final String SuggestSportsSignUp = BASE_URL + "activity";// 报名活动
    public static final String DaySignUp = BASE_URL + "user/sign-in"; // 每日 签到
    public static final String FeedBack = BASE_URL + "feedback";// 意见反馈
    public static final String AboutUs = BASE_URL + "user/about"; // 关于我们
    public static final String ChangePwd = BASE_URL + "user/change-password";
    public static final String GetTopicDisscussList = BASE_URL + "discussion";
    public static final String UNFollowTopic = BASE_URL + "topic/un-follow";// 取消关注话题
    public static final String FollowTopic = BASE_URL + "topic/follow"; // 关注话题
    public static final String UNLikeDisscuss = BASE_URL + "discussion/un-like"; // 取消赞
    public static final String LikeDisscuss = BASE_URL + "discussion/like"; // 赞
    public static final String DeleteDisscuss = BASE_URL + "discussion/delete";// 删除讨论
    public static final String DeleteTopic = BASE_URL + "topic/delete"; // 话题删除
    public static final String PublistTopicDisscussOnlyText = BASE_URL + "discussion/create-discussion";// 讨论-发布(文字)
    public static final String PublistTopicDisscussPIC = BASE_URL + "discussion/create-image-base";// 讨论-发布(图片-base64)
    public static final String DisscussComment = BASE_URL + "discussion-comment";// 评论-列表
    public static final String ADDDisscussComment = BASE_URL + "discussion-comment/create";// 添加或者回复某人评论
    public static final String AddTopic = BASE_URL + "topic/create"; // 话题-发布
    public static final String SearchTopic = BASE_URL + "topic/search";// 话题-搜索
    public static final String HealthNewsList = BASE_URL + "health-news"; // 健康资讯-获取列表
    public static final String MyCollectionList = BASE_URL + "user/favorite";// 健康资讯-获取已收藏列表
    public static final String CollectHealthNews = BASE_URL + "user/create-favorite"; // 健康资讯-收藏
    public static final String UNCollectHealthNews = BASE_URL + "user/cancel-favorite";// 健康资讯-取消收藏
    public static final String LikeHealthNews = BASE_URL + "health-news/like"; // 健康资讯-赞
    public static final String HealthNewsSearch = BASE_URL + "health-news/search"; // 健康资讯-获取列表
    public static final String UNLikeHealthNews = BASE_URL + "health-news/cancel-like"; // 健康资讯-取消赞
    public static final String ActiveStructure = BASE_URL + "serial-number"; // 激活码获得客户信息及组织机构
    public static final String GetVilidateSNSCode = BASE_URL + "mobile-captcha/send"; // 获取短信验证码
    public static final String GetVilidateEMAILCode = BASE_URL + "email-captcha/send"; // 获取邮件验证码
    public static final String VilidateEMAILCode = BASE_URL + "email-captcha/validate"; // 验证邮件验证码
    public static final String VilidateSNSCode = BASE_URL + "mobile-captcha/validate";// 验证短信验证码
    public static final String GetDefaultAvator = BASE_URL + "avatar"; // 获取默认头像
    public static final String RegisterbyEMAILCode = BASE_URL + "email-sign-up";// 邮件注册
    public static final String RegisterbySNSCode = BASE_URL + "mobile-sign-up"; // 手机注册
    public static final String RegisterUpdateInfo = BASE_URL + "user/update"; // 注册信息完善接口(注册的时候使用)
    public static final String GetTodayRank = BASE_URL + "user/rank"; // 今天排名接口
    public static final String ForgotPsdBySMS = BASE_URL + "mobile-forgot-password"; // 短信找回密码
    public static final String ForgotPsdByEmail = BASE_URL + "email-forgot-password"; // 邮件找回密码
    public static final String UploadUserAvator = BASE_URL + "user/avatar"; // 上传头像
    public static final String UploadTopicNews = BASE_URL + "topic/update"; // 小组修改
    public static final String GetuserInfo = BASE_URL + "user/profile"; // 获取用户基本信息[我的模块使用]
    public static final String GetWeather = BASE_URL + "weather"; // 天气预报
    public static final String GetStructure = BASE_URL + "user/structure"; // 组织机构
    public static final String EditUserInfo = BASE_URL + "user/edit"; // 修改用户基本信息
    public static final String CompanyWelfareList = BASE_URL + "welfare"; // 公司福利列表
    public static final String MineSportsList = BASE_URL + "user/my-activity"; // 我的活动
    public static final String GetUserRankUrl = BASE_URL + "user/my-rank"; // 个人排行榜(H5)
    public static final String GetStructureRankUrl = BASE_URL + "structure/structure-rank"; // 部门排行榜(H5)
    public static final String SyncData = BASE_URL + "pedometer/sync"; // 数据同步相关接口
    public static final String SyncDataDay = BASE_URL + "pedometer/date-sync"; // 按天同步数据
    public static final String GetTrendUrl = BASE_URL + "user/trends-url"; // 趋势页面链接接口
    public static final String CheckUpdate = BASE_URL + "version";
    public static final String GetApkDownloadUrl = null;
    public static final String DeleteDisscussComment = BASE_URL + "discussion-comment/delete"; // 评论-删除
    public static final String GetIfUserHaveFollowedTopic = BASE_URL + "user/is-follow-topic"; // 话题-检测用户是否有关注话题
    public static final String GetPedometerTwoMonth = BASE_URL + "user/pedometer-two-months"; // 根据日期获取前两月记步数据
    public static final String PointsExchangeList = BASE_URL + "gift";// 获取兑换列表
    public static final String PointsExchange = BASE_URL + "gift/gift-exchange";// 兑换礼品
    public static final String MyExchangeList = BASE_URL + "gift/exchange-list"; // 我的兑换列表
    public static final String GetWelfareLotteryUrl = BASE_URL + "award"; // 福利抽奖地址
    public static final String GetMyPointsDetail = BASE_URL + "user/score"; // 我的积分详情
    public static final String GetPointsRules = BASE_URL + "user/score-rules"; // 获取积分
    // 规则地址
    public static final String MyPointsDetails = BASE_URL + "user/my-score";
    public static final String IsDeviceCanBind = BASE_URL + "device-bind/bind-status"; // 查询设备是否已经被绑定
    public static final String UnBindDevice = BASE_URL + "device-bind/bind-off"; // 绑定设备
    public static final String BindDevice = BASE_URL + "device-bind/bind-on"; // 解除绑定设备
    public static final String MSGList = BASE_URL + "news"; // 消息推送列表
    public static final String GetPhysicalReportUrl = BASE_URL + "physical"; // 体检报告
    public static final String GETMYLOTTERY = BASE_URL + "award/my-award";// 我的抽奖
    public static final String DeleteMsgByUserid = BASE_URL + "news/news-delete"; // 删除某一条消息推送
    public static final String DeleteAllMsg = null;
    public static final String Exit = BASE_URL + "sign-in/sign-out"; // 退出登陆user/sign-out
    public static final String UserPush = BASE_URL + "user/set-push"; // 设置消息推送
    public static final String CheckCreateTopic = BASE_URL + "topic/check-create"; // 检测本周内是否发布
    public static final String USER_MODULE = BASE_URL + "user/module"; // 发现模块控制接口
    public static final String SHARE_GROUP = BASE_URL + "health-news/share"; // 分享到小组
    public static final String ACTIVITY_SHARE_DROUP =BASE_URL+"activity/share";
    public static final String TOPIC_SHARE_GROUP = BASE_URL + "topic/share"; // 分享到小组

}

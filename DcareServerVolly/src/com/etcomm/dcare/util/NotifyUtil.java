package com.etcomm.dcare.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class NotifyUtil {

    private static final int FLAG = Notification.FLAG_INSISTENT;
    int requestCode = (int) SystemClock.uptimeMillis();
    private int NOTIFICATION_ID;
    private NotificationManager nm;
    private Notification notification;
    private NotificationCompat.Builder cBuilder;
    private Notification.Builder nBuilder;
    private Context mContext;


    public NotifyUtil(Context context, int ID) {
        this.NOTIFICATION_ID = ID;
        mContext = context;
        // 获取系统服务来初始化对象
        nm = (NotificationManager) mContext
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        cBuilder = new NotificationCompat.Builder(mContext);
    }

    /**
     * 设置在顶部通知栏中的各种信息
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     */
    private void setCompatBuilder(PendingIntent pendingIntent, int smallIcon, String ticker,
                                  String title, String content, boolean sound, boolean vibrate, boolean lights) {
//        // 如果当前Activity启动在前台，则不开启新的Activity。
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        // 当设置下面PendingIntent.FLAG_UPDATE_CURRENT这个参数的时候，常常使得点击通知栏没效果，你需要给notification设置一个独一无二的requestCode
//        // 将Intent封装进PendingIntent中，点击通知的消息后，就会启动对应的程序
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, FLAG);

        cBuilder.setContentIntent(pendingIntent);// 该通知要启动的Intent
        cBuilder.setSmallIcon(smallIcon);// 设置顶部状态栏的小图标
        cBuilder.setTicker(ticker);// 在顶部状态栏中的提示信息

        cBuilder.setContentTitle(title);// 设置通知中心的标题
        cBuilder.setContentText(content);// 设置通知中心中的内容
        cBuilder.setWhen(System.currentTimeMillis());

		/*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
		 * 不设置的话点击消息后也不清除，但可以滑动删除
		 */
        cBuilder.setAutoCancel(true);
        // 将Ongoing设为true 那么notification将不能滑动删除
        // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
		 * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
		 */
        cBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        /*
         * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
		 * Notification.DEFAULT_SOUND：系统默认铃声。
		 * Notification.DEFAULT_VIBRATE：系统默认震动。
		 * Notification.DEFAULT_LIGHTS：系统默认闪光。
		 * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
		 */
        int defaults = 0;

        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (lights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }

        cBuilder.setDefaults(defaults);
    }

    /**
     * 设置builder的信息，在用大文本时会用到这个
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     */
    private void setBuilder(PendingIntent pendingIntent, int smallIcon, String ticker, boolean sound, boolean vibrate, boolean lights) {
        nBuilder = new Notification.Builder(mContext);
        // 如果当前Activity启动在前台，则不开启新的Activity。
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, FLAG);
        nBuilder.setContentIntent(pendingIntent);

        nBuilder.setSmallIcon(smallIcon);


        nBuilder.setTicker(ticker);
        nBuilder.setWhen(System.currentTimeMillis());
        nBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        int defaults = 0;

        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (lights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }

        nBuilder.setDefaults(defaults);
    }

    /**
     * 普通的通知
     * <p/>
     * 1. 侧滑即消失，下拉通知菜单则在通知菜单显示
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param content
     */
    public void notify_normal_singline(PendingIntent pendingIntent, int smallIcon,
                                       String ticker, String title, String content, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        sent();
    }

    /**
     * 进行多项设置的通知(在小米上似乎不能设置大图标，系统默认大图标为应用图标)
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param content
     */
    public void notify_mailbox(PendingIntent pendingIntent, int smallIcon, int largeIcon, ArrayList<String> messageList,
                               String ticker, String title, String content, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);

        // 将Ongoing设为true 那么notification将不能滑动删除
        //cBuilder.setOngoing(true);

        /**
         // 删除时
         Intent deleteIntent = new Intent(mContext, DeleteService.class);
         int deleteCode = (int) SystemClock.uptimeMillis();
         // 删除时开启一个服务
         PendingIntent deletePendingIntent = PendingIntent.getService(mContext,
         deleteCode, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
         cBuilder.setDeleteIntent(deletePendingIntent);

         **/

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), largeIcon);
        cBuilder.setLargeIcon(bitmap);

        cBuilder.setDefaults(Notification.DEFAULT_ALL);// 设置使用默认的声音
        //cBuilder.setVibrate(new long[]{0, 100, 200, 300});// 设置自定义的振动
        cBuilder.setAutoCancel(true);
        // builder.setSound(Uri.parse("file:///sdcard/click.mp3"));

        // 设置通知样式为收件箱样式,在通知中心中两指往外拉动，就能出线更多内容，但是很少见
        //cBuilder.setNumber(messageList.size());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (String msg : messageList) {
            inboxStyle.addLine(msg);
        }
        inboxStyle.setSummaryText("[" + messageList.size() + "条]" + title);
        cBuilder.setStyle(inboxStyle);
        sent();
    }

    /**
     * 自定义视图的通知
     *
     * @param remoteViews
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     */
    public void notify_customview(RemoteViews remoteViews, PendingIntent pendingIntent,
                                  int smallIcon, String ticker, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, null, null, sound, vibrate, lights);

        notification = cBuilder.build();
        notification.contentView = remoteViews;
        // 发送该通知
        nm.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 可以容纳多行提示文本的通知信息 (因为在高版本的系统中才支持，所以要进行判断)
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param content
     */
    public void notify_normail_moreline(PendingIntent pendingIntent, int smallIcon, String ticker,
                                        String title, String content, boolean sound, boolean vibrate, boolean lights) {

        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            notify_normal_singline(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
            Toast.makeText(mContext, "您的手机低于Android 4.1.2，不支持多行通知显示！！", Toast.LENGTH_SHORT).show();
        } else {
            setBuilder(pendingIntent, smallIcon, ticker, true, true, false);
            nBuilder.setContentTitle(title);
            nBuilder.setContentText(content);
            nBuilder.setPriority(Notification.PRIORITY_HIGH);
            notification = new Notification.BigTextStyle(nBuilder).bigText(content).build();
            // 发送该通知
            nm.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * 有进度条的通知，可以设置为模糊进度或者精确进度
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param content
     */
    public void notify_progress(PendingIntent pendingIntent, int smallIcon,
                                String ticker, String title, String content, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        /*
         * 因为进度条要实时更新通知栏也就说要不断的发送新的提示，所以这里不建议开启通知声音。
		 * 这里是作为范例，给大家讲解下原理。所以发送通知后会听到多次的通知声音。
		 */

        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                for (incr = 0; incr <= 100; incr += 10) {
                    // 参数：1.最大进度， 2.当前进度， 3.是否有准确的进度显示
                    cBuilder.setProgress(100, incr, false);
                    // cBuilder.setProgress(0, 0, true);
                    sent();
                    try {
                        Thread.sleep(1 * 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 进度满了后，设置提示信息
                cBuilder.setContentText("下载完成").setProgress(0, 0, false);
                sent();
            }
        }).start();
    }

    /**
     * 容纳大图片的通知
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param bigPic
     */
    public void notify_bigPic(PendingIntent pendingIntent, int smallIcon, String ticker,
                              String title, String content, int bigPic, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, null, sound, vibrate, lights);
        NotificationCompat.BigPictureStyle picStyle = new NotificationCompat.BigPictureStyle();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                bigPic, options);
        picStyle.bigPicture(bitmap);
        picStyle.bigLargeIcon(bitmap);
        cBuilder.setContentText(content);
        cBuilder.setStyle(picStyle);
        sent();
    }

    /**
     * 里面有两个按钮的通知
     *
     * @param smallIcon
     * @param leftbtnicon
     * @param lefttext
     * @param leftPendIntent
     * @param rightbtnicon
     * @param righttext
     * @param rightPendIntent
     * @param ticker
     * @param title
     * @param content
     */
    public void notify_button(int smallIcon, int leftbtnicon, String lefttext, PendingIntent leftPendIntent, int rightbtnicon, String righttext, PendingIntent rightPendIntent, String ticker,
                              String title, String content, boolean sound, boolean vibrate, boolean lights) {

        requestCode = (int) SystemClock.uptimeMillis();
        setCompatBuilder(rightPendIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        cBuilder.addAction(leftbtnicon,
                lefttext, leftPendIntent);
        cBuilder.addAction(rightbtnicon,
                righttext, rightPendIntent);
        sent();
    }

    public void notify_HeadUp(PendingIntent pendingIntent, int smallIcon, int largeIcon,
                              String ticker, String title, String content, int leftbtnicon, String lefttext, PendingIntent leftPendingIntent, int rightbtnicon, String righttext, PendingIntent rightPendingIntent, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        cBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIcon));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cBuilder.addAction(leftbtnicon,
                    lefttext, leftPendingIntent);
            cBuilder.addAction(rightbtnicon,
                    righttext, rightPendingIntent);
        } else {
            Toast.makeText(mContext, "版本低于Andriod5.0，无法体验HeadUp样式通知", Toast.LENGTH_SHORT).show();
        }
        sent();
    }


    /**
     * 发送通知
     */
    private void sent() {
        notification = cBuilder.build();
        // 发送该通知
        nm.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 根据id清除通知
     */
    public void clear() {
        // 取消通知
        nm.cancelAll();
    }

    /**
     * 高仿淘宝
     */
    private void notify_normal_singLine() {
        //设置想要展示的数据内容
//        Intent intent = new Intent(mContext, OtherActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        int smallIcon = R.drawable.tb_bigicon;
//        String ticker = "您有一条新通知";
//        String title = "双十一大优惠！！！";
//        String content = "仿真皮肤充气娃娃，女朋友带回家！";
//
//        //实例化工具类，并且调用接口
//        NotifyUtil notify1 = new NotifyUtil(mContext, 1);
//        notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, true, true, false);
//        currentNotify = notify1;
    }

    /**
     * 高仿网易新闻
     */
    private void notify_normal_moreLine() {
        //设置想要展示的数据内容
//        Intent intent = new Intent(mContext, OtherActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        int smallIcon = R.drawable.netease_bigicon;
//        String ticker = "您有一条新通知";
//        String title = "朱立伦请辞国民党主席 副主席黄敏惠暂代党主席";
//        String content = "据台湾“中央社”报道，国民党主席朱立伦今天(18日)向中常会报告，为败选请辞党主席一职，他感谢各位中常委的指教包容，也宣布未来党务工作由副主席黄敏惠暂代，完成未来所有补选工作。";
//        //实例化工具类，并且调用接口
//        NotifyUtil notify2 = new NotifyUtil(mContext, 2);
//        notify2.notify_normail_moreline(pIntent, smallIcon, ticker, title, content, true, true, false);
//        currentNotify = notify2;
    }

    /**
     * 收件箱样式
     */
    private void notify_mailbox() {
        //设置想要展示的数据内容
//        Intent intent = new Intent(mContext, OtherActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        int largeIcon = R.drawable.fbb_largeicon;
//        int smallIcon = R.drawable.wx_smallicon;
//        String ticker = "您有一条新通知";
//        String title = "冰冰";
//        ArrayList<String> messageList = new ArrayList<String>();
//        messageList.add("文明,今晚有空吗？");
//        messageList.add("晚上跟我一起去玩吧?");
//        messageList.add("怎么不回复我？？我生气了！！");
//        messageList.add("我真生气了！！！！！你听见了吗!");
//        messageList.add("文明，别不理我！！！");
//        String content = "[" + messageList.size() + "条]" + title + ": " + messageList.get(0);
//        //实例化工具类，并且调用接口
//        NotifyUtil notify3 = new NotifyUtil(mContext, 3);
//        notify3.notify_mailbox(pIntent, smallIcon, largeIcon, messageList, ticker,
//                title, content, true, true, false);
//        currentNotify = notify3;
    }

    /**
     * 高仿系统截图通知
     */
    private void notify_bigPic() {
        //设置想要展示的数据内容
//        Intent intent = new Intent(mContext, OtherActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        int smallIcon = R.drawable.xc_smaillicon;
//        int largePic = R.drawable.screenshot;
//        String ticker = "您有一条新通知";
//        String title = "已经抓取屏幕截图";
//        String content = "触摸可查看您的屏幕截图";
//        //实例化工具类，并且调用接口
//        NotifyUtil notify4 = new NotifyUtil(mContext, 4);
//        notify4.notify_bigPic(pIntent, smallIcon, ticker, title, content, largePic, true, true, false);
//        currentNotify = notify4;
    }


    /**
     * 高仿应用宝
     */
    private void notify_customview() {
        //设置想要展示的数据内容
//        Intent intent = new Intent(mContext, OtherActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        String ticker = "您有一条新通知";
//
//        //设置自定义布局中按钮的跳转界面
//        Intent btnIntent = new Intent(mContext, OtherActivity.class);
//        btnIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        //如果是启动activity，那么就用PendingIntent.getActivity，如果是启动服务，那么是getService
//        PendingIntent Pintent = PendingIntent.getActivity(mContext,
//                (int) SystemClock.uptimeMillis(), btnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // 自定义布局
//        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
//                R.layout.yyb_notification);
//        remoteViews.setImageViewResource(R.id.image, R.drawable.yybao_bigicon);
//        remoteViews.setTextViewText(R.id.title, "垃圾安装包太多");
//        remoteViews.setTextViewText(R.id.text, "3个无用安装包，清理释放的空间");
//        remoteViews.setOnClickPendingIntent(R.id.button, Pintent);//定义按钮点击后的动作
//        int smallIcon = R.drawable.yybao_smaillicon;
//        //实例化工具类，并且调用接口
//        NotifyUtil notify5 = new NotifyUtil(mContext, 5);
//        notify5.notify_customview(remoteViews, pIntent, smallIcon, ticker, true, true, false);
//        currentNotify = notify5;
    }

    /**
     * 高仿Android更新提醒样式
     */
    private void notify_buttom() {
        //设置想要展示的数据内容
//        String ticker = "您有一条新通知";
//        int smallIcon = R.drawable.android_bigicon;
//        int lefticon = R.drawable.android_leftbutton;
//        String lefttext = "以后再说";
//        Intent leftIntent = new Intent();
//        leftIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent leftPendIntent = PendingIntent.getActivity(mContext,
//                requestCode, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        int righticon = R.drawable.android_rightbutton;
//        String righttext = "安装";
//        Intent rightIntent = new Intent(mContext, OtherActivity.class);
//        rightIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent rightPendIntent = PendingIntent.getActivity(mContext,
//                requestCode, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //实例化工具类，并且调用接口
//        NotifyUtil notify6 = new NotifyUtil(mContext, 6);
//        notify6.notify_button(smallIcon, lefticon, lefttext, leftPendIntent, righticon, righttext, rightPendIntent, ticker, "系统更新已下载完毕", "Android 6.0.1", true, true, false);
//        currentNotify = notify6;
    }


    /**
     * 高仿Android系统下载样式
     */
    private void notify_progress() {
        //设置想要展示的数据内容
//        Intent intent = new Intent(mContext, OtherActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent rightPendIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        int smallIcon = R.drawable.android_bigicon;
//        String ticker = "您有一条新通知";
//        //实例化工具类，并且调用接口
//        NotifyUtil notify7 = new NotifyUtil(mContext, 7);
//        notify7.notify_progress(rightPendIntent, smallIcon, ticker, "Android 6.0.1 下载", "正在下载中", true, false, false);
//        currentNotify = notify7;
    }

    /**
     * Android 5。0 新特性：悬浮式通知
     */
    private void notify_headUp() {
        //设置想要展示的数据内容
//        int smallIcon = R.drawable.hl_smallicon;
//        int largeIcon = R.drawable.fbb_largeicon;
//        String ticker = "您有一条新通知";
//        String title = "范冰冰";
//        String content = "文明，今晚在希尔顿酒店2016号房哈";
//        Intent intent = new Intent(mContext, OtherActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        int lefticon = R.drawable.hl_message;
//        String lefttext = "回复";
//        Intent leftIntent = new Intent();
//        leftIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent leftPendingIntent = PendingIntent.getActivity(mContext,
//                requestCode, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        int righticon = R.drawable.hl_call;
//        String righttext = "拨打";
//        Intent rightIntent = new Intent(mContext, OtherActivity.class);
//        rightIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent rightPendingIntent = PendingIntent.getActivity(mContext,
//                requestCode, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //实例化工具类，并且调用接口
//        NotifyUtil notify8 = new NotifyUtil(mContext, 8);
//        notify8.notify_HeadUp(pendingIntent, smallIcon, largeIcon, ticker, title, content, lefticon, lefttext, leftPendingIntent, righticon, righttext, rightPendingIntent, true, true, false);
//        currentNotify = notify8;
    }
    
}

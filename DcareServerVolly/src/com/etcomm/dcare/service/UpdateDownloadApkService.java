package com.etcomm.dcare.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.etcomm.dcare.MainActivity;
import com.etcomm.dcare.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class UpdateDownloadApkService extends BaseService {

	private static final String tag = "UpdateService";
	private NotificationManager nm;
	private Notification notification;
	private File tempFile = null;
	private boolean cancelUpdate = false;
	private MyHandler myHandler;
	private int download_precent = 0;
	private RemoteViews views;
	private int notificationId = 1234;
	private String apkurl;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(tag, "UpdateService onCreate");
		super.onCreate();
	}


	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		apkurl = intent.getStringExtra("apkurl");
		Log.i(tag, "onStartCommand getapkurl: "+apkurl);
		if(apkurl==null){
			Toast.makeText(this,"获取下载链接失败",Toast.LENGTH_SHORT).show();;
			return super.onStartCommand(intent, flags, startId);
		}
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		Notification.Builder build = new Notification.Builder(this);
//		build.setTicker(getString(R.string.app_name) + "更新");
//		build.setWhen(System.currentTimeMillis()+1500*10);
//		build.setDefaults(Notification.DEFAULT_LIGHTS);
//		views = new RemoteViews(getPackageName(), R.layout.update);
//		build.setContent(views);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//				new Intent(this, MainActivity.class), 0);
////		build.addAction(android.R.drawable.stat_sys_download, getString(R.string.app_name) + "更新", contentIntent);
//		Action.Builder actionbuild = new Action.Builder(new Action(android.R.drawable.stat_sys_download, getString(R.string.app_name) + "更新", contentIntent));
//		build.addAction(actionbuild.build());
//		notification =  build.build();

		notification = new Notification();
		notification.icon = android.R.drawable.stat_sys_download;
		// notification.icon=android.R.drawable.stat_sys_download_done;
		notification.tickerText = getString(R.string.app_name) + "更新";
		notification.when = System.currentTimeMillis();
		notification.defaults = Notification.DEFAULT_LIGHTS;
		// 设置任务栏中下载进程显示的views
		views = new RemoteViews(getPackageName(), R.layout.update);
		notification.contentView = views;
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);
//		notification.setLatestEventInfo(this, "", "", contentIntent);
		notification.contentIntent =contentIntent;
		// 将下载任务添加到任务栏中
		nm.notify(notificationId, notification);
		myHandler = new MyHandler(Looper.myLooper(), this);
		// 初始化下载任务内容views
		Message message = myHandler.obtainMessage(3, 0);
		myHandler.sendMessage(message);
		// 启动线程开始执行下载任务
		if(apkurl!=null){
			downFile(apkurl);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// 下载更新文件
	private void downFile(final String url) {
		Log.i(tag, "down File: "+url);
		new Thread() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					// params[0]代表连接的url
					HttpPost post = new HttpPost(url);
					HttpResponse response = client.execute(post);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					if (is != null) {
						File rootFile = new File(
								Environment.getExternalStorageDirectory(),
								"/dcare");
						if (!rootFile.exists() && !rootFile.isDirectory())
							rootFile.mkdir();

						tempFile = new File(
								Environment.getExternalStorageDirectory(),
								"/dcare/"
										+ url.substring(url.lastIndexOf("=") + 1));
						if (tempFile.exists())
							tempFile.delete();
						tempFile.createNewFile();

						// 已读出流作为参数创建一个带有缓冲的输出流
						BufferedInputStream bis = new BufferedInputStream(is);

						// 创建一个新的写入流，讲读取到的图像数据写入到文件中
						FileOutputStream fos = new FileOutputStream(tempFile);
						// 已写入流作为参数创建一个带有缓冲的写入流
						BufferedOutputStream bos = new BufferedOutputStream(fos);

						int read;
						long count = 0;
						int precent = 0;
						byte[] buffer = new byte[1024];
						while ((read = bis.read(buffer)) != -1 && !cancelUpdate) {
							bos.write(buffer, 0, read);
							count += read;
							precent = (int) (((double) count / length) * 100);
							// 每下载完成5%就通知任务栏进行修改下载进度
							if (precent - download_precent >= 5) {
								download_precent = precent;
								Message message = myHandler.obtainMessage(3,
										precent);
								myHandler.sendMessage(message);
							}
						}
						bos.flush();
						bos.close();
						fos.flush();
						fos.close();
						is.close();
						bis.close();
					}

					if (!cancelUpdate) {
						Message message = myHandler.obtainMessage(2, tempFile);
						myHandler.sendMessage(message);
					} else {
						tempFile.delete();
					}
				} catch (ClientProtocolException e) {
					Message message = myHandler.obtainMessage(4, "下载更新文件失败");
					myHandler.sendMessage(message);
				} catch (IOException e) {
					Message message = myHandler.obtainMessage(4, "下载更新文件失败");
					myHandler.sendMessage(message);
				} catch (Exception e) {
					Message message = myHandler.obtainMessage(4, "下载更新文件失败");
					myHandler.sendMessage(message);
				}
			}
		}.start();
	}

	// 安装下载后的apk文件
	private void Instanll(File file, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	class MyHandler extends Handler {
		private Context context;

		public MyHandler(Looper looper, Context c) {
			super(looper);
			this.context = c;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg != null) {
				switch (msg.what) {
					case 0:
						Toast.makeText(context, msg.obj.toString(),
								Toast.LENGTH_SHORT).show();
						break;
					case 1:
						break;
					case 2:
						// 下载完成后清除所有下载信息，执行安装提示
						download_precent = 0;
						nm.cancel(notificationId);
						Instanll((File) msg.obj, context);
						// 停止掉当前的服务
						stopSelf();
						break;
					case 3:
						// 更新状态栏上的下载进度信息
						views.setTextViewText(R.id.tvProcess, "已下载"
								+ download_precent + "%");
						views.setProgressBar(R.id.pbDownload, 100,
								download_precent, false);
						notification.contentView = views;
						nm.notify(notificationId, notification);
						break;
					case 4:
						nm.cancel(notificationId);
						break;
				}
			}
		}
	}
}

package com.etcomm.dcare;

import java.util.ArrayList;

import android.R.bool;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/**
 * 监视器类，构造时将会在Native创建子进程来监视当前进程，
 *
 * @author wangqiang
 * @date 2014-04-24
 */
public class Watcher {
	// TODO Fix this according to your service
	private static final String PACKAGE = "com.etcomm.dcare.DcareService";
	private String mMonitoredService = "DcareService";
	private volatile boolean bHeartBreak = false;
	private Context mContext;
	private boolean mRunning = true;



	public void createAppMonitor(String userId) {
		if (!createWatcher(userId)) {
			Log.e("Watcher", "<<Monitor created failed>>");
		}
		if (!connectToMonitor()) {
			Log.e("Watcher", "<<Connect To Monitor failed>>");
		}
	}

	public void restartListenSensorManager(){
		restartSensorManager();
	}
	public void startListenSensorManager(){
		startSensorManager();
	}
	public static Watcher getInstance(Context context){
		if(watcher==null){
			watcher = new Watcher(context);
		}
		return watcher;
	}
	private static Watcher watcher ;
	private Watcher(Context context) {
		mContext = context;
	}


	public int EnablePedometer(boolean isPedometer){
		Log.i("Watcher", "Watcher  isPedometer"+isPedometer);
		return setEnablePedometer(isPedometer);
	}
	public int setSensitivity(float sensitivity){
		return setnativeSensitivity(sensitivity);
	}
	public int setfilepath(String str){
		return setnativefilepath(str);
	}
	private int isServiceRunning() {
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) am.getRunningServices(1024);
		for (int i = 0; i < runningService.size(); ++i) {
			if (mMonitoredService.equals(runningService.get(i).service.getClassName().toString())) {
				return 1;
			}
		}
		return 0;
	}
	public  int getCurSteps() {
		// TODO Auto-generated method stub
		return nativegetCurSteps();
	}
	public void setWeightHeightSteplength(float bodyweight,float bodyheight,float steplength){
		nativeSetWeightHeightSteplenth(bodyweight,bodyheight,steplength);
	}


	public float[] getCurMileSecondCalory(){
		return timerIncreased();
	}
	/**
	 * Native方法，创建一个监视子进程.
	 *
	 * @param userId
	 *            当前进程的用户ID,子进程重启当前进程时需要用到当前进程的用户ID.
	 * @return 如果子进程创建成功返回true，否则返回false
	 */
	private native boolean createWatcher(String userId);

	/**
	 * Native方法，让当前进程连接到监视进程.
	 *
	 * @return 连接成功返回true，否则返回false
	 */
	private native boolean connectToMonitor();
	/**
	 *
	 * @return
	 */
	private native boolean startSensorManager();
	/**
	 * 屏幕关闭后，重新开启
	 * @return
	 */
	private native int restartSensorManager();
	/**
	 * 是否开启手机计步功能 
	 * @param str
	 * @return
	 */
	private native int setEnablePedometer(boolean isPedometer);
	/**
	 * 向NDK中设置计步灵敏度
	 * @param str
	 * @return
	 */
	private native int setnativeSensitivity(float sensitivity);
	/**
	 * 向NDK说明文件保存的路径,如果有sdcard则存入，否则保存到文件包下
	 * @param str
	 * @return
	 */
	private native int setnativefilepath(String str);
	/**
	 * 读取当前步数
	 * @return
	 */
	private native int nativegetCurSteps();
	/**
	 * 重置数据，重新开始计步
	 * @return
	 */
	private native int nativeresetCalMileData();
	/**
	 * Native方法，向监视进程发送任意信息
	 *
	 * @param 发给monitor的信息
	 * @return 实际发送的字节
	 */
	private native int sendMsgToMonitor(String msg);

	static {
		System.loadLibrary("dcare");
	}
	/**
	 *
	 * @param paramInt1  runningTimeInSeconds
	 * @param paramInt2  p:steps
	 * @param paramInt3
	 * @return [0]  miles
	 * 		   [1]  activeTimeInSeconds
	 * 		   [2]  calories
	 */
//	public native float[] timerIncreased(int paramInt1, int paramInt2, int paramInt3);
	public native float[] timerIncreased();
	public native int nativeSetWeightHeightSteplenth(float bodyweight, float bodyheight, float steplength);
	/**
	 *
	 * @param paramFloat
	 * @param paramInt
	 * @return
	 */
	public native float[] calcCaloriesForStepCounter(float paramFloat, int paramInt);

	public void resetCalMileData() {
		// TODO Auto-generated method stub
		nativeresetCalMileData();
	}
}

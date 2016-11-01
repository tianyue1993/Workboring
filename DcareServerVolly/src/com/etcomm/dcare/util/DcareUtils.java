package com.etcomm.dcare.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.os.Environment.getExternalStorageDirectory;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

public class DcareUtils {
	private static final String SDCARD_ROOT = getExternalStorageDirectory().getPath();
	private static final String DOCTOR_SDCARD_PATH = SDCARD_ROOT
			+ "/dcare_app";

	public static final File PHOTO_DIR = new File(getTmpCachePath());
	public static final String APP_PACKAGE_NAME = "com.etcomm.dcare";
	public static final String APP_CACHE_PHONE_PATH = "/data/data/"
			+ APP_PACKAGE_NAME + "/files/cache/";

	public static String getTmpCachePath() {
		String cachePath;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) == false) {
			cachePath = APP_CACHE_PHONE_PATH;
		} else {
			cachePath = DOCTOR_SDCARD_PATH + "/cache/";
		}

		tryMakePath(cachePath);

		return cachePath;
	}

	/**
	 * try to make the path if not existing, and change the mode of path to 777
	 *
	 * @param pathName
	 */
	private static void tryMakePath(String pathName) {
		if (!new File(pathName).exists()) {
			new File(pathName).mkdirs();

			Process p;
			int status;
			try {
				p = Runtime.getRuntime().exec("chmod 777 " + pathName);
				status = p.waitFor();
				if (status == 0) {
					LogUtil.i("tryMakePath, chmod succeed:" + pathName);
				} else {
					LogUtil.i("tryMakePath, chmod failed:" + pathName);
				}
			} catch (Exception ex) {
				LogUtil.i("tryMakePath, chmod failed:" + pathName);
				ex.printStackTrace();
			}
		}
	}

	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_HH_mm_ss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * @brief 获取圆角图片
	 *
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap sourceBitmap,
												float roundPx) {
		try {
			Bitmap targetBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
					sourceBitmap.getHeight(), Config.ARGB_8888);
			// 得到画布
			Canvas canvas = new Canvas(targetBitmap);
			// 创建画笔
			Paint paint = new Paint();
			paint.setAntiAlias(true);

			Rect rect = new Rect(0, 0, sourceBitmap.getWidth(),
					sourceBitmap.getHeight());
			RectF rectF = new RectF(rect);

			// 绘制
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(sourceBitmap, rect, rect, paint);
			return targetBitmap;

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		return null;

	}
}

package com.shwm.freshmallpos.util;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.view.WindowManager;

public class CommonUtil {
	private static String TAG = "CommonUtil";
	private static long lastClickTime;

	/**
	 * [防止快速点击]
	 * 
	 * @return true 执行
	 */
	public static boolean fastClick() {
		return fastClick(ConfigUtil.OnClickTime);
	}

	public static boolean fastClick(int clickTime) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (timeD < clickTime) {
			return false;
		}
		lastClickTime = time;
		return true;
	}

	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public static void backgroundAlpha(Activity activity, float bgAlpha) {
		WeakReference<Activity> weakReference = new WeakReference<Activity>(activity);
		Activity activityThis = weakReference.get();
		if (activityThis != null) {
			WindowManager.LayoutParams layoutParams = activityThis.getWindow().getAttributes();
			layoutParams.alpha = bgAlpha;
			activity.getWindow().setAttributes(layoutParams);
		}
	}

	
}

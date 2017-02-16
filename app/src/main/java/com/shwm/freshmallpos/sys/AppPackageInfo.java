package com.shwm.freshmallpos.sys;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.shwm.freshmallpos.R;

public class AppPackageInfo {
	private static final String TAG = "AppPackageInfo";

	/* Get The full name of the desired package */
	private static PackageInfo getPackageInfo(Context context, String pageName) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(pageName, 0);
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return info;
	}

	public static String getCurVerName(Context context, String pageName) {
		PackageInfo info = getPackageInfo(context, pageName);
		return (info == null) ? "" : info.versionName;
	}

	public static int getCurVerCode(Context context, String pageName) {
		PackageInfo info = getPackageInfo(context, pageName);
		return (info == null) ? -1 : info.versionCode;
	}

	public static String getAppInfo(Context context) {
		// String verName=null;
		String verName = context.getResources().getText(R.string.app_name).toString();
		return verName;
	}
}

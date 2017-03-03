package com.shwm.freshmallpos.update;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.net.AppConfig;
import com.shwm.freshmallpos.util.SDPathUtil;

public class UpdateUtil {

	/**
	 * 安装APK
	 * @param context
	 * @param updateFile 安装包的路径
	 */
	public static void installApk(Context context, File updateFile) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + updateFile.getPath()), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 删除上次更新存储在本地的apk
	 * @param updateFile 安装包的路径
	 */
	public static void removeOldApk(File updateFile) {
		if (updateFile != null && updateFile.exists() && updateFile.isFile()) {
			updateFile.delete();
		}
	}

	/** 根据"包名_版本名称"生成文件名 */
	public static File getFilePathUpdate(String versionName) {
		versionName = versionName.replace(".", "_");
		return new File(SDPathUtil.getSDCardPrivateCacheDir(ApplicationMy.getContext(), AppConfig.apkName + "_" + versionName
				+ AppConfig.apkNameSuffix));
	}


}

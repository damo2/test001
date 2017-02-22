package com.shwm.freshmallpos.sys;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.VersionEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.AppConfig;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.AppRequest;
import com.shwm.freshmallpos.service.MyUpdateService;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.value.ValueKey;

public class CheckUpdateInfo {
	private String TAG = CheckUpdateInfo.this.getClass().getSimpleName();
	private WeakReference<Activity> mActivity;
	private static int curVerCode = 0;
	private static String curVerName = "";
	private static VersionEntity version;
	// 文件存储
	private File updateFile = null;

	public CheckUpdateInfo(Activity activity) {
		this.mActivity = new WeakReference<Activity>(activity);
		if (mActivity.get() != null) {
			curVerCode = AppPackageInfo.getCurVerCode(mActivity.get(), mActivity.get().getPackageName());
			curVerName = AppPackageInfo.getCurVerName(mActivity.get(), mActivity.get().getPackageName());
		}
	}

	public static VersionEntity getVersion() {
		return version;
	}

	public void checkUpdateInfo() {
		new MyAsyncTaskUtil(new IAsyncListener() {

			@Override
			public void onSuccess(HashMap<String, Object> hashmap) {
				// TODO Auto-generated method stub
				version = (VersionEntity) hashmap.get(ValueKey.Version);
				updateFile = ConfigUtil.getFilePathUpdate(version.getName());
				UL.d(TAG, "update文件下载目录：" + updateFile.getPath());
				showUpdate();
			}

			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFail(int statu, Exception exception) {
				// TODO Auto-generated method stub
			}

			@Override
			public HashMap<String, Object> doInBackground() {
				// TODO Auto-generated method stub
				return AppRequest.getUpdateInfo(AppConfig.packageName);
			}
		}).execute();
	}

	public void showUpdate() {
		if (version.getCode() > curVerCode) {
			if (version.getOpt() == 3) {
				if (mActivity.get() != null) {
					Intent serviceIntent = new Intent(mActivity.get(), MyUpdateService.class);
					mActivity.get().startService(serviceIntent);
				}
			} else {
				showNoticeDialog();
			}
		} else {
			notNewVersionShow();
		}
	}

	private void notNewVersionShow() {
		if (mActivity.get() != null&&!mActivity.get().isFinishing()) {
			android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity.get());
			builder.setTitle(mActivity.get().getString(R.string.update_version));
			builder.setMessage(mActivity.get().getString(R.string.update_nonew) + " " + version.getName());
			builder.setPositiveButton(mActivity.get().getString(R.string.sure), null);
			builder.show();
		}
	}

	private void showNoticeDialog() {
		if (mActivity.get() != null) {
			String sure = "";
			android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity.get());
			if (updateFile.exists()) {
				sure = mActivity.get().getString(R.string.update_install);

				builder.setNegativeButton(mActivity.get().getString(R.string.update_again), new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent serviceIntent = new Intent(mActivity.get(), MyUpdateService.class);
						mActivity.get().startService(serviceIntent);
					}
				});
			} else {
				sure = mActivity.get().getString(R.string.update_down);
			}
			builder.setTitle(mActivity.get().getString(R.string.update_version));
			builder.setMessage(mActivity.get().getString(R.string.update_current) + " " + curVerName + "\n"
					+ mActivity.get().getString(R.string.update_new) + " " + version.getName() + "\n" + version.getNoticeText());
			builder.setPositiveButton(sure, new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					if (updateFile.exists()) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse("file://" + updateFile.getPath()), "application/vnd.android.package-archive");
						mActivity.get().startActivity(intent);
					} else {
						Intent serviceIntent = new Intent(mActivity.get(), MyUpdateService.class);
						mActivity.get().startService(serviceIntent);
					}
				}
			});
			builder.setNeutralButton(mActivity.get().getString(R.string.cancel), null);
			builder.show();
		}
	}
}

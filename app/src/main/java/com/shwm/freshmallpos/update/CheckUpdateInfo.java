package com.shwm.freshmallpos.update;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.been.VersionEntity;
import com.shwm.freshmallpos.inter.IAsyncListener;
import com.shwm.freshmallpos.net.AppConfig;
import com.shwm.freshmallpos.net.MyAsyncTaskUtil;
import com.shwm.freshmallpos.request.AppRequest;
import com.shwm.freshmallpos.sys.AppPackageInfo;
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

	// private MyUpdateService mService;

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
				updateFile = UpdateUtil.getFilePathUpdate(version.getName());
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

	private void showUpdate() {
		showNoticeDialog();
		// if (version.getCode() > curVerCode) {
		// if (version.getOpt() == 3) {
		// if (mActivity.get() != null) {
		// startDownService();
		// }
		// } else {
		// showNoticeDialog();
		// }
		// } else {
		// notNewVersionShow();
		// }
	}

	private void notNewVersionShow() {
		if (mActivity.get() != null) {
			android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity.get());
			builder.setTitle(mActivity.get().getString(R.string.update_version));
			builder.setMessage(mActivity.get().getString(R.string.update_nonew) + " " + version.getName());
			builder.setPositiveButton(mActivity.get().getString(R.string.sure), null);
			builder.show();
		}
	}

	private void showNoticeDialog() {
		UL.d(TAG, "update文件下载目录：" + updateFile.getPath());
		if (mActivity.get() != null) {
			String sure = "";
			android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity.get());
			if (updateFile.exists()) {
				sure = mActivity.get().getString(R.string.update_install);
				builder.setNegativeButton(mActivity.get().getString(R.string.update_again), new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						startDownService();
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
						UpdateUtil.installApk(mActivity.get(), updateFile);
					} else {
						startDownService();
					}
				}
			});
			builder.setNeutralButton(mActivity.get().getString(R.string.cancel), null);
			builder.show();
		}
	}

	public void startDownService() {
		if (mActivity.get() != null) {
			Intent serviceIntent = new Intent(mActivity.get().getApplicationContext(), MyUpdateService.class);
			// mActivity.get().getApplicationContext().bindService(serviceIntent, mConnection,
			// Context.BIND_AUTO_CREATE);
			mActivity.get().getApplicationContext().startService(serviceIntent);
		}
	}

	/** 定义service绑定的回调，传给bindService() 的 */
	// private ServiceConnection mConnection = new ServiceConnection() {
	// @Override
	// public void onServiceConnected(ComponentName className, IBinder service) {
	// // 我们已经绑定到了LocalService，把IBinder进行强制类型转换并且获取LocalService实例．
	// DownBinder binder = (DownBinder) service;
	// mService = binder.getService();
	// }
	//
	// @Override
	// public void onServiceDisconnected(ComponentName arg0) {
	//
	// }
	// };

	public void onDestoryCheckUpdate() {
		// mActivity.get().unbindService(mConnection);
	}
}

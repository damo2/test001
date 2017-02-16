package com.shwm.freshmallpos.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.net.AppConfig;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.sys.CheckUpdateInfo;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.UL;

@SuppressLint("NewApi")
public class MyUpdateService extends Service {
	private String TAG = "MyUpdateService";
	// 文件存储
	private File updateFile = null;
	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification.Builder builder;
	// 通知栏跳转Intent
	private long totalSize = 0;// 总大小
	private int updateTotalSize = 0; // 下载大小
	private int progress = 0;

	// 下载状态
	private final static int DOWNLOAD_PRE = 0;
	private final static int DOWNLOAD_FAIL = 1;
	private final static int DOWNLOAD_SUC = 2;
	public static final int NOTIFICATION = 80;
	public Message message;
	private static final int NotificationId = 103;

	private Thread downloadThread;

	/** 下载线程是否暂停 */
	public boolean isPause = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// 创建文件
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			updateFile = ConfigUtil.getFilePathUpdate(CheckUpdateInfo.getVersion().getName());
		}
		this.builder = new Notification.Builder(this);
		this.updateNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		message = updateHandler.obtainMessage();

		builder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setContentIntent(getDefalutIntent(0))
				// .setNumber(number)//显示数量
				.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				.setAutoCancel(false)// 设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(true)// ture，不能滑动消失,设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_LIGHTS)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
															// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音
															// requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher)// 设置图标
				.setContentTitle(getString(R.string.update_statu))// 设置通知的标题
				.setContentText("0%")// 设置通知的内容
				.setTicker(getString(R.string.update_statu));// 状态栏上显示
		Notification notification = builder.build();
		updateNotificationManager.notify(NotificationId, notification);
		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		downloadThread = new Thread(new updateRunnable());
		downloadThread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
		UL.v(TAG, downloadUrl);
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setConnectTimeout(ConfigUtil.OutTimeConnectDown);
			httpConnection.setReadTimeout(ConfigUtil.OutTimeReadDown);
			updateTotalSize = httpConnection.getContentLength();
			UL.v(TAG, "安装包大小=" + (updateTotalSize / 1024));
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("404");
			}
			is = httpConnection.getInputStream();
			if (is != null) {
				fos = new FileOutputStream(saveFile, false);
				byte buffer[] = new byte[1024];
				int readsize = 0;
				while ((readsize = is.read(buffer)) != -1) {
					if (!isPause) {
						fos.write(buffer, 0, readsize);
						totalSize += readsize;
						int temp = (int) (((float) totalSize / updateTotalSize) * 100);
						if (temp != progress) {
							progress = temp;
							showCustomProgressNotify("正在下载   " + (totalSize / 1024) + "kb /" + (updateTotalSize / 1024) + "kb   " + temp
									+ "%");
						}
					}
				}
				if (totalSize == updateTotalSize) {
					progress = 0;
					// 下载成功
					message.what = DOWNLOAD_SUC;
					updateHandler.sendMessage(message);
				}
				fos.flush();
			} else {
				progress = 0;
				// 下载失败
				message.what = DOWNLOAD_FAIL;
				updateHandler.sendMessage(message);
			}
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

	class updateRunnable implements Runnable {
		@Override
		public void run() {
			try {
				// 增加权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				downloadUpdateFile(HttpUtil.getDownloadUrl(AppConfig.packageName), updateFile);
			} catch (Exception ex) {
				ex.printStackTrace();
				// 下载失败
				message.what = DOWNLOAD_FAIL;
				updateHandler.sendMessage(message);
			}
		}
	}

	/** 显示自定义的带进度条通知栏 */
	private void showCustomProgressNotify(String status) {
		RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_custom_progress);
		mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.drawable.ic_launcher);
		mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, getString(R.string.update_statu));
		mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, status);
		if (progress >= 100 || downloadThread == null) {
			mRemoteViews.setProgressBar(R.id.custom_progressbar, 0, 0, false);
			mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
		} else {
			mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, progress, false);
			mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
		}
		builder.setContent(mRemoteViews).setTicker(getString(R.string.update_statu));
		Notification nitify = builder.build();
		nitify.contentView = mRemoteViews;
		updateNotificationManager.notify(NotificationId, nitify);
	}

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUC:
				// 点击安装PendingIntent
				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
				PendingIntent updatePendingIntent = PendingIntent.getActivity(MyUpdateService.this, 0, installIntent, 0);
				builder.setDefaults(Notification.DEFAULT_VIBRATE);//
				builder.setContentIntent(updatePendingIntent);
				builder.setAutoCancel(true);// 设置这个标志当用户单击面板就可以让通知将自动取消
				builder.setOngoing(false);
				showCustomProgressNotify("下载完成,点击安装。");
				// 停止服务
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				builder.setDefaults(Notification.DEFAULT_VIBRATE);
				builder.setAutoCancel(true);// 设置这个标志当用户单击面板就可以让通知将自动取消
				builder.setOngoing(false);
				showCustomProgressNotify("下载失败");
				break;
			default:

			}
		}
	};

	/** 取消下载 */
	public void stopDownloadNotify() {
		if (downloadThread != null) {
			downloadThread.interrupt();
		}
		downloadThread = null;
		showCustomProgressNotify("下载已取消");
	}

	/** 暂停下载 */
	public void pauseDownloadNotify() {
		isPause = true;
		showCustomProgressNotify("已暂停");
	}

	/** 开始下载 */
	public void startDownloadNotify() {
		isPause = false;
		if (downloadThread != null && downloadThread.isAlive()) {
			// downloadThread.start();
		} else {
			downloadThread = new Thread(new updateRunnable());
			downloadThread.start();
		}
	}

	/** 清除通知栏 */
	public void clearNotify() {
		updateNotificationManager.cancel(NotificationId);
	}

	public PendingIntent getDefalutIntent(int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
		return pendingIntent;
	}

}

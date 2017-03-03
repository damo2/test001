package com.shwm.freshmallpos.update;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.shwm.freshmallpos.net.AppConfig;
import com.shwm.freshmallpos.net.FileUtil;
import com.shwm.freshmallpos.net.HttpOkRequest;
import com.shwm.freshmallpos.net.HttpUtil;
import com.shwm.freshmallpos.net.ProgressResponseBody.ProgressListener;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.UL;

@SuppressLint("NewApi")
public class MyUpdateService extends Service {
	private String TAG = "MyUpdateService";
	// 文件存储
	private File updateFile = null;

	// 下载状态
	private final static int DOWNLOAD_PRE = 0;
	private final static int DOWNLOAD_FAIL = 1;
	private final static int DOWNLOAD_SUC = 2;
	public static final int NOTIFICATION = 80;
	public Message message;
	private static final int NotificationId = 103;

	private Thread downloadThread;

	private NotificationDownUtil notificationDownUtil;

	private DownBroadcaseReceiver downBroadcaseReceiver;

	private int progress;

	private boolean isPause;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("onBind.....");
		// startDown();
		IBinder result = null;
		if (null == result)
			result = new DownBinder();
		return result;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("onCreate.....");
		notificationDownUtil = new NotificationDownUtil(this);
		notificationDownUtil.showNotification(NotificationId);

		downBroadcaseReceiver = new DownBroadcaseReceiver();  // 1.创建广播接收者对象
		IntentFilter filter = new IntentFilter();    // 2.创建intent-filter对象
		filter.addAction("com.shwm.freshmallpos.app.cancel");
		registerReceiver(downBroadcaseReceiver, filter);// 3.注册广播接收者
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		System.out.println("onStart.....");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("onStartCommand.....");
		startDown();
		return super.onStartCommand(intent, flags, startId);
	}

	public class DownBinder extends Binder {
		public MyUpdateService getService() {
			return MyUpdateService.this;
		}
	}

	// 开启线程下载
	public void startDown() {
		updateFile = UpdateUtil.getFilePathUpdate(CheckUpdateInfo.getVersion().getName());// 创建文件
		message = updateHandler.obtainMessage();
		// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
		downloadThread = new Thread(new updateRunnable());
		downloadThread.start();
	}

	// 线程
	private class updateRunnable implements Runnable {
		public void run() {
			try {
				// 增加权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				new HttpOkRequest().downFile(HttpUtil.getDownloadUrl(AppConfig.packageName), updateFile, new ProgressListener() {
					@Override
					public void update(long bytesRead, long contentLength, boolean done) {
						// TODO Auto-generated method stub
						UL.d(TAG, "总大小==" + contentLength + "  已经下载" + bytesRead);
						if (!isPause) {
							int temp = (int) (((float) bytesRead / contentLength) * 100);
							String statuInfo = "正在下载   " + (bytesRead / 1024) + "kb /" + (contentLength / 1024) + "kb   " + temp + "%";
							if (progress != temp) {
								progress = temp;
								notificationDownUtil.updateProgress(NotificationId, progress, statuInfo);
							}
						}
						if (done) {
							// 下载成功
							message.what = DOWNLOAD_SUC;
							updateHandler.sendMessage(message);
						}
					}
				});
			} catch (Exception ex) {
				ex.printStackTrace();
				// 下载失败
				message.what = DOWNLOAD_FAIL;
				updateHandler.sendMessage(message);
			}
		}
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
				notificationDownUtil.updateProgress(NotificationId, 100, "下载成功，点击安装！");
				notificationDownUtil.updatePendingIntent(NotificationId, updatePendingIntent);
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				notificationDownUtil.updateProgress(NotificationId, -1, "下载失败！");
				break;
			default:
			}
		}
	};

	/** 开始下载 */
	public void startDownloadNotify() {
		isPause = false;
		if (downloadThread == null || !downloadThread.isAlive()) {
			downloadThread = new Thread(new updateRunnable());
		}
		downloadThread.start();
	}

	/** 取消下载 */
	public void stopDownloadNotify() {
		FileUtil.pause();
		isPause = true;
		if (downloadThread != null) {
			downloadThread.interrupt();
		}
		downloadThread = null;
		notificationDownUtil.cancelProgress(NotificationId);
	}

	public PendingIntent getDefalutIntent(int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
		return pendingIntent;
	}

	public class DownBroadcaseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals("com.shwm.freshmallpos.app.cancel")) {
				stopDownloadNotify();
			}
		}
	}

}

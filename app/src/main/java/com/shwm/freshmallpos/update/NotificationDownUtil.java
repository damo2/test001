package com.shwm.freshmallpos.update;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.shwm.freshmallpos.R;

public class NotificationDownUtil {

	private Context mContext;
	// NotificationManager ： 是状态栏通知的管理类，负责发通知、清楚通知等。
	private NotificationManager manager;

	private Notification.Builder builder;
	// 定义Map来保存Notification对象
	private Map<Integer, Notification> map = null;

	public NotificationDownUtil(Context context) {
		this.mContext = context;
		// NotificationManager 是一个系统Service，必须通过 getSystemService()方法来获取。
		manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		map = new HashMap<Integer, Notification>();
	}

	@SuppressLint("NewApi")
	public void showNotification(int notificationId) {
		// 判断对应id的Notification是否已经显示， 以免同一个Notification出现多次
		if (!map.containsKey(notificationId)) {
			// 创建通知对象
			builder = new Notification.Builder(mContext);
			builder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
					.setContentIntent(getDefalutIntent(0))
					// .setNumber(number)//显示数量
					.setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
					.setAutoCancel(false)// 设置这个标志当用户单击面板就可以让通知将自动取消
					.setOngoing(true)// ture，不能滑动消失,设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
					.setDefaults(Notification.DEFAULT_LIGHTS)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
																// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND
																// 添加声音
																// requires VIBRATE permission
					.setSmallIcon(R.drawable.ic_launcher)// 设置图标
					// .setContentTitle(mContext.getString(R.string.update_statu))// 设置通知的标题
					// .setContentText("0%")// 设置通知的内容
					.setTicker(mContext.getString(R.string.update_statu));// 状态栏上显示

			RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.view_custom_progress);
			mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.drawable.ic_launcher);
			mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, mContext.getString(R.string.update_statu));
			mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, "开始下载");
			mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, 0, false);
			// 取消按钮的点击事件
			Intent pause = new Intent("com.shwm.freshmallpos.app.cancel");// 广播
			PendingIntent pauseIn = PendingIntent.getBroadcast(mContext, 0, pause, 0);
			mRemoteViews.setOnClickPendingIntent(R.id.ibtn_custom_progress_pause, pauseIn);

			builder.setContent(mRemoteViews).setTicker(mContext.getString(R.string.update_statu));
			Notification nitify = builder.build();
			nitify.contentView = mRemoteViews;
			manager.notify(notificationId, nitify);
			map.put(notificationId, builder.getNotification());// 存入Map中
		}
	}

	public void updateProgress(int notificationId, int progress, String statuInfo) {
		Notification notify = map.get(notificationId);
		if (null != notify) {
			// 修改进度条
			if (progress > -1) {
				notify.contentView.setProgressBar(R.id.custom_progressbar, 100, progress, false);
			}
			notify.contentView.setTextViewText(R.id.tv_custom_progress_status, statuInfo);
			manager.notify(notificationId, notify);
		}
	}

	public void updatePendingIntent(int notificationId, PendingIntent intent) {
		builder.setContentIntent(intent);

	}

	public PendingIntent getDefalutIntent(int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, new Intent(), flags);
		return pendingIntent;
	}

	public void cancelProgress(int notificationId) {
		manager.cancel(notificationId);
	}
}

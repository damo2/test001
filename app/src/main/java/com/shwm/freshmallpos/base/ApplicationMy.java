package com.shwm.freshmallpos.base;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.shwm.freshmallpos.net.CacheInterceptor;
import com.shwm.freshmallpos.net.HttpOkRequest;
import com.shwm.freshmallpos.util.ConfigUtil;
import com.shwm.freshmallpos.util.SDPathUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilPx;
import com.squareup.leakcanary.LeakCanary;

public class ApplicationMy extends Application {
	private static Context context;
	private static ApplicationMy mApplication;
	public static Map<String, Long> map = new HashMap<String, Long>();
	private static OkHttpClient mOkHttpClient;
	/**
	 * 分割 Dex 支持
	 * @param base
	 */
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	public static ApplicationMy getInstance() {
		if (mApplication == null) {
			synchronized (ApplicationMy.class) {
				if (mApplication == null) {
					mApplication = new ApplicationMy();
				}
			}
		}
		return mApplication;
	}

	@Override
	public void onCreate() {
		context = getApplicationContext();
		ImageLoaderConfig(this);
		LeakCanary.install(this);
	}

	public static Context getContext() {
		return context;
	}

	public void ImageLoaderConfig(Context context) {
		// LruCache通过构造函数传入缓存值，以KB为单位。
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// 缓存文件的目录
		// File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getExternalCacheDir().getPath());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).diskCacheExtraOptions(200, 300, null)
				.memoryCacheExtraOptions(200, 300)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5 加密
				.memoryCacheSize(maxMemory / 4) // 内存缓存的最大值
				.diskCacheSize(100 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
				.tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(200) // 缓存的文件数量
				// 由原先的discCache -> diskCache
				// .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 20 * 1000))
				// .writeDebugLogs() // Remove for release app
				.build();
		// 全局初始化此配置
		ImageLoader.getInstance().init(config);
		// ImageLoad的打印日志 disableLogging关闭 enableLogging开启
		com.nostra13.universalimageloader.utils.L.disableLogging();
	}

	public OkHttpClient getOkHttpClient() {
		if (mOkHttpClient == null) {
			synchronized (HttpOkRequest.class) {
				if (mOkHttpClient == null) {
					File sdcache = new File(SDPathUtil.getSDCardPrivateCacheDir(context, "okhttp"));// 缓存目录
					int cacheSize = 10 * 1024 * 1024;// 缓存文件大小
					OkHttpClient.Builder builder = new OkHttpClient.Builder().addNetworkInterceptor(new CacheInterceptor())
							.connectTimeout(ConfigUtil.OutTimeConnect, TimeUnit.MILLISECONDS)
							.writeTimeout(ConfigUtil.OutTimeWrite, TimeUnit.MILLISECONDS)
							.readTimeout(ConfigUtil.OutTimeRead, TimeUnit.MILLISECONDS).cache(new Cache(sdcache, cacheSize));
					mOkHttpClient = builder.build();
				}
			}
		}
		return mOkHttpClient;
	}

	public static int dp2px(int dp) {
		UL.d("Application", dp + "dp----->" + UtilPx.dp2px(getContext(), dp) + "px");
		return UtilPx.dp2px(getContext(), dp);
	}

	public static String getStringRes(int resId) {
		return context.getString(resId);
	}

}
package com.shwm.freshmallpos.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
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
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by as on 2017/3/6.
 */

public class ApplicationMy extends DefaultApplicationLike {
    private Application application;
    private static Context context;
    private static OkHttpClient mOkHttpClient;
    private static RefWatcher refWatcher;//使用 RefWatcher 监控那些本该被回收的对象。
    public static final String TAG = "Tinker.SampleApplicationLike";

    public ApplicationMy(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=getApplication();
        context = application.getApplicationContext();

        ImageLoaderConfig(context);
        UMengConfig();
        refWatcher = LeakCanary.install(application);//监测内存泄漏

        // 这里实现SDK初始化，appId替换成你的在Bugly平台,申请的appId调试时，将第三个参数改为true
        Bugly.init(application, "e26f1fef03", false);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    public static Context getContext() {
        return context;
    }

    public static int dp2px(int dp) {
        UL.d("Application", dp + "dp----->" + UtilPx.dp2px(getContext(), dp) + "px");
        return UtilPx.dp2px(getContext(), dp);
    }

    public static String getStringRes(int resId) {
        return context.getString(resId);
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    private void UMengConfig() {
        MobclickAgent.setDebugMode(false);
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.openActivityDurationTrack(false);//  false 禁止默认的页面统计方式，自己统计
    }

    public void ImageLoaderConfig(Context context) {
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 缓存文件的目录
        // File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getExternalCacheDir().getPath());
        File cacheDir = new File(SDPathUtil.getSDCardPrivateCacheDir(context, "imageCache"));// 缓存目录
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCacheExtraOptions(480, 800, null)//内存缓存文件的最大长宽
                .memoryCacheExtraOptions(480, 800)// 本地缓存的详细信息(缓存的最大长宽)
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)// default 设置当前线程的优先级
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5 加密
                .memoryCacheSize(maxMemory / 4) // 内存缓存的最大值
                .diskCacheSize(200 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(400) // 缓存的文件数量
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 20 * 1000))
                // .writeDebugLogs() // Remove for release app
                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
        // ImageLoad的打印日志 disableLogging 关闭 enableLogging 开启
        com.nostra13.universalimageloader.utils.L.writeDebugLogs(false);
    }

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (HttpOkRequest.class) {
                if (mOkHttpClient == null) {
                    File sdCache = new File(SDPathUtil.getSDCardPrivateCacheDir(context, "okhttpCache"));// 缓存目录
                    int cacheSize = 10 * 1024 * 1024;// 缓存文件大小
                    OkHttpClient.Builder builder = new OkHttpClient.Builder().addNetworkInterceptor(new CacheInterceptor())
                            .connectTimeout(ConfigUtil.OutTimeConnect, TimeUnit.MILLISECONDS)
                            .writeTimeout(ConfigUtil.OutTimeWrite, TimeUnit.MILLISECONDS)
                            .readTimeout(ConfigUtil.OutTimeRead, TimeUnit.MILLISECONDS).cache(new Cache(sdCache, cacheSize));
                    mOkHttpClient = builder.build();
                }
            }
        }
        return mOkHttpClient;
    }

}

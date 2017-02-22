package com.shwm.freshmallpos.manage;

import android.app.Activity;
import android.support.v4.util.ArrayMap;

import com.shwm.freshmallpos.util.UL;

import java.util.Iterator;
import java.util.Map;

public class ActivityCollector {
    private static final String TAG="ActivityCollector";
    private static ArrayMap<String, Activity> activityMap = new ArrayMap<>();

    public static void addActivity(String tag, Activity activity) {
        if(activityMap.containsKey(tag)){
            UL.e(TAG,"已经存在Activity "+tag);
        }
        activityMap.put(tag, activity);
        UL.d(TAG,"添加Activity "+tag);
    }

    public static void removeActivity(String tag) {
        activityMap.remove(tag);
        UL.d(TAG,"移除Activity "+tag);
    }

    public static void finishAll() {
        Iterator iterator = activityMap.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Activity activity = (Activity) entry.getValue();
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}

package com.lonebytesoft.thetaleclient;

import android.app.Application;
import android.content.Context;

import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.sdk.log.Logger;
import com.lonebytesoft.thetaleclient.sdk.log.SilentLogStrategy;
import com.lonebytesoft.thetaleclient.util.NotificationManager;
import com.lonebytesoft.thetaleclient.util.SdkLogStrategy;
import com.lonebytesoft.thetaleclient.util.map.MapUtils;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenStateWatcher;

/**
 * @author Hamster
 * @since 08.10.2014
 */
public class TheTaleClientApplication extends Application {

    private static Context context;
    private static OnscreenStateWatcher onscreenStateWatcher;
    private static NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        onscreenStateWatcher = new OnscreenStateWatcher();
        notificationManager = new NotificationManager(context);

        if(BuildConfig.DEBUG) {
            Logger.setLogStrategy(new SdkLogStrategy(true));
        } else {
            Logger.setLogStrategy(new SilentLogStrategy());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        RequestCacheManager.invalidate();
        MapUtils.cleanup();

        System.gc();
    }

    public static Context getContext() {
        return context;
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
    }

    public static OnscreenStateWatcher getOnscreenStateWatcher() {
        return onscreenStateWatcher;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

}

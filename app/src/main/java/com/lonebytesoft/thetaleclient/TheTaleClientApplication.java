package com.lonebytesoft.thetaleclient;

import android.app.Application;
import android.content.Context;

import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.fragment.onscreen.OnscreenStateWatcher;
import com.lonebytesoft.thetaleclient.util.map.MapManager;

/**
 * @author Hamster
 * @since 08.10.2014
 */
public class TheTaleClientApplication extends Application {

    private static Context context;
    private static OnscreenStateWatcher onscreenStateWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        onscreenStateWatcher = new OnscreenStateWatcher();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        RequestCacheManager.invalidate();
        MapManager.cleanup();

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

}

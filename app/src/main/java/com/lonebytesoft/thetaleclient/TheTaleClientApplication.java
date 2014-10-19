package com.lonebytesoft.thetaleclient;

import android.app.Application;
import android.content.Context;

import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.util.map.MapManager;

/**
 * @author Hamster
 * @since 08.10.2014
 */
public class TheTaleClientApplication extends Application {

    private static Context context;
    private static ApplicationPart applicationPart;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        applicationPart = ApplicationPart.INSIGNIFICANT;
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

    public static void onApplicationPartSelected(final ApplicationPart applicationPart) {
        TheTaleClientApplication.applicationPart = applicationPart;
    }

    public static ApplicationPart getSelectedApplicationPart() {
        return applicationPart;
    }

}

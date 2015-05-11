package com.lonebytesoft.thetaleclient.sdkandroid.util;

import android.content.Context;

import com.lonebytesoft.thetaleclient.sdkandroid.BuildConfig;

/**
 * @author Hamster
 * @since 11.05.2015
 */
public class RequestUtils {

    public static String getClientId(final Context context) {
        return context.getPackageName() + "-" + BuildConfig.VERSION_CODE;
    }

}

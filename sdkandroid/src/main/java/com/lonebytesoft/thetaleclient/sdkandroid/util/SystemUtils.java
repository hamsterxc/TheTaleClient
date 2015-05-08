package com.lonebytesoft.thetaleclient.sdkandroid.util;

/**
 * @author Hamster
 * @since 30.04.2015
 */
public class SystemUtils {

    public static long getFreeMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
    }

}

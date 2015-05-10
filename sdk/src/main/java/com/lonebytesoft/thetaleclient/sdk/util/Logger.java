package com.lonebytesoft.thetaleclient.sdk.util;

/**
 * @author Hamster
 * @since 10.05.2015
 */
public class Logger {

    private static String tag = null;
    private static boolean isEnabled = false;

    public static void setTag(final String tag) {
        Logger.tag = tag;
    }

    public static void setEnabled(final boolean isEnabled) {
        Logger.isEnabled = isEnabled;
    }

    public static void log(final String message) {
        if(isEnabled) {
            System.out.println(String.format("%s%s",
                    (tag == null) || (tag.length() == 0) ? "" : tag + ": ", message));
        }
    }

}

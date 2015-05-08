package com.lonebytesoft.thetaleclient.sdkandroid.util;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * @author Hamster
 * @since 30.04.2015
 */
public class UiUtils {

    private static void runChecked(final Fragment fragment, final Runnable task) {
        if((fragment != null) && (fragment.isAdded())) {
            runChecked(fragment.getActivity(), task);
        }
    }

    private static void runChecked(final Activity activity, final Runnable task) {
        if((activity != null) && !activity.isFinishing()) {
            activity.runOnUiThread(task);
        }
    }

    public static void runChecked(final Object uiComponent, final Runnable task) {
        if(uiComponent instanceof Fragment) {
            runChecked((Fragment) uiComponent, task);
        } else if(uiComponent instanceof Activity) {
            runChecked((Activity) uiComponent, task);
        } else {
            task.run();
        }
    }

}

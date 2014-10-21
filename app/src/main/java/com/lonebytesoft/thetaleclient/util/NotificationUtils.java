package com.lonebytesoft.thetaleclient.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.lonebytesoft.thetaleclient.ApplicationPart;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;

/**
 * @author Hamster
 * @since 10.10.2014
 */
public class NotificationUtils {

    private static final Context context = TheTaleClientApplication.getContext();

    public static void notifyDeath() {
        if(TheTaleClientApplication.getSelectedApplicationPart() != ApplicationPart.GAME_INFO) {
            notify(getMainActivityIntent(), context.getString(R.string.notification_death));
        }
    }

    public static void notifyIdleness() {
        if(TheTaleClientApplication.getSelectedApplicationPart() != ApplicationPart.GAME_INFO) {
            notify(getMainActivityIntent(), context.getString(R.string.notification_idle));
        }
    }

    public static void notifyLowHealth(final int health) {
        if(TheTaleClientApplication.getSelectedApplicationPart() != ApplicationPart.GAME_INFO) {
            notify(getMainActivityIntent(), context.getString(R.string.notification_low_health, health));
        }
    }

    public static void notifyHighEnergy(final int energy) {
        if(TheTaleClientApplication.getSelectedApplicationPart() != ApplicationPart.GAME_INFO) {
            notify(getMainActivityIntent(), context.getString(R.string.notification_high_energy, energy));
        }
    }

    public static void notifyNewMessages(final int newMessages) {
        notify(getMainActivityIntent(), context.getString(R.string.notification_new_messages, newMessages));
    }

    private static void notify(final PendingIntent intent, final String message) {
        final Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_push)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(context.getPackageName(), 0, notification);
    }

    private static PendingIntent getMainActivityIntent() {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MainActivity.KEY_GAME_TAB_INDEX, GameFragment.GamePage.GAME_INFO.ordinal());
        return PendingIntent.getActivity(
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}

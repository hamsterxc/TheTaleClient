package com.lonebytesoft.thetaleclient.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.notifier.DeathNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.EnergyNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.HealthNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.IdlenessNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.NewMessagesNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.Notifier;
import com.lonebytesoft.thetaleclient.service.notifier.QuestChoiceNotifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Hamster
 * @since 07.12.2014
 */
public class NotificationManager {

    private static final int NOTIFICATION_ID = 0;
    public static final String BROADCAST_NOTIFICATION_DELETE_ACTION =
            TheTaleClientApplication.getContext().getPackageName() + ".notification.delete";

    private final Context context;
    private final android.app.NotificationManager notificationManager;
    private final List<Notifier> notifiers;
    private final List<String> lastNotification;
    private final List<Notifier> lastNotifiers;

    public NotificationManager(final Context context) {
        this.context = context;
        notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notifiers = new ArrayList<>();
        notifiers.add(new DeathNotifier());
        notifiers.add(new IdlenessNotifier());
        notifiers.add(new HealthNotifier());
        notifiers.add(new EnergyNotifier());
        notifiers.add(new NewMessagesNotifier());
        notifiers.add(new QuestChoiceNotifier());

        lastNotification = new ArrayList<>();
        lastNotifiers = new ArrayList<>();
    }

    public void notify(final GameInfoResponse gameInfoResponse) {
        if(PreferencesManager.isNotificationNighttimeEnabled()) {
            final Calendar calendar = Calendar.getInstance();
            if(!isTimeAcceptable(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                    PreferencesManager.getNotificationNighttimeFromHour(), PreferencesManager.getNotificationNighttimeFromMinute(),
                    PreferencesManager.getNotificationNighttimeToHour(), PreferencesManager.getNotificationNighttimeToMinute())) {
                return;
            }
        }

        final List<String> notificationLines = new ArrayList<>();
        PendingIntent pendingIntent = null;
        lastNotifiers.clear();
        for(final Notifier notifier : notifiers) {
            notifier.setInfo(gameInfoResponse);
            if(notifier.isNotifying()) {
                notificationLines.add(notifier.getNotification(context));
                lastNotifiers.add(notifier);
                if(pendingIntent == null) {
                    pendingIntent = notifier.getPendingIntent(context);
                }
            }
        }

        final int linesCount = notificationLines.size();
        if(linesCount == 0) {
            notificationManager.cancel(context.getPackageName(), NOTIFICATION_ID);
            lastNotification.clear();
        } else if(!lastNotification.equals(notificationLines)) {
            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_push)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentIntent(pendingIntent)
                    .setDeleteIntent(PendingIntent.getBroadcast(context, 0, new Intent(BROADCAST_NOTIFICATION_DELETE_ACTION),
                            PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_CANCEL_CURRENT))
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);
            if(linesCount == 1) {
                notificationBuilder.setContentText(notificationLines.get(0));
            } else {
//                final String linesQuantity = context.getResources().getQuantityString(R.plurals.notification_lines, linesCount, linesCount);
                final String linesQuantity = UiUtils.getQuantityString(context,
                        R.string.notification_lines_one, R.string.notification_lines_few, R.string.notification_lines_many,
                        linesCount);
                final NotificationCompat.InboxStyle notificationStyle = new NotificationCompat.InboxStyle()
                        .setBigContentTitle(context.getString(R.string.app_name))
                        .setSummaryText(linesQuantity);
                for(int i = 0; i < linesCount; i++) {
                    notificationStyle.addLine(notificationLines.get(i));
                }

                notificationBuilder
                        .setContentText(linesQuantity)
                        .setStyle(notificationStyle);
            }

            lastNotification.clear();
            lastNotification.addAll(notificationLines);

            notificationManager.notify(context.getPackageName(), NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    public void onNotificationDelete() {
        for(final Notifier notifier : lastNotifiers) {
            notifier.onNotificationDelete();
        }
    }

    public void clearNotifications() {
        notificationManager.cancel(context.getPackageName(), NOTIFICATION_ID);
        onNotificationDelete();
    }

    private static boolean isTimeAcceptable(final int timeHours, final int timeMinutes,
                                            final int startHours, final int startMinutes,
                                            final int endHours, final int endMinutes) {
        if((endHours < startHours) || (endHours == startHours) && (endMinutes  < startMinutes)) {
            return isTimeAcceptable(timeHours, timeMinutes, startHours, startMinutes, 24, 0)
                    && isTimeAcceptable(timeHours, timeMinutes, 0, 0, endHours, endMinutes);
        } else {
            if(startHours < endHours) {
                return (timeHours < startHours) || (timeHours == startHours) && (timeMinutes < startMinutes)
                        || (timeHours > endHours) || (timeHours == endHours) && (timeMinutes > endMinutes);
            } else {
                return (timeHours != startHours) || (timeMinutes < startMinutes) || (timeMinutes > endMinutes);
            }
        }
    }

}

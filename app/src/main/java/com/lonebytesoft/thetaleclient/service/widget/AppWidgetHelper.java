package com.lonebytesoft.thetaleclient.service.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.model.HeroActionInfo;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 14.01.2015
 */
public class AppWidgetHelper {

    public static final String BROADCAST_WIDGET_HELP_ACTION =
            TheTaleClientApplication.getContext().getPackageName() + ".widget.help";
    public static final String BROADCAST_WIDGET_REFRESH_ACTION =
            TheTaleClientApplication.getContext().getPackageName() + ".widget.refresh";

    public static void update(final Context context, final DataViewMode viewMode, final GameInfoResponse gameInfoResponse) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final boolean isAppWidgetSizingSupported = isAppWidgetSizingSupported(context);

        for(final AppWidget appWidget : AppWidget.values()) {
            final ComponentName componentName = new ComponentName(context, appWidget.getProviderClass());

            if(isAppWidgetSizingSupported && (viewMode == DataViewMode.DATA)) {
                for (final int appWidgetId : appWidgetManager.getAppWidgetIds(componentName)) {
                    final RemoteViews remoteViews = getRemoteViews(context, viewMode);
                    updateRemoteViewsContentLayout(context, remoteViews, appWidgetManager.getAppWidgetOptions(appWidgetId));
                    fillRemoteViewsData(context, remoteViews, gameInfoResponse);
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                }
            } else {
                final RemoteViews remoteViews = getRemoteViews(context, viewMode);
                switch(viewMode) {
                    case DATA:
                        updateRemoteViewsContentLayout(context, remoteViews, appWidget.getWidth(), appWidget.getHeight());
                        fillRemoteViewsData(context, remoteViews, gameInfoResponse);
                        break;

                    case ERROR:
                        fillRemoteViewsError(context, remoteViews, gameInfoResponse);
                        break;
                }
                appWidgetManager.updateAppWidget(componentName, remoteViews);
            }
        }
    }

    public static void updateWithRequest(final Context context) {
        update(context, DataViewMode.LOADING, null);
        new GameInfoRequest(true).execute(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(GameInfoResponse response) {
                update(context, DataViewMode.DATA, response);
            }

            @Override
            public void processError(GameInfoResponse response) {
                update(context, DataViewMode.ERROR, response);
            }
        }, true);
    }

    public static void updateWithError(final Context context, final String error) {
        try {
            update(context, DataViewMode.ERROR, new GameInfoResponse(RequestUtils.getGenericErrorResponse(error)));
        } catch(JSONException e) {
            update(context, DataViewMode.ERROR, null);
        }
    }

    private static RemoteViews getRemoteViews(final Context context, final DataViewMode viewMode) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        remoteViews.setViewVisibility(R.id.app_widget_content, viewMode == DataViewMode.DATA ? View.VISIBLE : View.GONE);
        remoteViews.setViewVisibility(R.id.app_widget_progress, viewMode == DataViewMode.LOADING ? View.VISIBLE : View.GONE);
        remoteViews.setViewVisibility(R.id.app_widget_error, viewMode == DataViewMode.ERROR ? View.VISIBLE : View.GONE);

        remoteViews.setOnClickPendingIntent(R.id.app_widget, UiUtils.getApplicationIntent(context));
        remoteViews.setOnClickPendingIntent(R.id.app_widget_error_retry, PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                new Intent(BROADCAST_WIDGET_REFRESH_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT));

        return remoteViews;
    }

    private static void updateRemoteViewsContentLayout(final Context context, final RemoteViews remoteViews,
                                                       final int width, final int height) {
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_bars_progress, true);

        final boolean isWidthSmall = width < context.getResources().getDimension(R.dimen.app_widget_size_2);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_bars_icon, !isWidthSmall);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_bars_progress_health_text, isWidthSmall);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_bars_progress_experience_text, isWidthSmall);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_bars_progress_energy_text, isWidthSmall);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_bars_text, !isWidthSmall);

        final boolean isWidthMedium = width < context.getResources().getDimension(R.dimen.app_widget_size_3);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_help, !isWidthMedium);

        final boolean isHeightSmall = height < context.getResources().getDimension(R.dimen.app_widget_size_2);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_action_info, !isWidthSmall && !isHeightSmall);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_header, !isWidthSmall && !isHeightSmall);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_logo, !isWidthMedium || !isHeightSmall);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateRemoteViewsContentLayout(final Context context, final RemoteViews remoteViews,
                                                       final Bundle appWidgetOptions) {
        updateRemoteViewsContentLayout(context, remoteViews,
                appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH),
                appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
    }

    private static void fillRemoteViewsData(final Context context, final RemoteViews remoteViews,
                                            final GameInfoResponse gameInfoResponse) {
        remoteViews.setProgressBar(R.id.app_widget_bars_progress_health,
                gameInfoResponse.account.hero.basicInfo.healthMax,
                gameInfoResponse.account.hero.basicInfo.healthCurrent,
                false);
        remoteViews.setTextViewText(R.id.app_widget_bars_text_health, String.format("%d/%d",
                gameInfoResponse.account.hero.basicInfo.healthCurrent,
                gameInfoResponse.account.hero.basicInfo.healthMax));

        remoteViews.setProgressBar(R.id.app_widget_bars_progress_experience,
                gameInfoResponse.account.hero.basicInfo.experienceForNextLevel,
                gameInfoResponse.account.hero.basicInfo.experienceCurrent,
                false);
        remoteViews.setTextViewText(R.id.app_widget_bars_text_experience, String.format("%d/%d",
                gameInfoResponse.account.hero.basicInfo.experienceCurrent,
                gameInfoResponse.account.hero.basicInfo.experienceForNextLevel));

        remoteViews.setProgressBar(R.id.app_widget_bars_progress_energy,
                gameInfoResponse.account.hero.energy.max,
                gameInfoResponse.account.hero.energy.current,
                false);
        remoteViews.setTextViewText(R.id.app_widget_bars_text_energy,
                GameInfoUtils.getEnergyString(gameInfoResponse.account.hero.energy));

        remoteViews.setTextViewText(R.id.app_widget_help, context.getString(R.string.game_help));
        remoteViews.setOnClickPendingIntent(R.id.app_widget_help, PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                new Intent(BROADCAST_WIDGET_HELP_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT));

        final HeroActionInfo actionInfo = gameInfoResponse.account.hero.action;
        remoteViews.setTextViewText(R.id.app_widget_action_info_text,
                GameInfoUtils.getActionString(context, actionInfo));
        remoteViews.setProgressBar(R.id.app_widget_action_info_progress,
                1000, (int) (1000.0 * actionInfo.completion), false);

        remoteViews.setTextViewText(R.id.app_widget_header_level, String.valueOf(gameInfoResponse.account.hero.basicInfo.level));
        remoteViews.setTextViewText(R.id.app_widget_header_name, gameInfoResponse.account.hero.basicInfo.name);
    }

    private static void fillRemoteViewsError(final Context context, final RemoteViews remoteViews,
                                             final GameInfoResponse gameInfoResponse) {
        if(gameInfoResponse == null) {
            remoteViews.setTextViewText(R.id.app_widget_error_text, context.getString(R.string.common_error_short));
        } else {
            remoteViews.setTextViewText(R.id.app_widget_error_text, gameInfoResponse.errorMessage);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static boolean isAppWidgetSizingSupported(final Context context) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return false;
        }

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        for(final AppWidget appWidget : AppWidget.values()) {
            final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, appWidget.getProviderClass()));
            if(appWidgetIds.length > 0) {
                final Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetIds[0]);
                return (options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) > 0) &&
                        (options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) > 0);
            }
        }

        return false;
    }

    private static void setRemoteViewsViewVisibility(final RemoteViews remoteViews, final int viewId, final boolean isVisible) {
        remoteViews.setViewVisibility(viewId, isVisible ? View.VISIBLE : View.GONE);
    }

}

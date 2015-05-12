package com.lonebytesoft.thetaleclient.service.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.ApiCallback;
import com.lonebytesoft.thetaleclient.service.WatcherService;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 14.01.2015
 */
public class AppWidgetHelper {

    public static void update(final Context context, final DataViewMode viewMode, final GameInfoResponse gameInfoResponse) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        for(final AppWidget appWidget : AppWidget.values()) {
            appWidgetManager.updateAppWidget(
                    new ComponentName(context, appWidget.getProviderClass()),
                    getRemoteViews(context, appWidget, viewMode, gameInfoResponse));
        }
    }

    public static void update(final Context context, final AbstractApiResponse abstractApiResponse) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        for(final AppWidget appWidget : AppWidget.values()) {
            appWidgetManager.updateAppWidget(
                    new ComponentName(context, appWidget.getProviderClass()),
                    getRemoteViews(context, appWidget, abstractApiResponse));
        }
    }

    public static void updateWithRequest(final Context context) {
        update(context, DataViewMode.LOADING, null);
        RequestUtils.executeGameInfoRequest(
                context,
                new ApiCallback<GameInfoResponse>() {
                    @Override
                    public void onSuccess(GameInfoResponse response) {
                        update(context, DataViewMode.DATA, response);
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        update(context, response);
                    }
                });
    }

    public static void updateWithError(final Context context, final String error) {
        try {
            update(context, DataViewMode.ERROR, new GameInfoResponse(RequestUtils.getGenericErrorResponse(error)));
        } catch(JSONException e) {
            update(context, DataViewMode.ERROR, null);
        }
    }

    private static RemoteViews getRemoteViews(final Context context, final AppWidget appWidget,
                                              final DataViewMode viewMode, final GameInfoResponse gameInfoResponse) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), appWidget.getLayoutResId());

        UiUtils.setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_content, viewMode == DataViewMode.DATA);
        UiUtils.setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_progress, viewMode == DataViewMode.LOADING);
        UiUtils.setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_error, viewMode == DataViewMode.ERROR);

        remoteViews.setOnClickPendingIntent(R.id.app_widget, UiUtils.getApplicationIntent(context, null, true));

        switch(viewMode) {
            case DATA:
                appWidget.fillData(context, remoteViews, gameInfoResponse);
                break;

            case ERROR:
                if(gameInfoResponse == null) {
                    remoteViews.setTextViewText(R.id.app_widget_error_text, context.getString(R.string.common_error_short));
                } else {
                    remoteViews.setTextViewText(R.id.app_widget_error_text, gameInfoResponse.errorMessage);
                }

                remoteViews.setOnClickPendingIntent(R.id.app_widget_error_retry, PendingIntent.getBroadcast(
                        context,
                        (int) System.currentTimeMillis(),
                        new Intent(WatcherService.BROADCAST_WIDGET_REFRESH_ACTION),
                        PendingIntent.FLAG_CANCEL_CURRENT));
                break;
        }

        return remoteViews;
    }

    private static RemoteViews getRemoteViews(final Context context, final AppWidget appWidget,
                                              final AbstractApiResponse abstractApiResponse) {
        try {
            return getRemoteViews(context, appWidget, DataViewMode.ERROR,
                    new GameInfoResponse(abstractApiResponse.rawResponse));
        } catch (JSONException e) {
            return getRemoteViews(context, appWidget, DataViewMode.ERROR, null);
        }
    }

}

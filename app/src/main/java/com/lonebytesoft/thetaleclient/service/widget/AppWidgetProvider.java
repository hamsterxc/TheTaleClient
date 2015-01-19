package com.lonebytesoft.thetaleclient.service.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Bundle;

import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 13.01.2015
 */
public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AppWidgetHelper.updateWithRequest(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        AppWidgetHelper.updateWithRequest(context);
    }

    @Override
    public void onEnabled(Context context) {
        PreferencesManager.setWidgetEnabled(true);
    }

    @Override
    public void onDisabled(Context context) {
        PreferencesManager.setWidgetEnabled(false);
    }

}

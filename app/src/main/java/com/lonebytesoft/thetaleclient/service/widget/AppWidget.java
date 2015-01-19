package com.lonebytesoft.thetaleclient.service.widget;

import android.appwidget.AppWidgetProvider;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;

/**
 * @author Hamster
 * @since 13.01.2015
 */
public enum AppWidget {

    SMALL(AppWidgetProviderSmall.class, R.dimen.app_widget_size_1, R.dimen.app_widget_size_1),
    MEDIUM(AppWidgetProviderMedium.class, R.dimen.app_widget_size_2, R.dimen.app_widget_size_1),
    LARGE(AppWidgetProviderLarge.class, R.dimen.app_widget_size_3, R.dimen.app_widget_size_1),
    HUGE(AppWidgetProviderHuge.class, R.dimen.app_widget_size_4, R.dimen.app_widget_size_2),
    ;

    private final Class<? extends AppWidgetProvider> providerClass;
    private final int width;
    private final int height;

    private AppWidget(final Class<? extends android.appwidget.AppWidgetProvider> providerClass,
                      final int widthResId, final int heightResId) {
        this.providerClass = providerClass;
        this.width = (int) TheTaleClientApplication.getContext().getResources().getDimension(widthResId);
        this.height = (int) TheTaleClientApplication.getContext().getResources().getDimension(heightResId);
    }

    public Class<? extends AppWidgetProvider> getProviderClass() {
        return providerClass;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}

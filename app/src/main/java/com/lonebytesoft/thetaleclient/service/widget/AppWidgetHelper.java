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

    private static final int SIZE_SMALL = 0;
    private static final int SIZE_MEDIUM = 1;
    private static final int SIZE_LARGE = 2;
    private static final int SIZE_HUGE = 3;

    public static void update(final Context context, final DataViewMode viewMode, final GameInfoResponse gameInfoResponse) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final boolean isAppWidgetSizingSupported = isAppWidgetSizingSupported(context);

        for(final AppWidget appWidget : AppWidget.values()) {
            final ComponentName componentName = new ComponentName(context, appWidget.getProviderClass());

            if(isAppWidgetSizingSupported && (viewMode == DataViewMode.DATA)) {
                for (final int appWidgetId : appWidgetManager.getAppWidgetIds(componentName)) {
                    final Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
                    final RemoteViews remoteViews = getRemoteViews(context, viewMode);
                    fillRemoteViewsData(context, remoteViews,
                            getWidgetSize(context, appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)),
                            getWidgetSize(context, appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)),
                            gameInfoResponse);
                    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                }
            } else {
                final RemoteViews remoteViews = getRemoteViews(context, viewMode);
                switch(viewMode) {
                    case DATA:
                        fillRemoteViewsData(context, remoteViews,
                                getWidgetSize(context, appWidget.getWidth()),
                                getWidgetSize(context, appWidget.getHeight()),
                                gameInfoResponse);
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

        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_content, viewMode == DataViewMode.DATA);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_progress, viewMode == DataViewMode.LOADING);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_error, viewMode == DataViewMode.ERROR);

        remoteViews.setOnClickPendingIntent(R.id.app_widget, UiUtils.getApplicationIntent(context));
        remoteViews.setOnClickPendingIntent(R.id.app_widget_error_retry, PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                new Intent(BROADCAST_WIDGET_REFRESH_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT));

        return remoteViews;
    }

    private static void fillRemoteViewsData(final Context context, final RemoteViews remoteViews,
                                            final int width, final int height,
                                            final GameInfoResponse gameInfoResponse) {
        final boolean isCompanionPresent = gameInfoResponse.account.hero.companionInfo != null;

        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_logo, height >= SIZE_MEDIUM);

        final boolean isActionPresent = (width >= SIZE_MEDIUM) && (height >= SIZE_MEDIUM) && !((width == SIZE_MEDIUM) && (height == SIZE_MEDIUM));
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_action_info, isActionPresent);
        if(isActionPresent) {
            final HeroActionInfo actionInfo = gameInfoResponse.account.hero.action;
            remoteViews.setTextViewText(R.id.app_widget_action_info_text,
                    GameInfoUtils.getActionString(context, actionInfo));
            remoteViews.setProgressBar(R.id.app_widget_action_info_progress,
                    1000, (int) (1000.0 * actionInfo.completion), false);
        }

        final boolean isHeroBasicInfoPresent = (width >= SIZE_LARGE) && (height >= SIZE_MEDIUM);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_hero_basic_info, isHeroBasicInfoPresent);
        if(isHeroBasicInfoPresent) {
            remoteViews.setTextViewText(R.id.app_widget_hero_level, String.valueOf(gameInfoResponse.account.hero.basicInfo.level));
            remoteViews.setTextViewText(R.id.app_widget_hero_name, gameInfoResponse.account.hero.basicInfo.name);
        }

        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_hero_bars_icon, width >= SIZE_MEDIUM);

        remoteViews.setProgressBar(R.id.app_widget_hero_bars_progress_health,
                gameInfoResponse.account.hero.basicInfo.healthMax,
                gameInfoResponse.account.hero.basicInfo.healthCurrent,
                false);
        remoteViews.setProgressBar(R.id.app_widget_hero_bars_progress_experience,
                gameInfoResponse.account.hero.basicInfo.experienceForNextLevel,
                gameInfoResponse.account.hero.basicInfo.experienceCurrent,
                false);
        remoteViews.setProgressBar(R.id.app_widget_hero_bars_progress_energy,
                gameInfoResponse.account.hero.energy.max,
                gameInfoResponse.account.hero.energy.current,
                false);

        final boolean isHeroBarsCaptionsPresent = width <= SIZE_SMALL;
        final boolean isHeroBarsValuesPresent = (width >= SIZE_LARGE) && (height == SIZE_MEDIUM);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_hero_bars_progress_health_text, isHeroBarsCaptionsPresent || isHeroBarsValuesPresent);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_hero_bars_progress_experience_text, isHeroBarsCaptionsPresent || isHeroBarsValuesPresent);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_hero_bars_progress_energy_text, isHeroBarsCaptionsPresent || isHeroBarsValuesPresent);
        if(isHeroBarsCaptionsPresent) {
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_health_text,
                    context.getString(R.string.game_info_health));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_experience_text,
                    context.getString(R.string.game_info_experience));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_energy_text,
                    context.getString(R.string.game_info_energy));
        } else if(isHeroBarsValuesPresent) {
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_health_text,
                    GameInfoUtils.getHealthString(gameInfoResponse.account.hero.basicInfo));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_experience_text,
                    GameInfoUtils.getExperienceString(gameInfoResponse.account.hero.basicInfo));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_energy_text,
                    GameInfoUtils.getEnergyString(gameInfoResponse.account.hero.energy));
        }

        final boolean isHeroBarsTextPresent = (width >= SIZE_MEDIUM) && !((width >= SIZE_LARGE) && (height == SIZE_MEDIUM));
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_hero_bars_text, isHeroBarsTextPresent);
        if(isHeroBarsTextPresent) {
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_text_health,
                    GameInfoUtils.getHealthString(gameInfoResponse.account.hero.basicInfo));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_text_experience,
                    GameInfoUtils.getExperienceString(gameInfoResponse.account.hero.basicInfo));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_text_energy,
                    GameInfoUtils.getEnergyString(gameInfoResponse.account.hero.energy));
        }

        final boolean isCompanionRightPresent = isCompanionPresent && (width >= SIZE_LARGE) && (height == SIZE_MEDIUM);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_companion_right_info, isCompanionRightPresent);
        if(isCompanionRightPresent) {
            remoteViews.setTextViewText(R.id.app_widget_companion_right_level, String.valueOf(gameInfoResponse.account.hero.companionInfo.coherence));
            remoteViews.setTextViewText(R.id.app_widget_companion_right_name, gameInfoResponse.account.hero.companionInfo.name);

            remoteViews.setProgressBar(R.id.app_widget_companion_right_bars_progress_health,
                    gameInfoResponse.account.hero.companionInfo.healthMax,
                    gameInfoResponse.account.hero.companionInfo.healthCurrent,
                    false);
            remoteViews.setProgressBar(R.id.app_widget_companion_right_bars_progress_experience,
                    gameInfoResponse.account.hero.companionInfo.experienceForNextLevel,
                    gameInfoResponse.account.hero.companionInfo.experienceCurrent,
                    false);

            remoteViews.setTextViewText(R.id.app_widget_companion_right_bars_progress_health_text,
                    GameInfoUtils.getCompanionHealthString(gameInfoResponse.account.hero.companionInfo));
            remoteViews.setTextViewText(R.id.app_widget_companion_right_bars_progress_experience_text,
                    GameInfoUtils.getCompanionExperienceString(gameInfoResponse.account.hero.companionInfo));

            setRemoteViewsHelpAction(context, remoteViews, R.id.app_widget_help_right_bottom);
        }

        final boolean isHelpRightPresent = (width >= SIZE_LARGE) && (height <= SIZE_SMALL);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_help_right, isHelpRightPresent);
        if(isHelpRightPresent) {
            setRemoteViewsHelpAction(context, remoteViews, R.id.app_widget_help_right);
        }

        final boolean isCompanionBelowPresent = isCompanionPresent && (width >= SIZE_LARGE) && (height >= SIZE_LARGE);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_companion_below_info, isCompanionBelowPresent);
        if(isCompanionBelowPresent) {
            remoteViews.setTextViewText(R.id.app_widget_companion_bottom_level, String.valueOf(gameInfoResponse.account.hero.companionInfo.coherence));
            remoteViews.setTextViewText(R.id.app_widget_companion_bottom_name, gameInfoResponse.account.hero.companionInfo.name);

            remoteViews.setProgressBar(R.id.app_widget_companion_bottom_bars_progress_health,
                    gameInfoResponse.account.hero.companionInfo.healthMax,
                    gameInfoResponse.account.hero.companionInfo.healthCurrent,
                    false);
            remoteViews.setProgressBar(R.id.app_widget_companion_bottom_bars_progress_experience,
                    gameInfoResponse.account.hero.companionInfo.experienceForNextLevel,
                    gameInfoResponse.account.hero.companionInfo.experienceCurrent,
                    false);

            remoteViews.setTextViewText(R.id.app_widget_companion_bottom_bars_text_health,
                    GameInfoUtils.getCompanionHealthString(gameInfoResponse.account.hero.companionInfo));
            remoteViews.setTextViewText(R.id.app_widget_companion_bottom_bars_text_experience,
                    GameInfoUtils.getCompanionExperienceString(gameInfoResponse.account.hero.companionInfo));
        }

        final boolean isHelpBelowPresent = !isCompanionRightPresent && (height >= SIZE_MEDIUM);
        setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_help_bottom, isHelpBelowPresent);
        if(isHelpBelowPresent) {
            setRemoteViewsHelpAction(context, remoteViews, R.id.app_widget_help_bottom);
        }
    }

    private static int getWidgetSize(final Context context, final int size) {
        if(size < context.getResources().getDimension(R.dimen.app_widget_size_2)) {
            return SIZE_SMALL;
        } else if(size < context.getResources().getDimension(R.dimen.app_widget_size_3)) {
            return SIZE_MEDIUM;
        } else if(size < context.getResources().getDimension(R.dimen.app_widget_size_4)) {
            return SIZE_LARGE;
        } else {
            return SIZE_HUGE;
        }
    }

    private static void setRemoteViewsHelpAction(final Context context, final RemoteViews remoteViews, final int resId) {
        remoteViews.setOnClickPendingIntent(resId, PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                new Intent(BROADCAST_WIDGET_HELP_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT));
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

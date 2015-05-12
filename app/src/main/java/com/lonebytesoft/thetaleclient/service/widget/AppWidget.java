package com.lonebytesoft.thetaleclient.service.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.sdk.model.CompanionInfo;
import com.lonebytesoft.thetaleclient.sdk.model.HeroActionInfo;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.WatcherService;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 13.01.2015
 */
public enum AppWidget {

    SMALL(AppWidgetProviderSmall.class, R.layout.app_widget_small) {
        @Override
        public void fillData(Context context, RemoteViews remoteViews, GameInfoResponse gameInfoResponse) {
            fillHeroBars(remoteViews, gameInfoResponse);
        }
    },

    MEDIUM(AppWidgetProviderMedium.class, R.layout.app_widget_medium) {
        @Override
        public void fillData(Context context, RemoteViews remoteViews, GameInfoResponse gameInfoResponse) {
            fillHeroBars(remoteViews, gameInfoResponse);
            fillHeroBarsNearbyText(remoteViews, gameInfoResponse);
        }
    },

    LARGE(AppWidgetProviderLarge.class, R.layout.app_widget_large) {
        @Override
        public void fillData(Context context, RemoteViews remoteViews, GameInfoResponse gameInfoResponse) {
            fillHeroBars(remoteViews, gameInfoResponse);
            fillHeroBarsNearbyText(remoteViews, gameInfoResponse);
            setHelpAction(context, remoteViews, R.id.app_widget_help_right);
        }
    },

    HUGE(AppWidgetProviderHuge.class, R.layout.app_widget_huge) {
        @Override
        public void fillData(Context context, RemoteViews remoteViews, GameInfoResponse gameInfoResponse) {
            remoteViews.setTextViewText(R.id.app_widget_hero_level, String.valueOf(gameInfoResponse.account.hero.basicInfo.level));
            remoteViews.setTextViewText(R.id.app_widget_hero_name, gameInfoResponse.account.hero.basicInfo.name);

            fillHeroBars(remoteViews, gameInfoResponse);

            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_health_text,
                    GameInfoUtils.getHealthString(gameInfoResponse.account.hero.basicInfo));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_experience_text,
                    GameInfoUtils.getExperienceString(gameInfoResponse.account.hero.basicInfo));
            remoteViews.setTextViewText(R.id.app_widget_hero_bars_progress_energy_text,
                    GameInfoUtils.getEnergyString(gameInfoResponse.account.hero.energy));

            final CompanionInfo companionInfo = gameInfoResponse.account.hero.companionInfo;
            if(companionInfo == null) {
                UiUtils.setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_companion_right_info, false);
                UiUtils.setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_no_companion, true);
            } else {
                UiUtils.setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_no_companion, false);
                UiUtils.setRemoteViewsViewVisibility(remoteViews, R.id.app_widget_companion_right_info, true);

                remoteViews.setTextViewText(R.id.app_widget_companion_right_level, String.valueOf(companionInfo.coherence));
                remoteViews.setTextViewText(R.id.app_widget_companion_right_name, companionInfo.name);

                remoteViews.setProgressBar(R.id.app_widget_companion_right_bars_progress_health,
                        companionInfo.healthMax, companionInfo.healthCurrent, false);
                remoteViews.setProgressBar(R.id.app_widget_companion_right_bars_progress_experience,
                        companionInfo.experienceForNextLevel, companionInfo.experienceCurrent, false);

                remoteViews.setTextViewText(R.id.app_widget_companion_right_bars_progress_health_text,
                        GameInfoUtils.getCompanionHealthString(companionInfo));
                remoteViews.setTextViewText(R.id.app_widget_companion_right_bars_progress_experience_text,
                        GameInfoUtils.getCompanionExperienceString(companionInfo));
            }

            setHelpAction(context, remoteViews, R.id.app_widget_help_right_bottom);

            final HeroActionInfo actionInfo = gameInfoResponse.account.hero.action;
            remoteViews.setTextViewText(R.id.app_widget_action_info_text,
                    GameInfoUtils.getActionString(context, actionInfo));
            remoteViews.setProgressBar(R.id.app_widget_action_info_progress,
                    1000, (int) (1000.0 * actionInfo.completion), false);
        }
    },
    ;

    private final Class<? extends AppWidgetProvider> providerClass;
    private final int layoutResId;

    AppWidget(final Class<? extends android.appwidget.AppWidgetProvider> providerClass,
                      final int layoutResId) {
        this.providerClass = providerClass;
        this.layoutResId = layoutResId;
    }

    public Class<? extends AppWidgetProvider> getProviderClass() {
        return providerClass;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public abstract void fillData(Context context, final RemoteViews remoteViews, final GameInfoResponse gameInfoResponse);

    private static void fillHeroBars(final RemoteViews remoteViews, final GameInfoResponse gameInfoResponse) {
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
    }

    private static void fillHeroBarsNearbyText(final RemoteViews remoteViews, final GameInfoResponse gameInfoResponse) {
        remoteViews.setTextViewText(R.id.app_widget_hero_bars_text_health,
                GameInfoUtils.getHealthString(gameInfoResponse.account.hero.basicInfo));
        remoteViews.setTextViewText(R.id.app_widget_hero_bars_text_experience,
                GameInfoUtils.getExperienceString(gameInfoResponse.account.hero.basicInfo));
        remoteViews.setTextViewText(R.id.app_widget_hero_bars_text_energy,
                GameInfoUtils.getEnergyString(gameInfoResponse.account.hero.energy));
    }

    private static void setHelpAction(final Context context, final RemoteViews remoteViews, final int resId) {
        remoteViews.setOnClickPendingIntent(resId, PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                new Intent(WatcherService.BROADCAST_WIDGET_HELP_ACTION),
                PendingIntent.FLAG_CANCEL_CURRENT));
    }

}

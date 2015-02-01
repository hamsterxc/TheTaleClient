package com.lonebytesoft.thetaleclient.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;

import java.util.Map;

/**
 * @author Hamster
 * @since 08.10.2014
 */
@SuppressLint("CommitPrefEdits")
public class PreferencesManager {

    private static final String KEY_MAP_STYLE = "KEY_MAP_STYLE";
    private static final String KEY_MAP_CENTER_PLACE_ID = "KEY_MAP_CENTER_PLACE_ID";
    private static final String KEY_SESSION = "KEY_SESSION";
    private static final String KEY_READ_ALOUD_CONFIRMED = "KEY_READ_ALOUD_CONFIRMED";
    private static final String KEY_LAST_DIARY_ENTRY_READ = "KEY_LAST_DIARY_ENTRY_READ";
    private static final String KEY_DESIRED_GAME_PAGE = "KEY_DESIRED_GAME_PAGE";
    private static final String KEY_WIDGET_ENABLED = "KEY_WIDGET_ENABLED";
    private static final String KEY_SHOULD_EXIT = "KEY_SHOULD_EXIT";

    private static final String KEY_ACCOUNT_ID = "KEY_ACCOUNT_ID";
    private static final String KEY_ACCOUNT_NAME = "KEY_ACCOUNT_NAME";
    private static final String KEY_MAP_VERSION = "KEY_MAP_VERSION";
    private static final String KEY_ABILITY_COST = "KEY_ABILITY_COST_%s";
    private static final String KEY_TURN_DELTA = "KEY_TURN_DELTA";
    private static final String KEY_STATIC_CONTENT_URL = "KEY_STATIC_CONTENT_URL";

    private static final String KEY_NOTIFICATION_LAST = "%s_LAST";
    private static final String KEY_NOTIFICATION_SHOULD_SHOW = "%s_SHOULD_SHOW";
    private static final String KEY_NOTIFICATION_SHOWN = "%s_SHOWN";

    private static SharedPreferences sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(TheTaleClientApplication.getContext());

    private static boolean getBoolean(final int resId) {
        return TheTaleClientApplication.getContext().getResources().getBoolean(resId);
    }

    private static int getInteger(final int resId) {
        return TheTaleClientApplication.getContext().getResources().getInteger(resId);
    }

    private static String getString(final int resId) {
        return TheTaleClientApplication.getContext().getResources().getString(resId);
    }

    private static int getIntegerIfExist(final int keyResId, final int defaultValueResId) {
        final String value = sharedPreferences.getString(getString(keyResId), null);
        final int intDefault = getInteger(defaultValueResId);
        try {
            return TextUtils.isEmpty(value) ? intDefault : Integer.decode(value);
        } catch (NumberFormatException e) {
            return intDefault;
        }
    }

    public static boolean shouldNotifyDeath() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_death),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static boolean shouldShowNotificationDeath() {
        return sharedPreferences.getBoolean(
                String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_death)),
                true);
    }

    public static void setShouldShowNotificationDeath(final boolean shouldShowNotificationDeath) {
        sharedPreferences.edit()
                .putBoolean(
                        String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_death)),
                        shouldShowNotificationDeath)
                .commit();
    }

    public static boolean shouldNotifyIdleness() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_idleness),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static boolean shouldShowNotificationIdleness() {
        return sharedPreferences.getBoolean(
                String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_idleness)),
                true);
    }

    public static void setShouldShowNotificationIdleness(final boolean shouldShowNotificationIdleness) {
        sharedPreferences.edit()
                .putBoolean(
                        String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_idleness)),
                        shouldShowNotificationIdleness)
                .commit();
    }

    public static boolean shouldNotifyHealth() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_health),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static int getNotificationThresholdHealth() {
        return getIntegerIfExist(R.string.settings_key_notification_health_threshold, R.integer.settings_notification_health_threshold_default);
    }

    public static boolean shouldShowNotificationHealth() {
        return sharedPreferences.getBoolean(
                String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_health)),
                true);
    }

    public static void setShouldShowNotificationHealth(final boolean shouldShowNotificationHealth) {
        sharedPreferences.edit()
                .putBoolean(
                        String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_health)),
                        shouldShowNotificationHealth)
                .commit();
    }

    public static boolean shouldNotifyEnergy() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_energy),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static int getNotificationThresholdEnergy() {
        return getIntegerIfExist(R.string.settings_key_notification_energy_threshold, R.integer.settings_notification_energy_threshold_default);
    }

    public static boolean shouldShowNotificationEnergy() {
        return sharedPreferences.getBoolean(
                String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_energy)),
                true);
    }

    public static void setShouldShowNotificationEnergy(final boolean shouldShowNotificationEnergy) {
        sharedPreferences.edit()
                .putBoolean(
                        String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_energy)),
                        shouldShowNotificationEnergy)
                .commit();
    }

    public static int getLastNotificationEnergy() {
        return sharedPreferences.getInt(
                String.format(KEY_NOTIFICATION_LAST, getString(R.string.settings_key_notification_energy)),
                -1);
    }

    public static void setLastNotificationEnergy(final int lastNotificationEnergy) {
        sharedPreferences.edit()
                .putInt(
                        String.format(KEY_NOTIFICATION_LAST, getString(R.string.settings_key_notification_energy)),
                        lastNotificationEnergy)
                .commit();
    }

    public static int getLastShownNotificationEnergy() {
        return sharedPreferences.getInt(
                String.format(KEY_NOTIFICATION_SHOWN, getString(R.string.settings_key_notification_energy)),
                -1);
    }

    public static void setLastShownNotificationEnergy(final int lastShownNotificationEnergy) {
        sharedPreferences.edit()
                .putInt(
                        String.format(KEY_NOTIFICATION_SHOWN, getString(R.string.settings_key_notification_energy)),
                        lastShownNotificationEnergy)
                .commit();
    }

    public static boolean shouldNotifyNewMessages() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_new_messages),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static boolean shouldShowNotificationNewMessages() {
        return sharedPreferences.getBoolean(
                String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_new_messages)),
                true);
    }

    public static void setShouldShowNotificationNewMessages(final boolean shouldShowNotificationNewMessages) {
        sharedPreferences.edit()
                .putBoolean(
                        String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_new_messages)),
                        shouldShowNotificationNewMessages)
                .commit();
    }

    public static int getLastNotificationNewMessages() {
        return sharedPreferences.getInt(
                String.format(KEY_NOTIFICATION_LAST, getString(R.string.settings_key_notification_new_messages)),
                -1);
    }

    public static void setLastNotificationNewMessages(final int lastNotificationNewMessages) {
        sharedPreferences.edit()
                .putInt(
                        String.format(KEY_NOTIFICATION_LAST, getString(R.string.settings_key_notification_new_messages)),
                        lastNotificationNewMessages)
                .commit();
    }

    public static int getLastShownNotificationNewMessages() {
        return sharedPreferences.getInt(
                String.format(KEY_NOTIFICATION_SHOWN, getString(R.string.settings_key_notification_new_messages)),
                -1);
    }

    public static void setLastShownNotificationNewMessages(final int lastShownNotificationNewMessages) {
        sharedPreferences.edit()
                .putInt(
                        String.format(KEY_NOTIFICATION_SHOWN, getString(R.string.settings_key_notification_new_messages)),
                        lastShownNotificationNewMessages)
                .commit();
    }

    public static boolean shouldNotifyQuestChoice() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_quest_choice),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static boolean shouldShowNotificationQuestChoice() {
        return sharedPreferences.getBoolean(
                String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_quest_choice)),
                true);
    }

    public static void setShouldShowNotificationQuestChoice(final boolean shouldShowNotificationQuestChoice) {
        sharedPreferences.edit()
                .putBoolean(
                        String.format(KEY_NOTIFICATION_SHOULD_SHOW, getString(R.string.settings_key_notification_quest_choice)),
                        shouldShowNotificationQuestChoice)
                .commit();
    }

    public static boolean isNotificationNighttimeEnabled() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_settings_nighttime),
                false);
    }

    public static int getNotificationNighttimeFromHour() {
        return sharedPreferences.getInt(getString(R.string.settings_key_notification_settings_nighttime_from_hour), 0);
    }

    public static int getNotificationNighttimeFromMinute() {
        return sharedPreferences.getInt(getString(R.string.settings_key_notification_settings_nighttime_from_minute), 0);
    }

    public static int getNotificationNighttimeToHour() {
        return sharedPreferences.getInt(getString(R.string.settings_key_notification_settings_nighttime_to_hour), 0);
    }

    public static int getNotificationNighttimeToMinute() {
        return sharedPreferences.getInt(getString(R.string.settings_key_notification_settings_nighttime_to_minute), 0);
    }

    public static boolean shouldAutohelpDeath() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_death),
                getBoolean(R.bool.settings_autohelp_common_default));
    }

    public static int getAutohelpDeathEnergyThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_death_energy_threshold, R.integer.settings_autohelp_death_energy_threshold_default);
    }

    public static boolean shouldAutohelpDeathUseBonusEnergy() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_death_bonus_energy),
                getBoolean(R.bool.settings_autohelp_common_bonus_energy_default));
    }

    public static int getAutohelpDeathBonusEnergyThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_death_bonus_energy_threshold, R.integer.settings_autohelp_common_bonus_energy_threshold_default);
    }

    public static boolean shouldAutohelpIdle() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_idle),
                getBoolean(R.bool.settings_autohelp_common_default));
    }

    public static int getAutohelpIdleEnergyThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_idle_energy_threshold, R.integer.settings_autohelp_idle_energy_threshold_default);
    }

    public static boolean shouldAutohelpIdleUseBonusEnergy() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_idle_bonus_energy),
                getBoolean(R.bool.settings_autohelp_common_bonus_energy_default));
    }

    public static int getAutohelpIdleBonusEnergyThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_idle_bonus_energy_threshold, R.integer.settings_autohelp_common_bonus_energy_threshold_default);
    }

    public static boolean shouldAutohelpHealth() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_health),
                getBoolean(R.bool.settings_autohelp_common_default));
    }

    public static int getAutohelpHealthAmountThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_health_amount_threshold, R.integer.settings_autohelp_health_amount_threshold_default);
    }

    public static int getAutohelpHealthEnergyThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_health_energy_threshold, R.integer.settings_autohelp_health_energy_threshold_default);
    }

    public static boolean shouldAutohelpHealthUseBonusEnergy() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_health_bonus_energy),
                getBoolean(R.bool.settings_autohelp_common_bonus_energy_default));
    }

    public static int getAutohelpHealthBonusEnergyThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_health_bonus_energy_threshold, R.integer.settings_autohelp_common_bonus_energy_threshold_default);
    }

    public static boolean shouldAutohelpHealthBossOnly() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_health_boss),
                getBoolean(R.bool.settings_autohelp_health_boss_default));
    }

    public static boolean shouldAutohelpEnergy() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_energy),
                getBoolean(R.bool.settings_autohelp_common_default));
    }

    public static int getAutohelpEnergyEnergyThreshold() {
        return getIntegerIfExist(R.string.settings_key_autohelp_energy_energy_threshold, R.integer.settings_autohelp_energy_energy_threshold_default);
    }

    public static boolean shouldAutohelpEnergyBattle() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_energy_battle),
                getBoolean(R.bool.settings_autohelp_common_default));
    }

    public static boolean shouldAutohelpEnergyReligious() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_energy_religious),
                getBoolean(R.bool.settings_autohelp_common_default));
    }

    public static boolean shouldAutohelpEnergyTravel() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autohelp_energy_travel),
                getBoolean(R.bool.settings_autohelp_common_default));
    }

    public static boolean shouldAutoactionCardTake() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_autoaction_cardtake),
                getBoolean(R.bool.settings_autoaction_common_default));
    }

    public static boolean shouldServiceStartBoot() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_service_start_boot),
                getBoolean(R.bool.settings_service_start_boot_default));
    }

    public static int getServiceInterval() {
        final int serviceIntervalDefault = getInteger(R.integer.settings_service_interval_default);
        final String serviceInterval = sharedPreferences.getString(
                getString(R.string.settings_key_service_interval),
                String.valueOf(serviceIntervalDefault));
        try {
            return Integer.parseInt(serviceInterval);
        } catch(NumberFormatException ignored) {
            return serviceIntervalDefault;
        }
    }

    public static boolean isJournalReadAloudEnabled() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_read_aloud_journal),
                getBoolean(R.bool.settings_read_aloud_default));
    }

    public static void setJournalReadAloudEnabled(final boolean isEnabled) {
        sharedPreferences.edit()
                .putBoolean(getString(R.string.settings_key_read_aloud_journal), isEnabled)
                .commit();
    }

    public static boolean isDiaryReadAloudEnabled() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_read_aloud_diary),
                getBoolean(R.bool.settings_read_aloud_default));
    }

    public static void setDiaryReadAloudEnabled(final boolean isEnabled) {
        sharedPreferences.edit()
                .putBoolean(getString(R.string.settings_key_read_aloud_diary), isEnabled)
                .commit();
    }

    public static boolean isReadAloudConfirmed() {
        return sharedPreferences.getBoolean(KEY_READ_ALOUD_CONFIRMED, false);
    }

    public static void setReadAloudConfirmed(final boolean isReadAloudConfirmed) {
        sharedPreferences.edit()
                .putBoolean(KEY_READ_ALOUD_CONFIRMED, isReadAloudConfirmed)
                .commit();
    }

    public static int getLastDiaryEntryRead() {
        return sharedPreferences.getInt(KEY_LAST_DIARY_ENTRY_READ, 0);
    }

    public static void setLastDiaryEntryRead(final int timestamp) {
        sharedPreferences.edit()
                .putInt(KEY_LAST_DIARY_ENTRY_READ, timestamp)
                .commit();
    }

    public static MapStyle getMapStyle() {
        return MapStyle.values()[sharedPreferences.getInt(KEY_MAP_STYLE, 0)];
    }

    public static void setMapStyle(final MapStyle mapStyle) {
        sharedPreferences.edit()
                .putInt(KEY_MAP_STYLE, mapStyle.ordinal())
                .commit();
    }

    public static int getMapCenterPlaceId() {
        return sharedPreferences.getInt(KEY_MAP_CENTER_PLACE_ID, -1);
    }

    public static void setMapCenterPlaceId(final int placeId) {
        sharedPreferences.edit()
                .putInt(KEY_MAP_CENTER_PLACE_ID, placeId)
                .commit();
    }

    public static GameFragment.GamePage getDesiredGamePage() {
        final int index = sharedPreferences.getInt(KEY_DESIRED_GAME_PAGE, -1);
        return index == -1 ? null : GameFragment.GamePage.values()[index];
    }

    public static void setDesiredGamePage(final GameFragment.GamePage gamePage) {
        sharedPreferences.edit()
                .putInt(KEY_DESIRED_GAME_PAGE, gamePage == null ? -1 : gamePage.ordinal())
                .commit();
    }

    public static String getSession() {
        return sharedPreferences.getString(KEY_SESSION, "");
    }

    public static void setSession(final String session) {
        sharedPreferences.edit()
                .putString(KEY_SESSION, session)
                .commit();
    }

    public static void setAccountId(final int accountId) {
        sharedPreferences.edit()
                .putInt(KEY_ACCOUNT_ID, accountId)
                .commit();
    }

    public static int getAccountId() {
        return sharedPreferences.getInt(KEY_ACCOUNT_ID, 0);
    }

    public static void setAccountName(final String accountName) {
        sharedPreferences.edit()
                .putString(KEY_ACCOUNT_NAME, accountName)
                .commit();
    }

    public static String getAccountName() {
        return sharedPreferences.getString(KEY_ACCOUNT_NAME, null);
    }

    public static void setMapVersion(final String mapVersion) {
        sharedPreferences.edit()
                .putString(KEY_MAP_VERSION, mapVersion)
                .commit();
    }

    public static String getMapVersion() {
        return sharedPreferences.getString(KEY_MAP_VERSION, null);
    }

    public static void setAbilitiesCost(final Map<Action, Integer> abilitiesCost) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        for(final Map.Entry<Action, Integer> entry : abilitiesCost.entrySet()) {
            editor.putInt(String.format(KEY_ABILITY_COST, entry.getKey().name()), entry.getValue());
        }

        editor.commit();
    }

    public static int getAbilityCost(final Action action) {
        return sharedPreferences.getInt(String.format(KEY_ABILITY_COST, action.name()), -1);
    }

    public static void setTurnDelta(final int turnDelta) {
        sharedPreferences.edit()
                .putInt(KEY_TURN_DELTA, turnDelta)
                .commit();
    }

    public static int getTurnDelta() {
        return sharedPreferences.getInt(KEY_TURN_DELTA, 0);
    }

    public static void setStaticContentUrl(final String staticContentUrl) {
        sharedPreferences.edit()
                .putString(KEY_STATIC_CONTENT_URL, staticContentUrl)
                .commit();
    }

    public static String getStaticContentUrl() {
        return sharedPreferences.getString(KEY_STATIC_CONTENT_URL, null);
    }

    public static void setWidgetEnabled(final boolean isEnabled) {
        sharedPreferences.edit()
                .putBoolean(KEY_WIDGET_ENABLED, isEnabled)
                .commit();
    }

    public static boolean isWidgetEnabled() {
        return sharedPreferences.getBoolean(KEY_WIDGET_ENABLED, false);
    }

    public static void setShouldExit(final boolean shouldExit) {
        sharedPreferences.edit()
                .putBoolean(KEY_SHOULD_EXIT, shouldExit)
                .commit();
    }

    public static boolean shouldExit() {
        return sharedPreferences.getBoolean(KEY_SHOULD_EXIT, false);
    }

    public static boolean isWatcherEnabled() {
        return
                isWidgetEnabled()
                || shouldNotifyDeath()
                || shouldNotifyIdleness()
                || shouldNotifyHealth()
                || shouldNotifyEnergy()
                || shouldNotifyNewMessages()
                || shouldAutoactionCardTake()
                || shouldAutohelpDeath()
                || shouldAutohelpIdle()
                || shouldAutohelpHealth()
                || shouldAutohelpEnergy()
                ;
    }

}

package com.lonebytesoft.thetaleclient.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.dictionary.MapStyle;

/**
 * @author Hamster
 * @since 08.10.2014
 */
@SuppressLint("CommitPrefEdits")
public class PreferencesManager {

    private static final String KEY_MAP_STYLE = "KEY_MAP_STYLE";
    private static final String KEY_SESSION = "KEY_SESSION";
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
                getString(R.string.settings_key_misc_service_start_boot),
                getBoolean(R.bool.settings_misc_service_start_boot_default));
    }

    public static boolean isJournalReadAloudEnabled() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_misc_journal_read_aloud),
                getBoolean(R.bool.settings_misc_journal_read_aloud_default));
    }

    public static void setJournalReadAloudEnabled(final boolean isEnabled) {
        sharedPreferences.edit()
                .putBoolean(getString(R.string.settings_key_misc_journal_read_aloud), isEnabled)
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

    public static String getSession() {
        return sharedPreferences.getString(KEY_SESSION, "");
    }

    public static void setSession(final String session) {
        sharedPreferences.edit()
                .putString(KEY_SESSION, session)
                .commit();
    }

    public static boolean isWatcherEnabled() {
        return shouldNotifyDeath()
                || shouldNotifyIdleness()
                || shouldNotifyHealth()
                || shouldNotifyEnergy()
                || shouldNotifyNewMessages()
                || shouldAutoactionCardTake()
                || shouldAutohelpDeath()
                || shouldAutohelpIdle()
                || shouldAutohelpHealth()
                || shouldAutohelpEnergy();
    }

}

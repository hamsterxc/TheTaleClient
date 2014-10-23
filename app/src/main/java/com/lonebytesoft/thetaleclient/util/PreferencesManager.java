package com.lonebytesoft.thetaleclient.util;

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
public class PreferencesManager {

    private static final String KEY_LOGIN = "KEY_LOGIN";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";
    private static final String KEY_MAP_STYLE = "KEY_MAP_STYLE";

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

    public static String getLogin() {
        return sharedPreferences.getString(KEY_LOGIN, null);
    }

    public static String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    public static boolean setCredentials(final String login, final String password) {
        return sharedPreferences.edit()
                .putString(KEY_LOGIN, login)
                .putString(KEY_PASSWORD, password)
                .commit();
    }

    public static boolean shouldNotifyDeath() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_death),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static boolean shouldNotifyIdleness() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_idleness),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static boolean shouldNotifyHealth() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_health),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static int getNotificationThresholdHealth() {
        return getIntegerIfExist(R.string.settings_key_notification_health_threshold, R.integer.settings_notification_health_threshold_default);
    }

    public static boolean shouldNotifyEnergy() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_energy),
                getBoolean(R.bool.settings_notification_common_default));
    }

    public static int getNotificationThresholdEnergy() {
        return getIntegerIfExist(R.string.settings_key_notification_energy_threshold, R.integer.settings_notification_energy_threshold_default);
    }

    public static boolean shouldNotifyNewMessages() {
        return sharedPreferences.getBoolean(
                getString(R.string.settings_key_notification_new_messages),
                getBoolean(R.bool.settings_notification_common_default));
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

    public static MapStyle getMapStyle() {
        return MapStyle.values()[sharedPreferences.getInt(KEY_MAP_STYLE, 0)];
    }

    public static void setMapStyle(final MapStyle mapStyle) {
        sharedPreferences.edit()
                .putInt(KEY_MAP_STYLE, mapStyle.ordinal())
                .commit();
    }

}

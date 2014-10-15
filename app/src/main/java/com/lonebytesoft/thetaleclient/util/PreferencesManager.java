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
        return sharedPreferences.getBoolean(TheTaleClientApplication.getContext().getString(R.string.settings_key_notification_death), true);
    }

    public static boolean shouldNotifyIdleness() {
        return sharedPreferences.getBoolean(TheTaleClientApplication.getContext().getString(R.string.settings_key_notification_idleness), true);
    }

    public static boolean shouldNotifyHealth() {
        return sharedPreferences.getBoolean(TheTaleClientApplication.getContext().getString(R.string.settings_key_notification_health), true);
    }

    public static int getNotificationThresholdHealth() {
        final String value = sharedPreferences.getString(
                TheTaleClientApplication.getContext().getString(R.string.settings_key_notification_health_threshold), null);
        return TextUtils.isEmpty(value) ?
                TheTaleClientApplication.getContext().getResources().getInteger(R.integer.settings_notification_threshold_health_default) :
                Integer.decode(value);
    }

    public static boolean shouldNotifyEnergy() {
        return sharedPreferences.getBoolean(TheTaleClientApplication.getContext().getString(R.string.settings_key_notification_energy), true);
    }

    public static int getNotificationThresholdEnergy() {
        final String value = sharedPreferences.getString(
                TheTaleClientApplication.getContext().getString(R.string.settings_key_notification_energy_threshold), null);
        return TextUtils.isEmpty(value) ?
                TheTaleClientApplication.getContext().getResources().getInteger(R.integer.settings_notification_threshold_energy_default) :
                Integer.decode(value);
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

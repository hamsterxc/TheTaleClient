package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 11.10.2014
 */
public class SettingsFragment extends PreferenceFragment {

    private CheckBoxPreference health;
    private Preference healthThreshold;
    private CheckBoxPreference energy;
    private Preference energyThreshold;

    public SettingsFragment() {
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        addPreferencesFromResource(R.xml.settings);

        health = (CheckBoxPreference) findPreference(getString(R.string.settings_key_notification_health));
        healthThreshold = findPreference(getString(R.string.settings_key_notification_health_threshold));
        setupDependentFields(health, healthThreshold);
        setupValueField(healthThreshold, R.string.settings_summary_notification_health,
                String.valueOf(PreferencesManager.getNotificationThresholdHealth()));

        energy = (CheckBoxPreference) findPreference(getString(R.string.settings_key_notification_energy));
        energyThreshold = findPreference(getString(R.string.settings_key_notification_energy_threshold));
        setupDependentFields(energy, energyThreshold);
        setupValueField(energyThreshold, R.string.settings_summary_notification_energy,
                String.valueOf(PreferencesManager.getNotificationThresholdEnergy()));
    }

    private void setupDependentFields(final CheckBoxPreference checkbox, final Preference value) {
        value.setEnabled(checkbox.isChecked());
        checkbox.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                value.setEnabled((Boolean) newValue);
                return true;
            }
        });
    }

    private void setupValueField(final Preference valueField, final int summaryResId, final String currentValue) {
        valueField.setSummary(getString(summaryResId, currentValue));
        valueField.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                valueField.setSummary(getString(summaryResId, newValue));
                return true;
            }
        });
    }

}

package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.widget.TimeIntervalPreference;

/**
 * @author Hamster
 * @since 11.10.2014
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        addPreferencesFromResource(R.xml.settings);

        // notification: health
        final CheckBoxPreference health = (CheckBoxPreference) findPreference(getString(R.string.settings_key_notification_health));
        final Preference healthThreshold = findPreference(getString(R.string.settings_key_notification_health_threshold));
        setupDependentFields(health, healthThreshold);
        setupValueField(healthThreshold, R.string.settings_summary_notification_health,
                String.valueOf(PreferencesManager.getNotificationThresholdHealth()));

        // notification: energy
        final CheckBoxPreference energy = (CheckBoxPreference) findPreference(getString(R.string.settings_key_notification_energy));
        final Preference energyThreshold = findPreference(getString(R.string.settings_key_notification_energy_threshold));
        setupDependentFields(energy, energyThreshold);
        setupValueField(energyThreshold, R.string.settings_summary_notification_energy,
                String.valueOf(PreferencesManager.getNotificationThresholdEnergy()));

        // notification settings
        final TimeIntervalPreference nighttimeInterval = (TimeIntervalPreference) findPreference(getString(R.string.settings_key_notification_settings_nighttime));
        nighttimeInterval.setTitle(R.string.settings_title_notification_settings_nighttime);
        nighttimeInterval.setActivity(getActivity());

        // autohelp: death
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_death)),
                findPreference(getString(R.string.settings_key_autohelp_death_screen)));
        setupValueField(
                findPreference(getString(R.string.settings_key_autohelp_death_energy_threshold)),
                R.string.settings_summary_autohelp_common_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpDeathEnergyThreshold()));
        final Preference deathBonusEnergyThreshold = findPreference(getString(R.string.settings_key_autohelp_death_bonus_energy_threshold));
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_death_bonus_energy)),
                deathBonusEnergyThreshold);
        setupValueField(deathBonusEnergyThreshold, R.string.settings_summary_autohelp_common_bonus_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpDeathBonusEnergyThreshold()));

        // autohelp: idleness
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_idle)),
                findPreference(getString(R.string.settings_key_autohelp_idle_screen)));
        setupValueField(
                findPreference(getString(R.string.settings_key_autohelp_idle_energy_threshold)),
                R.string.settings_summary_autohelp_common_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpIdleEnergyThreshold()));
        final Preference idleBonusEnergyThreshold = findPreference(getString(R.string.settings_key_autohelp_idle_bonus_energy_threshold));
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_idle_bonus_energy)),
                idleBonusEnergyThreshold);
        setupValueField(idleBonusEnergyThreshold, R.string.settings_summary_autohelp_common_bonus_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpIdleBonusEnergyThreshold()));

        // autohelp: health
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_health)),
                findPreference(getString(R.string.settings_key_autohelp_health_screen)));
        setupValueField(
                findPreference(getString(R.string.settings_key_autohelp_health_amount_threshold)),
                R.string.settings_summary_autohelp_health_amount_threshold,
                String.valueOf(PreferencesManager.getAutohelpHealthAmountThreshold()));
        setupValueField(
                findPreference(getString(R.string.settings_key_autohelp_health_energy_threshold)),
                R.string.settings_summary_autohelp_common_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpHealthEnergyThreshold()));
        final Preference healthBonusEnergyThreshold = findPreference(getString(R.string.settings_key_autohelp_health_bonus_energy_threshold));
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_health_bonus_energy)),
                healthBonusEnergyThreshold);
        setupValueField(healthBonusEnergyThreshold, R.string.settings_summary_autohelp_common_bonus_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpHealthBonusEnergyThreshold()));

        // autohelp: energy
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_energy)),
                findPreference(getString(R.string.settings_key_autohelp_energy_screen)));
        setupValueField(
                findPreference(getString(R.string.settings_key_autohelp_energy_energy_threshold)),
                R.string.settings_summary_autohelp_common_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpEnergyEnergyThreshold()));
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

package com.lonebytesoft.thetaleclient.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.service.WatcherService;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.TextToSpeechUtils;
import com.lonebytesoft.thetaleclient.widget.TimeIntervalPreference;

import java.util.Arrays;

/**
 * @author Hamster
 * @since 11.10.2014
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {
    }

    @Override
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

        // autohelp: companion care
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_companion_care)),
                findPreference(getString(R.string.settings_key_autohelp_companion_care_screen)));
        setupValueField(
                findPreference(getString(R.string.settings_key_autohelp_companion_care_health_amount_threshold)),
                R.string.settings_summary_autohelp_companion_care_health_amount_threshold,
                String.valueOf(PreferencesManager.getAutohelpCompanionCareHealthAmountThreshold()));
        setupValueField(
                findPreference(getString(R.string.settings_key_autohelp_companion_care_energy_threshold)),
                R.string.settings_summary_autohelp_common_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpCompanionCareEnergyThreshold()));
        final Preference companionCareBonusEnergyThreshold = findPreference(getString(R.string.settings_key_autohelp_companion_care_bonus_energy_threshold));
        setupDependentFields(
                (CheckBoxPreference) findPreference(getString(R.string.settings_key_autohelp_companion_care_bonus_energy)),
                companionCareBonusEnergyThreshold);
        setupValueField(companionCareBonusEnergyThreshold, R.string.settings_summary_autohelp_common_bonus_energy_threshold,
                String.valueOf(PreferencesManager.getAutohelpCompanionCareBonusEnergyThreshold()));

        // service
        final ListPreference serviceInterval = (ListPreference) findPreference(getString(R.string.settings_key_service_interval));
        serviceInterval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                serviceInterval.setSummary(getEntryTitle(
                        R.array.settings_entries_service_interval,
                        R.array.settings_service_interval_values,
                        (String) newValue));
                getActivity().sendBroadcast(new Intent(WatcherService.BROADCAST_SERVICE_RESTART_REFRESH_ACTION));
                return true;
            }
        });
        serviceInterval.setSummary(getEntryTitle(
                R.array.settings_entries_service_interval,
                R.array.settings_service_interval_values,
                serviceInterval.getValue()));

        // reading aloud
        final CheckBoxPreference journalReadAloud = (CheckBoxPreference) findPreference(getString(R.string.settings_key_read_aloud_journal));
        journalReadAloud.setOnPreferenceChangeListener(getReadAloudPreferenceChangeListener(journalReadAloud));

        final CheckBoxPreference diaryReadAloud = (CheckBoxPreference) findPreference(getString(R.string.settings_key_read_aloud_diary));
        diaryReadAloud.setOnPreferenceChangeListener(getReadAloudPreferenceChangeListener(diaryReadAloud));
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

    // https://code.google.com/p/android/issues/detail?id=4611 (#35)
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference instanceof PreferenceScreen) {
            final PreferenceScreen newPreferenceScreen = (PreferenceScreen) preference;
            if(newPreferenceScreen.getDialog() != null) {
                newPreferenceScreen.getDialog().getWindow().getDecorView().setBackgroundResource(R.color.common_background_window);
            }
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private String getEntryTitle(final int entriesResId, final int valuesResId, final String value) {
        final int index = Arrays.asList(getResources().getStringArray(valuesResId)).indexOf(value);
        if(index == -1) {
            return null;
        } else {
            return getResources().getStringArray(entriesResId)[index];
        }
    }

    private Preference.OnPreferenceChangeListener getReadAloudPreferenceChangeListener(
            final CheckBoxPreference checkBoxPreference) {
        return new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(PreferencesManager.isReadAloudConfirmed()) {
                    return true;
                } else {
                    DialogUtils.showConfirmationDialog(getFragmentManager(),
                            getString(R.string.game_read_aloud_caption),
                            getString(R.string.game_read_aloud_confirmation),
                            new Runnable() {
                                @Override
                                public void run() {
                                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                                            getString(R.string.game_read_aloud_caption),
                                            getString(R.string.game_read_aloud_wait_message),
                                            true, false);
                                    TextToSpeechUtils.init(
                                            TheTaleClientApplication.getContext(),
                                            new TextToSpeechUtils.InitCallback() {
                                                @Override
                                                public void onSuccess() {
                                                    PreferencesManager.setReadAloudConfirmed(true);
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            checkBoxPreference.setChecked(true);
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onInitError() {
                                                    progressDialog.dismiss();
                                                    DialogUtils.showMessageDialog(getFragmentManager(),
                                                            getString(R.string.game_read_aloud_caption),
                                                            getString(R.string.game_read_aloud_error_init));
                                                }

                                                @Override
                                                public void onLanguageError() {
                                                    progressDialog.dismiss();
                                                    DialogUtils.showMessageDialog(getFragmentManager(),
                                                            getString(R.string.game_read_aloud_caption),
                                                            getString(R.string.game_read_aloud_error_not_supported));
                                                }
                                            });
                                }
                            });
                    return false;
                }
            }
        };
    }

}

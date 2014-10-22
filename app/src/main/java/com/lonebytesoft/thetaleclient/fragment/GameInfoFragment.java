package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.model.JournalEntry;
import com.lonebytesoft.thetaleclient.api.model.MightInfo;
import com.lonebytesoft.thetaleclient.api.request.AbilityUseRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.InfoRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.widget.RequestActionView;

/**
 * @author Hamster
 * @since 05.10.2014
 */
public class GameInfoFragment extends WrapperFragment {

    private static final long REFRESH_TIMEOUT_MILLIS = 10000; // 10 s

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            refresh(false);
            handler.postDelayed(this, REFRESH_TIMEOUT_MILLIS);
        }
    };

    private LayoutInflater layoutInflater;
    private View rootView;

    private TextView textRaceGender;
    private TextView textLevel;
    private TextView textName;
    private View textLevelUp;

    private ProgressBar progressHealth;
    private TextView textHealth;
    private ProgressBar progressExperience;
    private TextView textExperience;
    private ProgressBar progressEnergy;
    private TextView textEnergy;

    private TextView textPowerPhysical;
    private TextView textPowerMagical;
    private TextView textMoney;
    private TextView textMight;

    private ProgressBar progressAction;
    private TextView textAction;
    private RequestActionView actionHelp;

    private ViewGroup journalContainer;

    public GameInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_game_info, container, false);

        textRaceGender = (TextView) rootView.findViewById(R.id.game_info_race_gender);
        textLevel = (TextView) rootView.findViewById(R.id.game_info_level);
        textName = (TextView) rootView.findViewById(R.id.game_info_name);
        textLevelUp = rootView.findViewById(R.id.game_info_lvlup);

        progressHealth = (ProgressBar) rootView.findViewById(R.id.game_info_health_progress);
        textHealth = (TextView) rootView.findViewById(R.id.game_info_health_text);
        progressExperience = (ProgressBar) rootView.findViewById(R.id.game_info_experience_progress);
        textExperience = (TextView) rootView.findViewById(R.id.game_info_experience_text);
        progressEnergy = (ProgressBar) rootView.findViewById(R.id.game_info_energy_progress);
        textEnergy = (TextView) rootView.findViewById(R.id.game_info_energy_text);

        textPowerPhysical = (TextView) rootView.findViewById(R.id.game_info_power_physical);
        textPowerMagical = (TextView) rootView.findViewById(R.id.game_info_power_magical);
        textMoney = (TextView) rootView.findViewById(R.id.game_info_money);
        textMight = (TextView) rootView.findViewById(R.id.game_info_might);

        progressAction = (ProgressBar) rootView.findViewById(R.id.game_info_action_progress);
        textAction = (TextView) rootView.findViewById(R.id.game_info_action_text);
        actionHelp = (RequestActionView) rootView.findViewById(R.id.game_help);

        journalContainer = (ViewGroup) rootView.findViewById(R.id.journal_container);

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void onResume() {
        super.onResume();

        actionHelp.setActionClickListener(new Runnable() {
            @Override
            public void run() {
                new AbilityUseRequest(Action.HELP).execute(0, new ApiResponseCallback<CommonResponse>() {
                    @Override
                    public void processResponse(CommonResponse response) {
                        actionHelp.setMode(RequestActionView.Mode.ACTION);
                        refresh(false);
                    }

                    @Override
                    public void processError(CommonResponse response) {
                        actionHelp.setErrorText(response.errorMessage);
                    }
                });
            }
        });

        handler.postDelayed(refreshRunnable, REFRESH_TIMEOUT_MILLIS);
    }

    @Override
    public void onPause() {
        super.onPause();

        handler.removeCallbacks(refreshRunnable);
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(GameInfoResponse response) {
                if(!isAdded()) {
                    return;
                }

                if(response.account == null) {
                    return;
                }

                textRaceGender.setText(String.format("%s-%s",
                        response.account.hero.basicInfo.race.getName(),
                        response.account.hero.basicInfo.gender.getName()));
                textLevel.setText(String.valueOf(response.account.hero.basicInfo.level));
                textName.setText(response.account.hero.basicInfo.name);
                textLevelUp.setVisibility(response.account.hero.basicInfo.destinyPoints > 0 ? View.VISIBLE : View.GONE);

                progressHealth.setMax(response.account.hero.basicInfo.healthMax);
                progressHealth.setProgress(response.account.hero.basicInfo.healthCurrent);
                textHealth.setText(String.format("%d/%d",
                        response.account.hero.basicInfo.healthCurrent,
                        response.account.hero.basicInfo.healthMax));

                progressExperience.setMax(response.account.hero.basicInfo.experienceForNextLevel);
                progressExperience.setProgress(response.account.hero.basicInfo.experienceCurrent);
                textExperience.setText(String.format("%d/%d",
                        response.account.hero.basicInfo.experienceCurrent,
                        response.account.hero.basicInfo.experienceForNextLevel));

                progressEnergy.setMax(response.account.hero.energy.max);
                progressEnergy.setProgress(response.account.hero.energy.current);
                textEnergy.setText(String.format("%d/%d + %d",
                        response.account.hero.energy.current,
                        response.account.hero.energy.max,
                        response.account.hero.energy.bonus));

                textPowerPhysical.setText(String.valueOf(response.account.hero.basicInfo.powerPhysical));
                textPowerMagical.setText(String.valueOf(response.account.hero.basicInfo.powerMagical));
                textMoney.setText(String.valueOf(response.account.hero.basicInfo.money));

                final MightInfo mightInfo = response.account.hero.might;
                textMight.setText(String.valueOf(mightInfo.value));
                textMight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showMightDialog(getFragmentManager(), mightInfo);
                    }
                });

                progressAction.setMax(1000);
                progressAction.setProgress((int) (1000 * response.account.hero.action.completion));
                String currentAction = response.account.hero.action.description;
                if(response.account.hero.action.isBossFight) {
                    currentAction += getString(R.string.game_boss_fight);
                }
                textAction.setText(currentAction);

                journalContainer.removeAllViews();
                for(int i = response.account.hero.journal.size() - 1; i >= 0; i--) {
                    final JournalEntry journalEntry = response.account.hero.journal.get(i);
                    final View journalEntryView = layoutInflater.inflate(R.layout.item_journal, journalContainer, false);
                    ((TextView) journalEntryView.findViewById(R.id.journal_time)).setText(journalEntry.time);
                    ((TextView) journalEntryView.findViewById(R.id.journal_text)).setText(journalEntry.text);
                    journalContainer.addView(journalEntryView);
                }

                final int currentEnergyTotal = response.account.hero.energy.current + response.account.hero.energy.bonus;
                actionHelp.setVisibility(View.GONE);
                new InfoRequest().execute(new ApiResponseCallback<InfoResponse>() {
                    @Override
                    public void processResponse(InfoResponse response) {
                        if(currentEnergyTotal >= response.abilitiesCost.get(Action.HELP)) {
                            actionHelp.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void processError(InfoResponse response) {
                        actionHelp.setErrorText(response.errorMessage);
                        actionHelp.setMode(RequestActionView.Mode.ERROR);
                        actionHelp.setVisibility(View.VISIBLE);
                    }
                });

                setMode(DataViewMode.DATA);
            }

            @Override
            public void processError(GameInfoResponse response) {
                setError(response.errorMessage);
            }
        }, false);
    }

}

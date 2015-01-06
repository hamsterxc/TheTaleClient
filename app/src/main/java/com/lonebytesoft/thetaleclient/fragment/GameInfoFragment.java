package com.lonebytesoft.thetaleclient.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.InfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.dictionary.HeroAction;
import com.lonebytesoft.thetaleclient.api.model.HeroActionInfo;
import com.lonebytesoft.thetaleclient.api.model.JournalEntry;
import com.lonebytesoft.thetaleclient.api.model.MightInfo;
import com.lonebytesoft.thetaleclient.api.request.AbilityUseRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.TextToSpeechUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenPart;
import com.lonebytesoft.thetaleclient.widget.RequestActionView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextView progressActionInfo;
    private TextView textAction;
    private RequestActionView actionHelp;

    private ViewGroup journalContainer;

    private int lastJournalTimestamp;
    private double lastFightProgress;
    private int lastKnownHealth;

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
        progressActionInfo = (TextView) rootView.findViewById(R.id.game_info_action_progress_info);
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
                new AbilityUseRequest(Action.HELP).execute(0, RequestUtils.wrapCallback(new ApiResponseCallback<CommonResponse>() {
                    @Override
                    public void processResponse(CommonResponse response) {
                        actionHelp.setMode(RequestActionView.Mode.ACTION);
                        refresh(false);
                    }

                    @Override
                    public void processError(CommonResponse response) {
                        actionHelp.setErrorText(response.errorMessage);
                    }
                }, GameInfoFragment.this));
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

        if(isGlobal) {
            lastJournalTimestamp = 0;
            lastFightProgress = 0;
            lastKnownHealth = 0;
        }

        new GameInfoRequest(true).execute(RequestUtils.wrapCallback(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(final GameInfoResponse gameInfoResponse) {
                if(lastKnownHealth == 0) {
                    lastKnownHealth = (int) Math.round((450.0 + 50.0 * gameInfoResponse.account.hero.basicInfo.level) / 4.0);
                }

                textRaceGender.setText(String.format("%s-%s",
                        gameInfoResponse.account.hero.basicInfo.race.getName(),
                        gameInfoResponse.account.hero.basicInfo.gender.getName()));
                textLevel.setText(String.valueOf(gameInfoResponse.account.hero.basicInfo.level));
                textName.setText(gameInfoResponse.account.hero.basicInfo.name);
                textLevelUp.setVisibility(gameInfoResponse.account.hero.basicInfo.destinyPoints > 0 ? View.VISIBLE : View.GONE);

                progressHealth.setMax(gameInfoResponse.account.hero.basicInfo.healthMax);
                progressHealth.setProgress(gameInfoResponse.account.hero.basicInfo.healthCurrent);
                textHealth.setText(String.format("%d/%d",
                        gameInfoResponse.account.hero.basicInfo.healthCurrent,
                        gameInfoResponse.account.hero.basicInfo.healthMax));

                progressExperience.setMax(gameInfoResponse.account.hero.basicInfo.experienceForNextLevel);
                progressExperience.setProgress(gameInfoResponse.account.hero.basicInfo.experienceCurrent);
                textExperience.setText(String.format("%d/%d",
                        gameInfoResponse.account.hero.basicInfo.experienceCurrent,
                        gameInfoResponse.account.hero.basicInfo.experienceForNextLevel));

                progressEnergy.setMax(gameInfoResponse.account.hero.energy.max);
                // https://code.google.com/p/android/issues/detail?id=12945
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    progressEnergy.setProgress(0);
                }
                progressEnergy.setProgress(gameInfoResponse.account.hero.energy.current);
                textEnergy.setText(String.format("%d/%d + %d",
                        gameInfoResponse.account.hero.energy.current,
                        gameInfoResponse.account.hero.energy.max,
                        gameInfoResponse.account.hero.energy.bonus));

                textPowerPhysical.setText(String.valueOf(gameInfoResponse.account.hero.basicInfo.powerPhysical));
                textPowerMagical.setText(String.valueOf(gameInfoResponse.account.hero.basicInfo.powerMagical));
                textMoney.setText(String.valueOf(gameInfoResponse.account.hero.basicInfo.money));

                final MightInfo mightInfo = gameInfoResponse.account.hero.might;
                textMight.setText(String.valueOf(mightInfo.value));
                textMight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.showMightDialog(getFragmentManager(), mightInfo);
                    }
                });

                final HeroActionInfo action = gameInfoResponse.account.hero.action;
                progressAction.setMax(1000);
                progressAction.setProgress((int) (1000 * action.completion));
                String currentAction = action.description;
                if(action.isBossFight) {
                    currentAction += getString(R.string.game_boss_fight);
                }
                textAction.setText(currentAction);

                final List<JournalEntry> journal = gameInfoResponse.account.hero.journal;
                final int journalSize = journal.size();
                journalContainer.removeAllViews();
                for(int i = journalSize - 1; i >= 0; i--) {
                    final JournalEntry journalEntry = journal.get(i);
                    final View journalEntryView = layoutInflater.inflate(R.layout.item_journal, journalContainer, false);
                    ((TextView) journalEntryView.findViewById(R.id.journal_time)).setText(journalEntry.time);
                    ((TextView) journalEntryView.findViewById(R.id.journal_text)).setText(journalEntry.text);
                    journalContainer.addView(journalEntryView);
                }

                if(!isGlobal
                        && TheTaleClientApplication.getOnscreenStateWatcher().isOnscreen(OnscreenPart.GAME_INFO)
                        && PreferencesManager.isJournalReadAloudEnabled()) {
                    for(int i = 0; i < journalSize; i++) {
                        final JournalEntry journalEntry = journal.get(i);
                        if(journalEntry.timestamp > lastJournalTimestamp) {
                            TextToSpeechUtils.speak(journalEntry.text);
                        }
                    }
                }

                if(journalSize > 0) {
                    if((journalSize > 1) && (journal.get(journalSize - 2).timestamp == lastJournalTimestamp) && (action.type == HeroAction.BATTLE)) {
                        final Pattern pattern = Pattern.compile("(\\d+)");
                        final Matcher matcher = pattern.matcher(journal.get(journalSize - 1).text);
                        if(matcher.find()) {
                            final String number = matcher.group(1);
                            if(!matcher.find()) {
                                final int amount = Integer.decode(number);
                                final double difference = Math.abs(action.completion - lastFightProgress);
                                if(difference != 0) {
                                    lastKnownHealth = (int) Math.round(amount / difference);
                                }
                            }
                        }
                    }

                    lastJournalTimestamp = journal.get(journalSize - 1).timestamp;
                    if(action.type == HeroAction.BATTLE) {
                        lastFightProgress = action.completion;
                    } else {
                        lastFightProgress = 0;
                    }
                } else {
                    lastJournalTimestamp = 0;
                    lastFightProgress = 0;
                }

                switch(action.type) {
                    case BATTLE:
                        if(lastKnownHealth != 0) {
                            setProgressActionInfo(String.format("%d / %d HP",
                                    Math.round(lastKnownHealth * (1 - action.completion)), lastKnownHealth));
                        } else {
                            setProgressActionInfo(null);
                        }
                        break;

                    case IDLE:
                        setProgressActionInfo(getActionTimeString((long) Math.ceil(
                                (1 - action.completion) * gameInfoResponse.account.hero.basicInfo.level)));
                        break;

                    case RESURRECTION:
                        setProgressActionInfo(getActionTimeString((long) Math.ceil(
                                (1 - action.completion) * 3.0 * gameInfoResponse.account.hero.basicInfo.level)));
                        break;

                    default:
                        setProgressActionInfo(null);
                }

                actionHelp.setEnabled(false);
                new InfoPrerequisiteRequest(new Runnable() {
                    @Override
                    public void run() {
                        if(GameInfoUtils.isEnoughEnergy(gameInfoResponse.account.hero.energy, PreferencesManager.getAbilityCost(Action.HELP))) {
                            actionHelp.setEnabled(true);
                        }
                    }
                }, new PrerequisiteRequest.ErrorCallback<InfoResponse>() {
                    @Override
                    public void processError(InfoResponse response) {
                        actionHelp.setErrorText(response.errorMessage);
                        actionHelp.setMode(RequestActionView.Mode.ERROR);
                    }
                }, GameInfoFragment.this).execute();

                setMode(DataViewMode.DATA);
            }

            @Override
            public void processError(GameInfoResponse response) {
                setError(response.errorMessage);
            }
        }, this), false);
    }

    private void setProgressActionInfo(final CharSequence info) {
        if(TextUtils.isEmpty(info)) {
            progressActionInfo.setVisibility(View.GONE);
            UiUtils.setHeight(progressAction, (int) getResources().getDimension(R.dimen.game_info_bar_height));
        } else {
            progressActionInfo.setText(info);
            progressActionInfo.setVisibility(View.VISIBLE);
            progressActionInfo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final int height = progressActionInfo.getHeight();
                    if(height > 0) {
                        if(isAdded()) {
                            UiUtils.setHeight(progressAction,
                                    height + 2 * (int) getResources().getDimension(R.dimen.game_info_bar_padding));
                        }
                        UiUtils.removeGlobalLayoutListener(progressActionInfo, this);
                    }
                }
            });
        }
    }

    private String getActionTimeString(final long minutes) {
        if(minutes < 0) {
            return null;
        }

        final long hours = minutes / 60;
        if(hours > 0) {
            return getString(R.string.game_action_time, hours, minutes % 60);
        } else {
            return getString(R.string.game_action_time_short, minutes);
        }
    }

    @Override
    public void onOffscreen() {
        super.onOffscreen();
        TheTaleClientApplication.getOnscreenStateWatcher().onscreenStateChange(OnscreenPart.GAME_INFO, false);

        TextToSpeechUtils.pause();
    }

    @Override
    public void onOnscreen() {
        super.onOnscreen();
        TheTaleClientApplication.getOnscreenStateWatcher().onscreenStateChange(OnscreenPart.GAME_INFO, true);

        TheTaleClientApplication.getNotificationManager().clearNotifications();
    }

}

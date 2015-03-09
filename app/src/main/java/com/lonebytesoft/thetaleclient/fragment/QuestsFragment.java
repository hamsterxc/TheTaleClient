package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.QuestType;
import com.lonebytesoft.thetaleclient.api.model.QuestActorInfo;
import com.lonebytesoft.thetaleclient.api.model.QuestChoiceInfo;
import com.lonebytesoft.thetaleclient.api.model.QuestStepInfo;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.MapRequest;
import com.lonebytesoft.thetaleclient.api.request.QuestChoiceRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 07.10.2014
 */
public class QuestsFragment extends WrapperFragment {

    private LayoutInflater layoutInflater;

    private View rootView;

    private ViewGroup container;

    private final Map<TextView, Integer> actorNames = new HashMap<>();

    public QuestsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = layoutInflater.inflate(R.layout.fragment_quests, container, false);

        this.container = (ViewGroup) rootView.findViewById(R.id.quests_container);

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        final ApiResponseCallback<GameInfoResponse> callback = RequestUtils.wrapCallback(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(GameInfoResponse response) {
                container.removeAllViews();
                actorNames.clear();
                final int questLinesCount = response.account.hero.quests.size();
                for(int i = 0; i < questLinesCount; i++) {
                    final List<QuestStepInfo> questLine = response.account.hero.quests.get(i);
                    final int questStepsCount = questLine.size();
                    for(int j = 0; j < questStepsCount; j++) {
                        final QuestStepInfo questStep = questLine.get(j);
                        final View questStepView = layoutInflater.inflate(R.layout.item_quest, container, false);

                        final TextView questNameView = (TextView) questStepView.findViewById(R.id.quest_name);
                        final String rewards;
                        if((questStep.experience > 0) && (questStep.power == 0)) {
                            rewards = String.format(" (%s)", getString(R.string.quest_reward_experience, questStep.experience));
                        } else if((questStep.experience == 0) && (questStep.power > 0)) {
                            rewards = String.format(" (%s)", getString(R.string.quest_reward_power, questStep.power));
                        } else if((questStep.experience > 0) && (questStep.power > 0)) {
                            rewards = String.format(" (%s, %s)",
                                    getString(R.string.quest_reward_experience, questStep.experience),
                                    getString(R.string.quest_reward_power, questStep.power));
                        } else {
                            rewards = null;
                        }
                        if(TextUtils.isEmpty(rewards)) {
                            questNameView.setText(questStep.name);
                        } else {
                            final Spannable rewardsString = new SpannableString(rewards);
                            rewardsString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.game_additional_info)),
                                    0, rewardsString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            questNameView.setText(TextUtils.concat(questStep.name, rewardsString));
                        }

                        ((ImageView) questStepView.findViewById(R.id.quest_icon)).setImageResource(questStep.type.getDrawableResId());

                        final ViewGroup actorsContainer = (ViewGroup) questStepView.findViewById(R.id.quest_actors_container);
                        for(final QuestActorInfo actor : questStep.actors) {
                            final View actorView = layoutInflater.inflate(R.layout.item_text, actorsContainer, false);

                            final TextView actorTextView = (TextView) actorView.findViewById(R.id.item_text_content);
                            final CharSequence actorText;
                            final Spannable actorName = new SpannableString(actor.name);
                            actorName.setSpan(new StyleSpan(Typeface.BOLD), 0, actorName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            switch(actor.type) {
                                case PERSON:
                                    actorText = TextUtils.concat(actorName, ": ", actor.personInfo.name);
                                    actorNames.put(actorTextView, actor.personInfo.placeId);
                                    break;

                                case PLACE:
                                    actorText = TextUtils.concat(actorName, ": ", actor.placeInfo.name);
                                    break;

                                case SPENDING:
                                    actorText = TextUtils.concat(actorName, ": ", actor.spendingInfo.goal);
                                    break;

                                default:
                                    actorText = actorName;
                                    break;
                            }
                            actorTextView.setText(actorText);
                            actorTextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtils.showQuestActorDialog(getFragmentManager(), actor);
                                }
                            });

                            actorsContainer.addView(actorView);
                        }

                        if((questStep.type != QuestType.SPENDING) && (j == questStepsCount - 1)) {
                            UiUtils.setText(questStepView.findViewById(R.id.quest_action), questStep.heroAction);
                            UiUtils.setText(questStepView.findViewById(R.id.quest_current_choice), questStep.currentChoice);
                        }

                        if(response.account.isOwnInfo) {
                            final ViewGroup choicesContainer = (ViewGroup) questStepView.findViewById(R.id.quest_choices_container);
                            final View choiceProgress = questStepView.findViewById(R.id.quest_choice_progress);
                            final TextView choiceError = (TextView) questStepView.findViewById(R.id.quest_choice_error);
                            for(final QuestChoiceInfo choice : questStep.choices) {
                                final View choiceView = layoutInflater.inflate(R.layout.item_quest_choice, choicesContainer, false);

                                final Spannable choiceDescription = new SpannableString(choice.description);
                                choiceDescription.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_link)),
                                        0, choice.description.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                final TextView choiceTextView = (TextView) choiceView.findViewById(R.id.quest_choice);
                                choiceTextView.setText(TextUtils.concat(getString(R.string.quest_choice_part), choiceDescription));
                                choiceTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(PreferencesManager.isConfirmationQuestChoiceEnabled()) {
                                            DialogUtils.showConfirmationDialog(
                                                    getChildFragmentManager(),
                                                    getString(R.string.game_quest_choice),
                                                    Html.fromHtml(getString(R.string.game_quest_choice_confirmation, questStep.name, choice.description)),
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            selectQuestStep(questStepView, choice.id);
                                                        }
                                                    });
                                        } else {
                                            selectQuestStep(questStepView, choice.id);
                                        }
                                    }
                                });

                                choicesContainer.addView(choiceView);
                            }
                        }

                        container.addView(questStepView);
                    }

                    if(i != questLinesCount - 1) {
                        layoutInflater.inflate(R.layout.item_quest_delimiter, container, true);
                    }
                }

                // add town name to quest person actors
                if(actorNames.size() > 0) {
                    new MapRequest(response.mapVersion).execute(RequestUtils.wrapCallback(new CommonResponseCallback<MapResponse, String>() {
                        @Override
                        public void processResponse(MapResponse response) {
                            for (final Map.Entry<TextView, Integer> actorNameEntry : actorNames.entrySet()) {
                                final Spannable placeText = new SpannableString(String.format(" (%s)", response.places.get(actorNameEntry.getValue()).name));
                                placeText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.game_additional_info)),
                                        0, placeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                final TextView actorName = actorNameEntry.getKey();
                                actorName.setText(TextUtils.concat(actorName.getText(), placeText));
                            }
                        }

                        @Override
                        public void processError(String error) {
                            // do nothing
                        }
                    }, QuestsFragment.this));
                }

                setMode(DataViewMode.DATA);
            }

            @Override
            public void processError(GameInfoResponse response) {
                setError(response.errorMessage);
            }
        }, this);

        final int watchingAccountId = PreferencesManager.getWatchingAccountId();
        if(watchingAccountId == 0) {
            new GameInfoRequest(true).execute(callback, true);
        } else {
            new GameInfoRequest(true).execute(watchingAccountId, callback, true);
        }
    }

    private void selectQuestStep(final View questStepView, final String choiceId) {
        final View choicesContainer = questStepView.findViewById(R.id.quest_choices_container);
        final View choiceProgress = questStepView.findViewById(R.id.quest_choice_progress);

        choicesContainer.setVisibility(View.GONE);
        choiceProgress.setVisibility(View.VISIBLE);

        new QuestChoiceRequest().execute(choiceId, RequestUtils.wrapCallback(new ApiResponseCallback<CommonResponse>() {
            @Override
            public void processResponse(CommonResponse response) {
                refresh(false);
            }

            @Override
            public void processError(CommonResponse response) {
                choicesContainer.setVisibility(View.GONE);
                choiceProgress.setVisibility(View.GONE);
                UiUtils.setText(questStepView.findViewById(R.id.quest_choice_error), response.errorMessage);
            }
        }, QuestsFragment.this));
    }

    @Override
    public void onOffscreen() {
        super.onOffscreen();
        TheTaleClientApplication.getOnscreenStateWatcher().onscreenStateChange(OnscreenPart.QUESTS, false);
    }

    @Override
    public void onOnscreen() {
        super.onOnscreen();
        TheTaleClientApplication.getOnscreenStateWatcher().onscreenStateChange(OnscreenPart.QUESTS, true);

        TheTaleClientApplication.getNotificationManager().clearNotifications();
    }

}

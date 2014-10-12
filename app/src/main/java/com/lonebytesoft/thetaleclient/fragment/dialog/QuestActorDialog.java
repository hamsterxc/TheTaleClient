package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.model.QuestActorInfo;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.MapRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 10.10.2014
 */
public class QuestActorDialog extends BaseDialog {

    private static final String PARAM_QUEST_ACTOR_INFO = "PARAM_QUEST_ACTOR_INFO";

    public static QuestActorDialog newInstance(final QuestActorInfo questActorInfo) {
        final QuestActorDialog dialog = new QuestActorDialog();

        Bundle args = new Bundle();
        args.putParcelable(PARAM_QUEST_ACTOR_INFO, questActorInfo);

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final QuestActorInfo questActorInfo = getArguments().getParcelable(PARAM_QUEST_ACTOR_INFO);
        final View view;

        switch(questActorInfo.type) {
            case PERSON:
                view = inflater.inflate(R.layout.dialog_content_quest_actor_person, container, false);
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_race),
                        getInfoItem(R.string.quest_actor_race, questActorInfo.personInfo.race.getName()));
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_gender),
                        getInfoItem(R.string.quest_actor_gender, questActorInfo.personInfo.gender.getName()));
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_profession),
                        getInfoItem(R.string.quest_actor_profession, questActorInfo.personInfo.profession.getName()));
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_mastery),
                        getInfoItem(R.string.quest_actor_mastery, questActorInfo.personInfo.mastery));
                new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(GameInfoResponse response) {
                        new MapRequest(response.mapVersion).execute(new CommonResponseCallback<MapResponse, String>() {
                            @Override
                            public void processResponse(MapResponse response) {
                                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_place),
                                        getInfoItem(R.string.quest_actor_place, response.places.get(questActorInfo.personInfo.placeId).name));
                            }

                            @Override
                            public void processError(String error) {
                            }
                        });
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
                    }
                });
                break;

            case PLACE:
                view = inflater.inflate(R.layout.dialog_content_quest_actor_place, container, false);
                new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(GameInfoResponse response) {
                        new MapRequest(response.mapVersion).execute(new CommonResponseCallback<MapResponse, String>() {
                            @Override
                            public void processResponse(MapResponse response) {
                                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_place_size),
                                        String.valueOf(response.places.get(questActorInfo.placeInfo.id).size));
                            }

                            @Override
                            public void processError(String error) {
                            }
                        });
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
                    }
                });
                break;

            case SPENDING:
                view = inflater.inflate(R.layout.dialog_content_quest_actor_spending, container, false);
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_spending_description),
                        questActorInfo.spendingInfo.description);
                break;

            default:
                view = null;
                break;
        }

        return wrapView(inflater, view, questActorInfo.type.getName());
    }

    private Spanned getInfoItem(final int captionResId, final String info) {
        final Spannable caption = new SpannableString(getString(captionResId));
        caption.setSpan(new StyleSpan(Typeface.BOLD), 0, caption.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return (Spanned) TextUtils.concat(caption, ": ", info);
    }

}

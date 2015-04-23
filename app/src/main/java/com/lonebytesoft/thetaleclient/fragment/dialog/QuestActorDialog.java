package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonebytesoft.thetaleclient.DrawerItem;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;
import com.lonebytesoft.thetaleclient.apisdk.model.QuestActorInfo;
import com.lonebytesoft.thetaleclient.apisdk.request.MapRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.dictionary.QuestActorType;
import com.lonebytesoft.thetaleclient.sdk.model.QuestActorDetailsPerson;
import com.lonebytesoft.thetaleclient.sdk.model.QuestActorDetailsPlace;
import com.lonebytesoft.thetaleclient.sdk.model.QuestActorDetailsSpending;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 10.10.2014
 */
public class QuestActorDialog extends BaseDialog {

    private static final String PARAM_QUEST_ACTOR_INFO = "PARAM_QUEST_ACTOR_INFO";

    private static final Map<QuestActorType, Integer> questActorTypeStringIds;

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
                final QuestActorDetailsPerson detailsPerson = (QuestActorDetailsPerson) questActorInfo.details;
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_name),
                        UiUtils.getInfoItem(getString(R.string.quest_actor_name), detailsPerson.name));
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_race),
                        UiUtils.getInfoItem(getString(R.string.quest_actor_race), detailsPerson.race.name));
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_gender),
                        UiUtils.getInfoItem(getString(R.string.quest_actor_gender), detailsPerson.gender.name));
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_profession),
                        UiUtils.getInfoItem(getString(R.string.quest_actor_profession), detailsPerson.profession.name));
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_person_mastery),
                        UiUtils.getInfoItem(getString(R.string.quest_actor_mastery), detailsPerson.mastery));
                MapRequestBuilder.execute(getActivity(), new ApiCallback<MapResponse>() {
                    @Override
                    public void onSuccess(MapResponse response) {
                        setPlaceLink(
                                view.findViewById(R.id.dialog_quest_actor_person_place),
                                getString(R.string.quest_actor_place),
                                response.places.get(detailsPerson.placeId).name,
                                detailsPerson.placeId);
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        // do nothing
                    }
                });
                break;

            case PLACE:
                view = inflater.inflate(R.layout.dialog_content_quest_actor_place, container, false);
                final QuestActorDetailsPlace detailsPlace = (QuestActorDetailsPlace) questActorInfo.details;
                setPlaceLink(
                        view.findViewById(R.id.dialog_quest_actor_place_name),
                        getString(R.string.map_place_name),
                        detailsPlace.name,
                        detailsPlace.id);
                MapRequestBuilder.execute(getActivity(), new ApiCallback<MapResponse>() {
                    @Override
                    public void onSuccess(MapResponse response) {
                        UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_place_size), UiUtils.getInfoItem(
                                getString(R.string.map_place_size),
                                String.valueOf(response.places.get(detailsPlace.id).size)));
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        // do nothing
                    }
                });
                break;

            case SPENDING:
                view = inflater.inflate(R.layout.dialog_content_quest_actor_spending, container, false);
                UiUtils.setText(view.findViewById(R.id.dialog_quest_actor_spending_description),
                        ((QuestActorDetailsSpending) questActorInfo.details).description);
                break;

            default:
                view = null;
                break;
        }

        return wrapView(inflater, view, getString(questActorTypeStringIds.get(questActorInfo.type)));
    }

    private void setPlaceLink(final View view, final CharSequence caption, final CharSequence info, final int placeId) {
        final Activity activity = getActivity();
        if(!(activity instanceof MainActivity)) {
            return;
        }

        final Spannable linkText = new SpannableString(info);
        linkText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_link)),
                0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        UiUtils.setText(view, UiUtils.getInfoItem(caption, linkText));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setMapCenterPlaceId(placeId);
                dismiss();
                ((MainActivity) activity).onNavigationDrawerItemSelected(DrawerItem.MAP);
            }
        });
    }

    static {
        questActorTypeStringIds = new HashMap<>(QuestActorType.values().length);
        questActorTypeStringIds.put(QuestActorType.PLACE, R.string.quest_actor_type_place);
        questActorTypeStringIds.put(QuestActorType.PERSON, R.string.quest_actor_type_person);
        questActorTypeStringIds.put(QuestActorType.SPENDING, R.string.quest_actor_type_spending);
    }

}

package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.apisdk.model.CardInfoParcelable;
import com.lonebytesoft.thetaleclient.apisdk.request.PlaceRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.request.PlacesRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.request.UseCardRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.util.DictionaryData;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardTargetType;
import com.lonebytesoft.thetaleclient.sdk.model.CouncilMemberInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.sdk.response.CommonResponse;
import com.lonebytesoft.thetaleclient.sdk.response.PlaceResponse;
import com.lonebytesoft.thetaleclient.sdk.response.PlacesResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 03.05.2015
 */
public class CardUseDialog extends BaseDialog {

    private static final String PARAM_TITLE = "PARAM_TITLE";
    private static final String PARAM_CARD = "PARAM_CARD";

    private Runnable onSuccess;
    
    private List<PlaceInfo> places;
    private Map<Integer, List<CouncilMemberInfo>> persons;
    private CardInfoParcelable card;

    private View viewAction;
    private View blockPlace;
    private View blockPerson;
    private TextView textPlace;
    private TextView textPerson;

    public static CardUseDialog newInstance(final String title, final CardInfoParcelable card) {
        final CardUseDialog dialog = new CardUseDialog();

        final Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putParcelable(PARAM_CARD, card);

        dialog.setArguments(args);
        return dialog;
    }

    public void setOnSuccessListener(final Runnable listener) {
        this.onSuccess = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_content_card_use, container, false);

        card = getArguments().getParcelable(PARAM_CARD);
        final Spannable cardName = new SpannableString(card.name);
        cardName.setSpan(new ForegroundColorSpan(getResources().getColor(DictionaryData.getCardRarityColorId(card.type.rarity))),
                0, cardName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        UiUtils.setText(view.findViewById(R.id.dialog_card_use_name), cardName);
        UiUtils.setText(view.findViewById(R.id.dialog_card_use_description), card.type.description);

        persons = new HashMap<>();
        final boolean isPlacePresent = isPlacePresent(card.type.targetType);
        final boolean isPersonPresent = isPersonPresent(card.type.targetType);

        viewAction = view.findViewById(R.id.dialog_card_use_action);
        blockPlace = view.findViewById(R.id.dialog_card_use_place_block);
        blockPerson = view.findViewById(R.id.dialog_card_use_person_block);
        textPlace = (TextView) view.findViewById(R.id.dialog_card_use_place);
        textPerson = (TextView) view.findViewById(R.id.dialog_card_use_person);

        if(isPlacePresent) {
            viewAction.setEnabled(false);

            blockPlace.setVisibility(View.VISIBLE);
            textPlace.setEnabled(false);
            textPlace.setText(getString(R.string.common_loading));

            if(isPersonPresent) {
                blockPerson.setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.dialog_card_use_person_title)).setText(getString(
                        card.type.targetType == CardTargetType.BUILDING
                                ? R.string.game_card_use_building
                                : R.string.game_card_use_person));
                textPerson.setEnabled(false);
                textPerson.setText(getString(R.string.common_loading));
            } else {
                blockPerson.setVisibility(View.GONE);
            }

            RequestExecutor.execute(
                    getActivity(),
                    new PlacesRequestBuilder(),
                    RequestUtils.wrapCallback(new ApiCallback<PlacesResponse>() {
                        @Override
                        public void onSuccess(PlacesResponse response) {
                            places = new ArrayList<>(response.places.values());
                            Collections.sort(places, new Comparator<PlaceInfo>() {
                                @Override
                                public int compare(PlaceInfo lhs, PlaceInfo rhs) {
                                    return lhs.name.compareTo(rhs.name);
                                }
                            });
                            final int count = places.size();
                            final String[] placeNames = new String[count];
                            for(int i = 0; i < count; i++) {
                                placeNames[i] = places.get(i).name;
                            }

                            textPlace.setEnabled(true);
                            onPlaceSelected(0);
                            textPlace.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtils.showChoiceDialog(
                                            getChildFragmentManager(),
                                            getString(R.string.game_card_use_place),
                                            placeNames,
                                            new ChoiceDialog.ItemChooseListener() {
                                                @Override
                                                public void onItemSelected(int position) {
                                                    onPlaceSelected(position);
                                                }
                                            });
                                }
                            });
                        }

                        @Override
                        public void onError(AbstractApiResponse response) {
                            textPlace.setText(getString(R.string.common_error_hint));
                            if(isPersonPresent) {
                                textPerson.setText(getString(R.string.common_error_hint));
                            }
                        }
                    }, CardUseDialog.this));
        } else {
            blockPlace.setVisibility(View.GONE);
            blockPerson.setVisibility(View.GONE);
            viewAction.setEnabled(true);
            viewAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                            getString(R.string.game_card_use),
                            getString(R.string.game_card_use_progress),
                            true, false);
                    RequestExecutor.execute(
                            getActivity(),
                            new UseCardRequestBuilder().setCardId(card.id),
                            getCardUseCallback(progressDialog));
                }
            });
        }

        return wrapView(inflater, view, getArguments().getString(PARAM_TITLE));
    }

    private boolean isPlacePresent(final CardTargetType targetType) {
        return (targetType == CardTargetType.PLACE)
                || (targetType == CardTargetType.PERSON)
                || (targetType == CardTargetType.BUILDING);
    }

    private boolean isPersonPresent(final CardTargetType targetType) {
        return (targetType == CardTargetType.PERSON)
                || (targetType == CardTargetType.BUILDING);
    }

    private void onPlaceSelected(final int placeIndex) {
        final PlaceInfo place = places.get(placeIndex);
        textPlace.setText(place.name);
        if(isPersonPresent(card.type.targetType)) {
            viewAction.setEnabled(false);
            textPerson.setEnabled(false);

            final List<CouncilMemberInfo> council = persons.get(placeIndex);
            if(council == null) {
                textPerson.setText(getString(R.string.common_loading));
                RequestExecutor.execute(
                        getActivity(),
                        new PlaceRequestBuilder().setPlaceId(place.id),
                        RequestUtils.wrapCallback(new ApiCallback<PlaceResponse>() {
                            @Override
                            public void onSuccess(PlaceResponse response) {
                                if(card.type.targetType == CardTargetType.BUILDING) {
                                    for(final Iterator<CouncilMemberInfo> councilIterator = response.council.iterator();
                                        councilIterator.hasNext();) {
                                        if(councilIterator.next().buildingId == null) {
                                            councilIterator.remove();
                                        }
                                    }
                                }
                                Collections.sort(response.council, new Comparator<CouncilMemberInfo>() {
                                    @Override
                                    public int compare(CouncilMemberInfo lhs, CouncilMemberInfo rhs) {
                                        return Double.compare(rhs.power, lhs.power);
                                    }
                                });
                                persons.put(placeIndex, response.council);
                                fillPersons(placeIndex);
                            }

                            @Override
                            public void onError(AbstractApiResponse response) {
                                textPerson.setText(getString(R.string.common_error_hint));
                            }
                        }, CardUseDialog.this));
            } else {
                fillPersons(placeIndex);
            }
        } else {
            viewAction.setEnabled(true);
            viewAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                            getString(R.string.game_card_use),
                            getString(R.string.game_card_use_progress),
                            true, false);
                    RequestExecutor.execute(
                            getActivity(),
                            new UseCardRequestBuilder()
                                    .setCardId(card.id)
                                    .setTarget(card.type.targetType, place.id),
                            getCardUseCallback(progressDialog));
                }
            });
        }
    }

    private void fillPersons(final int placeIndex) {
        final List<CouncilMemberInfo> council = persons.get(placeIndex);
        final int personsCount = council.size();
        if(personsCount == 0) {
            textPerson.setEnabled(false);
            textPerson.setText(getString(R.string.game_card_use_no_buildings));
            return;
        }

        textPerson.setEnabled(true);
        final String[] personNames = new String[personsCount];
        for(int i = 0; i < personsCount; i++) {
            personNames[i] = council.get(i).name;
        }

        onPersonSelected(placeIndex, 0);
        textPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showChoiceDialog(
                        getChildFragmentManager(),
                        getString(card.type.targetType == CardTargetType.BUILDING
                                ? R.string.game_card_use_building : R.string.game_card_use_person),
                        personNames,
                        new ChoiceDialog.ItemChooseListener() {
                            @Override
                            public void onItemSelected(int position) {
                                onPersonSelected(placeIndex, position);
                            }
                        });
            }
        });
    }

    private void onPersonSelected(final int placeIndex, final int personIndex) {
        final CouncilMemberInfo councilMemberInfo = persons.get(placeIndex).get(personIndex);
        textPerson.setText(councilMemberInfo.name);
        viewAction.setEnabled(true);
        viewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                        getString(R.string.game_card_use),
                        getString(R.string.game_card_use_progress),
                        true, false);
                final int targetId = card.type.targetType == CardTargetType.BUILDING
                        ? councilMemberInfo.buildingId : councilMemberInfo.id;
                RequestExecutor.execute(
                        getActivity(),
                        new UseCardRequestBuilder()
                                .setCardId(card.id)
                                .setTarget(card.type.targetType, targetId),
                        getCardUseCallback(progressDialog));
            }
        });
    }

    private ApiCallback<CommonResponse> getCardUseCallback(final ProgressDialog progressDialog) {
        return RequestUtils.wrapCallback(new ApiCallback<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse response) {
                progressDialog.dismiss();
                dismiss();
                if(onSuccess != null) {
                    onSuccess.run();
                }
            }

            @Override
            public void onError(AbstractApiResponse response) {
                progressDialog.dismiss();
                DialogUtils.showCommonErrorDialog(getActivity().getSupportFragmentManager(), getActivity());
                dismiss();
            }
        }, CardUseDialog.this);
    }

}

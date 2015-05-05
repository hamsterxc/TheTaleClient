package com.lonebytesoft.thetaleclient.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.CardRarity;
import com.lonebytesoft.thetaleclient.api.model.CardInfo;
import com.lonebytesoft.thetaleclient.api.request.CombineCardsRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.TakeCardRequest;
import com.lonebytesoft.thetaleclient.api.response.CombineCardsResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.TakeCardResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.widget.RequestActionView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 09.10.2014
 */
public class CardsFragment extends WrapperFragment {

    private LayoutInflater layoutInflater;

    private View rootView;

    private View helpCounterContainer;
    private TextView helpCounter;
    private ProgressBar helpCounterProgress;
    private RequestActionView helpTakeCardWidget;

    private ViewGroup cardsContainer;

    private View combineActionStart;
    private View combineActions;
    private View combineActionConfirm;
    private View combineActionCancel;
    private View combineContainer;
    private ViewGroup combineList;

    private int cardTextHeight;
    private Collection<CardInfo> cardsListInitial;
    private Map<CardInfo, Integer> cardsInitial;
    private boolean isCombining;
    private List<Pair<CardInfo, View>> combiningCardsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_cards, container, false);

        helpCounterContainer = rootView.findViewById(R.id.cards_container_help_to_next_card);
        helpCounter = (TextView) rootView.findViewById(R.id.cards_help_counter);
        helpCounterProgress = (ProgressBar) rootView.findViewById(R.id.cards_help_counter_progress);
        helpTakeCardWidget = (RequestActionView) rootView.findViewById(R.id.cards_take_card);

        cardsContainer = (ViewGroup) rootView.findViewById(R.id.cards_container);

        combineActionStart = rootView.findViewById(R.id.cards_combine_action_start);
        combineActions = rootView.findViewById(R.id.cards_combine_actions);
        combineActionConfirm = rootView.findViewById(R.id.cards_combine_action_confirm);
        combineActionCancel = rootView.findViewById(R.id.cards_combine_action_cancel);
        combineContainer = rootView.findViewById(R.id.cards_combine_container);
        combineList = (ViewGroup) rootView.findViewById(R.id.cards_combine_list);

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        if(isGlobal) {
            isCombining = false;
            combiningCardsList = new ArrayList<>();
            updateCombineViews();
        }

        combineActionStart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int height = combineActionStart.getHeight();
                if(height > 0) {
                    if(isAdded()) {
                        cardTextHeight = (int) (height
                                - 2 * getResources().getDimension(R.dimen.request_action_padding)
                                + 2 * getResources().getDimension(R.dimen.game_card_margins_vertical));
                    }
                    UiUtils.removeGlobalLayoutListener(combineActionStart, this);
                }
            }
        });

        final ApiResponseCallback<GameInfoResponse> callback = RequestUtils.wrapCallback(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(final GameInfoResponse response) {
                if(!response.account.isOwnInfo) {
                    setError(getString(R.string.game_cards_unavailable_foreign));
                    return;
                }

                if(response.account.hero.cards.cardHelpCurrent >= response.account.hero.cards.cardHelpBarrier) {
                    helpCounterContainer.setVisibility(View.GONE);
                    helpTakeCardWidget.setMode(RequestActionView.Mode.ACTION);
                    helpTakeCardWidget.setVisibility(View.VISIBLE);

                    helpTakeCardWidget.setActionClickListener(new Runnable() {
                        @Override
                        public void run() {
                            new TakeCardRequest().execute(RequestUtils.wrapCallback(new ApiResponseCallback<TakeCardResponse>() {
                                @Override
                                public void processResponse(TakeCardResponse response) {
                                    helpTakeCardWidget.setMode(RequestActionView.Mode.ACTION);
                                    DialogUtils.showCardInfoDialog(getChildFragmentManager(),
                                            getString(R.string.game_card_take_result),
                                            response.card,
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    refresh(true);
                                                }
                                            });
                                }

                                @Override
                                public void processError(TakeCardResponse response) {
                                    helpTakeCardWidget.setErrorText(response.errorMessage);
                                }
                            }, CardsFragment.this));
                        }
                    });
                } else {
                    helpCounterContainer.setVisibility(View.VISIBLE);
                    helpTakeCardWidget.setVisibility(View.GONE);

                    helpCounterProgress.setMax(response.account.hero.cards.cardHelpBarrier);
                    helpCounterProgress.setProgress(response.account.hero.cards.cardHelpCurrent);
                    helpCounter.setText(getString(R.string.game_help_progress_to_next_card,
                            response.account.hero.cards.cardHelpCurrent,
                            response.account.hero.cards.cardHelpBarrier));
                    helpCounter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if(isAdded()) {
                                UiUtils.setHeight(helpCounterProgress,
                                        (int) (helpCounter.getHeight() + 2 * getResources().getDimension(R.dimen.game_info_bar_padding)));
                            }
                            UiUtils.removeGlobalLayoutListener(helpCounter, this);
                        }
                    });
                }

                combineActionStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isCombining = true;
                        updateCombineViews();
                    }
                });

                cardsListInitial = response.account.hero.cards.cards;
                cardsContainer.removeAllViews();
                final Map<CardInfo, Integer> cards = ObjectUtils.getItemsCountList(response.account.hero.cards.cards,
                        new Comparator<CardInfo>() {
                            @Override
                            public int compare(CardInfo lhs, CardInfo rhs) {
                                return lhs.compareTo(rhs);
                            }
                        });
                cardsInitial = cards;
                for(final Map.Entry<CardInfo, Integer> cardsEntry : cards.entrySet()) {
                    final CardInfo card = cardsEntry.getKey();
                    final int count = cardsEntry.getValue();
                    final View cardEntryView = getCardEntryView(layoutInflater, card, count, false);
                    cardEntryView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isCombining) {
                                updateCombineList(cardEntryView, card, true);
                            } else {
                                if(card.type != null) {
                                    DialogUtils.showCardUseDialog(
                                            getChildFragmentManager(),
                                            getString(R.string.game_card_use),
                                            card,
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            refresh(true);
                                                        }
                                                    });
                                                }
                                            });
                                }
                            }
                        }
                    });
                    cardsContainer.addView(cardEntryView);
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

    private void updateCombineViews() {
        if(isCombining) {
            combineActionStart.setVisibility(View.GONE);
            combineActions.setVisibility(View.VISIBLE);
            combineContainer.setVisibility(View.VISIBLE);
            UiUtils.setHeight(combineContainer, (int) (3 * cardTextHeight
                    + 2 * getResources().getDimension(R.dimen.game_card_combine_margins)));

            combiningCardsList.clear();
            updateCombineList();

            combineActionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh(true);
                }
            });
            combineActionConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                            getString(R.string.game_cards_combine),
                            getString(R.string.game_cards_combine_progress),
                            true, false);

                    final Collection<CardInfo> cardsList = new ArrayList<>(cardsListInitial);
                    final List<Integer> cardIds = new ArrayList<>();
                    for(final Pair<CardInfo, View> pair : combiningCardsList) {
                        for(final Iterator<CardInfo> iterator = cardsList.iterator(); iterator.hasNext();) {
                            final CardInfo card = iterator.next();
                            if(pair.first.equals(card)) {
                                iterator.remove();
                                cardIds.add(card.id);
                                break;
                            }
                        }
                    }

                    new CombineCardsRequest(cardIds).execute(RequestUtils.wrapCallback(new ApiResponseCallback<CombineCardsResponse>() {
                        @Override
                        public void processResponse(CombineCardsResponse response) {
                            progressDialog.dismiss();
                            DialogUtils.showCardInfoDialog(getChildFragmentManager(),
                                    getString(R.string.game_cards_combine_result),
                                    response.card,
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            refresh(true);
                                        }
                                    });
                        }

                        @Override
                        public void processError(CombineCardsResponse response) {
                            progressDialog.dismiss();
                            DialogUtils.showMessageDialog(getChildFragmentManager(),
                                    getString(R.string.game_cards_combine),
                                    response.errorMessage);
                        }
                    }, CardsFragment.this));
                }
            });
        } else {
            combineActions.setVisibility(View.GONE);
            combineActionStart.setVisibility(View.VISIBLE);
            combineContainer.setVisibility(View.GONE);
        }
    }

    private void updateCombineList() {
        combineList.removeAllViews();
        Collections.sort(combiningCardsList, new Comparator<Pair<CardInfo, View>>() {
            @Override
            public int compare(Pair<CardInfo, View> lhs, Pair<CardInfo, View> rhs) {
                return lhs.first.compareTo(rhs.first);
            }
        });

        for(final Pair<CardInfo, View> pair : combiningCardsList) {
            final View combiningCardView = getCardEntryView(layoutInflater, pair.first, 1, true);
            combiningCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCombineList(pair.second, pair.first, false);
                }
            });
            combineList.addView(combiningCardView);
        }
    }

    private void updateCombineList(final View mainCardView, final CardInfo card, final boolean isAdded) {
        int combineCardCount = cardsInitial.get(card);
        for(final Pair<CardInfo, View> pair : combiningCardsList) {
            if(pair.first.equals(card)) {
                combineCardCount--;
            }
        }

        if(isAdded) {
            if(combineCardCount > 0) {
                combiningCardsList.add(Pair.create(card, mainCardView));
                combineCardCount--;
            }
        } else {
            for(final Iterator<Pair<CardInfo, View>> iterator = combiningCardsList.iterator(); iterator.hasNext();) {
                final Pair<CardInfo, View> pair = iterator.next();
                if(pair.first.equals(card) && (pair.second == mainCardView)) {
                    iterator.remove();
                    break;
                }
            }
            combineCardCount++;
        }
        updateCombineList();

        updateCardEntryView(mainCardView, card, combineCardCount, false);
    }

    private void updateCardEntryView(final View cardEntryView, final CardInfo card, final int count, final boolean isShort) {
        if(count > 0) {
            cardEntryView.setVisibility(View.VISIBLE);
        } else {
            cardEntryView.setVisibility(View.GONE);
            return;
        }

        final CardRarity rarity = card.type == null ? card.rarity : card.type.getRarity();
        final Spannable cardName = new SpannableString(card.name);
        cardName.setSpan(new ForegroundColorSpan(getResources().getColor(rarity.getColorResId())),
                0, cardName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(isShort) {
            ((TextView) cardEntryView.findViewById(R.id.card_name)).setText(cardName);
            cardEntryView.findViewById(R.id.card_description).setVisibility(View.GONE);
            cardEntryView.findViewById(R.id.card_tradable).setVisibility(View.GONE);
        } else {
            ((TextView) cardEntryView.findViewById(R.id.card_name)).setText(
                    TextUtils.concat(cardName, " x ", String.valueOf(count)));
            final TextView cardDescription = (TextView) cardEntryView.findViewById(R.id.card_description);
            if(card.type == null) {
                cardDescription.setVisibility(View.GONE);
            } else {
                cardDescription.setVisibility(View.VISIBLE);
                cardDescription.setText(card.type.getDescription());
            }
            cardEntryView.findViewById(R.id.card_tradable).setVisibility(card.isTradable ? View.VISIBLE : View.GONE);
        }
    }

    private View getCardEntryView(final LayoutInflater layoutInflater, final CardInfo card, final int count, final boolean isShort) {
        final View cardEntryView = layoutInflater.inflate(R.layout.item_card, cardsContainer, false);
        updateCardEntryView(cardEntryView, card, count, isShort);
        return cardEntryView;
    }

}

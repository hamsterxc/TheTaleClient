package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.model.CardInfo;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.TakeCardRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.widget.RequestActionView;

import java.util.Comparator;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_cards, container, false);

        helpCounterContainer = rootView.findViewById(R.id.cards_container_help_to_next_card);
        helpCounter = (TextView) rootView.findViewById(R.id.cards_help_counter);
        helpCounterProgress = (ProgressBar) rootView.findViewById(R.id.cards_help_counter_progress);
        helpTakeCardWidget = (RequestActionView) rootView.findViewById(R.id.cards_take_card);

        cardsContainer = (ViewGroup) rootView.findViewById(R.id.cards_container);

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

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
                            new TakeCardRequest(response.account.accountId).execute(RequestUtils.wrapCallback(new ApiResponseCallback<CommonResponse>() {
                                @Override
                                public void processResponse(CommonResponse response) {
                                    refresh(false);
                                }

                                @Override
                                public void processError(CommonResponse response) {
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

                cardsContainer.removeAllViews();
                final Map<CardInfo, Integer> cards = ObjectUtils.getItemsCountList(response.account.hero.cards.cards,
                        new Comparator<CardInfo>() {
                            @Override
                            public int compare(CardInfo lhs, CardInfo rhs) {
                                return lhs.compareTo(rhs);
                            }
                        });
                for(final Map.Entry<CardInfo, Integer> cardsEntry : cards.entrySet()) {
                    final CardInfo card = cardsEntry.getKey();
                    final int count = cardsEntry.getValue();
                    final View cardEntryView = layoutInflater.inflate(R.layout.item_card, cardsContainer, false);

                    final Spannable cardName = new SpannableString(card.name);
                    cardName.setSpan(new ForegroundColorSpan(getResources().getColor(card.type.getRarity().getColorResId())),
                            0, cardName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((TextView) cardEntryView.findViewById(R.id.card_name)).setText(
                            TextUtils.concat(cardName, " x ", String.valueOf(count)));

                    ((TextView) cardEntryView.findViewById(R.id.card_description)).setText(card.type.getDescription());
                    cardEntryView.findViewById(R.id.card_tradable).setVisibility(card.isTradable ? View.VISIBLE : View.GONE);

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

}

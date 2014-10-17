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
import com.lonebytesoft.thetaleclient.api.dictionary.CardType;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.TakeCardRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.widget.RequestActionView;

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

        new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(final GameInfoResponse response) {
                if(!isAdded()) {
                    return;
                }

                if(response.account == null) {
                    return;
                }

                if(response.account.hero.basicInfo.cardHelpCurrent >= response.account.hero.basicInfo.cardHelpTotal) {
                    helpCounterContainer.setVisibility(View.GONE);
                    helpTakeCardWidget.setMode(RequestActionView.Mode.ACTION);
                    helpTakeCardWidget.setVisibility(View.VISIBLE);

                    helpTakeCardWidget.setActionClickListener(new Runnable() {
                        @Override
                        public void run() {
                            new TakeCardRequest(response.account.accountId).execute(new ApiResponseCallback<CommonResponse>() {
                                @Override
                                public void processResponse(CommonResponse response) {
                                    refresh(false);
                                }

                                @Override
                                public void processError(CommonResponse response) {
                                    helpTakeCardWidget.setErrorText(response.errorMessage);
                                }
                            });
                        }
                    });
                } else {
                    helpCounterContainer.setVisibility(View.VISIBLE);
                    helpTakeCardWidget.setVisibility(View.GONE);

                    helpCounterProgress.setMax(response.account.hero.basicInfo.cardHelpTotal);
                    helpCounterProgress.setProgress(response.account.hero.basicInfo.cardHelpCurrent);
                    helpCounter.setText(getString(R.string.game_help_progress_to_next_card,
                            response.account.hero.basicInfo.cardHelpCurrent,
                            response.account.hero.basicInfo.cardHelpTotal));
                    helpCounter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            final ViewGroup.LayoutParams layoutParams = helpCounterProgress.getLayoutParams();
                            layoutParams.height = (int) (helpCounter.getHeight() + 2 * getResources().getDimension(R.dimen.cards_help_progress_margins));
                            helpCounterProgress.setLayoutParams(layoutParams);
                            UiUtils.removeGlobalLayoutListener(helpCounter, this);
                        }
                    });
                }

                cardsContainer.removeAllViews();
                final int cardTypesCount = CardType.values().length;
                for(int i = 0; i < cardTypesCount; i++) {
                    final CardType cardType = CardType.values()[i];
                    final Integer cardCount = response.account.hero.cards.get(cardType);
                    if((cardCount != null) && (cardCount > 0)) {
                        final View cardEntryView = layoutInflater.inflate(R.layout.item_card, cardsContainer, false);

                        final Spannable cardName = new SpannableString(cardType.getName());
                        cardName.setSpan(new ForegroundColorSpan(getResources().getColor(cardType.getRarity().getColorResId())),
                                0, cardName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ((TextView) cardEntryView.findViewById(R.id.card_name)).setText(
                                TextUtils.concat(cardName, " x ", String.valueOf(cardCount)));

                        ((TextView) cardEntryView.findViewById(R.id.card_description)).setText(cardType.getDescription());

                        cardsContainer.addView(cardEntryView);
                    }
                }
                setMode(DataViewMode.DATA);
            }

            @Override
            public void processError(GameInfoResponse response) {
                setError(response.errorMessage);
            }
        });
    }

}

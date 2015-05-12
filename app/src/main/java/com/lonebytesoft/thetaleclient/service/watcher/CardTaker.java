package com.lonebytesoft.thetaleclient.service.watcher;

import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.request.GetCardRequestBuilder;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 14.11.2014
 */
public class CardTaker implements GameStateWatcher {

    @Override
    public void processGameState(GameInfoResponse gameInfoResponse) {
        if(PreferencesManager.shouldAutoactionCardTake()) {
            if(gameInfoResponse.account.hero.cards.cardHelpCurrent >= gameInfoResponse.account.hero.cards.cardHelpBarrier) {
                RequestExecutor.execute(TheTaleClientApplication.getContext(), new GetCardRequestBuilder(), null);
            }
        }
    }

}

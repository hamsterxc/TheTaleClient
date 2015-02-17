package com.lonebytesoft.thetaleclient.service.watcher;

import com.lonebytesoft.thetaleclient.api.request.TakeCardRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
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
                new TakeCardRequest(gameInfoResponse.account.accountId).execute(null);
            }
        }
    }

}

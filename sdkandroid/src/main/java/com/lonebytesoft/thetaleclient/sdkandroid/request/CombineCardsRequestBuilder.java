package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.CombineCardsRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class CombineCardsRequestBuilder implements AbstractRequestBuilder<CombineCardsRequest> {

    private final List<Integer> cardIds = new ArrayList<>();

    public CombineCardsRequestBuilder addCardId(final int cardId) {
        this.cardIds.add(cardId);
        return this;
    }

    public CombineCardsRequestBuilder addCardIds(final List<Integer> cardIds) {
        this.cardIds.addAll(cardIds);
        return this;
    }

    @Override
    public CombineCardsRequest build(String clientId) {
        return new CombineCardsRequest(clientId, cardIds);
    }

}

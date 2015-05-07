package com.lonebytesoft.thetaleclient.apisdk.request;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardTargetType;
import com.lonebytesoft.thetaleclient.sdk.request.UseCardRequest;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class UseCardRequestBuilder implements AbstractRequestBuilder<UseCardRequest> {

    private int cardId = 0;
    private CardTargetType targetType = null;
    private int entityId = 0;

    public UseCardRequestBuilder setCardId(final int cardId) {
        this.cardId = cardId;
        return this;
    }

    public UseCardRequestBuilder setTarget(final CardTargetType targetType, final int entityId) {
        this.targetType = targetType;
        this.entityId = entityId;
        return this;
    }

    @Override
    public UseCardRequest build(String clientId) {
        if(targetType == null) {
            return new UseCardRequest(clientId, cardId);
        } else {
            return new UseCardRequest(clientId, cardId, targetType, entityId);
        }
    }

}

package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Action;
import com.lonebytesoft.thetaleclient.sdk.request.PerformActionRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class PerformActionRequestBuilder implements AbstractRequestBuilder<PerformActionRequest> {

    private Action action = null;
    private Integer entityId = null;

    public PerformActionRequestBuilder setAction(Action action) {
        this.action = action;
        return this;
    }

    public PerformActionRequestBuilder setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    @Override
    public PerformActionRequest build(String clientId) {
        if(entityId == null) {
            return new PerformActionRequest(clientId, action);
        } else {
            return new PerformActionRequest(clientId, action, entityId);
        }
    }

}

package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.QuestChoiceRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class QuestChoiceRequestBuilder implements AbstractRequestBuilder<QuestChoiceRequest> {

    private String choiceId = null;

    public QuestChoiceRequestBuilder setChoiceId(String choiceId) {
        this.choiceId = choiceId;
        return this;
    }

    @Override
    public QuestChoiceRequest build(String clientId) {
        return new QuestChoiceRequest(clientId, choiceId);
    }

}

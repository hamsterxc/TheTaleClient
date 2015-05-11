package com.lonebytesoft.thetaleclient.sdkandroid;

import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;

/**
 * @author Hamster
 * @since 07.04.2015
 */
public interface AbstractRequestBuilder<T extends AbstractRequest> {

    T build(String clientId);

}

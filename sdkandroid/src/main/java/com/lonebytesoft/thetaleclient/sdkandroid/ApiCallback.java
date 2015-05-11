package com.lonebytesoft.thetaleclient.sdkandroid;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;

/**
 * @author Hamster
 * @since 07.04.2015
 */
public interface ApiCallback<T extends AbstractResponse> {

    void onSuccess(T response);

    void onError(AbstractApiResponse response);

}

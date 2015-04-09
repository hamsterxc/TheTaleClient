package com.lonebytesoft.thetaleclient.apisdk;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;

/**
 * @author Hamster
 * @since 07.04.2015
 */
public interface ApiCallback<T extends AbstractApiResponse> {

    void onSuccess(T response);

    void onError(AbstractApiResponse response);

}

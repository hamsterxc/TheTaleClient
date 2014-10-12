package com.lonebytesoft.thetaleclient.api;

/**
 * @author Hamster
 * @since 30.09.2014
 */
public interface ApiResponseCallback<T extends AbstractApiResponse> extends CommonResponseCallback<T, T> {

    void processResponse(final T response);

    void processError(final T response);

}

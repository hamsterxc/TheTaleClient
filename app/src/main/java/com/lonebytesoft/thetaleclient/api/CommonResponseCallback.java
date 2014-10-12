package com.lonebytesoft.thetaleclient.api;

/**
 * @author Hamster
 * @since 07.10.2014
 */
public interface CommonResponseCallback<T, E> {

    void processResponse(final T response);

    void processError(final E error);

}

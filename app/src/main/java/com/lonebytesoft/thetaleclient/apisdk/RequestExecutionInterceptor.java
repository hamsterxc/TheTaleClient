package com.lonebytesoft.thetaleclient.apisdk;

import android.content.Context;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public interface RequestExecutionInterceptor<Q extends AbstractRequest<A>, A extends AbstractApiResponse> {

    /**
     * @return true if his request should be executed, false if skipped
     */
    boolean beforeExecute();

    /**
     * Execute this request
     */
    void execute(Context context, AbstractRequestBuilder<Q> requestBuilder, ApiCallback<A> callback);

    /**
     * @return true if this result is considered successful
     */
    boolean isSuccessAfterSkip();
    boolean isSuccessAfterSuccess(A response);
    boolean isSuccessAfterError(AbstractApiResponse response);

    /**
     * @return possibly forged successful result
     */
    A getSuccessResponseAfterSkip();
    A getSuccessResponseAfterSuccess(A response);
    A getSuccessResponseAfterError(AbstractApiResponse response);

    /**
     * @return possibly forged erroneous result
     */
    AbstractApiResponse getErrorResponseAfterSkip();
    AbstractApiResponse getErrorResponseAfterSuccess(A response);
    AbstractApiResponse getErrorResponseAfterError(AbstractApiResponse response);

}

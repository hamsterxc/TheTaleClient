package com.lonebytesoft.thetaleclient.apisdk;

import android.content.Context;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;

/**
 * Base request execution interceptor with default behaviour
 * Does not change the default execution flow
 * @author Hamster
 * @since 17.04.2015
 */
public abstract class BaseRequestExecutionInterceptor<Q extends AbstractRequest<A>, A extends AbstractApiResponse>
        implements RequestExecutionInterceptor<Q, A> {

    @Override
    public boolean beforeExecute() {
        return true;
    }

    @Override
    public void execute(Context context, AbstractRequestBuilder<Q> requestBuilder, ApiCallback<A> callback) {
        RequestExecutor.execute(context, requestBuilder, callback);
    }

    @Override
    public boolean isSuccessAfterSkip() {
        return true;
    }

    @Override
    public boolean isSuccessAfterSuccess(A response) {
        return true;
    }

    @Override
    public boolean isSuccessAfterError(AbstractApiResponse response) {
        return false;
    }

    @Override
    public A getSuccessResponseAfterSkip() {
        return null;
    }

    @Override
    public A getSuccessResponseAfterSuccess(A response) {
        return response;
    }

    @Override
    public A getSuccessResponseAfterError(AbstractApiResponse response) {
        return null;
    }

    @Override
    public AbstractApiResponse getErrorResponseAfterSkip() {
        return null;
    }

    @Override
    public AbstractApiResponse getErrorResponseAfterSuccess(A response) {
        return response;
    }

    @Override
    public AbstractApiResponse getErrorResponseAfterError(AbstractApiResponse response) {
        return response;
    }

}

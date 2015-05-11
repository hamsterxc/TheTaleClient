package com.lonebytesoft.thetaleclient.sdkandroid.interceptor;

import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public interface PrerequisiteRequest<Q extends AbstractRequest<A>, A extends AbstractResponse> {

    AbstractRequestBuilder<Q> getRequestBuilder();

    RequestExecutionInterceptor<Q, A> getRequestExecutionInterceptor();

}

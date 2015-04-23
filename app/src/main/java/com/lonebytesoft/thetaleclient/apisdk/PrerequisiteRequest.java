package com.lonebytesoft.thetaleclient.apisdk;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public interface PrerequisiteRequest<Q extends AbstractRequest<A>, A extends AbstractResponse> {

    AbstractRequestBuilder<Q> getRequestBuilder();

    RequestExecutionInterceptor<Q, A> getRequestExecutionInterceptor();

}

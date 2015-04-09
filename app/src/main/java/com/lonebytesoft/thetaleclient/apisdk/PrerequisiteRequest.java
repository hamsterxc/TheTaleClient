package com.lonebytesoft.thetaleclient.apisdk;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public interface PrerequisiteRequest<Q extends AbstractRequest<A>, A extends AbstractApiResponse> {

    AbstractRequestBuilder<Q> getRequestBuilder();

    RequestExecutionInterceptor<A> getRequestExecutionInterceptor();

}

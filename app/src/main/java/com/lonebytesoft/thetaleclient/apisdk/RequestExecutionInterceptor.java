package com.lonebytesoft.thetaleclient.apisdk;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public interface RequestExecutionInterceptor<T extends AbstractApiResponse> {

    boolean shouldExecute();

    void afterExecution(T response);

}

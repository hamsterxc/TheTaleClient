package com.lonebytesoft.thetaleclient.sdk;

import com.lonebytesoft.thetaleclient.sdk.cache.Cache;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.log.Logger;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public abstract class AbstractApiGetRequest<T extends AbstractResponse> extends AbstractRequest<T> {

    private long staleTime = 0;

    @Override
    protected String executeRequest() throws ApiException {
        final String url = getUrl();
        String result = Cache.get(url, System.currentTimeMillis() - staleTime);
        if(result == null) {
            result = super.executeRequest();
            Cache.complete(url, result, System.currentTimeMillis());
        } else {
            if(Logger.isInfoEnabled()) {
                Logger.info(String.format("%s | GET %s", getClass().getSimpleName(), url));
                Logger.info(String.format("Cache hit | %d bytes", result.length()));
            }
            if(Logger.isDebugEnabled()) {
                Logger.debug(result);
            }
        }
        return result;
    }

    @Override
    protected final HttpUriRequest getHttpUriRequest(String csrfToken) {
        return RequestUtils.getHttpGetRequest(getUrl());
    }

    protected abstract String getUrl();

    public abstract T execute() throws ApiException, JSONException;

    public void setStaleTime(final long staleTime) {
        this.staleTime = staleTime;
    }

}

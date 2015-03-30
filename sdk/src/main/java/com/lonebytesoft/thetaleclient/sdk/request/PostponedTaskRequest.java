package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author Hamster
 * @since 19.03.2015
 */
public class PostponedTaskRequest extends AbstractRequest<String> {

    private final String url;

    public PostponedTaskRequest(final String url) {
        this.url = url;
    }

    @Override
    protected HttpUriRequest getHttpUriRequest(String csrfToken) {
        return RequestUtils.getHttpGetRequest(RequestUtils.getApiPostponedTaskUrl(url));
    }

    @Override
    public String execute() throws ApiException {
        return executeRequest();
    }

}

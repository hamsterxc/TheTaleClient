package com.lonebytesoft.thetaleclient.api.cache;

import com.lonebytesoft.thetaleclient.api.HttpMethod;

import java.util.Map;

/**
* @author Hamster
* @since 08.10.2014
*/
public class Request {

    private String url;
    private HttpMethod httpMethod;
    private Map<String, String> getParams;
    private Map<String, String> postParams;

    public Request(final String url, final HttpMethod httpMethod,
                   final Map<String, String> getParams, final Map<String, String> postParams) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.getParams = getParams;
        this.postParams = postParams;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Request)) {
            return false;
        }

        final Request another = (Request) o;
        return another.url.equals(url)
                && another.httpMethod.equals(httpMethod)
                && ((another.getParams == null) && (getParams == null) || (another.getParams != null) && another.getParams.equals(getParams))
                && ((another.postParams == null) && (postParams == null) || (another.postParams != null) && another.postParams.equals(postParams));
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + httpMethod.ordinal();
        if(getParams != null) {
            result = 31 * result + getParams.hashCode();
        }
        if(postParams != null) {
            result = 31 * result + postParams.hashCode();
        }
        return result;
    }

}

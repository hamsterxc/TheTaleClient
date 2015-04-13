package com.lonebytesoft.thetaleclient.sdk.util;

import com.lonebytesoft.thetaleclient.sdk.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public class RequestUtils {

    public static final String PARAM_GET_METHOD_VERSION = "api_version";
    public static final String PARAM_GET_CLIENT_ID = "api_client";
    public static final String PARAM_POST_CSRF_TOKEN = "csrfmiddlewaretoken";

    public static String getApiUrl(final String methodUrl) {
        if(methodUrl == null) {
            return null;
        }

        return String.format(Urls.BASE_API, removeSlashes(methodUrl));
    }

    public static String getApiPostponedTaskUrl(final String url) {
        if(url == null) {
            return null;
        }

        return String.format(Urls.API_POSTPONED_TASK, removeSlashes(url));
    }

    private static String removeSlashes(String url) {
        while(url.startsWith("/")) {
            url = url.substring(1);
        }
//        while(methodUrl.endsWith("/")) {
//            methodUrl = methodUrl.substring(0, methodUrl.length() - 1);
//        }

        return url;
    }

    public static Map<String, String> getApiMethodGetParams(final String version, final String clientId) {
        final Map<String, String> getParams = new HashMap<>();
        getParams.put(PARAM_GET_METHOD_VERSION, version);
        getParams.put(PARAM_GET_CLIENT_ID, clientId);
        return getParams;
    }

    public static String appendGetParams(final String url, final Map<String, String> getParams) {
        try {
            final URIBuilder uriBuilder = new URIBuilder(url);
            if(getParams != null) {
                for(final Map.Entry<String, String> getParam : getParams.entrySet()) {
                    uriBuilder.addParameter(getParam.getKey(), getParam.getValue());
                }
            }
            return uriBuilder.toString();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static HttpGet getHttpGetRequest(final String url) {
        return new HttpGet(url);
    }

    public static HttpPost getHttpPostRequestWithParams(final String url, final Map<String, String> getParams, final Map<String, String> postParams) {
        final String urlComplete = appendGetParams(url, getParams);
        if(urlComplete == null) {
            return null;
        }

        final HttpPost httpPost = new HttpPost(urlComplete);

        if(postParams == null) {
            return httpPost;
        }

        final List<NameValuePair> params = new ArrayList<>(postParams.size());
        for(final Map.Entry<String, String> postParam : postParams.entrySet()) {
            params.add(new BasicNameValuePair(postParam.getKey(), postParam.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        } catch(UnsupportedEncodingException e) {
            return null;
        }

        return httpPost;
    }

}

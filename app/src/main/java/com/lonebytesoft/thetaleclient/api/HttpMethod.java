package com.lonebytesoft.thetaleclient.api;

import android.net.Uri;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 30.09.2014
 */
public enum HttpMethod {

    GET {
        public HttpRequest getHttpRequest(final String url,
                                          final Map<String, String> getParams, final Map<String, String> postParams) {
            return new HttpGet(appendGetParams(url, getParams));
        }
    },
    POST {
        public HttpRequest getHttpRequest(final String url,
                                          final Map<String, String> getParams, final Map<String, String> postParams) {
            final HttpPost httpPost = new HttpPost(appendGetParams(url, getParams));

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
    },
    ;

    public abstract HttpRequest getHttpRequest(final String url,
                                               final Map<String, String> getParams, final Map<String, String> postParams);

    private static String appendGetParams(final String url, final Map<String, String> getParams) {
        if(getParams == null) {
            return url;
        }

        final Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        for(final Map.Entry<String, String> getParam : getParams.entrySet()) {
            uriBuilder.appendQueryParameter(getParam.getKey(), getParam.getValue());
        }
        return uriBuilder.build().toString();
    }

}

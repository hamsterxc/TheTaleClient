package com.lonebytesoft.thetaleclient.api;

import android.os.AsyncTask;

import com.lonebytesoft.thetaleclient.api.cache.Request;
import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Hamster
 * @since 07.10.2014
 */
public abstract class CommonRequest {

    public void execute(final String url, final HttpMethod httpMethod,
                        final Map<String, String> getParams, final Map<String, String> postParams,
                        final CommonResponseCallback<String, Throwable> callback) {
        final Request request = new Request(url, httpMethod, getParams, postParams);

        final long staleTime = getStaleTime();
        if((staleTime > 0) && !RequestCacheManager.initRequest(request, staleTime)) {
            RequestCacheManager.addListener(request, new CommonResponseCallback<String, Void>() {
                @Override
                public void processResponse(String response) {
                    RequestUtils.processResultInMainThread(callback, false, response, null);
                }

                @Override
                public void processError(Void error) {
                    CommonRequest.this.execute(url, httpMethod, getParams, postParams, callback);
                }
            }, staleTime);
            return;
        }

        new AsyncTask<Void, Void, Object>() {

            protected Object doInBackground(Void... params) {
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpRequest httpRequest = httpMethod.getHttpRequest(url, getParams, postParams);
                try {
                    final OutputStream outputStream = new ByteArrayOutputStream();
                    httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                    outputStream.close();
                    return outputStream.toString();
                } catch(IOException e) {
                    return e;
                }
            }

            protected void onPostExecute(Object result) {
                if(result instanceof String) {
                    if(staleTime > 0) {
                        RequestCacheManager.onRequestFinished(request, (String) result);
                    }
                    RequestUtils.processResultInMainThread(callback, false, (String) result, null);
                } else if(result instanceof Throwable) {
                    if(staleTime > 0) {
                        RequestCacheManager.onRequestFinishError(request);
                    }
                    RequestUtils.processResultInMainThread(callback, true, null, (Throwable) result);
                } else {
                    RequestUtils.processResultInMainThread(callback, true, null, null);
                }
            }

        }.execute();
    }

    protected abstract long getStaleTime();

}

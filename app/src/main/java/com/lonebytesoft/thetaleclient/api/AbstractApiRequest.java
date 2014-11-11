package com.lonebytesoft.thetaleclient.api;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.lonebytesoft.thetaleclient.BuildConfig;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.cache.Request;
import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 30.09.2014
 */
public abstract class AbstractApiRequest<T extends AbstractApiResponse> {

    private static final String URL = "http://the-tale.org/%s";
    private static final long RETRY_TIMEOUT_MILLIS = 1000; // 1 s

    private static final String COOKIE_CSRF_TOKEN = "csrftoken";
    private static final String PARAM_CSRF_TOKEN = "csrfmiddlewaretoken";
    private static final String PARAM_API_VERSION = "api_version";
    private static final String PARAM_CLIENT_ID = "api_client";

    private static final Handler handler = new Handler(Looper.getMainLooper());

    private final HttpMethod httpMethod;
    private final String methodUrl;
    private final String version;
    private final boolean needAdditionalParams;

    protected AbstractApiRequest(final HttpMethod httpMethod, final String methodUrl,
                                 final String version, final boolean needAdditionalParams) {
        this.httpMethod = httpMethod;
        this.version = version;
        this.needAdditionalParams = needAdditionalParams;
        if(methodUrl.startsWith("/")) {
            this.methodUrl = methodUrl.substring(1);
        } else {
            this.methodUrl = methodUrl;
        }
    }

    protected void execute(final Map<String, String> getParams, final Map<String, String> postParams,
                           final ApiResponseCallback<T> callback) {
        execute(getParams, postParams, callback, true);
    }

    protected void execute(final Map<String, String> getParams, final Map<String, String> postParams,
                           final ApiResponseCallback<T> callback, final boolean useCache) {
        final String url = String.format(URL, methodUrl);
        final Request request = new Request(url, httpMethod, getParams, postParams);

        final long staleTime = getStaleTime();
        if(useCache && (staleTime > 0) && !RequestCacheManager.initRequest(request, staleTime)) {
            RequestCacheManager.addListener(request, new CommonResponseCallback<String, Void>() {
                @Override
                public void processResponse(String response) {
                    try {
                        final T responseObject = getResponse(response);
                        processFinishedResponse(responseObject, callback);
                    } catch (JSONException e) {
                        RequestUtils.processResultInMainThread(callback, true, null, null);
                    }
                }

                @Override
                public void processError(Void error) {
                    AbstractApiRequest.this.execute(getParams, postParams, callback);
                }
            }, staleTime);
            return;
        }

        new AsyncTask<Void, Void, String[]>() {
            protected String[] doInBackground(Void... params) {
                Map<String, String> requestGetParams = getParams;
                Map<String, String> requestPostParams = postParams;

                if(needAdditionalParams) {
                    if(requestGetParams == null) {
                        requestGetParams = new HashMap<>();
                    }
                    requestGetParams.put(PARAM_API_VERSION, version);
                    requestGetParams.put(PARAM_CLIENT_ID,
                            TheTaleClientApplication.getContext().getPackageName() + "-" + BuildConfig.VERSION_CODE);
                }

                final DefaultHttpClient httpClient = new DefaultHttpClient();

                if(CookieHandler.getDefault() == null) {
                    CookieHandler.setDefault(new CookieManager());
                }
                RequestUtils.setSession();

                for(final HttpCookie httpCookie : ((CookieManager) CookieHandler.getDefault()).getCookieStore().getCookies()) {
                    final BasicClientCookie cookie = new BasicClientCookie(httpCookie.getName(), httpCookie.getValue());
                    cookie.setDomain(httpCookie.getDomain());
                    cookie.setPath(httpCookie.getPath());
                    httpClient.getCookieStore().addCookie(cookie);

                    if((httpMethod == HttpMethod.POST) && (httpCookie.getName().equals(COOKIE_CSRF_TOKEN))) {
                        if(requestPostParams == null) {
                            requestPostParams = new HashMap<>();
                        }
                        requestPostParams.put(PARAM_CSRF_TOKEN, httpCookie.getValue());
                    }
                }

                final HttpRequest httpRequest = httpMethod.getHttpRequest(url, requestGetParams, requestPostParams);
                try {
                    final OutputStream outputStream = new ByteArrayOutputStream();
                    httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                    outputStream.close();

                    for(final Cookie cookie : httpClient.getCookieStore().getCookies()) {
                        final HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
                        httpCookie.setDomain(cookie.getDomain());
                        httpCookie.setPath(cookie.getPath());
                        ((CookieManager) CookieHandler.getDefault()).getCookieStore().add(
                                URI.create(cookie.getDomain() + cookie.getPath()),
                                httpCookie);

                        if(cookie.getName().equals(RequestUtils.COOKIE_SESSION_ID)) {
                            PreferencesManager.setSession(cookie.getValue());
                        }
                    }

                    return new String[]{outputStream.toString(), null};
                } catch(IOException e) {
                    return new String[]{null, e.getLocalizedMessage()};
                }
            }

            protected void onPostExecute(String[] result) {
                if(staleTime > 0) {
                    if (result[0] == null) {
                        RequestCacheManager.onRequestFinishError(request);
                    } else {
                        RequestCacheManager.onRequestFinished(request, result[0]);
                    }
                }

                if(callback != null) {
                    final String responseString;
                    if(result[0] == null) {
                        responseString = RequestUtils.getGenericErrorResponse(result[1]);
                    } else {
                        responseString = result[0];
                    }

                    try {
                        final T response = getResponse(responseString);
                        if (isFinished(response)) {
                            processFinishedResponse(response, callback);
                        } else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    retry(getParams, postParams, response, callback);
                                }
                            }, RETRY_TIMEOUT_MILLIS);
                        }
                    } catch (JSONException e) {
                        RequestUtils.processResultInMainThread(callback, true, null, null);
                    }
                }
            }

        }.execute();
    }

    protected abstract T getResponse(final String response) throws JSONException;

    protected void processFinishedResponse(final T response, final ApiResponseCallback<T> callback) {
        if (isError(response)) {
            RequestUtils.processResultInMainThread(callback, true, null, response);
        } else {
            RequestUtils.processResultInMainThread(callback, false, response, null);
        }
    }

    protected boolean isFinished(final T response) {
        return (response == null) || (response.status != ApiResponseStatus.PROCESSING);
    }

    protected boolean isError(final T response) {
        return (response != null)
                && ((response.status == ApiResponseStatus.ERROR) || (response.status == ApiResponseStatus.GENERIC));
    }

    protected void retry(final Map<String, String> getParams, final Map<String, String> postParams,
                         final T response, final ApiResponseCallback<T> callback) {
        execute(getParams, postParams, callback);
    }

    protected abstract long getStaleTime();

}

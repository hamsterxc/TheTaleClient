package com.lonebytesoft.thetaleclient.sdk;

import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.exception.HttpException;
import com.lonebytesoft.thetaleclient.sdk.exception.UpdateException;
import com.lonebytesoft.thetaleclient.sdk.util.Logger;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Hamster
 * @since 12.03.2015
 */
public abstract class AbstractRequest<T> {

    private static final Object lock = new Object();

    /**
     * Executes current request using provided {@code HttpUriRequest}, {@link #getHttpUriRequest(String)}
     * The only saved state between requests is cookies in the system-wide default cookie store
     * @return Request result
     * @throws ApiException
     */
    protected String executeRequest() throws ApiException {
        final CookieManager cookieManager = RequestUtils.getCookieManager();

        final List<HttpCookie> cookies;
        synchronized (lock) {
            cookies = new ArrayList<>(cookieManager.getCookieStore().getCookies());
        }
        final DefaultHttpClient httpClient = new DefaultHttpClient();

        String csrfToken = null;
        for(final HttpCookie httpCookie : cookies) {
            final BasicClientCookie cookie = new BasicClientCookie(httpCookie.getName(), httpCookie.getValue());
            cookie.setDomain(httpCookie.getDomain());
            cookie.setPath(httpCookie.getPath());
            httpClient.getCookieStore().addCookie(cookie);

            if(httpCookie.getName().equals(RequestUtils.COOKIE_CSRF_TOKEN)) {
                csrfToken = httpCookie.getValue();
            }
        }

        if(csrfToken == null) {
            final Random random = new Random();
            final int length = 32;
            final StringBuilder stringBuilder = new StringBuilder(length);
            final String seed = "0123456789abcdef";
            final int seedLength = seed.length();
            for(int i = 0; i < length; i++) {
                    stringBuilder.append(seed.charAt(Math.abs(random.nextInt() % seedLength)));
            }
            csrfToken = stringBuilder.toString();

            final BasicClientCookie cookie = new BasicClientCookie(RequestUtils.COOKIE_CSRF_TOKEN, csrfToken);
            cookie.setDomain(Urls.BASE_DOMAIN);
            cookie.setPath("/");
            httpClient.getCookieStore().addCookie(cookie);
        }

        final HttpUriRequest httpUriRequest = getHttpUriRequest(csrfToken);

        try {
			long time = System.currentTimeMillis();
            final HttpResponse httpResponse = httpClient.execute(httpUriRequest);
			time = System.currentTimeMillis() - time;

            for(final Cookie cookie : httpClient.getCookieStore().getCookies()) {
                final HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
                httpCookie.setDomain(cookie.getDomain());
                httpCookie.setPath(cookie.getPath());
                cookieManager.getCookieStore().add(
                        URI.create(cookie.getDomain() + cookie.getPath()),
                        httpCookie);
            }

            final int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
            final String response = EntityUtils.toString(httpResponse.getEntity());
            Logger.log(String.format("%s | %d ms | %d bytes | %d %s %s",
                    getClass().getSimpleName(), time, response == null ? 0 : response.length(),
                    httpStatusCode, httpUriRequest.getMethod(), httpUriRequest.getURI()));
            switch(httpStatusCode) {
                case HttpStatus.SC_OK:
                    return response;

                case HttpStatus.SC_SERVICE_UNAVAILABLE:
                    throw new UpdateException();

                default:
                    throw new HttpException(httpStatusCode);
            }
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    /**
     * @param csrfToken current csrfToken
     * @return HttpUriRequest for this request
     */
    protected abstract HttpUriRequest getHttpUriRequest(final String csrfToken);

    public abstract T execute() throws ApiException, JSONException;

}

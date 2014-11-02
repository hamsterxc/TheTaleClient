package com.lonebytesoft.thetaleclient.util;

import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.api.ApiResponseStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;

/**
 * @author Hamster
 * @since 29.10.2014
 */
public class RequestUtils {

    public static final String URL_BASE = "http://the-tale.org";

    public static final String COOKIE_SESSION_ID = "sessionid";

    public static void setSession(final String session) {
        if(CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }

        final String domain = "the-tale.org";
        final String path = "/";
        final HttpCookie httpCookie = new HttpCookie(COOKIE_SESSION_ID, session);
        httpCookie.setDomain(domain);
        httpCookie.setPath(path);
        ((CookieManager) CookieHandler.getDefault()).getCookieStore().add(
                URI.create(domain + path), httpCookie);
    }

    public static void setSession() {
        final String session = PreferencesManager.getSession();
        if(!TextUtils.isEmpty(session)) {
            setSession(session);
        }
    }

    public static String getGenericErrorResponse(final String error) {
        try {
            final JSONObject json = new JSONObject();
            json.put("status", ApiResponseStatus.GENERIC.getCode());
            json.put("code", ApiResponseStatus.GENERIC.getCode());
            json.put("error", error);
            return json.toString();
        } catch(JSONException e) {
            return "";
        }
    }

}

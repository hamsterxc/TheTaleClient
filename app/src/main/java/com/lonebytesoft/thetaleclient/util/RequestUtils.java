package com.lonebytesoft.thetaleclient.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.BuildConfig;
import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.ApiResponseStatus;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;

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

    private static final Handler handler = new Handler(Looper.getMainLooper());

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

    public static <T, E> void processResultInMainThread(final CommonResponseCallback<T, E> callback, final boolean isError,
                                                        final T responseResult, final E errorResult) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(isError) {
                    callback.processError(errorResult);
                } else {
                    callback.processResponse(responseResult);
                }
            }
        });
    }

    public static <T, E> CommonResponseCallback<T, E> wrapCallback(final CommonResponseCallback<T, E> callback, final Fragment fragment) {
        return new CommonResponseCallback<T, E>() {
            @Override
            public void processResponse(T response) {
                if(fragment.isAdded()) {
                    callback.processResponse(response);
                }
            }

            @Override
            public void processError(E error) {
                if(fragment.isAdded()) {
                    callback.processError(error);
                }
            }
        };
    }

    public static <T extends AbstractApiResponse> ApiResponseCallback<T> wrapCallback(final ApiResponseCallback<T> callback, final Fragment fragment) {
        return new ApiResponseCallback<T>() {
            @Override
            public void processResponse(T response) {
                if(fragment.isAdded()) {
                    callback.processResponse(response);
                }
            }

            @Override
            public void processError(T response) {
                if(fragment.isAdded()) {
                    callback.processError(response);
                }
            }
        };
    }

    public static String getClientId(final Context context) {
        return context.getPackageName() + "-" + BuildConfig.VERSION_CODE;
    }

    public static <T extends com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse> ApiCallback<T> wrapCallback(final ApiCallback<T> callback, final Fragment fragment) {
        if(fragment == null) {
            return callback;
        } else {
            return new ApiCallback<T>() {
                @Override
                public void onSuccess(final T response) {
                    if(fragment.isAdded()) {
                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(response);
                            }
                        });
                    }
                }

                @Override
                public void onError(final com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse response) {
                    if(fragment.isAdded()) {
                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError(response);
                            }
                        });
                    }
                }
            };
        }
    }

    public static <T extends com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse> ApiCallback<T> wrapCallback(final ApiCallback<T> callback, final Activity activity) {
        if(activity == null) {
            return callback;
        } else {
            return new ApiCallback<T>() {
                @Override
                public void onSuccess(final T response) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(response);
                        }
                    });
                }

                @Override
                public void onError(final com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse response) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(response);
                        }
                    });
                }
            };
        }
    }

}

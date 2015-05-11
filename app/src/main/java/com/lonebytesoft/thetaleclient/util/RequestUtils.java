package com.lonebytesoft.thetaleclient.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.ApiResponseStatus;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.apisdk.interceptor.GameInfoRequestCacheInterceptor;
import com.lonebytesoft.thetaleclient.apisdk.prerequisite.GameInfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.ApiCallback;
import com.lonebytesoft.thetaleclient.sdkandroid.RequestExecutor;
import com.lonebytesoft.thetaleclient.sdkandroid.interceptor.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.request.GameInfoRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.request.MapRequestBuilder;

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

    private static void runChecked(final Fragment fragment, final Runnable task) {
        if((fragment != null) && (fragment.isAdded())) {
            runChecked(fragment.getActivity(), task);
        }
    }

    private static void runChecked(final Activity activity, final Runnable task) {
        if((activity != null)
                && !activity.isFinishing()
                && !((activity instanceof MainActivity) && ((MainActivity) activity).isPaused())) {
            activity.runOnUiThread(task);
        }
    }

    private static void runChecked(final Object uiComponent, final Runnable task) {
        if(uiComponent instanceof Fragment) {
            runChecked((Fragment) uiComponent, task);
        } else if(uiComponent instanceof Activity) {
            runChecked((Activity) uiComponent, task);
        } else {
            task.run();
        }
    }

    public static <T extends com.lonebytesoft.thetaleclient.sdk.AbstractResponse> ApiCallback<T> wrapCallback(final ApiCallback<T> callback, final Object uiComponent) {
        if(!(uiComponent instanceof Fragment) && !(uiComponent instanceof Activity)) {
            return callback;
        } else {
            return new ApiCallback<T>() {
                @Override
                public void onSuccess(final T response) {
                    runChecked(uiComponent, new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(response);
                        }
                    });
                }

                @Override
                public void onError(final com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse response) {
                    runChecked(uiComponent, new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(response);
                        }
                    });
                }
            };
        }
    }

    public static <Q extends AbstractRequest<A>, A extends AbstractResponse> void executePrerequisite(
            final Context context, final PrerequisiteRequest<Q, A> prerequisiteRequest, final ApiCallback<A> callback) {
        RequestExecutor.execute(context,
                prerequisiteRequest.getRequestBuilder(),
                prerequisiteRequest.getRequestExecutionInterceptor(),
                callback);
    }

    public static void executeGameInfoRequestWatching(final Context context, final ApiCallback<GameInfoResponse> callback) {
        final int watchingAccountId = PreferencesManager.getWatchingAccountId();
        final boolean isForeignAccount = watchingAccountId != 0;

        final GameInfoRequestBuilder requestBuilder = new GameInfoRequestBuilder();
        if(isForeignAccount) {
            requestBuilder.setAccountId(watchingAccountId);
        } else {
            requestBuilder.setBase(PreferencesManager.getGameInfoResponseCache());
        }

        RequestExecutor.execute(
                context,
                requestBuilder,
                isForeignAccount ? null : new GameInfoRequestCacheInterceptor(),
                callback);
    }

    public static void executeMapRequest(final Context context, final ApiCallback<MapResponse> callback) {
        executePrerequisite(context, new GameInfoPrerequisiteRequest(), new ApiCallback<GameInfoResponse>() {
            @Override
            public void onSuccess(GameInfoResponse response) {
                RequestExecutor.execute(
                        context,
                        new MapRequestBuilder()
                                .setDynamicContentUrl(PreferencesManager.getDynamicContentUrl())
                                .setMapVersion(PreferencesManager.getMapVersion()),
                        callback);
            }

            @Override
            public void onError(com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse response) {
                callback.onError(response);
            }
        });
    }

}

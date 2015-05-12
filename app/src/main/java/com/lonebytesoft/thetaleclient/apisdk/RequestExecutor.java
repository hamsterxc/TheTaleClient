package com.lonebytesoft.thetaleclient.apisdk;

import android.content.Context;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;
import com.lonebytesoft.thetaleclient.sdk.ApiResponseStatus;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.exception.HttpException;
import com.lonebytesoft.thetaleclient.sdk.exception.UpdateException;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.ApiCallback;
import com.lonebytesoft.thetaleclient.sdkandroid.ErrorApiResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.interceptor.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.interceptor.RequestExecutionInterceptor;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 07.04.2015
 */
public class RequestExecutor {

    public static <Q extends AbstractRequest<A>, A extends AbstractResponse> void execute(
            final Context context, final AbstractRequestBuilder<Q> requestBuilder, final ApiCallback<A> apiCallback) {
        final ApiCallback<A> callback;
        if(apiCallback == null) {
            callback = new ApiCallback<A>() {
                @Override
                public void onSuccess(A response) {
                }

                @Override
                public void onError(AbstractApiResponse response) {
                }
            };
        } else {
            callback = apiCallback;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                int errorStringResId = 0;

                final Q request = requestBuilder.build(
                        com.lonebytesoft.thetaleclient.sdkandroid.util.RequestUtils.getClientId(context));
                try {
                    final A response = request.execute();
                    if((response instanceof AbstractApiResponse) && (((AbstractApiResponse) response).status != ApiResponseStatus.OK)) {
                        callback.onError((AbstractApiResponse) response);
                    } else {
                        callback.onSuccess(response);
                        RequestUtils.saveSession();
                    }
                } catch (UpdateException e) {
                    errorStringResId = R.string.api_error_update;
                } catch (HttpException e) {
                    errorStringResId = R.string.api_error_network;
                } catch (ApiException|JSONException e) {
                    errorStringResId = R.string.api_error;
                }

                if(errorStringResId != 0) {
                    try {
                        callback.onError(new ErrorApiResponse(context.getString(errorStringResId)));
                    } catch (JSONException e) {
                        callback.onError(null);
                    }
                }
            }
        }).start();
    }

    public static <Q extends AbstractRequest<A>, A extends AbstractResponse> void execute(
            final Context context, final AbstractRequestBuilder<Q> requestBuilder,
            final RequestExecutionInterceptor<Q, A> interceptor, final ApiCallback<A> callback) {
        if(interceptor == null) {
            execute(context, requestBuilder, callback);
            return;
        }

        if(interceptor.beforeExecute()) {
            interceptor.execute(context, requestBuilder, new ApiCallback<A>() {
                @Override
                public void onSuccess(A response) {
                    if(interceptor.isSuccessAfterSuccess(response)) {
                        callback.onSuccess(interceptor.getSuccessResponseAfterSuccess(response));
                    } else {
                        callback.onError(interceptor.getErrorResponseAfterSuccess(response));
                    }
                }

                @Override
                public void onError(AbstractApiResponse response) {
                    if(interceptor.isSuccessAfterError(response)) {
                        callback.onSuccess(interceptor.getSuccessResponseAfterError(response));
                    } else {
                        callback.onError(interceptor.getErrorResponseAfterError(response));
                    }
                }
            });
        } else {
            if(interceptor.isSuccessAfterSkip()) {
                callback.onSuccess(interceptor.getSuccessResponseAfterSkip());
            } else {
                callback.onError(interceptor.getErrorResponseAfterSkip());
            }
        }
    }

    public static <Q extends AbstractRequest<A>, A extends AbstractResponse> void executeOptional(
            final Context context, final PrerequisiteRequest<Q, A> prerequisiteRequest, final ApiCallback<A> callback) {
        execute(context,
                prerequisiteRequest.getRequestBuilder(),
                prerequisiteRequest.getRequestExecutionInterceptor(),
                callback);
    }

}

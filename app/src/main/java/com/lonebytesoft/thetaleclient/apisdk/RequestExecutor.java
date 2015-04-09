package com.lonebytesoft.thetaleclient.apisdk;

import android.content.Context;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.exception.HttpException;
import com.lonebytesoft.thetaleclient.sdk.exception.UpdateException;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONException;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

/**
 * @author Hamster
 * @since 07.04.2015
 */
public class RequestExecutor {

    public static <Q extends AbstractRequest<A>, A extends AbstractApiResponse> void execute(
            final Context context, final AbstractRequestBuilder<Q> requestBuilder, final ApiCallback<A> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int errorStringResId = 0;

                final Q request = requestBuilder.build(RequestUtils.getClientId(context));
                try {
                    final A response = request.execute();
                    callback.onSuccess(response);
                } catch (UpdateException e) {
                    errorStringResId = R.string.api_error_update;
                } catch (HttpException e) {
                    errorStringResId = R.string.api_error_network;
                } catch (ApiException e) {
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

    public static <Q extends AbstractRequest<A>, A extends AbstractApiResponse> void executeOptional(
            final Context context, final AbstractRequestBuilder<Q> requestBuilder,
            final RequestExecutionInterceptor<A> interceptor, final ApiCallback<A> callback) {
        if(interceptor.shouldExecute()) {
            execute(context, requestBuilder, new ApiCallback<A>() {
                @Override
                public void onSuccess(A response) {
                    interceptor.afterExecution(response);
                    callback.onSuccess(response);
                }

                @Override
                public void onError(AbstractApiResponse response) {
                    callback.onError(response);
                }
            });
        } else {
            callback.onSuccess(null);
        }
    }

    public static <A extends AbstractApiResponse> void executeOptional(
            final Context context, final PrerequisiteRequest<?, A> prerequisiteRequest, final ApiCallback<A> callback) {
        executeOptional(
                context,
                prerequisiteRequest.getRequestBuilder(),
                prerequisiteRequest.getRequestExecutionInterceptor(),
                callback);
    }

}

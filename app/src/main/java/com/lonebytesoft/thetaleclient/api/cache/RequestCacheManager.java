package com.lonebytesoft.thetaleclient.api.cache;

import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Hamster
 * @since 08.10.2014
 */
public class RequestCacheManager {
    
    private static final String LOG_TAG = RequestCacheManager.class.getSimpleName();

    private static final Map<Request, Long> executing = new ConcurrentHashMap<>();
    private static final List<PendingRequest> pending = new CopyOnWriteArrayList<>();
    private static final Map<Request, RequestResult> completed = new ConcurrentHashMap<>();

    /**
     * Someone says that he wants to perform an http request
     * @param staleTimeout max age of the acceptable response, millis
     * @return true, if "OK, go on, when you're done, notify me",
     * false, if "no, wait, I'll notify you, give me a callback"
     */
    public static boolean initRequest(final Request request, final long staleTimeout) {
        final long time = System.currentTimeMillis();
//        Log.d(LOG_TAG, String.format("initRequest: time = %d, timeout = %d", time, staleTimeout));

        final RequestResult requestResult = completed.get(request);
        if((requestResult == null) || (requestResult.getTimeCompleted() + staleTimeout < time)) {
            final Long executingRequestTime = executing.get(request);
            if((executingRequestTime == null) || (executingRequestTime + staleTimeout < time)) {
                executing.put(request, time);
//                Log.d(LOG_TAG, "initRequest: result = true");
                return true;
            } else {
//                Log.d(LOG_TAG, "initRequest: result = false, reason = no request result or stale, but fresh executing request exists");
                return false;
            }
        } else {
//            Log.d(LOG_TAG, "initRequest: result = false, reason = fresh request result exists");
            return false;
        }
    }

    public static void addListener(final Request request, final CommonResponseCallback<String, Void> callback, final long staleTimeout) {
        final long time = System.currentTimeMillis();
//        Log.d(LOG_TAG, String.format("addListener: time = %d, timeout = %d", time, staleTimeout));

        final RequestResult requestResult = completed.get(request);
        if((requestResult == null) || (requestResult.getTimeCompleted() + staleTimeout < time)) {
            pending.add(new PendingRequest(request, callback, time, staleTimeout));
//            Log.d(LOG_TAG, "addListener: no request result or stale");
        } else {
            callback.processResponse(requestResult.getResult());
//            Log.d(LOG_TAG, "addListener: fresh request result exists");
        }
    }

    public static void onRequestFinished(final Request request, final String response) {
        final long time = System.currentTimeMillis();
//        Log.d(LOG_TAG, String.format("onRequestFinished: time = %d", time));

        executing.remove(request);
        completed.put(request, new RequestResult(time, response));

        for(final PendingRequest pendingRequest : pending) {
            if(!pendingRequest.isFinished() && pendingRequest.getRequest().equals(request)) {
                // got in time
                if(pendingRequest.getTime() + pendingRequest.getStaleTimeout() >= time) {
                    pendingRequest.getCallback().processResponse(response);
//                    Log.d(LOG_TAG, "onRequestFinished: processResponse");
                } else { // late
                    pendingRequest.getCallback().processError(null);
//                    Log.d(LOG_TAG, "onRequestFinished: processError");
                }
                pendingRequest.setFinished();
            }
        }
    }

    public static void onRequestFinishError(final Request request) {
//        Log.d(LOG_TAG, String.format("onRequestFinishError: time = %d", System.currentTimeMillis()));

        executing.remove(request);

        for(final PendingRequest pendingRequest : pending) {
            if(!pendingRequest.isFinished() && pendingRequest.getRequest().equals(request)) {
                pendingRequest.getCallback().processError(null);
//                Log.d(LOG_TAG, "onRequestFinishError: processError");
                pendingRequest.setFinished();
            }
        }
    }

    public static void invalidate() {
        executing.clear();
        pending.clear();
        completed.clear();
    }

}

package com.lonebytesoft.thetaleclient.api.cache;

import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;

/**
* @author Hamster
* @since 08.10.2014
*/
public class PendingRequest {

    private final Request request;
    private final CommonResponseCallback<String, Void> callback;
    private final long time;
    private final long staleTimeout;
    private boolean isFinished;

    public PendingRequest(final Request request, final CommonResponseCallback<String, Void> callback,
                          final long time, final long staleTimeout) {
        this.request = request;
        this.callback = callback;
        this.time = time;
        this.staleTimeout = staleTimeout;
        isFinished = false;
    }

    public Request getRequest() {
        return request;
    }

    public CommonResponseCallback<String, Void> getCallback() {
        return callback;
    }

    public long getTime() {
        return time;
    }

    public long getStaleTimeout() {
        return staleTimeout;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = true;
    }

}

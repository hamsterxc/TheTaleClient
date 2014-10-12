package com.lonebytesoft.thetaleclient.api.cache;

/**
* @author Hamster
* @since 08.10.2014
*/
public class RequestResult {

    private final long timeCompleted;
    private final String result;

    public RequestResult(final long timeCompleted, final String result) {
        this.timeCompleted = timeCompleted;
        this.result = result;
    }

    public long getTimeCompleted() {
        return timeCompleted;
    }

    public String getResult() {
        return result;
    }

}

package com.lonebytesoft.thetaleclient.sdk;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public abstract class AbstractResponse {

    public final String rawResponse;

    public AbstractResponse(final String response) {
        this.rawResponse = response;
    }

}

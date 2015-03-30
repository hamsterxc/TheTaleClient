package com.lonebytesoft.thetaleclient.sdk.exception;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public class HttpException extends ApiException {

    private final int httpStatusCode;

    public HttpException(final int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

}

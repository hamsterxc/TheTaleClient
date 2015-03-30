package com.lonebytesoft.thetaleclient.sdk.exception;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public class ApiException extends Exception {

    public ApiException() {
        super();
    }

    public ApiException(final String message) {
        super(message);
    }

    public ApiException(final Throwable cause) {
        super(cause);
    }

    public ApiException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

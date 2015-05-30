package com.lonebytesoft.thetaleclient.sdk.log;

/**
 * @author Hamster
 * @since 25.05.2015
 */
public interface LogStrategy {

    void debug(String message);
    void info(String message);
    void error(String message);

    boolean isDebugEnabled();
    boolean isInfoEnabled();
    boolean isErrorEnabled();

}

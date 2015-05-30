package com.lonebytesoft.thetaleclient.sdk.log;

/**
 * @author Hamster
 * @since 25.05.2015
 */
public class SilentLogStrategy implements LogStrategy {

    @Override
    public void debug(String message) {
    }

    @Override
    public void info(String message) {
    }

    @Override
    public void error(String message) {
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

}

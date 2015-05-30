package com.lonebytesoft.thetaleclient.sdk.log;

/**
 * @author Hamster
 * @since 25.05.2015
 */
public class Logger {

    private static LogStrategy logStrategy = new SilentLogStrategy();

    private static void ensureLogStrategy() {
        if(logStrategy == null) {
            logStrategy = new SilentLogStrategy();
        }
    }

    public static void setLogStrategy(final LogStrategy logStrategy) {
        Logger.logStrategy = logStrategy;
    }

    public static void debug(String message) {
        ensureLogStrategy();
        logStrategy.debug(message);
    }

    public static void info(String message) {
        ensureLogStrategy();
        logStrategy.info(message);
    }

    public static void error(String message) {
        ensureLogStrategy();
        logStrategy.error(message);
    }

    public static boolean isDebugEnabled() {
        ensureLogStrategy();
        return logStrategy.isDebugEnabled();
    }

    public static boolean isInfoEnabled() {
        ensureLogStrategy();
        return logStrategy.isInfoEnabled();
    }

    public static boolean isErrorEnabled() {
        ensureLogStrategy();
        return logStrategy.isErrorEnabled();
    }

}

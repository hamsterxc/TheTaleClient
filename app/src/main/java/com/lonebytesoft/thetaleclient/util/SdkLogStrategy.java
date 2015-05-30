package com.lonebytesoft.thetaleclient.util;

import android.text.TextUtils;
import android.util.Log;

import com.lonebytesoft.thetaleclient.sdk.log.LogStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 25.05.2015
 */
public class SdkLogStrategy implements LogStrategy {

    private static final String LOG_TAG = "TTCSDK";
    private static final int LINE_LENGTH = 1000;

    private final boolean isDetailed;

    public SdkLogStrategy(final boolean isDetailed) {
        this.isDetailed = isDetailed;
    }

    @Override
    public void debug(String s) {
        for(final String line : split(s)) {
            Log.d(LOG_TAG, line);
        }
    }

    @Override
    public void info(String s) {
        for(final String line : split(s)) {
            Log.i(LOG_TAG, line);
        }
    }

    @Override
    public void error(String s) {
        for(final String line : split(s)) {
            Log.e(LOG_TAG, line);
        }
    }

    private List<String> split(String s) {
        final List<String> result = new ArrayList<>();
        if(TextUtils.isEmpty(s)) {
            return result;
        }

        while(s.length() > LINE_LENGTH) {
            result.add(s.substring(0, LINE_LENGTH));
            s = s.substring(LINE_LENGTH);
        }
        result.add(s);

        return result;
    }

    @Override
    public boolean isDebugEnabled() {
        return isDetailed;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

}

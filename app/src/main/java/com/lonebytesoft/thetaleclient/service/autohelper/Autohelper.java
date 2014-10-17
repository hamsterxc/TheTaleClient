package com.lonebytesoft.thetaleclient.service.autohelper;

import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public interface Autohelper {
    boolean shouldHelp(GameInfoResponse gameInfoResponse);
}

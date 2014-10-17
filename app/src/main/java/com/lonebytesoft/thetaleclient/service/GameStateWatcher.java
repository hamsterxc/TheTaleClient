package com.lonebytesoft.thetaleclient.service;

import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public interface GameStateWatcher {
    void processGameState(GameInfoResponse gameInfoResponse);
}

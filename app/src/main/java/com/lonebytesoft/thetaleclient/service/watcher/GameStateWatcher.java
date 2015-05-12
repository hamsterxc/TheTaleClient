package com.lonebytesoft.thetaleclient.service.watcher;

import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public interface GameStateWatcher {

    void processGameState(GameInfoResponse gameInfoResponse);

}

package com.lonebytesoft.thetaleclient.fragment.onscreen;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 04.12.2014
 */
public class OnscreenStateWatcher {

    private final Map<OnscreenPart, Boolean> onscreenParts;

    public OnscreenStateWatcher() {
        onscreenParts = new HashMap<>(OnscreenPart.values().length);
        for(final OnscreenPart onscreenPart : OnscreenPart.values()) {
            onscreenParts.put(onscreenPart, false);
        }
    }

    public void onscreenStateChange(final OnscreenPart onscreenPart, final boolean isOnscreen) {
        onscreenParts.put(onscreenPart, isOnscreen);
    }

    public boolean isOnscreen(final OnscreenPart onscreenPart) {
        return onscreenParts.get(onscreenPart);
    }

}

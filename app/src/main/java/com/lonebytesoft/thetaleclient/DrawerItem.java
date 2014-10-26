package com.lonebytesoft.thetaleclient;

import android.support.v4.app.Fragment;

import com.lonebytesoft.thetaleclient.fragment.ChatFragment;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.fragment.MapFragment;
import com.lonebytesoft.thetaleclient.fragment.SettingsFragment;

/**
* @author Hamster
* @since 27.10.2014
*/
public enum DrawerItem {

    GAME(R.id.drawer_item_game, R.string.drawer_title_game, GameFragment.class),
    MAP(R.id.drawer_item_map, R.string.drawer_title_map, MapFragment.class),
    CHAT(R.id.drawer_item_chat, R.string.drawer_title_chat, ChatFragment.class),
    SITE(R.id.drawer_item_site, 0, null),
    SETTINGS(R.id.drawer_item_settings, R.string.drawer_title_settings, SettingsFragment.class),
    LOGOUT(R.id.drawer_item_logout, 0, null),
    ;

    private final int viewResId;
    private final int titleResId;
    private final Class<? extends Fragment> fragmentClass;
    private final String fragmentTag;

    DrawerItem(final int viewResId, final int titleResId, final Class<? extends Fragment> fragmentClass) {
        this.viewResId = viewResId;
        this.titleResId = titleResId;
        this.fragmentClass = fragmentClass;
        this.fragmentTag = "DRAWER_FRAGMENT_TAG_" + name();
    }

    public int getViewResId() {
        return viewResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public Fragment getFragment() {
        try {
            return fragmentClass.newInstance();
        } catch(InstantiationException|IllegalAccessException e) {
            return null;
        }
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

}

package com.lonebytesoft.thetaleclient;

import android.support.v4.app.Fragment;

import com.lonebytesoft.thetaleclient.fragment.ChatFragment;
import com.lonebytesoft.thetaleclient.fragment.FindPlayerFragment;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.fragment.MapFragment;
import com.lonebytesoft.thetaleclient.fragment.SettingsFragment;

/**
* @author Hamster
* @since 27.10.2014
*/
public enum DrawerItem {

    PROFILE(R.id.drawer_item_info),
    GAME(R.id.drawer_item_game, R.string.drawer_title_game, GameFragment.class, R.menu.game),
    MAP(R.id.drawer_item_map, R.string.drawer_title_map, MapFragment.class, R.menu.main),
    CHAT(R.id.drawer_item_chat, R.string.drawer_title_chat, ChatFragment.class, R.menu.main),
    SITE(R.id.drawer_item_site),
    FIND_PLAYER(R.id.drawer_item_find_player, R.string.drawer_title_find_player, FindPlayerFragment.class, R.menu.empty),
    SETTINGS(R.id.drawer_item_settings, R.string.drawer_title_settings, SettingsFragment.class, R.menu.empty),
    LOGOUT(R.id.drawer_item_logout),
    ABOUT(R.id.drawer_item_about),
    ;

    private final int viewResId;
    private final int titleResId;
    private final Class<? extends Fragment> fragmentClass;
    private final String fragmentTag;
    private final int menuResId;

    private DrawerItem(final int viewResId, final int titleResId, final Class<? extends Fragment> fragmentClass, final int menuResId) {
        this.viewResId = viewResId;
        this.titleResId = titleResId;
        this.fragmentClass = fragmentClass;
        this.fragmentTag = "DRAWER_FRAGMENT_TAG_" + name();
        this.menuResId = menuResId;
    }

    private DrawerItem(final int viewResId) {
        this.viewResId = viewResId;
        this.titleResId = 0;
        this.fragmentClass = null;
        this.fragmentTag = null;
        this.menuResId = 0;
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

    public int getMenuResId() {
        return menuResId;
    }

}

package com.lonebytesoft.thetaleclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.ApplicationPart;
import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.InfoRequest;
import com.lonebytesoft.thetaleclient.api.request.LogoutRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.fragment.MapFragment;
import com.lonebytesoft.thetaleclient.fragment.NavigationDrawerFragment;
import com.lonebytesoft.thetaleclient.fragment.Refreshable;
import com.lonebytesoft.thetaleclient.fragment.SettingsFragment;
import com.lonebytesoft.thetaleclient.fragment.WrapperFragment;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.UiUtils;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String KEY_GAME_TAB_INDEX = "KEY_GAME_TAB_INDEX";
    private static final String KEY_DRAWER_TAB_INDEX = "KEY_DRAWER_TAB_INDEX";

    private static final String URL_GAME = "http://the-tale.org/game/";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private DrawerItem currentItem;

    private Menu menu;

    private TextView accountNameTextView;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // set up the drawer
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        accountNameTextView = (TextView) findViewById(R.id.drawer_account_name);
        timeTextView = (TextView) findViewById(R.id.drawer_time);

        int tabIndex = DrawerItem.GAME.ordinal();
        if(savedInstanceState != null) {
            tabIndex = savedInstanceState.getInt(KEY_DRAWER_TAB_INDEX, tabIndex);
        }
        onNavigationDrawerItemSelected(DrawerItem.values()[tabIndex]);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int tabIndex = -1;
        if((getIntent() != null) && getIntent().hasExtra(KEY_GAME_TAB_INDEX)) {
            onNavigationDrawerItemSelected(DrawerItem.GAME);
            tabIndex = getIntent().getIntExtra(KEY_GAME_TAB_INDEX, GameFragment.GamePage.GAME_INFO.ordinal());
        }

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag());
        if(fragment instanceof GameFragment) {
            if(tabIndex != -1) {
                ((GameFragment) fragment).setCurrentPage(GameFragment.GamePage.values()[tabIndex]);
            }
            TheTaleClientApplication.onApplicationPartSelected(
                    ((GameFragment) fragment).getCurrentPage() == GameFragment.GamePage.GAME_INFO ? ApplicationPart.GAME_INFO : ApplicationPart.INSIGNIFICANT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        RequestCacheManager.invalidate();
        TheTaleClientApplication.onApplicationPartSelected(ApplicationPart.INSIGNIFICANT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_DRAWER_TAB_INDEX, currentItem.ordinal());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onNavigationDrawerItemSelected(DrawerItem item) {
        if(item != currentItem) {
            switch(item) {
                case SITE:
                    startActivity(UiUtils.getOpenLinkIntent(URL_GAME));
                    break;

                case LOGOUT:
                    PreferencesManager.setCredentials(null, null);

                    final Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag());
                    if(fragment instanceof WrapperFragment) {
                        ((WrapperFragment) fragment).setMode(DataViewMode.LOADING);
                    }

                    new LogoutRequest().execute(new ApiResponseCallback<CommonResponse>() {
                        @Override
                        public void processResponse(CommonResponse response) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void processError(CommonResponse response) {
                            if(fragment instanceof WrapperFragment) {
                                ((WrapperFragment) fragment).setError(response.errorMessage);
                            }
                        }
                    });
                    break;

                default:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    final Fragment oldFragment = fragmentManager.findFragmentByTag(item.getFragmentTag());
                    if(oldFragment == null) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, item.getFragment(), item.getFragmentTag())
                                .commit();
                    } else if(oldFragment.isDetached()) {
                        fragmentManager.beginTransaction()
                                .attach(oldFragment)
                                .commit();
                    }

                    mTitle = getString(item.getTitleResId());
                    currentItem = item;
                    TheTaleClientApplication.onApplicationPartSelected(
                            currentItem == DrawerItem.GAME ? ApplicationPart.GAME_INFO : ApplicationPart.INSIGNIFICANT);
                    supportInvalidateOptionsMenu();

                    break;
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            this.menu = menu;
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void refresh() {
        onRefreshStarted();
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag());
        if(fragment instanceof Refreshable) {
            ((Refreshable) fragment).refresh(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onRefreshStarted() {
        if(menu != null) {
            final MenuItem itemRefresh = menu.findItem(R.id.action_refresh);
            if(itemRefresh != null) {
                MenuItemCompat.setActionView(itemRefresh, getLayoutInflater().inflate(R.layout.menu_progress, null));
            }
        }
    }

    public void onRefreshFinished() {
        if(menu != null) {
            final MenuItem itemRefresh = menu.findItem(R.id.action_refresh);
            if(itemRefresh != null) {
                MenuItemCompat.setActionView(itemRefresh, null);
            }
        }
    }

    public void onDataRefresh() {
        new InfoRequest().execute(new ApiResponseCallback<InfoResponse>() {
            @Override
            public void processResponse(InfoResponse response) {
                UiUtils.setText(accountNameTextView, response.accountName);
                new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(GameInfoResponse response) {
                        UiUtils.setText(timeTextView, String.format("%s %s", response.turnInfo.verboseDate, response.turnInfo.verboseTime));
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
                        UiUtils.setText(timeTextView, null);
                    }
                }, true);
            }

            @Override
            public void processError(InfoResponse response) {
                UiUtils.setText(accountNameTextView, null);
                UiUtils.setText(timeTextView, null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch(currentItem) {
            case MAP:
            case SETTINGS:
                onNavigationDrawerItemSelected(DrawerItem.GAME);
                break;

            default:
                super.onBackPressed();
        }
    }

    public enum DrawerItem {

        GAME(R.string.drawer_title_game, "FRAGMENT_TAG_GAME") {
            @Override
            public Fragment getFragment() {
                return new GameFragment();
            }
        },
        MAP(R.string.drawer_title_map, "FRAGMENT_TAG_MAP") {
            @Override
            public Fragment getFragment() {
                return new MapFragment();
            }
        },
        SITE(0, ""),
        SETTINGS(R.string.drawer_title_settings, "FRAGMENT_TAG_SETTINGS") {
            @Override
            public Fragment getFragment() {
                return new SettingsFragment();
            }
        },
        LOGOUT(0, ""),
        ;

        private final int titleResId;
        private final String fragmentTag;

        private DrawerItem(final int titleResId, final String fragmentTag) {
            this.titleResId = titleResId;
            this.fragmentTag = fragmentTag;
        }

        public Fragment getFragment() {
            return null;
        }

        public int getTitleResId() {
            return titleResId;
        }

        public String getFragmentTag() {
            return fragmentTag;
        }

    }

}

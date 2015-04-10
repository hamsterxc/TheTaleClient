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
import android.view.View;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.DrawerItem;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.apisdk.prerequisite.InfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.apisdk.request.GameInfoRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.request.LogoutRequestBuilder;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.fragment.NavigationDrawerFragment;
import com.lonebytesoft.thetaleclient.fragment.Refreshable;
import com.lonebytesoft.thetaleclient.fragment.WrapperFragment;
import com.lonebytesoft.thetaleclient.fragment.dialog.ChoiceDialog;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.response.CommonResponse;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.HistoryStack;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.TextToSpeechUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.WebsiteUtils;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenPart;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String KEY_GAME_TAB_INDEX = "KEY_GAME_TAB_INDEX";
    public static final String KEY_SHOULD_RESET_WATCHING_ACCOUNT = "KEY_SHOULD_RESET_WATCHING_ACCOUNT";

    private static final String KEY_DRAWER_TAB_INDEX = "KEY_DRAWER_TAB_INDEX";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private DrawerItem currentItem;
    private HistoryStack<DrawerItem> history;
    private boolean isPaused;

    private Menu menu;

    private TextView accountNameTextView;
    private TextView timeTextView;
    private View drawerItemInfoView;

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
        drawerItemInfoView = findViewById(DrawerItem.PROFILE.getViewResId());

        history = new HistoryStack<>(DrawerItem.values().length);
        int tabIndex = DrawerItem.GAME.ordinal();
        if(savedInstanceState != null) {
            tabIndex = savedInstanceState.getInt(KEY_DRAWER_TAB_INDEX, tabIndex);
        }
        onNavigationDrawerItemSelected(DrawerItem.values()[tabIndex]);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(PreferencesManager.isReadAloudConfirmed()) {
            TextToSpeechUtils.init(TheTaleClientApplication.getContext(), null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;

        if(PreferencesManager.shouldExit()) {
            PreferencesManager.setShouldExit(false);
            finish();
        }

        final Intent intent = getIntent();
        int tabIndex = -1;
        if(intent != null) {
            if(intent.hasExtra(KEY_GAME_TAB_INDEX)) {
                onNavigationDrawerItemSelected(DrawerItem.GAME);
                tabIndex = intent.getIntExtra(KEY_GAME_TAB_INDEX, GameFragment.GamePage.GAME_INFO.ordinal());
                intent.removeExtra(KEY_GAME_TAB_INDEX);
            }

            if(intent.getBooleanExtra(KEY_SHOULD_RESET_WATCHING_ACCOUNT, false)) {
                PreferencesManager.setWatchingAccount(0, null);
                intent.removeExtra(KEY_SHOULD_RESET_WATCHING_ACCOUNT);
            }
        }

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag());
        if(tabIndex != -1) {
            final GameFragment.GamePage gamePage = GameFragment.GamePage.values()[tabIndex];
            if(fragment instanceof GameFragment) {
                ((GameFragment) fragment).setCurrentPage(gamePage);
            } else {
                PreferencesManager.setDesiredGamePage(gamePage);
            }
        }
        UiUtils.callOnscreenStateChange(fragment, true);

        TheTaleClientApplication.getOnscreenStateWatcher().onscreenStateChange(OnscreenPart.MAIN, true);
    }

    @Override
    protected void onPause() {
        isPaused = true;

        TheTaleClientApplication.getOnscreenStateWatcher().onscreenStateChange(OnscreenPart.MAIN, false);
        TextToSpeechUtils.pause();
        UiUtils.callOnscreenStateChange(getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag()), false);

        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        isPaused = true;
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_DRAWER_TAB_INDEX, currentItem.ordinal());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TextToSpeechUtils.destroy();
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
                case PROFILE:
                    DialogUtils.showChoiceDialog(getSupportFragmentManager(), getString(R.string.drawer_title_site),
                            new String[]{
                                    getString(R.string.drawer_dialog_profile_item_keeper),
                                    getString(R.string.drawer_dialog_profile_item_hero)
                            },
                            new ChoiceDialog.ItemChooseListener() {
                                @Override
                                public void onItemSelected(final int position) {
                                    RequestExecutor.executeOptional(MainActivity.this, new InfoPrerequisiteRequest(), RequestUtils.wrapCallback(new ApiCallback<InfoResponse>() {
                                        @Override
                                        public void onSuccess(InfoResponse response) {
                                            final int accountId = PreferencesManager.getAccountId();
                                            if(accountId == 0) {
                                                DialogUtils.showCommonErrorDialog(getSupportFragmentManager(), MainActivity.this);
                                            } else {
                                                switch(position) {
                                                    case 0:
                                                        startActivity(UiUtils.getOpenLinkIntent(String.format(WebsiteUtils.URL_PROFILE_KEEPER, accountId)));
                                                        break;

                                                    case 1:
                                                        startActivity(UiUtils.getOpenLinkIntent(String.format(WebsiteUtils.URL_PROFILE_HERO, accountId)));
                                                        break;

                                                    default:
                                                        DialogUtils.showCommonErrorDialog(getSupportFragmentManager(), MainActivity.this);
                                                        break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(AbstractApiResponse response) {
                                            DialogUtils.showCommonErrorDialog(getSupportFragmentManager(), MainActivity.this);
                                        }
                                    }, MainActivity.this));
                                }
                            });
                    break;

                case SITE:
                    startActivity(UiUtils.getOpenLinkIntent(WebsiteUtils.URL_GAME));
                    break;

                case LOGOUT:
                    PreferencesManager.setSession("");

                    final Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag());
                    if(fragment instanceof WrapperFragment) {
                        ((WrapperFragment) fragment).setMode(DataViewMode.LOADING);
                    }

                    RequestExecutor.execute(this, new LogoutRequestBuilder(), new ApiCallback<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse response) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(AbstractApiResponse response) {
                            if(fragment instanceof WrapperFragment) {
                                ((WrapperFragment) fragment).setError(response.errorMessage);
                            }
                        }
                    });
                    break;

                case ABOUT:
                    DialogUtils.showAboutDialog(getSupportFragmentManager());
                    break;

                default:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment oldFragment = fragmentManager.findFragmentByTag(item.getFragmentTag());
                    if(oldFragment == null) {
                        oldFragment = item.getFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, oldFragment, item.getFragmentTag())
                                .commit();
                    } else if(oldFragment.isDetached()) {
                        fragmentManager.beginTransaction()
                                .attach(oldFragment)
                                .commit();
                    }

                    if(currentItem != null) {
                        UiUtils.callOnscreenStateChange(getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag()), false);
                    }
                    UiUtils.callOnscreenStateChange(oldFragment, true);

                    currentItem = item;
                    mTitle = getString(currentItem.getTitleResId());
                    history.set(currentItem);
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
        if (!mNavigationDrawerFragment.isDrawerOpen() && (currentItem.getMenuResId() != 0)) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            this.menu = menu;
            getMenuInflater().inflate(currentItem.getMenuResId(), menu);
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

    public void refreshGameAdjacentFragments() {
        if(currentItem == DrawerItem.GAME) {
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(currentItem.getFragmentTag());
            if(fragment instanceof GameFragment) {
                ((GameFragment) fragment).refreshAdjacentFragments();
            }
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
        RequestExecutor.executeOptional(this, new InfoPrerequisiteRequest(), RequestUtils.wrapCallback(new ApiCallback<InfoResponse>() {
            @Override
            public void onSuccess(InfoResponse response) {
                drawerItemInfoView.setVisibility(View.VISIBLE);
                UiUtils.setText(accountNameTextView, PreferencesManager.getAccountName());
                RequestExecutor.execute(MainActivity.this, new GameInfoRequestBuilder(), RequestUtils.wrapCallback(new ApiCallback<GameInfoResponse>() {
                    @Override
                    public void onSuccess(GameInfoResponse response) {
                        UiUtils.setText(timeTextView, String.format("%s %s", response.turnInfo.verboseDate, response.turnInfo.verboseTime));
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        UiUtils.setText(timeTextView, null);
                    }
                }, MainActivity.this));
            }

            @Override
            public void onError(AbstractApiResponse response) {
                drawerItemInfoView.setVisibility(View.GONE);
                UiUtils.setText(accountNameTextView, null);
                UiUtils.setText(timeTextView, null);
            }
        }, this));
    }

    @Override
    public void onBackPressed() {
        if(mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        } else {
            final DrawerItem drawerItem = history.pop();
            if(drawerItem == null) {
                PreferencesManager.setShouldExit(true);
                finish();
            } else {
                onNavigationDrawerItemSelected(drawerItem);
            }
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean isPaused() {
        return isPaused;
    }

}

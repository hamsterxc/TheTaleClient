package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.TextToSpeechUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenStateListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 05.10.2014
 */
public class GameFragment extends Fragment implements Refreshable, OnscreenStateListener {

    private static final String KEY_PAGE_INDEX = "KEY_PAGE_INDEX";

    private ViewPager viewPager;
    private View findPlayerContainer;

    private int currentPageIndex;
    private boolean shouldCallOnscreen = false;

    public GameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.fragment_game_pager);
        viewPager.setAdapter(new GamePagerAdapter(getActivity().getSupportFragmentManager()));

        final PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.fragment_game_tab_strip);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                UiUtils.callOnscreenStateChange(getPageFragment(currentPageIndex), false);
                UiUtils.callOnscreenStateChange(getPageFragment(position), true);
                currentPageIndex = position;

                updateMenu();
            }
        });

        final GamePage gamePage = PreferencesManager.getDesiredGamePage();
        if(gamePage != null) {
            setCurrentPage(gamePage);
            PreferencesManager.setDesiredGamePage(null);
        } else if(savedInstanceState != null) {
            viewPager.setCurrentItem(savedInstanceState.getInt(KEY_PAGE_INDEX, 0));
        }

        findPlayerContainer = rootView.findViewById(R.id.fragment_game_find_player);
        UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_PAGE_INDEX, viewPager.getCurrentItem());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_read_aloud:
                if(PreferencesManager.isReadAloudConfirmed()) {
                    final boolean wasReadAloudEnabled;
                    switch(GamePage.values()[currentPageIndex]) {
                        case GAME_INFO:
                            wasReadAloudEnabled = PreferencesManager.isJournalReadAloudEnabled();
                            PreferencesManager.setJournalReadAloudEnabled(!wasReadAloudEnabled);
                            break;

                        case DIARY:
                            wasReadAloudEnabled = PreferencesManager.isDiaryReadAloudEnabled();
                            PreferencesManager.setDiaryReadAloudEnabled(!wasReadAloudEnabled);
                            break;

                        default:
                            return super.onOptionsItemSelected(item);
                    }

                    if(wasReadAloudEnabled) {
                        TextToSpeechUtils.pause();
                    }
                    updateMenu();
                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMenu() {
        final MenuItem readAloudMenuItem = UiUtils.getMenuItem(getActivity(), R.id.action_read_aloud);
        if(readAloudMenuItem != null) {
            boolean isVisible = false;
            boolean isOn = false;
            switch(GamePage.values()[currentPageIndex]) {
                case GAME_INFO:
                    isVisible = PreferencesManager.isReadAloudConfirmed();
                    if(isVisible) {
                        isOn = PreferencesManager.isJournalReadAloudEnabled();
                    }
                    break;

                case DIARY:
                    isVisible = PreferencesManager.isReadAloudConfirmed();
                    if(isVisible) {
                        isOn = PreferencesManager.isDiaryReadAloudEnabled();
                    }
                    break;
            }

            readAloudMenuItem.setVisible(isVisible);
            if(isVisible) {
                readAloudMenuItem.setIcon(isOn ? R.drawable.ic_volume_small : R.drawable.ic_volume_off_small);
            }
        }
    }

    @Override
    public void refresh(final boolean isGlobal) {
        final Fragment fragment = getPageFragment(viewPager.getCurrentItem());
        if(fragment instanceof WrapperFragment) {
            ((WrapperFragment) fragment).refresh(isGlobal);
        }
        UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());
    }

    private void refreshPageFragment(final int position) {
        final Fragment fragment = getPageFragment(position);
        if(fragment instanceof WrapperFragment) {
            ((WrapperFragment) fragment).refresh(true);
        }
    }

    public void refreshAdjacentFragments() {
        if(currentPageIndex > 0) {
            refreshPageFragment(currentPageIndex - 1);
        }
        if(currentPageIndex < GamePage.values().length - 1) {
            refreshPageFragment(currentPageIndex + 1);
        }
    }

    public void setCurrentPage(final GamePage page) {
        if(page != null) {
            viewPager.setCurrentItem(page.ordinal());
        }
    }

    private Fragment getPageFragment(final int position) {
        if(viewPager == null) {
            return null;
        }

        final PagerAdapter pagerAdapter = viewPager.getAdapter();
        if(pagerAdapter instanceof GamePagerAdapter) {
            return ((GamePagerAdapter) pagerAdapter).getFragment(position);
        } else {
            return null;
        }
    }

    @Override
    public void onOffscreen() {
        UiUtils.callOnscreenStateChange(getPageFragment(currentPageIndex), false);
    }

    @Override
    public void onOnscreen() {
        final Fragment fragment = getPageFragment(currentPageIndex);
        if(fragment == null) {
            shouldCallOnscreen = true;
        } else {
            UiUtils.callOnscreenStateChange(fragment, true);
            shouldCallOnscreen = false;
        }

        if(findPlayerContainer != null) {
            UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());
        }
    }

    public enum GamePage {

        GAME_INFO(R.string.game_title_info) {
            @Override
            public Fragment getFragment() {
                return new GameInfoFragment();
            }
        },
        QUESTS(R.string.game_title_quests) {
            @Override
            public Fragment getFragment() {
                return new QuestsFragment();
            }
        },
        EQUIPMENT(R.string.game_title_equipment) {
            @Override
            public Fragment getFragment() {
                return new EquipmentFragment();
            }
        },
        CARDS(R.string.game_title_cards) {
            @Override
            public Fragment getFragment() {
                return new CardsFragment();
            }
        },
        DIARY(R.string.game_title_diary) {
            @Override
            public Fragment getFragment() {
                return new DiaryFragment();
            }
        },
        PROFILE(R.string.game_title_profile) {
            @Override
            public Fragment getFragment() {
                return new ProfileFragment();
            }
        },
        ;

        private final int titleResId;

        private GamePage(final int titleResId) {
            this.titleResId = titleResId;
        }

        public abstract Fragment getFragment();

        public int getTitleResId() {
            return titleResId;
        }

    }

    private class GamePagerAdapter extends FragmentStatePagerAdapter {

        private Map<Integer, Fragment> fragments = new HashMap<>();

        public GamePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(GamePage.values()[position].getTitleResId());
        }

        @Override
        public Fragment getItem(int i) {
            return GamePage.values()[i].getFragment();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Fragment fragment = (Fragment) super.instantiateItem(container, position);
            fragments.put(position, fragment);
            if(shouldCallOnscreen) {
                UiUtils.callOnscreenStateChange(fragment, true);
                shouldCallOnscreen = false;
            }
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            fragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return GamePage.values().length;
        }

        public Fragment getFragment(final int position) {
            return fragments.get(position);
        }

    }

}

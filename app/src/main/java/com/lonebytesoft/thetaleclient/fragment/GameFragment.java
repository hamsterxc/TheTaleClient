package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.fragment.onscreen.OnscreenStateListener;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 05.10.2014
 */
public class GameFragment extends Fragment implements Refreshable, OnscreenStateListener {

    private static final String KEY_PAGE_INDEX = "KEY_PAGE_INDEX";

    private ViewPager viewPager;
    private int currentPageIndex;
    private boolean shouldCallOnscreen = false;

    public GameFragment() {
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
            }
        });

        if(savedInstanceState != null) {
            viewPager.setCurrentItem(savedInstanceState.getInt(KEY_PAGE_INDEX, 0));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_PAGE_INDEX, viewPager.getCurrentItem());
    }

    @Override
    public void refresh(final boolean isGlobal) {
        final Fragment fragment = getPageFragment(viewPager.getCurrentItem());
        if(fragment instanceof WrapperFragment) {
            ((WrapperFragment) fragment).refresh(isGlobal);
        }
    }

    public GamePage getCurrentPage() {
        return GamePage.values()[viewPager.getCurrentItem()];
    }

    public void setCurrentPage(final GamePage page) {
        viewPager.setCurrentItem(page.ordinal());
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

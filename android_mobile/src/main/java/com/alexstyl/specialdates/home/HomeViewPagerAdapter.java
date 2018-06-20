package com.alexstyl.specialdates.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alexstyl.specialdates.people.PeopleFragment;
import com.alexstyl.specialdates.settings.UserSettingsFragment;
import com.alexstyl.specialdates.upcoming.UpcomingEventsFragment;

import java.util.HashMap;

class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private HashMap<Integer, Fragment> pages = new HashMap<>();

    private static final int PAGE_COUNT = 3;

    HomeViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = pages.get(position);
        if (fragment == null) {
            fragment = createFragmentFor(position);
            pages.put(position, fragment);
        }
        return fragment;
    }

    private Fragment createFragmentFor(int position) {
        switch (position) {
            case HomeActivity.PAGE_EVENTS:
                return new UpcomingEventsFragment();
            case HomeActivity.PAGE_CONTACTS:
                return new PeopleFragment();
            case HomeActivity.PAGE_SETTINGS:
                return new UserSettingsFragment();
        }
        throw new IllegalArgumentException("Position " + position + " is invalid");
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

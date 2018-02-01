package com.alexstyl.specialdates.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alexstyl.specialdates.people.PeopleFragment;
import com.alexstyl.specialdates.settings.MainPreferenceFragment;
import com.alexstyl.specialdates.upcoming.UpcomingEventsFragment;

import java.util.HashMap;


class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private HashMap<Integer, Fragment> pages = new HashMap<>();

    private static final int PAGE_COUNT = 3;

    private static final int PAGE_UPCOMING = 0;
    private static final int PAGE_CONTACTS = 1;
    private static final int PAGE_SETTINGS = 2;

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
            case PAGE_UPCOMING:
                return new UpcomingEventsFragment();
            case PAGE_CONTACTS:
                return new PeopleFragment();
            case PAGE_SETTINGS:
                return new MainPreferenceFragment();
        }
        throw new IllegalArgumentException("Position " + position + " is invalid");
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

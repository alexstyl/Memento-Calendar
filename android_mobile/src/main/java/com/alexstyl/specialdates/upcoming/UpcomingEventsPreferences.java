package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

class UpcomingEventsPreferences {

    private static final int MIN_TIMES_TO_VIEW_DRAWER = 5;

    private final EasyPreferences preferences;

    public static UpcomingEventsPreferences newInstance(Context context) {
        return new UpcomingEventsPreferences(EasyPreferences.createForDefaultPreferences(context));
    }

    private UpcomingEventsPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    boolean isTheUserAwareOfNavigationDrawer() {
        return preferences.getInt(R.string.key_navigation_times_viewed, 0) > MIN_TIMES_TO_VIEW_DRAWER;
    }

    void triggerNavigationDrawerDisplayed() {
        int times = preferences.getInt(R.string.key_navigation_times_viewed, 0);
        preferences.setInteger(R.string.key_navigation_times_viewed, times + 1);
    }

}

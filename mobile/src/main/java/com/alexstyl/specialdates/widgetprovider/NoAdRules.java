package com.alexstyl.specialdates.widgetprovider;

import com.alexstyl.specialdates.upcoming.UpcomingEventsAdRules;

class NoAdRules implements UpcomingEventsAdRules {

    @Override
    public boolean shouldAppendAdForEndOfMonth(int index) {
        return false;
    }

    @Override
    public void onNewMonthAdded(int index) {
        // do nothing
    }

    @Override
    public boolean shouldAppendAdAt(int index) {
        return false;
    }

    @Override
    public void onNewAdAdded(int index) {
        // do nothing
    }
}

package com.alexstyl.specialdates.upcoming;

final class UpcomingEventsFreeUserAdRules implements UpcomingEventsAdRules {

    private static final int DAY_INTERVAL = 2;

    private int lastAdIndex = 0;
    private boolean monthHandled = false;

    @Override
    public boolean shouldAppendAdForEndOfMonth(int index) {
        return index - lastAdIndex >= DAY_INTERVAL;
    }

    @Override
    public void onNewMonthAdded(int index) {
        monthHandled = false;
        lastAdIndex = index;
    }

    @Override
    public boolean shouldAppendAdAt(int index) {
        return !monthHandled && index - lastAdIndex >= DAY_INTERVAL;
    }

    @Override
    public void onNewAdAdded(int index) {
        monthHandled = true;
        lastAdIndex = index;
    }
}

package com.alexstyl.specialdates.upcoming;

final class UpcomingEventsNoAdRules implements UpcomingEventsAdRules {
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
        throw new IllegalStateException(UpcomingEventsNoAdRules.class.getName() + " does not allow ads to be added");
    }
}

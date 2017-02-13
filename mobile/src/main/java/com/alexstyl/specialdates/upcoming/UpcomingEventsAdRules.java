package com.alexstyl.specialdates.upcoming;

interface UpcomingEventsAdRules {
    boolean shouldAppendAdForEndOfMonth(int index);

    void onNewMonthAdded(int index);

    boolean shouldAppendAdAt(int index);

    void onNewAdAdded(int index);
}

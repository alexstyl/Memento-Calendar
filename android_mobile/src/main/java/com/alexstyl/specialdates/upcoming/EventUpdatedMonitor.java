package com.alexstyl.specialdates.upcoming;

interface EventUpdatedMonitor {

    boolean dataWasUpdated();

    void refreshData();

    void initialise();
}

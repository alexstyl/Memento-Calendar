package com.alexstyl.specialdates.upcoming;

public interface EventUpdatedMonitor {

    boolean dataWasUpdated();

    void refreshData();

    void initialise();
}

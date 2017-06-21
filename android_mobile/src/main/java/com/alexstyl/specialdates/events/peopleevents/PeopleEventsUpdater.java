package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.permissions.PermissionChecker;

class PeopleEventsUpdater {

    private static final Object REFRESH_LOCK = new Object();

    private final PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher;
    private final EventPreferences eventPreferences;
    private final PermissionChecker permissionChecker;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;

    PeopleEventsUpdater(PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher,
                        NamedayDatabaseRefresher namedayDatabaseRefresher,
                        EventPreferences eventPreferences,
                        PermissionChecker permissionChecker) {
        this.peopleEventsDatabaseRefresher = peopleEventsDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
        this.eventPreferences = eventPreferences;
        this.permissionChecker = permissionChecker;
    }

    void updateEvents() {
        if (!permissionChecker.canReadAndWriteContacts()) {
            ErrorTracker.track(new RuntimeException("Tried to update events without permission"));
            return;
        }

        synchronized (REFRESH_LOCK) {
            if (isFirstTimeRunning()) {
                peopleEventsDatabaseRefresher.rebuildEvents();
                namedayDatabaseRefresher.refreshNamedaysIfEnabled();
                eventPreferences.markEventsAsInitialised();
            } else {
                updateEventsIfSettingsChanged();
            }
        }
    }

    private boolean isFirstTimeRunning() {
        return !eventPreferences.hasBeenInitialised();
    }

    private void updateEventsIfSettingsChanged() {
        peopleEventsDatabaseRefresher.rebuildEvents();
        namedayDatabaseRefresher.refreshNamedaysIfEnabled();
    }

}

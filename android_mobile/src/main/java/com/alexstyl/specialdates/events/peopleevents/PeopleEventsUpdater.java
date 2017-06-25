package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.permissions.PermissionChecker;

class PeopleEventsUpdater {

    private final PermissionChecker permissionChecker;
    private final DeviceEventsDatabaseRefresher deviceEventsDatabaseRefresher;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;

    PeopleEventsUpdater(PermissionChecker permissionChecker,
                        DeviceEventsDatabaseRefresher deviceEventsDatabaseRefresher,
                        NamedayDatabaseRefresher namedayDatabaseRefresher) {
        this.deviceEventsDatabaseRefresher = deviceEventsDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
        this.permissionChecker = permissionChecker;
    }

    void updateEvents() {
        if (!permissionChecker.canReadAndWriteContacts()) {
            ErrorTracker.track(new RuntimeException("Tried to update events without permission"));
            return;
        }
        deviceEventsDatabaseRefresher.rebuildEvents();
        namedayDatabaseRefresher.refreshNamedaysIfEnabled();
    }
}

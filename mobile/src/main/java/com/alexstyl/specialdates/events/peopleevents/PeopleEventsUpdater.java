package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.upcoming.NamedaySettingsMonitor;
import com.alexstyl.specialdates.util.ContactsObserver;

class PeopleEventsUpdater {

    private final PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher;
    private final EventPreferences eventPreferences;
    private final NamedaySettingsMonitor namedayMonitor;
    private final ContactsObserver contactsObserver;
    private final PermissionChecker permissionChecker;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;

    PeopleEventsUpdater(PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher,
                        NamedayDatabaseRefresher namedayDatabaseRefresher,
                        EventPreferences eventPreferences,
                        ContactsObserver contactsObserver,
                        NamedaySettingsMonitor namedayMonitor,
                        PermissionChecker permissionChecker
    ) {
        this.peopleEventsDatabaseRefresher = peopleEventsDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
        this.eventPreferences = eventPreferences;
        this.contactsObserver = contactsObserver;
        this.namedayMonitor = namedayMonitor;
        this.permissionChecker = permissionChecker;
    }

    void updateEventsIfNeeded() {
        if (!permissionChecker.canReadAndWriteContacts()) {
            ErrorTracker.track(new RuntimeException("Tried to update events without permission"));
            return;
        }

        boolean isFirstRun = isFirstTimeRunning();
        if (isFirstRun) {
            peopleEventsDatabaseRefresher.refreshEvents();
            namedayDatabaseRefresher.refreshNamedaysIfEnabled();
            eventPreferences.markEventsAsInitialised();
        } else {
            updateEventsIfSettingsChanged();
        }

    }

    private boolean isFirstTimeRunning() {
        return !eventPreferences.hasBeenInitialised();
    }

    private void updateEventsIfSettingsChanged() {
        boolean wereContactsUpdated = contactsObserver.wereContactsUpdated();
        boolean wereNamedaysSettingsUpdated = namedayMonitor.dataWasUpdated();

        if (wereContactsUpdated) {
            peopleEventsDatabaseRefresher.refreshEvents();
        }
        if (wereContactsUpdated || wereNamedaysSettingsUpdated) {
            namedayDatabaseRefresher.refreshNamedaysIfEnabled();
        }
        resetMonitorFlags();
    }

    private void resetMonitorFlags() {
        namedayMonitor.refreshData();
        contactsObserver.resetFlag();
    }

    void register() {
        contactsObserver.register();
    }
}

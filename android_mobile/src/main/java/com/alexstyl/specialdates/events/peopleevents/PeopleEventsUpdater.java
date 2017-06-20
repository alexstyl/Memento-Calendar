package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.ExternalWidgetRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.upcoming.NamedaySettingsMonitor;

class PeopleEventsUpdater {

    private static final Object REFRESH_LOCK = new Object();

    private final PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher;
    private final EventPreferences eventPreferences;
    private final NamedaySettingsMonitor namedayMonitor;
    private final ContactsObserver contactsObserver;
    private final PermissionChecker permissionChecker;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;
    private final ExternalWidgetRefresher widgetRefresher;

    PeopleEventsUpdater(PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher,
                        NamedayDatabaseRefresher namedayDatabaseRefresher,
                        EventPreferences eventPreferences,
                        ContactsObserver contactsObserver,
                        NamedaySettingsMonitor namedayMonitor,
                        PermissionChecker permissionChecker,
                        ExternalWidgetRefresher widgetRefresher) {
        this.peopleEventsDatabaseRefresher = peopleEventsDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
        this.eventPreferences = eventPreferences;
        this.contactsObserver = contactsObserver;
        this.namedayMonitor = namedayMonitor;
        this.permissionChecker = permissionChecker;
        this.widgetRefresher = widgetRefresher;
    }

    void updateEventsIfNeeded() {
        if (!permissionChecker.canReadAndWriteContacts()) {
            ErrorTracker.track(new RuntimeException("Tried to update events without permission"));
            return;
        }

        synchronized (REFRESH_LOCK) {
            if (isFirstTimeRunning()) {
                peopleEventsDatabaseRefresher.refreshEvents();
                widgetRefresher.refreshAllWidgets();
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
        boolean wereContactsUpdated = contactsObserver.wereContactsUpdated();
        boolean wereNamedaysSettingsUpdated = namedayMonitor.dataWasUpdated();

        if (wereContactsUpdated) {
            peopleEventsDatabaseRefresher.refreshEvents();
            widgetRefresher.refreshAllWidgets();
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

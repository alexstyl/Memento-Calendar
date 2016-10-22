package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;
import android.os.Handler;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.upcoming.NamedaySettingsMonitor;
import com.alexstyl.specialdates.util.ContactsObserver;

class PeopleEventsUpdater {

    private final BirthdayDatabaseRefresher birthdayDatabaseRefresher;
    private final EventPreferences eventPreferences;
    private final NamedaySettingsMonitor namedayMonitor;
    private final ContactsObserver contactsObserver;
    private final PermissionChecker permissionChecker;

    private NamedayDatabaseRefresher namedayDatabaseRefresher;

    static PeopleEventsUpdater newInstance(Context context) {
        BirthdayDatabaseRefresher birthdayDatabaseRefresher = BirthdayDatabaseRefresher.newInstance(context);
        NamedayDatabaseRefresher namedayDatabaseRefresher = NamedayDatabaseRefresher.newInstance(context);
        EventPreferences eventPreferences = new EventPreferences(context);
        ContactsObserver contactsObserver = new ContactsObserver(context.getContentResolver(), new Handler());
        NamedaySettingsMonitor namedaySettingsMonitor = new NamedaySettingsMonitor(NamedayPreferences.newInstance(context));
        PermissionChecker permissionChecker = new PermissionChecker(context);
        return new PeopleEventsUpdater(
                birthdayDatabaseRefresher,
                namedayDatabaseRefresher,
                eventPreferences,
                contactsObserver,
                namedaySettingsMonitor,
                permissionChecker
        );
    }

    PeopleEventsUpdater(BirthdayDatabaseRefresher birthdayDatabaseRefresher,
                        NamedayDatabaseRefresher namedayDatabaseRefresher,
                        EventPreferences eventPreferences,
                        ContactsObserver contactsObserver,
                        NamedaySettingsMonitor namedayMonitor,
                        PermissionChecker permissionChecker
    ) {
        this.birthdayDatabaseRefresher = birthdayDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
        this.eventPreferences = eventPreferences;
        this.contactsObserver = contactsObserver;
        this.namedayMonitor = namedayMonitor;
        this.permissionChecker = permissionChecker;
    }

    public void updateEventsIfNeeded() {
        if (!permissionChecker.canReadAndWriteContacts()) {
            ErrorTracker.track(new RuntimeException("Tried to update events without permission"));
            return;
        }

        boolean isFirstRun = isFirstTimeRunning();
        if (isFirstRun) {
            birthdayDatabaseRefresher.refreshBirthdays();
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

        if (wereNamedaysSettingsUpdated) {
            namedayDatabaseRefresher = NamedayDatabaseRefresher.newInstance(MementoApplication.getContext());
        }
        if (wereContactsUpdated) {
            birthdayDatabaseRefresher.refreshBirthdays();
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

    public void register() {
        contactsObserver.register();
    }
}

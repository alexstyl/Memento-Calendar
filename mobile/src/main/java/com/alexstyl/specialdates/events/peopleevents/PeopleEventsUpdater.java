package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.ContactProvider;
import com.alexstyl.specialdates.events.database.ContactColumns;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.upcoming.NamedaySettingsMonitor;
import com.alexstyl.specialdates.util.ContactsObserver;
import com.alexstyl.specialdates.util.DateParser;

class PeopleEventsUpdater {

    private final PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher;
    private final EventPreferences eventPreferences;
    private final NamedaySettingsMonitor namedayMonitor;
    private final ContactsObserver contactsObserver;
    private final PermissionChecker permissionChecker;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;

    static PeopleEventsUpdater newInstance(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        PeopleEventsRepository repository = new PeopleEventsRepository(contentResolver, ContactProvider.get(context), DateParser.INSTANCE);
        PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher = new PeopleEventsDatabaseRefresher(
                repository,
                new ContactEventsMarshaller(ContactColumns.SOURCE_DEVICE),
                new PeopleEventsPersister(new EventSQLiteOpenHelper(context))
        );
        NamedayDatabaseRefresher namedayDatabaseRefresher = NamedayDatabaseRefresher.newInstance(context);
        EventPreferences eventPreferences = new EventPreferences(context);
        ContactsObserver contactsObserver = new ContactsObserver(contentResolver, new Handler());
        NamedaySettingsMonitor namedaySettingsMonitor = new NamedaySettingsMonitor(NamedayPreferences.newInstance(context));
        namedaySettingsMonitor.initialise();
        PermissionChecker permissionChecker = new PermissionChecker(context);
        return new PeopleEventsUpdater(
                peopleEventsDatabaseRefresher,
                namedayDatabaseRefresher,
                eventPreferences,
                contactsObserver,
                namedaySettingsMonitor,
                permissionChecker
        );
    }

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

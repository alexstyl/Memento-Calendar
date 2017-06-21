package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.database.EventColumns;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.util.DateParser;

public class DebugPeopleEventsUpdater {

    private final PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;

    public static DebugPeopleEventsUpdater newInstance(Context context) {
        ContactsProvider contactsProvider = ContactsProvider.get(context);
        AndroidEventsRepository repository = new AndroidEventsRepository(context.getContentResolver(), contactsProvider, DateParser.INSTANCE);
        ContactEventsMarshaller deviceMarshaller = new ContactEventsMarshaller(EventColumns.SOURCE_DEVICE);
        PeopleEventsPersister databaseProvider = new PeopleEventsPersister(new EventSQLiteOpenHelper(context));

        PeopleEventsViewRefresher viewRefresher = PeopleEventsViewRefresher.get(context);
        PeopleEventsDatabaseRefresher databaseRefresher = new PeopleEventsDatabaseRefresher(repository, deviceMarshaller, databaseProvider, viewRefresher);

        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        NamedayCalendarProvider namedayCalendarProvider = NamedayCalendarProvider.newInstance(context.getResources());
        PeopleNamedaysCalculator peopleNamedaysCalculator = new PeopleNamedaysCalculator(namedayPreferences, namedayCalendarProvider, contactsProvider);
        return new DebugPeopleEventsUpdater(databaseRefresher, new NamedayDatabaseRefresher(
                namedayPreferences,
                databaseProvider,
                deviceMarshaller,
                peopleNamedaysCalculator
        ));
    }

    private DebugPeopleEventsUpdater(PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher, NamedayDatabaseRefresher namedayDatabaseRefresher) {
        this.peopleEventsDatabaseRefresher = peopleEventsDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
    }

    public void refresh() {
        peopleEventsDatabaseRefresher.refreshEvents();
        namedayDatabaseRefresher.refreshNamedaysIfEnabled();
    }
}

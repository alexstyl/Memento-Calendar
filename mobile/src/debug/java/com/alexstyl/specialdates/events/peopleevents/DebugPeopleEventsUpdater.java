package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;

import com.alexstyl.specialdates.contact.ContactProvider;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;

public class DebugPeopleEventsUpdater {

    private final PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher;
    private NamedayDatabaseRefresher namedayDatabaseRefresher;

    public static DebugPeopleEventsUpdater newInstance(Context context) {
        PeopleEventsRepository repository = new PeopleEventsRepository(context.getContentResolver(), ContactProvider.get(context));
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller();
        PeopleEventsPersister persister = new PeopleEventsPersister(new EventSQLiteOpenHelper(context));
        PeopleEventsDatabaseRefresher refresher = new PeopleEventsDatabaseRefresher(repository, marshaller, persister);

        return new DebugPeopleEventsUpdater(refresher, NamedayDatabaseRefresher.newInstance(context));
    }

    public DebugPeopleEventsUpdater(PeopleEventsDatabaseRefresher peopleEventsDatabaseRefresher, NamedayDatabaseRefresher namedayDatabaseRefresher) {
        this.peopleEventsDatabaseRefresher = peopleEventsDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
    }

    public void refresh() {
        peopleEventsDatabaseRefresher.refreshEvents();
        namedayDatabaseRefresher.refreshNamedaysIfEnabled();
    }
}

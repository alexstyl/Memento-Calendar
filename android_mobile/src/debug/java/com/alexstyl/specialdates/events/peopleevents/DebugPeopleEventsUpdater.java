package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.resource.AndroidJSONResourceLoader;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayJSONProvider;
import com.alexstyl.specialdates.events.namedays.calendar.resource.SpecialNamedaysHandlerFactory;
import com.alexstyl.specialdates.util.DateParser;

public final class DebugPeopleEventsUpdater {

    private final PeopleEventsStaticEventsRefresher peopleEventsStaticEventsRefresher;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;

    public static DebugPeopleEventsUpdater newInstance(Context context,
                                                       NamedayUserSettings namedayUserSettings,
                                                       ContactsProvider contactsProvider,
                                                       CrashAndErrorTracker tracker) {
        AndroidPeopleEventsRepository repository = new AndroidPeopleEventsRepository(context.getContentResolver(), contactsProvider, DateParser.INSTANCE, tracker);
        AndroidPeopleEventsPersister databaseProvider = new AndroidPeopleEventsPersister(
                new EventSQLiteOpenHelper(context),
                new ContactEventsMarshaller(),
                tracker
        );

        PeopleEventsStaticEventsRefresher databaseRefresher = new PeopleEventsStaticEventsRefresher(repository, databaseProvider);

        AndroidJSONResourceLoader loader = new AndroidJSONResourceLoader(context.getResources());
        NamedayCalendarProvider namedayCalendarProvider = new NamedayCalendarProvider(
                new NamedayJSONProvider(loader),
                new SpecialNamedaysHandlerFactory()
        );
        PeopleNamedaysCalculator peopleNamedaysCalculator = new PeopleNamedaysCalculator(
                namedayUserSettings,
                namedayCalendarProvider,
                contactsProvider
        );
        return new DebugPeopleEventsUpdater(databaseRefresher, new NamedayDatabaseRefresher(
                namedayUserSettings,
                databaseProvider,
                peopleNamedaysCalculator
        ));
    }

    private DebugPeopleEventsUpdater(PeopleEventsStaticEventsRefresher peopleEventsStaticEventsRefresher,
                                     NamedayDatabaseRefresher namedayDatabaseRefresher) {
        this.peopleEventsStaticEventsRefresher = peopleEventsStaticEventsRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
    }

    public void refresh() {
        peopleEventsStaticEventsRefresher.rebuildEvents();
        namedayDatabaseRefresher.refreshNamedaysIfEnabled();
    }
}

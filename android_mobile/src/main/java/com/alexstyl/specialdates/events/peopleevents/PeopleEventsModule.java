package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class PeopleEventsModule {

    private final Context context;

    public PeopleEventsModule(Context context) {
        this.context = context;
    }

    @Provides
    PeopleStaticEventsProvider peopleStaticEventsProvider(ContactsProvider contactsProvider) {
        return new AndroidPeopleStaticEventsProvider(
                new EventSQLiteOpenHelper(context),
                contactsProvider,
                new CustomEventProvider(context.getContentResolver())
        );
    }

    @Provides
    PeopleEventsProvider provider(NamedayUserSettings namedayUserSettings,
                                  PeopleNamedaysCalculator peopleNamedaysCalculator,
                                  PeopleStaticEventsProvider staticEvents) {
        return new PeopleEventsProvider(namedayUserSettings, peopleNamedaysCalculator, staticEvents);
    }

    @Provides
    PeopleNamedaysCalculator namedaysCalculator(NamedayUserSettings namedayPreferences,
                                                NamedayCalendarProvider namedaysCalendarProvider,
                                                ContactsProvider contactsProvider) {
        return new PeopleNamedaysCalculator(namedayPreferences, namedaysCalendarProvider, contactsProvider);
    }
}

package com.alexstyl.specialdates.events.namedays;

import android.content.ContentValues;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsMarshaller;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator;

import java.util.List;

public class NamedayDatabaseRefresher {

    private final NamedayPreferences namedayPreferences;
    private final PeopleEventsPersister perister;
    private final ContactEventsMarshaller eventMarshaller;
    private final PeopleNamedaysCalculator calculator;

    public NamedayDatabaseRefresher(NamedayPreferences namedayPreferences,
                                    PeopleEventsPersister databaseProvider,
                                    ContactEventsMarshaller eventMarshaller,
                                    PeopleNamedaysCalculator calculator
    ) {
        this.namedayPreferences = namedayPreferences;
        this.perister = databaseProvider;
        this.eventMarshaller = eventMarshaller;
        this.calculator = calculator;
    }

    public void refreshNamedaysIfEnabled() {
        perister.deleteAllNamedays();
        if (namedayPreferences.isEnabled()) {
            initialiseNamedays();
        }
    }

    private void initialiseNamedays() {
        List<ContactEvent> namedays = calculator.loadDeviceStaticNamedays();
        storeNamedaysToDisk(namedays);
    }

    private void storeNamedaysToDisk(List<ContactEvent> namedays) {
        ContentValues[] values = eventMarshaller.marshall(namedays);
        perister.insertAnnualEvents(values);
    }

}

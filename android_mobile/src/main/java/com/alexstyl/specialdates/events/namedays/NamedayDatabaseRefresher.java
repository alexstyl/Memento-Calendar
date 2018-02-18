package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator;

import java.util.List;

public class NamedayDatabaseRefresher {

    private final NamedayUserSettings namedayUserSettings;
    private final PeopleEventsPersister perister;
    private final PeopleNamedaysCalculator calculator;

    public NamedayDatabaseRefresher(NamedayUserSettings namedayPreferences,
                                    PeopleEventsPersister databaseProvider,
                                    PeopleNamedaysCalculator calculator) {
        this.namedayUserSettings = namedayPreferences;
        this.perister = databaseProvider;
        this.calculator = calculator;
    }

    public void refreshNamedaysIfEnabled() {
        perister.deleteAllNamedays();
        if (namedayUserSettings.isEnabled()) {
            initialiseNamedays();
        }
    }

    private void initialiseNamedays() {
        List<ContactEvent> namedays = calculator.loadDeviceStaticNamedays();
        storeNamedaysToDisk(namedays);
    }

    private void storeNamedaysToDisk(List<ContactEvent> namedays) {
        perister.insertAnnualEvents(namedays);
    }

}

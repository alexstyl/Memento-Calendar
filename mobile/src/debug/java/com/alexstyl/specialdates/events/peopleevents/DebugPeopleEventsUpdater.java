package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;

import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;

public class DebugPeopleEventsUpdater {

    private final BirthdayDatabaseRefresher birthdayDatabaseRefresher;
    private NamedayDatabaseRefresher namedayDatabaseRefresher;

    public static DebugPeopleEventsUpdater newInstance(Context context) {
        return new DebugPeopleEventsUpdater(BirthdayDatabaseRefresher.newInstance(context), NamedayDatabaseRefresher.newInstance(context));
    }

    public DebugPeopleEventsUpdater(BirthdayDatabaseRefresher birthdayDatabaseRefresher, NamedayDatabaseRefresher namedayDatabaseRefresher) {
        this.birthdayDatabaseRefresher = birthdayDatabaseRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
    }

    public void refresh() {
        birthdayDatabaseRefresher.refreshBirthdays();
        namedayDatabaseRefresher.refreshNamedaysIfEnabled();
    }
}

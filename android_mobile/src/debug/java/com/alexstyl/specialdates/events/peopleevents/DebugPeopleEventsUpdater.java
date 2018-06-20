package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;

public final class DebugPeopleEventsUpdater {

    private final PeopleEventsStaticEventsRefresher peopleEventsStaticEventsRefresher;
    private final NamedayDatabaseRefresher namedayDatabaseRefresher;

    public DebugPeopleEventsUpdater(PeopleEventsStaticEventsRefresher peopleEventsStaticEventsRefresher,
                                    NamedayDatabaseRefresher namedayDatabaseRefresher) {
        this.peopleEventsStaticEventsRefresher = peopleEventsStaticEventsRefresher;
        this.namedayDatabaseRefresher = namedayDatabaseRefresher;
    }

    public void refresh() {
        peopleEventsStaticEventsRefresher.rebuildEvents();
        namedayDatabaseRefresher.refreshNamedaysIfEnabled();
    }
}

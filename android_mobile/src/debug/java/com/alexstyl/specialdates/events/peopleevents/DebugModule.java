package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugModule {

    @Provides
    DebugPeopleEventsUpdater debugPeopleEventsUpdater(PeopleEventsStaticEventsRefresher peopleEventsStaticEventsRefresher,
                                                      NamedayDatabaseRefresher namedayDatabaseRefresher) {
        return new DebugPeopleEventsUpdater(peopleEventsStaticEventsRefresher, namedayDatabaseRefresher);
    }
}

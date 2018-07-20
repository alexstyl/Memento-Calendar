package com.alexstyl.specialdates.debug;

import android.content.Context;

import com.alexstyl.specialdates.dailyreminder.DailyReminderDebugPreferences;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.peopleevents.DebugPeopleEventsUpdater;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsStaticEventsRefresher;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugModule {

    @Provides
    DebugPeopleEventsUpdater debugPeopleEventsUpdater(PeopleEventsStaticEventsRefresher peopleEventsStaticEventsRefresher,
                                                      NamedayDatabaseRefresher namedayDatabaseRefresher) {
        return new DebugPeopleEventsUpdater(peopleEventsStaticEventsRefresher, namedayDatabaseRefresher);
    }

    @Provides
    DailyReminderDebugPreferences debugPreferences(Context context) {
        return DailyReminderDebugPreferences.Companion.newInstance(context);
    }
}

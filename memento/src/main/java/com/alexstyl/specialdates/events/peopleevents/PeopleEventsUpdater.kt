package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher

class PeopleEventsUpdater(private val peopleEventsStaticEventsRefresher: PeopleEventsStaticEventsRefresher,
                          private val namedayDatabaseRefresher: NamedayDatabaseRefresher) {

    fun updateEvents() {
        peopleEventsStaticEventsRefresher.rebuildEvents()
        namedayDatabaseRefresher.refreshNamedaysIfEnabled()
    }
}

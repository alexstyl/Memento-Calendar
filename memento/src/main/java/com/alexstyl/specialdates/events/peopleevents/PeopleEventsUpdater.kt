package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher

open class PeopleEventsUpdater(private val peopleEventsStaticEventsRefresher: PeopleEventsStaticEventsRefresher,
                               private val namedayDatabaseRefresher: NamedayDatabaseRefresher) {

    open fun updateEvents() {
        peopleEventsStaticEventsRefresher.rebuildEvents()
        namedayDatabaseRefresher.refreshNamedaysIfEnabled()
    }
}

package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher
import io.reactivex.Observable

open class PeopleEventsUpdater(private val peopleEventsStaticEventsRefresher: PeopleEventsStaticEventsRefresher,
                               private val namedayDatabaseRefresher: NamedayDatabaseRefresher) {

    open fun updateEvents() = Observable.fromCallable {
        peopleEventsStaticEventsRefresher.rebuildEvents()
        namedayDatabaseRefresher.refreshNamedaysIfEnabled()
    }!!
}

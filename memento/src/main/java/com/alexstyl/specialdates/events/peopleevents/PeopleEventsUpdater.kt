package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher
import io.reactivex.Observable
import io.reactivex.Scheduler

open class PeopleEventsUpdater(private val peopleEventsStaticEventsRefresher: PeopleEventsStaticEventsRefresher,
                               private val namedayDatabaseRefresher: NamedayDatabaseRefresher,
                               private val viewRefresher: UpcomingEventsViewRefresher,
                               private val workScheduler: Scheduler,
                               private val resultScheduler: Scheduler) {

    open fun updateEvents() = Observable.fromCallable {
        peopleEventsStaticEventsRefresher.rebuildEvents()
        namedayDatabaseRefresher.refreshNamedaysIfEnabled()
    }
            .subscribeOn(workScheduler)
            .observeOn(resultScheduler)
            .map {
                viewRefresher.refreshViews()
            }!!
}

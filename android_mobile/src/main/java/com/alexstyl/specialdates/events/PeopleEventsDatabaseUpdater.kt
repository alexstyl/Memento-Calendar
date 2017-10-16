package com.alexstyl.specialdates.events

import com.alexstyl.specialdates.EventsUpdateTrigger
import com.alexstyl.specialdates.events.peopleevents.DeviceEventsDatabaseRefresher
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject

/**
 * Class that monitors whether the events need to be refreshed due to some user action.
 *
 * This action can be internal to the app (such as the user wants to see the namedays of a different locale)
 * or external (such as a change in the device database)
 */
class PeopleEventsDatabaseUpdater(private val dbRefresher: DeviceEventsDatabaseRefresher,
                                  private val uiRefresher: PeopleEventsViewRefresher,
                                  private val workScheduler: Scheduler,
                                  private val resultScheduler: Scheduler) {

    private val subject = PublishSubject.create<Int>()

    private val onEventUpdateTriggered = EventsUpdateTrigger.Callback {
        subject.onNext(1)
    }

    fun startMonitoring(triggers: List<EventsUpdateTrigger>) {
        subject
                .observeOn(workScheduler)
                .map { dbRefresher.rebuildEvents() }
                .subscribeOn(resultScheduler)
                .subscribe {
                    uiRefresher.updateAllViews()
                }

        for (trigger in triggers) {
            trigger.startObserving(onEventUpdateTriggered)
        }
    }
}

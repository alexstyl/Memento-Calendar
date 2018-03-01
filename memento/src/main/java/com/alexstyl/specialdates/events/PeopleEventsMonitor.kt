package com.alexstyl.specialdates.events

import com.alexstyl.specialdates.EventsUpdateTrigger
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Class that monitors whether the events need to be refreshed due to some user action.
 *
 * This action can be internal to the app (such as the user wants to see the namedays of a different locale)
 * or external (such as a change in the device database)
 */
class PeopleEventsMonitor(private val peopleEventsUpdater: PeopleEventsUpdater,
                          private val uiRefresher: PeopleEventsViewRefresher,
                          private val workScheduler: Scheduler,
                          private val resultScheduler: Scheduler) {

    private val subject = PublishSubject.create<Int>()
    private var disposable: Disposable? = null

    private val onEventUpdateTriggered = EventsUpdateTrigger.Callback {
        updateEvents()
    }

    fun startMonitoring(triggers: List<EventsUpdateTrigger>) {
        disposable = subject
                .observeOn(workScheduler)
                .map {
                    peopleEventsUpdater.updateEvents()
                }
                .observeOn(resultScheduler)
                .subscribe {
                    uiRefresher.refreshViews()
                }

        for (trigger in triggers) {
            trigger.startObserving(onEventUpdateTriggered)
        }
    }


    fun updateEvents() {
        subject.onNext(1)
    }
}

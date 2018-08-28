package com.alexstyl.specialdates.events

import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class SettingsPresenter(private val peopleEventsUpdater: PeopleEventsUpdater,
                        private val uiRefresher: UpcomingEventsViewRefresher,
                        private val workScheduler: Scheduler) {

    private val subject = PublishSubject.create<Int>()
    private var disposable: Disposable? = null


    fun startPresenting() {
        subject
                .flatMap { peopleEventsUpdater.updateEvents() }
                .subscribeOn(workScheduler)
                .subscribe()
    }

    fun stopMonitoring() {
        disposable?.dispose()
    }

    fun refreshPeopleEvents() {
        subject.onNext(1)
    }

    fun updateEventOptions() {
        uiRefresher.refreshViews()
    }
}

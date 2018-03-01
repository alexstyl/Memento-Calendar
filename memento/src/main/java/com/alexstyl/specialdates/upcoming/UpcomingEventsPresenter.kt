package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsSettings
import io.reactivex.Scheduler

class UpcomingEventsPresenter(private val firstDay: Date,
                              private val permissions: MementoPermissions,
                              private val provider: UpcomingEventsProvider,
                              private val upcomingEventsSettings: UpcomingEventsSettings,
                              private val peopleEventsUpdater: PeopleEventsUpdater,
                              private val workScheduler: Scheduler,
                              private val resultScheduler: Scheduler) {

    companion object {
        private const val TRIGGER = 1
    }

    private val subject = PublishSubject.create<Int>()
    private var disposable: Disposable? = null

    fun startPresentingInto(view: UpcomingListMVPView) {
        disposable =
                subject
                        .doOnSubscribe {
                            if (view.isEmpty) {
                                view.showLoading()
                            }
                        }
                        .observeOn(workScheduler)
                        .map {
                            if (!upcomingEventsSettings.hasBeenInitialised()) {
                                peopleEventsUpdater.updateEvents()
                                upcomingEventsSettings.markEventsAsInitialised()
                            }
                            provider.calculateEventsBetween(TimePeriod.aYearFrom(firstDay))
                        }
                        .observeOn(resultScheduler)
                        .onErrorReturn { emptyList() }
                        .subscribe { upcomingRowViewModels ->
                            view.display(upcomingRowViewModels)
                        }
        if (permissions.canReadAndWriteContacts()) {
            refreshEvents()
        } else {
            view.display(emptyList())
        }
    }


    fun refreshEvents() {
        subject.onNext(TRIGGER)
    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}

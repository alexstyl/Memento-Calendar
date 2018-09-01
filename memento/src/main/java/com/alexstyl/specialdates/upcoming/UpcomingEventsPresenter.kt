package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.UpcomingEventsView
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsRefresher
import com.alexstyl.specialdates.permissions.MementoPermissions
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class UpcomingEventsPresenter(private val firstDay: Date,
                              private val permissions: MementoPermissions,
                              private val providerUpcoming: UpcomingEventsProvider,
                              private val refresher: UpcomingEventsRefresher,
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
                        .doOnSubscribe { _ ->
                            if (view.isShowingNoEvents) {
                                view.showLoading()
                            }
                        }
                        .observeOn(workScheduler)
                        .map { _ ->
                            val timePeriod = TimePeriod.aYearFrom(firstDay)
                            providerUpcoming.calculateEventsBetween(timePeriod)
                        }
                        .observeOn(resultScheduler)
                        .onErrorReturn { error ->
                            error.printStackTrace()
                            emptyList()
                        }
                        .subscribe { upcomingRowViewModels ->
                            view.display(upcomingRowViewModels)
                        }
        if (permissions.canReadAndWriteContacts()) {
            refreshEvents()
        }
        refresher.addEventsView(upcomingEventsView)
    }

    fun refreshEvents() {
        subject.onNext(TRIGGER)
    }


    fun stopPresenting() {
        disposable?.dispose()
        refresher.removeView(upcomingEventsView)
    }

    private val upcomingEventsView = object : UpcomingEventsView {
        override fun reloadUpcomingEventsView() {
            subject.onNext(TRIGGER)
        }
    }
}

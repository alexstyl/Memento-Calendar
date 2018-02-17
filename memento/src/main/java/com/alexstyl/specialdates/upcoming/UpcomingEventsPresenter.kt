package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.permissions.MementoPermissionsChecker
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class UpcomingEventsPresenter(private val firstDay: Date,
                              private val permissions: MementoPermissionsChecker,
                              private val provider: IUpcomingEventsProvider,
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
                        .map { provider.calculateEventsBetween(TimePeriod.aYearFrom(firstDay)) }
                        .observeOn(resultScheduler)
                        .subscribe { upcomingRowViewModels ->
                            view.display(upcomingRowViewModels)
                        }
        if (permissions.canReadAndWriteContacts()) {
            refreshEvents()
        } else {
            view.askForContactPermission()
        }
    }


    fun refreshEvents() {
        subject.onNext(TRIGGER)
    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}

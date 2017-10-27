package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.permissions.ContactPermissionRequest
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

internal class UpcomingEventsPresenter(private val firstDay: Date,
                                       private val permissions: ContactPermissionRequest,
                                       private val provider: IUpcomingEventsProvider,
                                       private val workScheduler: Scheduler,
                                       private val resultScheduler: Scheduler) {

    private val TRIGGER = 1
    private val subject = PublishSubject.create<Int>()
    private var disposable: Disposable? = null

    fun startPresentingInto(view: UpcomingListMVPView) {
        disposable =
                subject
                        .doOnSubscribe {
                            if (view.isEmpty) { view.showLoading() }
                        }
                        .observeOn(workScheduler)
                        .map { provider.calculateEventsBetween(TimePeriod.aYearFrom(firstDay)) }
                        .observeOn(resultScheduler)
                        .subscribe { upcomingRowViewModels ->
                            view.display(upcomingRowViewModels)
                        }
        if (permissions.permissionIsPresent()) {
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

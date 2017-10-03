package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date

import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.donate.DonateMonitor
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver
import com.alexstyl.specialdates.permissions.ContactPermissionRequest
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

internal class UpcomingEventsPresenter(private val firstDay: Date,
                                       private val permissions: ContactPermissionRequest,
                                       private val provider: IUpcomingEventsProvider,
                                       private val settingsMonitorUpcoming: UpcomingEventsSettingsMonitor,
                                       private val peopleEventsObserver: PeopleEventsObserver,
                                       private val workScheduler: Scheduler,
                                       private val resultScheduler: Scheduler) {

    private val TRIGGER = 1
    private val subject = PublishSubject.create<Int>()
    private val donateListener = DonateMonitor.DonateMonitorListener {
        refreshEvents()
    }
    private var disposable: Disposable? = null

    fun startPresentingInto(view: UpcomingListMVPView) {
        disposable =
                subject
                        .doOnSubscribe { view.showLoading() }
                        .observeOn(workScheduler)
                        .map { provider.calculateEventsBetween(TimePeriod.aYearFrom(firstDay)) }
                        .observeOn(resultScheduler)
                        .subscribe {
                            upcomingRowViewModels ->
                            view.display(upcomingRowViewModels)
                        }
        if (permissions.permissionIsPresent()) {
            setupContentUpdatedListeners()
            refreshEvents()
        } else {
            view.askForContactPermission()
        }
    }

    private fun setupContentUpdatedListeners() {
        settingsMonitorUpcoming.register {
            refreshEvents()
        }
        peopleEventsObserver.startObserving {
            refreshEvents()
        }
        DonateMonitor.getInstance().addListener { donateListener }
    }

    fun refreshEvents() {
        subject.onNext(TRIGGER)
    }

    fun stopPresenting() {
        disposable?.dispose()
        settingsMonitorUpcoming.unregister()
        peopleEventsObserver.stopObserving()
        DonateMonitor.getInstance().removeListener(donateListener)
    }
}

package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date

import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.donate.DonateMonitor
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver
import com.alexstyl.specialdates.permissions.ContactPermissionRequest
import com.alexstyl.specialdates.settings.EventsSettingsMonitor
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsProvider
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class UpcomingEventsPresenter(private val view: UpcomingListMVPView,
                              private val permissions: ContactPermissionRequest,
                              private val provider: UpcomingEventsProvider,
                              private val settingsMonitor: EventsSettingsMonitor,
                              private val peopleEventsObserver: PeopleEventsObserver,
                              private val workScheduler: Scheduler,
                              private val resultScheduler: Scheduler) {

    private val TRIGGER = 1
    private var disposable: Disposable? = null
    private val subject = PublishSubject.create<Int>()
    private val donateListener = DonateMonitor.DonateMonitorListener {
        refreshEvents()
    }

    fun startPresenting(firstDay: Date) {
        if (permissions.permissionIsPresent()) {
            disposable =
                    subject
                            .doOnSubscribe { view.showLoading() }
                            .observeOn(workScheduler)
                            .map<List<UpcomingRowViewModel>> {
                                provider.calculateEventsBetween(TimePeriod.aYearFrom(firstDay))
                            }
                            .observeOn(resultScheduler)
                            .subscribe {
                                upcomingRowViewModels ->
                                view.display(upcomingRowViewModels)
                            }
            refreshEvents()

            settingsMonitor.register {
                refreshEventsAsNew()
            }
            peopleEventsObserver.startObserving { refreshEvents() }
            DonateMonitor.getInstance().addListener { donateListener }
        } else {
            view.askForContactPermission()
        }
    }

    fun refreshEvents() {
        subject.onNext(TRIGGER)
    }

    private fun refreshEventsAsNew() {
        view.showLoading()
        view.showFirstEvent()
        refreshEvents()
    }


    fun stopPresenting() {
        if (disposable != null) {
            disposable!!.dispose()
        }
        settingsMonitor.unregister()
        peopleEventsObserver.stopObserving()
        DonateMonitor.getInstance().removeListener(donateListener)
    }

}

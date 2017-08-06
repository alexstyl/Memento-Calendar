package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
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
                              private val monitor: EventsSettingsMonitor,
                              private val observer: PeopleEventsObserver,
                              private val workScheduler: Scheduler,
                              private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null
    private val subject = PublishSubject.create<Date>()

    fun startPresenting(firstDay: Date) {
        if (permissions.permissionIsPresent()) {
            disposable =
                    subject
                            .doOnSubscribe { view.showLoading() }
                            .observeOn(workScheduler)
                            .map<List<UpcomingRowViewModel>> {
                                provider.calculateEventsBetween(TimePeriod.aYearFrom(it))
                            }
                            .observeOn(resultScheduler)
                            .subscribe {
                                upcomingRowViewModels ->
                                view.display(upcomingRowViewModels)
                            }
            refreshEvents(firstDay)

            monitor.register { refreshEvents(firstDay) }
            observer.startObserving { refreshEvents(firstDay) }

        } else {
            view.askForContactPermission()
        }
    }

    fun refreshEvents(firstDay: Date) {
        subject.onNext(firstDay)
    }

    fun stopPresenting() {
        if (disposable != null) {
            disposable!!.dispose()
        }
        monitor.unregister()
        observer.stopObserving()
    }

}

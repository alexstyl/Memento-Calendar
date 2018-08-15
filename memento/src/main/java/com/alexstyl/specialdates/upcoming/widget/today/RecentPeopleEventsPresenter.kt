package com.alexstyl.specialdates.upcoming.widget.today

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.permissions.MementoPermissions
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class RecentPeopleEventsPresenter(private val eventsProvider: PeopleEventsProvider,
                                  private val permissions: MementoPermissions,
                                  private val workScheduler: Scheduler,
                                  private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingInto(view: RecentPeopleEventsView) {
        if (permissions.canReadContacts()) {
            disposable =
                    Observable.fromCallable {
                        val today = todaysDate()
                        val date = eventsProvider.findClosestEventDateOnOrAfter(today)
                        if (date != null) {
                            val contactEventsOnADate = eventsProvider.fetchEventsOn(date)
                            Optional(contactEventsOnADate)
                        } else {
                            Optional.absent()
                        }
                    }
                            .subscribeOn(workScheduler)
                            .observeOn(resultScheduler)
                            .subscribe { viewModels ->
                                if (viewModels.isPresent) {
                                    view.onNextDateLoaded(viewModels.get())
                                } else {
                                    view.onNoEventsFound()
                                }
                            }
        } else {
            view.askForContactPermission()
        }
    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}

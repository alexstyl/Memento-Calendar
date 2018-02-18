package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class PeoplePresenter(
        private val peopleEventsProvider: PeopleEventsProvider,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingInto(view: PeopleView) {
        disposable =
                Observable.fromCallable {
                    peopleEventsProvider.getContactEventsFor(TimePeriod.aYearFromNow())
                }
                        .map { contacts ->
                            val viewModels = arrayListOf<PeopleRowViewModel>()
                            val contactIDs = HashSet<Long>()

                            viewModels.add(FacebookViewModel())

                            contacts.sortedWith(compareBy({ it.contact.displayName.toString() }))
                            contacts.forEach { contactEvent ->
                                val contact = contactEvent.contact
                                if (!contactIDs.contains(contact.contactID)) {
                                    viewModels.add(PersonViewModel(contact,
                                            contact.displayName.toString(),
                                            contact.imagePath,
                                            contact.contactID,
                                            contact.source))
                                    contactIDs.add(contact.contactID)
                                }
                            }
                            viewModels
                        }
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { viewModels ->
                            view.displayPeople(viewModels)
                        }


    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}



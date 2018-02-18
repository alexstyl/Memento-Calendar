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
                            val viewModels = arrayListOf<PeopleViewModel>()
                            val contactIDs = HashSet<Long>()
                            contacts.forEach { contactEvent ->
                                val contact = contactEvent.contact
                                if (!contactIDs.contains(contact.contactID)) {
                                    viewModels.add(PeopleViewModel(contact,
                                            contact.displayName.toString(),
                                            contact.imagePath,
                                            contact.contactID,
                                            contact.source))
                                    contactIDs.add(contact.contactID)
                                }
                            }
                            viewModels.sortedWith(compareBy({ it.personName }))
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



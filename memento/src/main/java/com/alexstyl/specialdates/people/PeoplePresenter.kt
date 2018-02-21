package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

class PeoplePresenter(
        private val peopleEventsProvider: PeopleEventsProvider,
        private val viewModelFactory: PeopleViewModelFactory,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private var disposable: Disposable? = null

    fun startPresentingInto(view: PeopleView) {
        disposable =
                Observable.fromCallable {
                    peopleEventsProvider.getContactEventsFor(TimePeriod.aYearFromNow())
                }
                        .doOnSubscribe {
                            view.showLoading()
                        }
                        .map { contacts ->
                            val viewModels = arrayListOf<PeopleRowViewModel>()
                            val contactIDs = HashSet<Long>()

                            viewModels.add(viewModelFactory.facebookViewModel())

                            if (contacts.isEmpty()) {
                                viewModels.add(viewModelFactory.noContactsViewModel())
                            } else {
                                val mutableList = contacts.toMutableList()
                                mutableList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.contact.displayName.toString() }))
                                mutableList.forEach { contactEvent ->
                                    val contact = contactEvent.contact
                                    if (!contactIDs.contains(contact.contactID)) {
                                        viewModels.add(viewModelFactory.personViewModel(contact))
                                        contactIDs.add(contact.contactID)
                                    }
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




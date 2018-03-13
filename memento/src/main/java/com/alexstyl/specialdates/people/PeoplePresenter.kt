package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.permissions.MementoPermissions
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class PeoplePresenter(
        private val permissions: MementoPermissions,
        private val peopleEventsProvider: PeopleEventsProvider,
        private val viewModelFactory: PeopleViewModelFactory,
        private val errorTracker: CrashAndErrorTracker,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    companion object {
        private const val TRIGGER = 1
    }

    private var disposable: Disposable? = null
    private val subject = PublishSubject.create<Int>()

    fun startPresentingInto(view: PeopleView) {
        disposable =
                subject
                        .doOnSubscribe { _ ->
                            view.showLoading()
                        }
                        .map { _ ->
                            peopleEventsProvider.fetchEventsBetween(TimePeriod.aYearFromNow())
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
                        .onErrorReturn { error ->
                            errorTracker.track(error)
                            arrayListOf()
                        }
                        .subscribe { viewModels ->
                            view.displayPeople(viewModels)
                        }

        if (permissions.canReadAndWriteContacts()) {
            refreshData()
        }
    }

    fun refreshData() {
        subject.onNext(TRIGGER)
    }

    fun stopPresenting() {
        disposable?.dispose()
    }
}




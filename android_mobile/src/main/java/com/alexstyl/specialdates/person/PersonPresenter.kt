package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.service.PeopleEventsProvider
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

internal class PersonPresenter(private val personView: PersonView,
                               private val provider: PeopleEventsProvider,
                               private val workScheduler: Scheduler,
                               private val resultScheduler: Scheduler,
                               private val toViewModel: PersonDetailsViewModelFactory) {

    private val disposable = CompositeDisposable()


    fun startPresenting(contact: Contact) {
//        val viewModelObservable: Observable<PersonDetailsViewModel> =
//                Observable.combineLatest<Contact, ContactEvent?, PersonDetailsViewModel>(
//                        Observable.just(contact),
//                        provider.getContactEventsFor(contact)
//                                .map { keepOnlyBirthday(it) },
//                        toViewModel
//                )
//
//        disposable.add(
//                viewModelObservable
//                        .observeOn(resultScheduler)
//                        .subscribeOn(workScheduler)
//                        .subscribe({ personView.displayInfoFor(it) })
//        )

    }

    private fun keepOnlyBirthday(it: MutableList<ContactEvent>) = it.find { it.type == StandardEventType.BIRTHDAY }

    fun stopPresenting() {
        disposable.dispose()
    }
}

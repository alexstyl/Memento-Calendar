package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.service.PeopleEventsProvider
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable

internal class PersonPresenter(private val personView: PersonView,
                               private val provider: PeopleEventsProvider,
                               private val workScheduler: Scheduler,
                               private val resultScheduler: Scheduler,
                               private val toPersonViewModel: PersonDetailsViewModelFactory,
                               private val toEventViewModel: EventViewModelFactory) {


    private var disposable: Disposable? = null

    fun startPresenting(contact: Contact) {
        disposable =
                provider.getContactEventsFor(contact)
                        .map { toPersonViewModel(contact, it.keepOnlyBirthday()) }
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe({
                            disposable?.dispose()
                            personView.displayInfoFor(it)
                            presentEventsFor(contact)
                        })
    }

    private fun presentEventsFor(contact: Contact) {
        disposable =
                provider.getContactEventsFor(contact)
                        .map { toEventViewModel(it) }
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe({
                            personView.displayContactMethods(PersonContactViewModel(it, ArrayList(), ArrayList()))
                        })
    }

    private fun List<ContactEvent>.keepOnlyBirthday() = find { it.type == StandardEventType.BIRTHDAY }

    fun stopPresenting() {
        disposable?.dispose()
    }
}


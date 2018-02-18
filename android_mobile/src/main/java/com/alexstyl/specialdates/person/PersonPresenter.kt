package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3

internal class PersonPresenter(private val personView: PersonView,
                               private val provider: PeopleEventsProvider,
                               private val personCallProvider: PersonCallProvider,
                               private val workScheduler: Scheduler,
                               private val resultScheduler: Scheduler,
                               private val toPersonViewModel: PersonDetailsViewModelFactory,
                               private val toEventViewModel: EventViewModelFactory,
                               private val persister: PeopleEventsPersister) {


    private var disposable = CompositeDisposable()

    private var contactOptional = Optional.absent<Contact>()

    fun startPresenting(contact: Contact) {
        contactOptional = Optional(contact)

        disposable.add(
                provider.getContactEventsFor(contact)
                        .map {

                            val isVisible = persister.getVisibilityFor(contact)
                            toPersonViewModel(contact, it.keepOnlyBirthday(), isVisible)
                        }
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe({
                            personView.displayPersonInfo(it)
                        }))


        disposable.add(
                Observable.combineLatest(
                        eventsOf(contact),
                        personCallProvider.getCallsFor(contact),
                        personCallProvider.getMessagesFor(contact),
                        Function3
                        <List<ContactEventViewModel>, List<ContactActionViewModel>, List<ContactActionViewModel>, PersonAvailableActionsViewModel>
                        { t1, t2, t3 ->
                            PersonAvailableActionsViewModel(t1, t2, t3)
                        }
                )
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe({
                            personView.displayAvailableActions(it)
                        }))
    }


    private fun eventsOf(contact: Contact) = provider.getContactEventsFor(contact)
            .map { toEventViewModel(it) }

    private fun List<ContactEvent>.keepOnlyBirthday() = find { it.type == StandardEventType.BIRTHDAY }


    fun stopPresenting() {
        disposable.clear()
    }

    fun hideContact() {
        if (!contactOptional.isPresent) {
            return
        }
        disposable.add(Observable.fromCallable {
            persister.markContactAsHidden(contactOptional.get())
        }.observeOn(resultScheduler)
                .subscribeOn(workScheduler)
                .subscribe {
                    personView.showPersonAsHidden()
                })

    }

    fun showContact() {
        if (!contactOptional.isPresent) {
            return
        }
        disposable.add(
                Observable.fromCallable {
                    persister.markContactAsVisible(contactOptional.get())
                }
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe {
                            personView.showPersonAsVisible()
                        })
    }
}


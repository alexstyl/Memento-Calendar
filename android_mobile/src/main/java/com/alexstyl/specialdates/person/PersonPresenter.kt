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

class PersonPresenter(private val provider: PeopleEventsProvider,
                      private val personActionsProvider: PersonActionsProvider,
                      private val toPersonViewModel: PersonDetailsViewModelFactory,
                      private val toEventViewModel: EventViewModelFactory,
                      private val persister: PeopleEventsPersister,
                      private val workScheduler: Scheduler,
                      private val resultScheduler: Scheduler) {


    private var disposable = CompositeDisposable()

    private var contactOptional = Optional.absent<Contact>()

    fun startPresentingInto(personView: PersonView, contact: Contact, executor: ContactActions) {
        contactOptional = Optional(contact)

        disposable.add(
                getEventsFor(contact)
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
                        personActionsProvider.getCallsFor(contact, executor),
                        personActionsProvider.getMessagesFor(contact, executor),
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

    private fun getEventsFor(contact: Contact) = Observable.fromCallable { provider.fetchEventsFor(contact) }


    private fun eventsOf(contact: Contact) = getEventsFor(contact)
            .map { toEventViewModel(it) }

    private fun List<ContactEvent>.keepOnlyBirthday() = find { it.type == StandardEventType.BIRTHDAY }


    fun stopPresenting() {
        disposable.clear()
    }

    fun hideContact(personView: PersonView) {
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

    fun showContact(personView: PersonView) {
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


package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.service.PeopleEventsProvider
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
                               private val toEventViewModel: EventViewModelFactory) {


    private var disposable = CompositeDisposable()

    fun startPresenting(contact: Contact) {

        disposable.add(
                provider.getContactEventsFor(contact)
                        .map { toPersonViewModel(contact, it.keepOnlyBirthday()) }
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe({
                            personView.displayPersonInfo(it)
                        }))


        disposable.add(

                Observable.combineLatest(
                        eventsOf(contact),
                        personCallProvider.getCallsFor(contact),
                        personCallProvider.getMessagesFor(contact),
                        Function3<List<ContactEventViewModel>, List<ContactActionViewModel>, List<ContactActionViewModel>, PersonAvailableActionsViewModel>
                        {
                            t1, t2, t3 ->
                            PersonAvailableActionsViewModel(t1, t2, t3)
                        }
                )
                        .observeOn(resultScheduler)
                        .subscribeOn(workScheduler)
                        .subscribe({
                            personView.displayAvailableActions(it)
                        }))
    }


    private fun eventsOf(contact: Contact) = provider.getContactEventsFor(contact)
            .map { toEventViewModel(it) }

    private fun List<ContactEvent>.keepOnlyBirthday() = find { it.type == StandardEventType.BIRTHDAY }

    fun stopPresenting() {
        disposable.dispose()
    }
}


package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.Event
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject
import java.net.URI


class AddContactEventsPresenter(private val analytics: Analytics,
                                private val contactOperations: ContactOperations,
                                private val messageDisplayer: MessageDisplayer,
                                private val operationsExecutor: ContactOperationsExecutor,
                                private val strings: Strings,
                                private val peopleEventsProvider: PeopleEventsProvider,
                                private val factory: AddEventContactEventViewModelFactory,

                                private val workScheduler: Scheduler,
                                private val resultScheduler: Scheduler) {


    private val imageSubject = ReplaySubject.create<URI>()
    private val contactSubject = ReplaySubject.create<Optional<Contact>>()
    private val contactNameSubject = ReplaySubject.create<String>()
    private val eventsSubject = ReplaySubject.create<List<AddEventContactEventViewModel>>()

    val events: List<AddEventContactEventViewModel>
        get() = eventsSubject.value.toList()

    private val disposable = CompositeDisposable()
    var isHoldingModifiedData = false


    fun onNameModified(text: String) {
        if (contactSubject.hasValue()) {
            contactSubject.onNext(Optional.absent())
        }
        contactNameSubject.onNext(text)
        isHoldingModifiedData = true
    }

    fun isDisplayingAvatar(): Boolean {
        return imageSubject.hasValue()
    }

    private fun List<AddEventContactEventViewModel>.toEvent(): Collection<Event> {
        return this.mapNotNull { viewModel ->
            if (viewModel.date.isPresent) {
                Event(viewModel.eventType, viewModel.date.get())
            } else {
                null
            }
        }
    }


    fun startPresentingInto(view: AddEventView) {
        disposable.addAll(
                eventsSubject.subscribe { viewModels ->
                    view.display(viewModels)
                },
                contactSubject.subscribe { contact ->
                    if (contact.isPresent) {
                        view.displayContact(contact.get())
                    } else {
                        view.removeAvatar()
                    }
                }
        )
        eventsSubject.onNext(startingViewModels())
    }

    private fun startingViewModels() =
            StandardEventType.values().filter {
                it != StandardEventType.NAMEDAY && it != StandardEventType.CUSTOM
            }.map {
                factory.createAddEventViewModelFor(it)
            }

    fun onContactSelected(contact: Contact) {
        analytics.trackContactSelected()
        contactSubject.onNext(Optional(contact))
        isHoldingModifiedData = false

        disposable.add(
                Observable.fromCallable { peopleEventsProvider.fetchEventsFor(contact) }
                        .flatMapIterable { it }
                        .filter { isNotEditableEvent(it.type) }
                        .map { event ->
                            factory.createViewModel(event)
                        }
                        .toList()
                        .map { viewModels ->
                            val contactEvents = viewModels.associateBy({ it.eventType }, { it })
                            StandardEventType.values().forEach { eventType ->
                                if (isNotEditableEvent(eventType) && !contactEvents.containsKey(eventType)) {
                                    viewModels.add(factory.createAddEventViewModelFor(eventType))
                                }
                            }
                            viewModels.toList()
                        }
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { viewModels ->
                            eventsSubject.onNext(viewModels)
                            contactSubject.onNext(Optional(contact))
                        }
        )
    }

    private fun isNotEditableEvent(eventType: EventType) =
            eventType != StandardEventType.NAMEDAY && eventType != StandardEventType.CUSTOM

    fun onEventDatePicked(eventType: EventType, date: Date) {
        analytics.trackEventDatePicked(eventType)
        isHoldingModifiedData = true

        Observable.fromCallable {
            factory.createViewModelWith(eventType, date)
        }.map {
            val lastElement = eventsSubject.value.toMutableList()
            val find = lastElement.indexOfFirst { it.eventType == eventType }
            lastElement[find] = it
            lastElement.toList()
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    eventsSubject.onNext(viewModels)
                }
    }

    fun removeEvent(eventType: EventType) {
        analytics.trackEventRemoved(eventType)
        isHoldingModifiedData = true

        Observable.fromCallable {
            factory.createAddEventViewModelFor(eventType)
        }.map {
            val lastElement = eventsSubject.value.toMutableList()
            val find = lastElement.indexOfFirst { it.eventType == eventType }
            lastElement[find] = it
            lastElement.toList()
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    eventsSubject.onNext(viewModels)
                }
    }


    fun saveChanges() {
        if (!isHoldingModifiedData) {
            return
        }
        if (contactSubject.hasContact()) {
//            disposable.add(
//                    Observable.fromCallable {
//                        val operations = contactOperations
//                                .updateExistingContact(contactSubject.value.get())
//                                .withEvents(events.toEvent())
//                                .build()
//                        operationsExecutor.execute(operations)
//                    }.map {
//                        if (it) {
//                            analytics.trackContactUpdated()
//                            strings.contactUpdated()
//                        } else {
//                            strings.contactUpdateFailed()
//                        }
//                    }.observeOn(workScheduler)
//                            .subscribe {
//                                messageDisplayer.showMessage(it)
//                            }
//            )
        } else {

//            disposable.add(
//                    Observable.fromCallable {
//                        val builder = contactOperations.createNewContact(contactNameSubject.value)
//                                .withEvents(events.toEvent())
//                        operationsExecutor.execute(builder.build())
//                    }.map {
//                        if (it) {
//                            analytics.trackContactCreated()
//                            strings.contactAdded()
//                        } else {
//                            strings.contactAddedFailed()
//                        }
//                    }
//                            .observeOn(workScheduler)
//                            .subscribe {
//                                messageDisplayer.showMessage(it)
//                            }
//            )
        }
    }

    private fun ReplaySubject<Optional<Contact>>.hasContact(): Boolean = hasValue() && value.isPresent
}



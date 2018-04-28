package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.Event
import com.alexstyl.specialdates.events.peopleevents.CustomEventType
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject
import java.net.URI


class AddEventsPresenter(private val analytics: Analytics,
                         private val contactOperations: ContactOperations,
                         private val messageDisplayer: MessageDisplayer,
                         private val operationsExecutor: ContactOperationsExecutor,
                         private val strings: Strings,
                         private val peopleEventsProvider: PeopleEventsProvider,
                         private val factory: AddEventViewModelFactory,

                         private val workScheduler: Scheduler,
                         private val resultScheduler: Scheduler) {


    private val saveUISubject = ReplaySubject.create<Boolean>()
    private val eventsUISubject = ReplaySubject.create<List<AddEventContactEventViewModel>>()

    private val imageSubject = ReplaySubject.create<URI>()
    private val contactSubject = ReplaySubject.create<Optional<Contact>>()
    private val contactNameSubject = ReplaySubject.create<String>()

    val isHoldingModifiedData: Boolean
        get() = eventsUISubject.hasValue() || contactNameSubject.hasValue() || contactSubject.hasValue() || imageSubject.hasValue()

    val events: List<AddEventContactEventViewModel>
        get() = eventsUISubject.value.toList()

    private val disposable = CompositeDisposable()


    fun startPresentingInto(view: AddEventView) {
        disposable.addAll(
                eventsUISubject
                        .subscribe { viewModels ->
                            view.display(viewModels)
                        },
                contactSubject
                        .subscribe { contact ->
                            if (contact.isPresent) {
                                view.display(contact.get().imagePath)
                            } else {
                                view.clearAvatar()
                            }
                        },
                saveUISubject
                        .subscribe { enable ->
                            if (enable) {
                                view.allowSave()
                            } else {
                                view.preventSave()
                            }
                        }
        )
        eventsUISubject.onNext(emptyViewModels())
        contactSubject.onNext(Optional.absent())
        saveUISubject.onNext(false)
        contactNameSubject.onNext("")
    }

    fun presentName(name: String) {
        if (contactSubject.hasContact()) {
            throw UnsupportedOperationException("Changing names of contacts is not supported")
        }
        contactNameSubject.onNext(name)
        invalidateSave()
    }

    private fun invalidateSave() {
        if (contactSubject.hasContact()) {
            TODO("need to handle new contact")
        }

        if (contactNameSubject.value.isNotEmpty() && eventsUISubject.value.containsEventsWithDates()) {
            saveUISubject.onNext(true)
        } else {
            saveUISubject.onNext(false)
        }
    }

    fun isDisplayingAvatar(): Boolean {
        return imageSubject.hasValue()
    }


    private fun List<AddEventContactEventViewModel>.toEvent(): List<Event> {
        return this.mapNotNull { viewModel ->
            if (viewModel.date.isPresent) {
                Event(viewModel.eventType, viewModel.date.get())
            } else {
                null
            }
        }
    }

    private fun emptyViewModels() =
            StandardEventType.values().filter {
                it != StandardEventType.NAMEDAY && it != StandardEventType.CUSTOM
            }.map {
                factory.createAddEventViewModelFor(it)
            }

    fun presentContact(contact: Contact) {
        analytics.trackContactSelected()
        contactSubject.onNext(Optional(contact))
        saveUISubject.onNext(false)

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
                            eventsUISubject.onNext(viewModels)
                            contactSubject.onNext(Optional(contact))
                        }
        )
    }

    private fun isNotEditableEvent(eventType: EventType) =
            eventType != StandardEventType.NAMEDAY && eventType != StandardEventType.CUSTOM && eventType !is CustomEventType

    fun onEventDatePicked(eventType: EventType, date: Date) {
        analytics.trackEventDatePicked(eventType)

        Observable.fromCallable {
            factory.createViewModelWith(eventType, date)
        }.map {
            val lastElement = eventsUISubject.value.toMutableList()
            val find = lastElement.indexOfFirst { it.eventType == eventType }
            lastElement[find] = it
            lastElement.toList()
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    eventsUISubject.onNext(viewModels)

                    if (viewModels != emptyViewModels()) {
                        saveUISubject.onNext(true)
                    }
                }
    }

    fun removeEvent(eventType: EventType) {
        analytics.trackEventRemoved(eventType)

        Observable.fromCallable {
            factory.createAddEventViewModelFor(eventType)
        }.map {
            val lastElement = eventsUISubject.value.toMutableList()
            val find = lastElement.indexOfFirst { it.eventType == eventType }
            lastElement[find] = it
            lastElement.toList()
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    eventsUISubject.onNext(viewModels)
                }
    }


    fun saveChanges() {
        if (!saveUISubject.value) {
            throw IllegalStateException("Tried to save changes, while it was prohibited to do so.")
        }
        if (contactSubject.hasContact()) {
            disposable.add(
                    Observable.fromCallable {
                        val operations = contactOperations
                                .updateExistingContact(contactSubject.value.get())
                                .withEvents(events.toEvent())
                                .build()
                        operationsExecutor.execute(operations)
                    }.map {
                        if (it) {
                            analytics.trackContactUpdated()
                            strings.contactUpdated()
                        } else {
                            strings.contactUpdateFailed()
                        }
                    }.observeOn(workScheduler)
                            .subscribe {
                                messageDisplayer.showMessage(it)
                            }
            )
        } else {

            disposable.add(
                    Observable.fromCallable {
                        // TODO image
                        operationsExecutor.execute(
                                contactOperations
                                        .newContact(contactNameSubject.value)
                                        .withEvents(events.toEvent())
                                        .build()
                        )
                    }.map {
                        if (it) {
                            analytics.trackContactCreated()
                            strings.contactAdded()
                        } else {
                            strings.contactAddedFailed()
                        }
                    }
                            .observeOn(workScheduler)
                            .subscribe {
                                messageDisplayer.showMessage(it)
                            }
            )
        }
    }

    private fun ReplaySubject<Optional<Contact>>.hasContact(): Boolean = hasValue() && value.isPresent

    fun stopPresenting() {
        disposable.dispose()
    }

    private fun List<AddEventContactEventViewModel>.containsEventsWithDates(): Boolean {
        return any { it.date.isPresent }
    }

    fun removeContact() {
        if (contactSubject.hasContact()) {
            contactSubject.onNext(Optional.absent())
        }
        eventsUISubject.onNext(emptyViewModels())
    }
}




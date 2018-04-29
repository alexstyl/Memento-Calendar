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
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
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
                         private val updater: PeopleEventsUpdater,
                         private val workScheduler: Scheduler,
                         private val resultScheduler: Scheduler) {


    private val saveUISubject = ReplaySubject.create<Boolean>()
    private val eventsUISubject = ReplaySubject.create<List<AddEventContactEventViewModel>>()
    private var startingEvents = emptyViewModels()

    private val imageSubject = ReplaySubject.create<URI>()
    private val contactSubject = ReplaySubject.create<Optional<Contact>>()
    private val contactNameSubject = ReplaySubject.create<String>()


    val isHoldingModifiedData: Boolean
        get() =
            (!contactSubject.hasContact() && contactNameSubject.value.isNotEmpty())
                    || contactSubject.hasContact() && (eventsUISubject.value != startingEvents)
                    || imageSubject.hasValue()

    private val eventViewModels: List<AddEventContactEventViewModel>
        get() = eventsUISubject.value.toList()

    private val disposable = CompositeDisposable()


    fun startPresentingInto(view: AddEventView) {
        disposable.addAll(
                saveUISubject
                        .subscribe { enable ->
                            if (enable) {
                                view.allowSave()
                            } else {
                                view.preventSave()
                            }
                        },
                eventsUISubject
                        .subscribe { viewModels ->
                            view.display(viewModels)
                        },
                imageSubject.subscribe {
                    view.display(it)
                },
                contactSubject
                        .subscribe { contact ->
                            if (contact.isPresent) {
                                view.display(contact.get().imagePath)
                                view.preventImagePick()
                            } else {
                                view.allowImagePick()
                                view.clearAvatar()
                            }
                        }
        )
        eventsUISubject.onNext(emptyViewModels())
        contactSubject.onNext(Optional.absent())
        saveUISubject.onNext(false)
        contactNameSubject.onNext("")
        imageSubject.onNext(URI.create(""))
    }

    fun presentName(name: String) {
        if (contactSubject.hasContact()) {
            throw UnsupportedOperationException("Changing names of contacts is not supported")
        }
        contactNameSubject.onNext(name)
        startingEvents = emptyViewModels()
        invalidateSave()
    }

    private fun invalidateSave() {
        if (contactSubject.hasContact()) {
            // for a contact, check if the events are different, and we have at least one event
            val eventsHaveChanged = eventsUISubject.values.first() != eventsUISubject.values.last()
            val containsEvents = eventViewModels.containsEventsWithDates()
            saveUISubject.onNext(eventsHaveChanged && containsEvents)
        } else {
            // for no contact, we allow a save if we have at least one event and a name
            saveUISubject.onNext(contactNameSubject.hasValue() && eventViewModels.containsEventsWithDates())
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
                factory.createViewModelFor(it)
            }

    fun presentContact(contact: Contact) {
        analytics.trackContactSelected()
        disposable.add(
                Observable.fromCallable { peopleEventsProvider.fetchEventsFor(contact) }
                        .flatMapIterable { it }
                        .filter { isNotEditableEvent(it.type) }
                        .map { event ->
                            factory.createViewModelFor(event)
                        }
                        .toList()
                        .map { viewModels ->
                            val contactEvents = viewModels.associateBy({ it.eventType }, { it })
                            StandardEventType.values().forEach { eventType ->
                                if (isNotEditableEvent(eventType) && !contactEvents.containsKey(eventType)) {
                                    viewModels.add(factory.createViewModelFor(eventType))
                                }
                            }
                            viewModels.toList()
                        }
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { viewModels ->
                            startingEvents = viewModels
                            saveUISubject.onNext(false)
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
            factory.createViewModelFor(eventType, date)
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
            factory.createViewModelFor(eventType)
        }.map {
            val existingViewModels = eventsUISubject.value.toMutableList()
            val find = existingViewModels.indexOfFirst { it.eventType == eventType }
            existingViewModels[find] = it
            existingViewModels.toList()
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    eventsUISubject.onNext(viewModels)
                    invalidateSave()
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
                                .withEvents(eventViewModels.toEvent())
                                .build()
                        operationsExecutor.execute(operations)
                    }.doOnNext { result ->
                        if (result) {
                            updater.updateEvents()
                                    .subscribeOn(workScheduler)
                                    .subscribe()
                        }
                    }
                            .map {
                                if (it) {
                                    analytics.trackContactUpdated()
                                    strings.contactUpdated()
                                } else {
                                    strings.contactUpdateFailed()
                                }
                            }
                            .subscribeOn(workScheduler)
                            .observeOn(resultScheduler)
                            .subscribe {
                                messageDisplayer.showMessage(it)
                            }
            )
        } else {
            disposable.add(
                    Observable.fromCallable {
                        operationsExecutor.execute(
                                contactOperations
                                        .newContact(contactNameSubject.value)
                                        .withEvents(eventViewModels.toEvent())
                                        .withImage(imageSubject.value)
                                        .build()
                        )
                    }.doOnNext { result ->
                        if (result) {
                            updater.updateEvents()
                                    .subscribeOn(workScheduler)
                                    .subscribe()
                        }
                    }.map {
                        if (it) {
                            analytics.trackContactCreated()
                            strings.contactAdded()
                        } else {
                            strings.contactAddedFailed()
                        }
                    }
                            .subscribeOn(workScheduler)
                            .observeOn(resultScheduler)
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

    fun present(uri: URI) {
        imageSubject.onNext(uri)
    }
}




package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject

class EventsPresenter(
        private val peopleEventsProvider: PeopleEventsProvider,
        private val factory: AddEventContactEventViewModelFactory,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    var events = SelectedEvents()

    private val subject = ReplaySubject.create<List<AddEventContactEventViewModel>>()
    private val disposable = CompositeDisposable()

    fun startPresentingInto(view: AddEventView) {
        disposable.add(
                subject.subscribe { viewModels ->
                    view.display(viewModels)
                }
        )
        subject.onNext(startingViewModels())
    }

    private fun startingViewModels() =
            StandardEventType.values().filter {
                it != StandardEventType.NAMEDAY && it != StandardEventType.CUSTOM
            }.map {
                factory.createAddEventViewModelFor(it)
            }

    fun onContactSelected(contact: Contact) {
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
                            subject.onNext(viewModels)
                        }
        )
    }

    private fun isNotEditableEvent(eventType: EventType) =
            eventType != StandardEventType.NAMEDAY && eventType != StandardEventType.CUSTOM

    fun onEventDatePicked(eventType: EventType, date: Date) {
        Observable.fromCallable {
            factory.createViewModelWith(eventType, date)
        }.map {
            val lastElement = subject.value.toMutableList()
            val find = lastElement.indexOfFirst { it.eventType == eventType }
            lastElement[find] = it
            lastElement.toList()
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    subject.onNext(viewModels)
                }
    }

    fun removeEvent(eventType: EventType) {
        Observable.fromCallable {
            factory.createAddEventViewModelFor(eventType)
        }.map {
            val lastElement = subject.value.toMutableList()
            val find = lastElement.indexOfFirst { it.eventType == eventType }
            lastElement[find] = it
            lastElement.toList()
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    subject.onNext(viewModels)
                }
    }
}

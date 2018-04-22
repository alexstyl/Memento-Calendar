package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.Event
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import io.reactivex.Observable
import io.reactivex.Scheduler

class EventsPresenter(
        private val peopleEventsProvider: PeopleEventsProvider,
        private val factory: AddEventContactEventViewModelFactory,
        private val addEventFactory: AddEventViewModelFactory,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    var events = SelectedEvents()
    private var view: AddEventView? = null


    fun startPresentingInto(view: AddEventView) {
        this.view = view
        Observable.fromCallable {
            addEventFactory.createViewModelsForAllEventsBut(emptyList())
        }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    view.display(viewModels)
                }
    }

    fun onContactSelected(contact: Contact) {
        Observable.fromCallable {
            peopleEventsProvider.fetchEventsFor(contact)
        }
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
                            viewModels.add(addEventFactory.createAddEventViewModelsFor(eventType))
                        }
                    }
                    viewModels
                }
                .subscribeOn(workScheduler)
                .observeOn(resultScheduler)
                .subscribe { viewModels ->
                    view?.display(viewModels)
                }
    }

    private fun isNotEditableEvent(eventType: EventType) =
            eventType != StandardEventType.NAMEDAY && eventType != StandardEventType.CUSTOM

    fun onEventDatePicked(eventType: EventType, date: Date) {
        val viewModels = factory.createViewModelWith(eventType, date)
        view!!.replace(viewModels)
        events.replaceDate(eventType, date)
    }

    fun removeEvent(eventType: EventType) {
        val viewModels = addEventFactory.createAddEventViewModelsFor(eventType)
        view!!.replace(viewModels)
        events.remove(eventType)
    }

    fun getEvents(): Collection<Event> {
        return events.events
    }

}

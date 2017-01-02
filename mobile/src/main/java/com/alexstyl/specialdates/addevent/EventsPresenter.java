package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.Event;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import java.util.Collection;
import java.util.List;

final class EventsPresenter {

    private final ContactEventsFetcher contactEventsFetcher;
    private final ContactEventsAdapter adapter;
    private final ContactEventViewModelFactory factory;
    private final AddEventViewModelFactory addEventFactory;

    private SelectedEvents events = new SelectedEvents();

    EventsPresenter(ContactEventsFetcher contactEventsFetcher,
                    ContactEventsAdapter adapter,
                    ContactEventViewModelFactory factory,
                    AddEventViewModelFactory addEventFactory) {
        this.contactEventsFetcher = contactEventsFetcher;
        this.adapter = adapter;
        this.factory = factory;
        this.addEventFactory = addEventFactory;
    }

    void startPresenting() {
        contactEventsFetcher.loadEmptyEvents(onNewContactLoadedCallback);
    }

    void onContactSelected(Contact contact) {
        contactEventsFetcher.load(contact, onNewContactLoadedCallback);
    }

    private final ContactEventsFetcher.OnDataFetchedCallback onNewContactLoadedCallback = new ContactEventsFetcher.OnDataFetchedCallback() {
        @Override
        public void onDataFetched(List<ContactEventViewModel> data) {
            adapter.replace(data);
        }
    };

    void onEventDatePicked(EventType eventType, Date date) {
        ContactEventViewModel viewModels = factory.createViewModelWith(eventType, date);
        adapter.replace(viewModels);
        events.replaceDate(eventType, date);
    }

    void onEventRemoved(EventType eventType) {
        ContactEventViewModel viewModels = addEventFactory.createAddEventViewModelsFor(eventType);
        adapter.replace(viewModels);
        events.remove(eventType);
    }

    public Collection<Event> getEvents() {
        return events.getEvents();
    }
}

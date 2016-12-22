package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class TemporaryEventsState {

    private static Optional<Contact> NEW_CONTACT = Optional.absent();

    private final Optional<Contact> selectedContact;
    private final Map<EventType, Date> potentialEvents;
    private final String contactName;

    static TemporaryEventsState forContact(Contact contact) {
        return new TemporaryEventsState(new Optional<>(contact), new HashMap<EventType, Date>(), contact.getDisplayName().toString());
    }

    static TemporaryEventsState newState() {
        return new TemporaryEventsState(NEW_CONTACT, new HashMap<EventType, Date>(), "");
    }

    TemporaryEventsState asAnnonymous(String contactName) {
        return new TemporaryEventsState(NEW_CONTACT, potentialEvents, contactName);
    }

    TemporaryEventsState(Optional<Contact> selectedContact, Map<EventType, Date> potentialEvents, String contactName) {
        this.selectedContact = selectedContact;
        this.potentialEvents = potentialEvents;
        this.contactName = contactName;
    }

    void keepState(List<ContactEventViewModel> data) {
        potentialEvents.clear();
        for (ContactEventViewModel viewModel : data) {
            Optional<Date> date = viewModel.getDate();
            if (date.isPresent()) {
                potentialEvents.put(viewModel.getEventType(), date.get());
            }
        }
    }

    void keepState(EventType eventType, Date date) {
        potentialEvents.put(eventType, date);
    }

    public Optional<Contact> getContact() {
        return selectedContact;
    }

    void keepEventsOf(TemporaryEventsState state) {
        potentialEvents.clear();
        potentialEvents.putAll(state.potentialEvents);
    }
}

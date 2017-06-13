package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

import java.util.ArrayList;
import java.util.List;

class ContactWithEvents {

    private static final Optional<ContactEvent> NO_EVENT = Optional.absent();

    private final Contact contact;
    private final List<ContactEvent> events;

    ContactWithEvents(Contact contact, List<ContactEvent> events) {
        this.contact = contact;
        this.events = events;
    }

    ContactWithEvents(Contact contact, ContactEvent event) {
        this.contact = contact;
        this.events = new ArrayList<>();
        this.events.add(event);
    }

    public Contact getContact() {
        return contact;
    }

    public List<ContactEvent> getEvents() {
        return events;
    }

    Optional<ContactEvent> getBirthday() {
        for (ContactEvent event : events) {
            if (event.getType() == StandardEventType.BIRTHDAY) {
                return new Optional<>(event);
            }
        }
        return NO_EVENT;
    }

    public List<ContactEvent> getNamedays() {
        List<ContactEvent> contactEvents = new ArrayList<>();
        for (ContactEvent event : events) {
            if (event.getType() == StandardEventType.NAMEDAY) {
                contactEvents.add(event);
            }
        }
        return contactEvents;
    }

    @Override
    public String toString() {
        return "ContactWithEvents{" +
                "contact=" + contact +
                ", events=" + events +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactWithEvents that = (ContactWithEvents) o;

        if (!contact.equals(that.contact)) {
            return false;
        }
        return events.equals(that.events);

    }

    @Override
    public int hashCode() {
        int result = contact.hashCode();
        result = 31 * result + events.hashCode();
        return result;
    }
}

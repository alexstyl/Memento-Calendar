package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.EventType;

/**
 * A representation of an event, affiliated to a contact
 */
public class ContactEvent {

    private final DayDate date;
    private final Contact contact;
    private final EventType eventType;

    public static ContactEvent newInstance(EventType eventType, DayDate date, Contact contact) {
        return new ContactEvent(eventType, date, contact);
    }

    ContactEvent(EventType eventType, DayDate date, Contact contact) {
        this.eventType = eventType;
        this.date = date;
        this.contact = contact;
    }

    /**
     * Returns the date of the event
     */
    public DayDate getDate() {
        return date;
    }

    public EventType getType() {
        return eventType;
    }

    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public int getYear() {
        return date.getYear();
    }

    /**
     * Returns the contact associated with this event
     */

    public Contact getContact() {
        return contact;
    }

}

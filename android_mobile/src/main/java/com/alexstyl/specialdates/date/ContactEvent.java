package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

public final class ContactEvent {

    private final Optional<Long> deviceEventId;
    private final Contact contact;
    private final EventType eventType;
    private final Date eventDate;

    public ContactEvent(Optional<Long> deviceEventId, EventType eventType, Date date, Contact contact) {
        this.deviceEventId = deviceEventId;
        this.eventType = eventType;
        this.eventDate = date;
        this.contact = contact;
    }

    public String getLabel(Date dateWithYear, Strings strings) {
        if (eventType == StandardEventType.BIRTHDAY) {
            if (eventDate.hasYear()) {
                int age = dateWithYear.getYear() - eventDate.getYear();
                if (age > 0) {
                    return strings.turnsAge(age);
                }
            }
        }
        return eventType.getEventName(strings);
    }

    public Date getDate() {
        return eventDate;
    }

    public EventType getType() {
        return eventType;
    }

    public Contact getContact() {
        return contact;
    }

    public Optional<Long> getDeviceEventId() {
        return deviceEventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactEvent that = (ContactEvent) o;

        if (!deviceEventId.equals(that.deviceEventId)) {
            return false;
        }
        if (!eventType.equals(that.eventType)) {
            return false;
        }
        if (!contact.equals(that.contact)) {
            return false;
        }
        return eventDate.equals(that.eventDate);

    }

    @Override
    public int hashCode() {
        int result = deviceEventId.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + contact.hashCode();
        result = 31 * result + eventDate.hashCode();
        return result;
    }
}

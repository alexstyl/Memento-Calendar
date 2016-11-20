package com.alexstyl.specialdates.date;

import android.content.res.Resources;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

public final class ContactEvent {

    private final Optional<Long> deviceEventId;
    private final EventType eventType;
    private final Contact contact;
    private final Date date;

    public ContactEvent(Optional<Long> deviceEventId, EventType eventType, Date date, Contact contact) {
        this.deviceEventId = deviceEventId;
        this.eventType = eventType;
        this.date = date;
        this.contact = contact;
    }

    public String getLabel(Resources resources) {
        if (eventType == StandardEventType.BIRTHDAY) {
            if (date.hasYear()) {
                int age = Date.CURRENT_YEAR - date.getYear();
                if (age > 0) {
                    return resources.getString(R.string.turns_age, age);
                }
            }
        }
        return eventType.getEventName(resources);
    }

    public Date getDate() {
        return date;
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
        return date.equals(that.date);

    }

    @Override
    public int hashCode() {
        int result = deviceEventId.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + contact.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ContactEvent{" +
                "deviceEventId=" + deviceEventId +
                ", eventType=" + eventType +
                ", contact=" + contact +
                ", date=" + date +
                '}';
    }
}

package com.alexstyl.specialdates.date;

import android.content.res.Resources;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

public final class ContactEvent {

    private static final Optional<Long> NO_EVENT_ID = Optional.absent();

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
}

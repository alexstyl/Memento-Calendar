package com.alexstyl.specialdates.date;

import android.content.res.Resources;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.EventType;

/**
 * A representation of an event, affiliated to a contact
 */
public final class ContactEvent {

    private final DayDate date;
    private final Contact contact;
    private final EventType eventType;

    public ContactEvent(EventType eventType, DayDate date, Contact contact) {
        this.eventType = eventType;
        this.date = date;
        this.contact = contact;
    }

    public String getLabel(Resources resources) {
        if (eventType == EventType.BIRTHDAY) {
            Birthday birthday = contact.getBirthday();
            if (birthday.includesYear()) {
                int age = birthday.getAgeOnYear(getYear());
                if (age > 0) {
                    return resources.getString(R.string.turns_age, age);
                }
            }
        }
        return resources.getString(eventType.nameRes());
    }

    public DayDate getDate() {
        return date;
    }

    public EventType getType() {
        return eventType;
    }

    public int getYear() {
        return date.getYear();
    }

    public Contact getContact() {
        return contact;
    }
}

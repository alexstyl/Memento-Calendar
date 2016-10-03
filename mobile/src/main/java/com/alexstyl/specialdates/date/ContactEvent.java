package com.alexstyl.specialdates.date;

import android.content.res.Resources;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.peopleevents.EventType;

/**
 * A representation of an event, affiliated to a contact
 */
public final class ContactEvent {

    private final EventType eventType;
    private final Contact contact;
    private final Date date;

    public ContactEvent(EventType eventType, Date date, Contact contact) {
        this.eventType = eventType;
        this.date = date;
        this.contact = contact;
    }

    public String getLabel(Resources resources) {
        if (eventType == EventType.BIRTHDAY) {
            Date birthday = contact.getDateOfBirth();
            if (birthday.hasYear()) {
                int age = date.getYear() - birthday.getYear();
                if (age > 0) {
                    return resources.getString(R.string.turns_age, age);
                }
            }
        }
        return resources.getString(eventType.nameRes());
    }

    public Date getDate() {
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

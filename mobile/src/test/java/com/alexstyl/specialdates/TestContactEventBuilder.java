package com.alexstyl.specialdates;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alexstyl.specialdates.events.peopleevents.StandardEventType.ANNIVERSARY;
import static com.alexstyl.specialdates.events.peopleevents.StandardEventType.BIRTHDAY;
import static com.alexstyl.specialdates.events.peopleevents.StandardEventType.NAMEDAY;

public class TestContactEventBuilder {

    private static final Optional<Long> NO_DEVICE_CONTACT_ID = Optional.absent();
    private List<ContactEvent> contactEvents = new ArrayList<>();

    public TestContactEventBuilder addBirthdayFor(Contact contact, Date date) {
        addEventFor(contact, BIRTHDAY, date);
        return this;
    }

    public TestContactEventBuilder addAnniversaryFor(Contact contact, Date date) {
        addEventFor(contact, ANNIVERSARY, date);
        return this;
    }

    public TestContactEventBuilder addNamedayFor(Contact contact, Date date) {
        addEventFor(contact, NAMEDAY, date);
        return this;
    }

    private void addEventFor(Contact contact, EventType eventType, Date date) {
        contactEvents.add(new ContactEvent(NO_DEVICE_CONTACT_ID, eventType, date, contact));
    }

    public List<ContactEvent> build() {
        return Collections.unmodifiableList(contactEvents);
    }
}

package com.alexstyl.specialdates;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import static com.alexstyl.specialdates.events.peopleevents.StandardEventType.*;

public class ContactEventFixture {

    private static final Optional<Long> NO_DEVICE_CONTACT_ID = Optional.absent();

    public ContactEvent aBirthdayFor(Contact contact, Date date) {
        return anEventFor(contact, BIRTHDAY, date);
    }

    public ContactEvent anAnniversaryFor(Contact contact, Date date) {
        return anEventFor(contact, ANNIVERSARY, date);
    }

    public ContactEvent aNamedayFor(Contact contact, Date date) {
        return anEventFor(contact, NAMEDAY, date);
    }

    private ContactEvent anEventFor(Contact contact, EventType eventType, Date date) {
        return new ContactEvent(NO_DEVICE_CONTACT_ID, eventType, date, contact);
    }

}

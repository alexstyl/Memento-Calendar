package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;

import static com.alexstyl.specialdates.events.peopleevents.StandardEventType.BIRTHDAY;

final class ContactEventTestBuilder {

    private static final Optional<Long> NO_DEVICE_EVENT_ID = Optional.absent();

    private final Contact contact;

    ContactEventTestBuilder(Contact contact) {
        this.contact = contact;
    }

    ContactEvent buildBirthday(Date date) {
        return new ContactEvent(NO_DEVICE_EVENT_ID, BIRTHDAY, date, contact);
    }
}

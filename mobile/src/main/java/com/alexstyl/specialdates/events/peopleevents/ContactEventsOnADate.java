package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactEventsOnADate {

    private final Date date;
    private final List<ContactEvent> contactEventList;
    private final List<Contact> contacts;

    public static ContactEventsOnADate createFrom(Date date, List<ContactEvent> contactEvent) {
        List<Contact> contacts = getContactsIn(contactEvent);
        return new ContactEventsOnADate(date, contactEvent, contacts);
    }

    private static List<Contact> getContactsIn(List<ContactEvent> contactEvent) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactEvent event : contactEvent) {
            Contact contact = event.getContact();
            if (!contacts.contains(contact)) {
                contacts.add(contact);
            }
        }
        return Collections.unmodifiableList(contacts);
    }

    private ContactEventsOnADate(Date date, List<ContactEvent> contactEventList, List<Contact> contacts) {
        this.date = date;
        this.contactEventList = contactEventList;
        this.contacts = contacts;
    }

    public Date getDate() {
        return date;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<ContactEvent> getEvents() {
        return contactEventList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactEventsOnADate that = (ContactEventsOnADate) o;

        if (!date.equals(that.date)) {
            return false;
        }
        if (!contactEventList.equals(that.contactEventList)) {
            return false;
        }
        return contacts.equals(that.contacts);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + contactEventList.hashCode();
        result = 31 * result + contacts.hashCode();
        return result;
    }
}

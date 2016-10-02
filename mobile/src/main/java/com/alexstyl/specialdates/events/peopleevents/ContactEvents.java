package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactEvents {

    private final Date date;
    private final List<ContactEvent> contactEventList;
    private final List<Contact> contacts;

    public static ContactEvents createFrom(Date date, List<ContactEvent> contactEvent) {
        List<Contact> contacts = getContactsIn(contactEvent);
        return new ContactEvents(date, contactEvent, contacts);
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

    private ContactEvents(Date date, List<ContactEvent> contactEventList, List<Contact> contacts) {
        this.date = date;
        this.contactEventList = contactEventList;
        this.contacts = contacts;
    }

    public int size() {
        return contactEventList.size();
    }

    public ContactEvent getEvent(int index) {
        return contactEventList.get(index);
    }

    public Date getDate() {
        return date;
    }

    public List<Contact> getContacts() {
        return Collections.unmodifiableList(contacts);
    }

    public int getContactCount() {
        return contacts.size();
    }
}

package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactEvents {

    private final DayDate date;
    private final List<ContactEvent> contactEventList;
    private final List<Contact> contacts;

    public static ContactEvents createFrom(DayDate date, List<ContactEvent> contactEvent) {
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

    private ContactEvents(DayDate date, List<ContactEvent> contactEventList, List<Contact> contacts) {
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

    public DayDate getDate() {
        return date;
    }

    public List<Contact> getContacts() {
        return Collections.unmodifiableList(contacts);
    }

    public int getContactCount() {
        return contacts.size();
    }
}

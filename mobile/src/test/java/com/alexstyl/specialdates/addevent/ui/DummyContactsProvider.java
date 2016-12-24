package com.alexstyl.specialdates.addevent.ui;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final public class DummyContactsProvider implements ContactsProvider {

    private final List<Contact> allContacts;

    public DummyContactsProvider() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new TestContact(0, DisplayName.from("Alex Styl")));
        contacts.add(new TestContact(1, DisplayName.from("Alex Evil Twin")));
        contacts.add(new TestContact(2, DisplayName.from("Banana")));
        contacts.add(new TestContact(3, DisplayName.from("Anna Papadopoulou")));
        contacts.add(new TestContact(4, DisplayName.from("Just some Name")));
        contacts.add(new TestContact(5, DisplayName.from("Eva Tinatini")));
        allContacts = Collections.unmodifiableList(contacts);
    }

    @Override
    public List<Contact> fetchAllDeviceContacts() {
        return allContacts;
    }
}

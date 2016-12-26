package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.contact.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TestContactBuilder {

    public List<Contact> withNames(String... names) {
        long idIndex = 0;
        List<Contact> contacts = new ArrayList<>(names.length);
        for (String name : names) {
            TestContact testContact = new TestContact(idIndex, DisplayName.from(name));
            contacts.add(testContact);
            idIndex++;
        }
        return Collections.unmodifiableList(contacts);
    }

}

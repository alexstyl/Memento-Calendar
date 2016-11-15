package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class ContactWithEventsSearch {

    private final ContactsProvider contactsProvider;

    private final NameMatcher nameMatcher;

    ContactWithEventsSearch(ContactsProvider contactsProvider, NameMatcher nameMatcher) {
        this.contactsProvider = contactsProvider;
        this.nameMatcher = nameMatcher;
    }

    List<Contact> searchForContacts(String searchQuery, int counter) {
        // since we cannot select ignoring accents, we have to manually do a searching
        searchQuery = searchQuery.trim();

        List<Contact> contacts = new ArrayList<>();
        Set<Long> stashedContacts = new HashSet<>();

        List<Contact> allDeviceContacts = contactsProvider.fetchAllDeviceContacts();
        int size = 0;

        for (Contact contact : allDeviceContacts) {
            long contactID = contact.getContactID();
            if (stashedContacts.contains(contactID)) {
                continue;
            }
            if (nameMatcher.match(contact.getDisplayName(), searchQuery)) {
                stashedContacts.add(contactID);
                contacts.add(contact);
                size++;
            }
            if (size >= counter) {
                break;
            }
        }

        return contacts;
    }

}

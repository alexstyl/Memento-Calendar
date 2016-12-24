package com.alexstyl.specialdates.addevent.ui;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.search.NameMatcher;

import java.util.ArrayList;
import java.util.List;

final class ContactsSearch {

    private final ContactsProvider contactsProvider;
    private final NameMatcher nameMatcher;

    ContactsSearch(ContactsProvider contactsProvider, NameMatcher nameMatcher) {
        this.contactsProvider = contactsProvider;
        this.nameMatcher = nameMatcher;
    }

    List<Contact> searchForContacts(String searchQuery, int numberOfResults) {
        List<Contact> matchedContacts = new ArrayList<>();
        if (numberOfResults == 0) {
            return matchedContacts;
        }
        searchQuery = searchQuery.trim();
        List<Contact> allContacts = contactsProvider.fetchAllDeviceContacts();
        int count = 0;
        int index = 0;
        int contactSize = allContacts.size();
        do {
            Contact contact = allContacts.get(index);
            DisplayName displayName = contact.getDisplayName();
            if (nameMatcher.match(displayName, searchQuery)) {
                matchedContacts.add(contact);
                count++;
            }
            index++;
        } while (count < numberOfResults && index < contactSize);

        return matchedContacts;
    }

}

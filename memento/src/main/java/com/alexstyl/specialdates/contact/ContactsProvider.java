package com.alexstyl.specialdates.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContactsProvider {

    private final Map<Integer, ContactsProviderSource> sources;

    ContactsProvider(Map<Integer, ContactsProviderSource> sources) {
        this.sources = sources;
    }

    public Contacts getContacts(List<Long> contactIds, @ContactSource int source) {
        if (sources.containsKey(source)) {
            return sources.get(source).queryContacts(contactIds);
        }
        throw new IllegalArgumentException("Unknown source type: " + source);
    }

    public Contact getContact(long contactID, @ContactSource int source) throws ContactNotFoundException {
        if (sources.containsKey(source)) {
            return sources.get(source).getOrCreateContact(contactID);
        }
        throw new IllegalArgumentException("Unknown source type: " + source);

    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        for (ContactsProviderSource providerSource : sources.values()) {
            contacts.addAll(providerSource.getAllContacts().getContacts());
        }
        return Collections.unmodifiableList(contacts);
    }

}

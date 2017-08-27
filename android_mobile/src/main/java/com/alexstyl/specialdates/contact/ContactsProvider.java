package com.alexstyl.specialdates.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ContactsProvider {

    private final Map<Integer, ContactsProviderSource> sources;

    ContactsProvider(Map<Integer, ContactsProviderSource> sources) {
        this.sources = sources;
    }

    public List<Contact> getContacts(List<Long> contactIds, @ContactSource int source) {
        if (sources.containsKey(source)) {
            ContactsProviderSource contactsProviderSource = sources.get(source);

            return contactsProviderSource.getContacts(contactIds);
        }
        throw new IllegalArgumentException("Unknown source type: " + source);
    }

    public Contact getContact(long contactID, @ContactSource int source) throws ContactNotFoundException {
        if (sources.containsKey(source)) {
            ContactsProviderSource contactsProviderSource = sources.get(source);
            return contactsProviderSource.getOrCreateContact(contactID);
        }
        throw new IllegalArgumentException("Unknown source type: " + source);

    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        for (ContactsProviderSource providerSource : sources.values()) {
            contacts.addAll(providerSource.getAllContacts());
        }
        return Collections.unmodifiableList(contacts);
    }

}

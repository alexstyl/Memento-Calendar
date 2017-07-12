package com.alexstyl.specialdates.contact;

import java.util.List;

class AndroidContactsProviderSource implements ContactsProviderSource {

    private final ContactCache<Contact> cache;
    private final AndroidContactFactory factory;

    AndroidContactsProviderSource(ContactCache<Contact> contactCache, AndroidContactFactory factory) {
        this.cache = contactCache;
        this.factory = factory;
    }

    @Override
    public Contact getOrCreateContact(long contactID) throws ContactNotFoundException {
        Contact deviceContact = cache.getContact(contactID);
        if (deviceContact == null) {
            deviceContact = factory.createContactWithId(contactID);
            cache.addContact(deviceContact);
        }
        return deviceContact;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> allContacts = factory.getAllContacts();
        cache.evictAll();
        for (Contact allContact : allContacts) {
            cache.addContact(allContact);
        }
        return allContacts;
    }
}

package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.events.peopleevents.DeviceContactsQuery;

import java.util.List;

class AndroidContactsProviderSource implements ContactsProviderSource {

    private final ContactCache<Contact> cache;
    private final DeviceContactFactory factory;
    private final DeviceContactsQuery deviceContactsQuery;

    AndroidContactsProviderSource(ContactCache<Contact> contactCache, DeviceContactFactory factory, DeviceContactsQuery deviceContactsQuery) {
        this.cache = contactCache;
        this.factory = factory;
        this.deviceContactsQuery = deviceContactsQuery;
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
        List<Contact> allContacts = deviceContactsQuery.getAllContacts();
        for (Contact allContact : allContacts) {
            cache.addContact(allContact);
        }
        return allContacts;
    }
}

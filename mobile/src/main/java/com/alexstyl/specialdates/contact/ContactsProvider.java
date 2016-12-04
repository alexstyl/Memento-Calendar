package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;

import com.alexstyl.specialdates.events.peopleevents.DeviceContactsQuery;
import com.alexstyl.specialdates.util.ContactsObserver;

import java.util.List;

public class ContactsProvider {

    private static ContactsProvider INSTANCE;

    private static final int CACHE_SIZE = 2 * 1024;
    private final ContactCache<Contact> cache;
    private final DeviceContactFactory deviceContactFactory;
    private final DeviceContactsQuery deviceContactsQuery;

    public static ContactsProvider get(Context context) {
        if (INSTANCE == null) {
            ContentResolver contentResolver = context.getContentResolver();
            DeviceContactFactory factory = new DeviceContactFactory(contentResolver);
            ContactCache<Contact> contactCache = new ContactCache<>(CACHE_SIZE);
            DeviceContactsQuery deviceContactsQuery = new DeviceContactsQuery(contentResolver);
            INSTANCE = new ContactsProvider(contactCache, factory, deviceContactsQuery);
            INSTANCE.initialise(contentResolver);
        }
        return INSTANCE;
    }

    private void initialise(ContentResolver contentResolver) {
        ContactsObserver observer = new ContactsObserver(contentResolver, new Handler());
        observer.registerWith(evictIfContactChanged);
    }

    private ContactsProvider(ContactCache<Contact> cache, DeviceContactFactory deviceContactFactory, DeviceContactsQuery deviceContactsQuery) {
        this.cache = cache;
        this.deviceContactFactory = deviceContactFactory;
        this.deviceContactsQuery = deviceContactsQuery;
    }

    public Contact getOrCreateContact(long contactID) throws ContactNotFoundException {
        Contact deviceContact = cache.getContact(contactID);
        if (deviceContact == null) {
            deviceContact = deviceContactFactory.createContactWithId(contactID);
            cache.addContact(deviceContact);
        }
        return deviceContact;
    }

    public List<Contact> fetchAllDeviceContacts() {
        // evict the cache. there is no way of reusing existing objects...
        // this might not be the most optimal thing to do
        cache.evictAll();
        List<Contact> allContacts = deviceContactsQuery.getAllContacts();
        for (Contact allContact : allContacts) {
            cache.addContact(allContact);
        }
        return allContacts;
    }

    private final ContactsObserver.Callback evictIfContactChanged = new ContactsObserver.Callback() {
        @Override
        public void onContactsUpdated() {
            cache.evictAll();
        }
    };
}

package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;

import com.alexstyl.specialdates.util.ContactsObserver;

public class ContactProvider {

    private static final int CACHE_SIZE = 2 * 1024;
    private final ContactCache<Contact> cache;
    private final DeviceContactFactory deviceContactFactory;

    private static ContactProvider INSTANCE;

    public static ContactProvider get(Context context) {
        if (INSTANCE == null) {
            ContentResolver contentResolver = context.getContentResolver();
            DeviceContactFactory factory = new DeviceContactFactory(contentResolver);
            ContactCache<Contact> contactCache = new ContactCache<>(CACHE_SIZE);
            INSTANCE = new ContactProvider(contactCache, factory);
            INSTANCE.initialise(contentResolver);
        }
        return INSTANCE;
    }

    private void initialise(ContentResolver contentResolver) {
        ContactsObserver observer = new ContactsObserver(contentResolver, new Handler());
        observer.registerWith(evictIfContactChanged);
    }

    private ContactProvider(ContactCache<Contact> cache, DeviceContactFactory deviceContactFactory) {
        this.cache = cache;
        this.deviceContactFactory = deviceContactFactory;
    }

    public Contact getOrCreateContact(long contactID) throws ContactNotFoundException {
        Contact deviceContact = cache.getContact(contactID);
        if (deviceContact == null) {
            deviceContact = deviceContactFactory.createContactWithId(contactID);
            cache.addContact(deviceContact);
        }
        return deviceContact;
    }

    private final ContactsObserver.Callback evictIfContactChanged = new ContactsObserver.Callback() {
        @Override
        public void onContactsUpdated() {
            cache.evictAll();
        }
    };

}

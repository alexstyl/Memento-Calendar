package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract.Data;

import com.alexstyl.specialdates.util.ContactsObserver;
import com.alexstyl.specialdates.util.DateParser;

public class ContactProvider {

    private static final int CACHE_SIZE = 2 * 1024;
    private final ContactCache<DeviceContact> cache;
    private final DeviceContactFactory deviceContactFactory;

    private static ContactProvider INSTANCE;

    public static ContactProvider get(Context context) {
        if (INSTANCE == null) {
            ContentResolver contentResolver = context.getContentResolver();
            DateParser dateParser = new DateParser();
            DeviceContactFactory factory = new DeviceContactFactory(contentResolver, dateParser);
            ContactCache<DeviceContact> contactCache = new ContactCache<>(CACHE_SIZE);
            INSTANCE = new ContactProvider(contactCache, factory);
            INSTANCE.initialise(contentResolver);
        }
        return INSTANCE;
    }

    private void initialise(ContentResolver contentResolver) {
        ContactsObserver observer = new ContactsObserver(contentResolver, new Handler());
        observer.registerWith(evictIfContactChanged);
    }

    public ContactProvider(ContactCache<DeviceContact> cache, DeviceContactFactory deviceContactFactory) {
        this.cache = cache;
        this.deviceContactFactory = deviceContactFactory;
    }

    public DeviceContact getOrCreateContact(long contactID) throws ContactNotFoundException {
        DeviceContact deviceContact = cache.getContact(contactID);
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

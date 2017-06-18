package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.peopleevents.DeviceContactsQuery;

import java.util.List;

public class ContactsProvider {

    private static ContactsProvider INSTANCE;

    private final ContactsProviderSource source;

    private static final int CACHE_SIZE = 2 * 1024;

    public ContactsProvider(ContactsProviderSource source) {
        this.source = source;
    }

    public static ContactsProvider get(Context context) {
        if (INSTANCE == null) {
            ContentResolver contentResolver = context.getContentResolver();
            DeviceContactFactory factory = new DeviceContactFactory(contentResolver);
            ContactCache<Contact> contactCache = new ContactCache<>(CACHE_SIZE);
            DeviceContactsQuery deviceContactsQuery = new DeviceContactsQuery(contentResolver);
            INSTANCE = new ContactsProvider(new AndroidContactsProviderSource(contactCache, factory, deviceContactsQuery));

        }
        return INSTANCE;
    }

    public Contact getOrCreateContact(long contactID) throws ContactNotFoundException {
        return source.getOrCreateContact(contactID, AnnualEventsContract.SOURCE_DEVICE);
    }

    public List<Contact> getAllContacts() {
        return source.getAllContacts();
    }

}

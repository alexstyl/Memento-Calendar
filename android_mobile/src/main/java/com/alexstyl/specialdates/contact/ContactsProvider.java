package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;
import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

public final class ContactsProvider {

    private static ContactsProvider instance;

    private static final int CACHE_SIZE = 1024;
    private final Map<Integer, ContactsProviderSource> sources;

    public static ContactsProvider get(Context context) {
        if (instance == null) {
            Map<Integer, ContactsProviderSource> sources = new HashMap<>();
            sources.put(SOURCE_DEVICE, buildAndroidSource(context));
            sources.put(SOURCE_FACEBOOK, buildFacebookSource(context));
            instance = new ContactsProvider(sources);
        }
        return instance;
    }

    private static ContactsProviderSource buildAndroidSource(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        AndroidContactFactory factory = new AndroidContactFactory(contentResolver);
        ContactCache<Contact> contactCache = new ContactCache<>(CACHE_SIZE);
        return new AndroidContactsProviderSource(contactCache, factory);
    }

    private static ContactsProviderSource buildFacebookSource(Context context) {
        ContactCache<Contact> contactCache = new ContactCache<>(CACHE_SIZE);
        return new FacebookContactsSource(new EventSQLiteOpenHelper(context), contactCache);
    }

    private ContactsProvider(Map<Integer, ContactsProviderSource> sources) {
        this.sources = sources;
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

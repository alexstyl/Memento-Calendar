package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;
import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

@SuppressWarnings({"FinalClass"}) // TODO abstract the Contacts Provider so that it is not platform dependant
public class ContactsProvider {

    private static final NameComparator NAME_COMPARATOR = NameComparator.INSTANCE;
    private static final int CACHE_SIZE = 1024;

    private static ContactsProvider instance;
    private final Map<Integer, ContactsProviderSource> sources;

    /**
     * @Deprecated Use Dagger to inject this wherever needed instead
     */
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

    public List<Contact> contactsCalled(@NotNull String name) {
        List<Contact> matchingContacts = new ArrayList<>();
        for (Contact contact : getAllContacts()) {
            DisplayName displayName = contact.getDisplayName();
            for (String contactName : displayName.getFirstNames()) {
                if (NAME_COMPARATOR.areTheSameName(contactName, name)) {
                    matchingContacts.add(contact);
                    break;
                }
            }
        }
        return matchingContacts;
    }
}

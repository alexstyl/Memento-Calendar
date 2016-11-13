package com.alexstyl.specialdates.search;

import android.content.Context;
import android.database.Cursor;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.database.PeopleEventsContract.PeopleEvents;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final public class ContactWithEventsSearch {

    private final Context context;
    private final ContactsProvider contactsProvider;

    private final NameMatcher nameMatcher;

    public static ContactWithEventsSearch newInstance(Context context) {
        ContactsProvider contactsProvider = ContactsProvider.get(context);
        NameMatcher nameMatcher = NameMatcher.newInstance();
        return new ContactWithEventsSearch(context, contactsProvider, nameMatcher);
    }

    ContactWithEventsSearch(Context context, ContactsProvider contactsProvider, NameMatcher nameMatcher) {
        this.contactsProvider = contactsProvider;
        this.nameMatcher = nameMatcher;
        this.context = context.getApplicationContext();
    }

    public List<Contact> searchForContacts(String searchQuery, int counter) {
        // since we cannot select ignoring accents, we have to manually do a searching
        searchQuery = searchQuery.trim();

        List<Contact> contacts = new ArrayList<>();
        Set<Long> stashedContacts = new HashSet<>();

        Cursor cursor = queryAllContactsWithEvents();

        int size = 0;

        while (cursor.moveToNext() && size < counter) {
            long contactID = getContactIDFrom(cursor);
            if (stashedContacts.contains(contactID)) {
                continue;
            }
            DisplayName displayName = getDisplayNameFrom(cursor);
            if (nameMatcher.match(displayName, searchQuery)) {
                long contactId = PeopleEvents.getContactIdFrom(cursor);
                Contact contact;
                try {
                    contact = contactsProvider.getOrCreateContact(contactId);
                    stashedContacts.add(contactID);
                    contacts.add(contact);
                    size++;
                } catch (ContactNotFoundException e) {
                    Log.w(e);
                }
            }
        }

        cursor.close();

        return contacts;
    }

    private DisplayName getDisplayNameFrom(Cursor cursor) {
        int displayNameIndex = cursor.getColumnIndex(PeopleEvents.DISPLAY_NAME);
        String displayName = cursor.getString(displayNameIndex);
        return DisplayName.from(displayName);
    }

    private long getContactIDFrom(Cursor cursor) {
        int indexContactID = cursor.getColumnIndexOrThrow(PeopleEvents.CONTACT_ID);
        return cursor.getLong(indexContactID);
    }

    private Cursor queryAllContactsWithEvents() {
        return context.getContentResolver().query(
                PeopleEvents.CONTENT_URI,
                null, null, null,
                PeopleEvents.CONTACT_ID
        );
    }

}

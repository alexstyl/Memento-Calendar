package com.alexstyl.specialdates.search;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactProvider;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final public class ContactsSearch {

    private final Context context;
    private final ContactProvider contactProvider;

    private final NameMatcher nameMatcher;

    public static ContactsSearch newInstance(Context context) {
        ContactProvider contactProvider = ContactProvider.get(context);
        NameMatcher nameMatcher = NameMatcher.newInstance();
        return new ContactsSearch(context, contactProvider, nameMatcher);
    }

    ContactsSearch(Context context, ContactProvider contactProvider, NameMatcher nameMatcher) {
        this.contactProvider = contactProvider;
        this.nameMatcher = nameMatcher;
        this.context = context.getApplicationContext();
    }

    public List<Contact> searchForContacts(String searchQuery, int counter) {
        searchQuery = searchQuery.trim();

        List<Contact> contacts = new ArrayList<>();
        Set<Long> stashedContacts = new HashSet<>();

        Cursor cursor = queryAllContacts();

        int size = 0;

        while (cursor.moveToNext() && size < counter) {
            long contactID = getContactIDFrom(cursor);
            if (stashedContacts.contains(contactID)) {
                continue;
            }
            DisplayName displayName = getDisplayNameFrom(cursor);
            if (nameMatcher.match(displayName, searchQuery)) {
                try {
                    Contact contact = contactProvider.getOrCreateContact(contactID);
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
        int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String displayName = cursor.getString(displayNameIndex);
        if (displayName == null || displayName.length() == 0) {
            return DisplayName.NO_NAME;
        }
        return DisplayName.from(displayName);
    }

    private long getContactIDFrom(Cursor cursor) {
        int indexContactID = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
        return cursor.getLong(indexContactID);
    }

    private Cursor queryAllContacts() {
        return context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, ContactsContract.Contacts.IN_VISIBLE_GROUP + " = 1", null,
                ContactsContract.Contacts._ID
        );
    }

}

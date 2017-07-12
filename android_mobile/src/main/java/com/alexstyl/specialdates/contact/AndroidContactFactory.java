package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.ErrorTracker;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alexstyl.specialdates.contact.AndroidContactsQuery.SORT_ORDER;

class AndroidContactFactory {

    private static final String WHERE = ContactsContract.Data.IN_VISIBLE_GROUP + "=1";
    private static final String SELECTION_CONTACT_WITH_ID = Contacts._ID + " = ?";


    private final ContentResolver resolver;

    AndroidContactFactory(ContentResolver contentResolver) {
        resolver = contentResolver;
    }

    List<Contact> getAllContacts() {
        Cursor cursor = resolver.query(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                WHERE,
                null,
                AndroidContactsQuery.SORT_ORDER
        );
        List<Contact> contacts = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Contact contact = createContactFrom(cursor);
                contacts.add(contact);
            }
        } catch (Exception e) {
            ErrorTracker.track(e);
        } finally {
            cursor.close();
        }
        return Collections.unmodifiableList(contacts);
    }

    Contact createContactWithId(long contactID) throws ContactNotFoundException {
        Cursor cursor = queryContactsWithContactId(contactID);
        if (isInvalid(cursor)) {
            throw new RuntimeException("Cursor was invalid");
        }
        try {
            if (cursor.moveToFirst()) {
                return createContactFrom(cursor);
            }
        } finally {
            cursor.close();
        }
        throw new ContactNotFoundException(contactID);
    }

    private Contact createContactFrom(Cursor cursor) {
        long contactID = getContactIdFrom(cursor);
        DisplayName displayName = getDisplayNameFrom(cursor);
        Uri lookupKey = getLookupKeyFrom(cursor);
        URI imagePath = URI.create(ContentUris.withAppendedId(Contacts.CONTENT_URI, contactID).toString());
        return new AndroidContact(contactID, displayName, imagePath, lookupKey);
    }

    private Cursor queryContactsWithContactId(long contactID) {
        return resolver.query(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                SELECTION_CONTACT_WITH_ID,
                makeSelectionArgumentsFor(contactID),
                SORT_ORDER + " LIMIT 1"
        );
    }

    @NonNull
    private String[] makeSelectionArgumentsFor(long contactID) {
        return new String[]{
                String.valueOf(contactID)
        };
    }

    private static boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

    private static long getContactIdFrom(Cursor cursor) {
        return cursor.getLong(AndroidContactsQuery.CONTACT_ID);
    }

    private static DisplayName getDisplayNameFrom(Cursor cursor) {
        return DisplayName.from(cursor.getString(AndroidContactsQuery.DISPLAY_NAME));
    }

    private static Uri getLookupKeyFrom(Cursor cursor) {
        return Uri.parse(cursor.getString(AndroidContactsQuery.LOOKUP_KEY));
    }

}

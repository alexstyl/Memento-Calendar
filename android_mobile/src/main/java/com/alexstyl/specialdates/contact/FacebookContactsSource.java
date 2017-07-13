package com.alexstyl.specialdates.contact;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.facebook.FacebookImagePath;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

class FacebookContactsSource implements ContactsProviderSource {

    private static final String IS_A_FACEBOOK_CONTACT = AnnualEventsContract.SOURCE + "== " + SOURCE_FACEBOOK;

    private final EventSQLiteOpenHelper eventSQLHelper;
    private final ContactCache<Contact> cache;

    FacebookContactsSource(EventSQLiteOpenHelper eventSQLHelper, ContactCache<Contact> cache) {
        this.eventSQLHelper = eventSQLHelper;
        this.cache = cache;
    }

    @Override
    public Contact getOrCreateContact(long contactID) throws ContactNotFoundException {
        Contact contact = cache.getContact(contactID);
        if (contact == null) {
            contact = queryContactWith(contactID);
        }
        return contact;
    }

    private Contact queryContactWith(long contactID) throws ContactNotFoundException {
        SQLiteDatabase readableDatabase = eventSQLHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = readableDatabase.query(
                    AnnualEventsContract.TABLE_NAME,
                    null,
                    IS_A_FACEBOOK_CONTACT + " AND " + AnnualEventsContract.CONTACT_ID + " == " + contactID,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                return createContactFrom(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        throw new ContactNotFoundException(contactID);
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> allContacts = queryAllContacts();
        cache.evictAll();
        for (Contact allContact : allContacts) {
            cache.addContact(allContact);
        }
        return allContacts;
    }

    @NonNull
    private List<Contact> queryAllContacts() {
        SQLiteDatabase readableDatabase = eventSQLHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Contact> contacts = new ArrayList<>();
        try {
            cursor = readableDatabase.query(
                    AnnualEventsContract.TABLE_NAME,
                    null,
                    IS_A_FACEBOOK_CONTACT,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                Contact contact = createContactFrom(cursor);
                contacts.add(contact);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contacts;
    }

    private static Contact createContactFrom(Cursor cursor) {
        long uid = cursor.getLong(cursor.getColumnIndexOrThrow(AnnualEventsContract.CONTACT_ID));
        DisplayName displayName = DisplayName.from(cursor.getString(cursor.getColumnIndexOrThrow(AnnualEventsContract.DISPLAY_NAME)));
        URI imagePath = FacebookImagePath.forUid(uid);
        return new Contact(uid, displayName, imagePath, SOURCE_FACEBOOK);
    }
}

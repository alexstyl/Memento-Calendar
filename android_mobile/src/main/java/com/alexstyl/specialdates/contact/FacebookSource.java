package com.alexstyl.specialdates.contact;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.events.database.DatabaseContract;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.facebook.FacebookImagePathCreator;
import com.alexstyl.specialdates.facebook.friendimport.FacebookContact;

import java.util.ArrayList;
import java.util.List;

class FacebookSource implements ContactsProviderSource {
    private final EventSQLiteOpenHelper eventSQLHelper;

    FacebookSource(EventSQLiteOpenHelper eventSQLHelper) {
        this.eventSQLHelper = eventSQLHelper;
    }

    @Override
    public Contact getOrCreateContact(long contactID) throws ContactNotFoundException {
        SQLiteDatabase readableDatabase = eventSQLHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = readableDatabase.query(
                    DatabaseContract.AnnualEventsContract.TABLE_NAME,
                    null,
                    DatabaseContract.AnnualEventsContract.SOURCE + "== " + DatabaseContract.AnnualEventsContract.SOURCE_FACEBOOK + " AND "
                            + DatabaseContract.AnnualEventsContract.CONTACT_ID + " == " + contactID,
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

        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        SQLiteDatabase readableDatabase = eventSQLHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Contact> contacts = new ArrayList<>();
        try {
            cursor = readableDatabase.query(
                    DatabaseContract.AnnualEventsContract.TABLE_NAME,
                    null,
                    DatabaseContract.AnnualEventsContract.SOURCE + "== " + DatabaseContract.AnnualEventsContract.SOURCE_FACEBOOK,
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

    private Contact createContactFrom(Cursor cursor) {
        long uid = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.AnnualEventsContract.CONTACT_ID));
        DisplayName displayName = DisplayName.from(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.AnnualEventsContract.DISPLAY_NAME)));
        Uri imagePath = FacebookImagePathCreator.INSTANCE.forUid(uid);
        return new FacebookContact(uid, displayName, imagePath);
    }
}

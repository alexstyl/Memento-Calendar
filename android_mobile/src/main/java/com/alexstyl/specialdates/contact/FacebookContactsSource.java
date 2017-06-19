package com.alexstyl.specialdates.contact;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.facebook.FacebookImagePathCreator;
import com.alexstyl.specialdates.facebook.friendimport.FacebookContact;

import java.util.ArrayList;
import java.util.List;

class FacebookContactsSource implements ContactsProviderSource {

    private static final String IS_A_FACEBOOK_CONTACT = AnnualEventsContract.SOURCE + "== " + AnnualEventsContract.SOURCE_FACEBOOK;

    private final EventSQLiteOpenHelper eventSQLHelper;

    FacebookContactsSource(EventSQLiteOpenHelper eventSQLHelper) {
        this.eventSQLHelper = eventSQLHelper;
    }

    @Override
    public Contact getOrCreateContact(long contactID) throws ContactNotFoundException {
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

        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
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
        Uri imagePath = FacebookImagePathCreator.INSTANCE.forUid(uid);
        return new FacebookContact(uid, displayName, imagePath);
    }
}

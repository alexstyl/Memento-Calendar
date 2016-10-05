package com.alexstyl.specialdates.addevent;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;

public class BirthdayQuery {

    private final ContentResolver contentResolver;

    public BirthdayQuery(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public ContactQuery forContact(Contact contact) {
        DateParser dateParser = new DateParser();
        return new ContactQuery(contact, dateParser, contentResolver);
    }

    public static class ContactQuery {

        private final Contact contact;
        private final DateParser dateParser;
        private ContentResolver contentResolver;

        public ContactQuery(Contact contact, DateParser dateParser, ContentResolver contentResolver) {
            this.contact = contact;
            this.dateParser = dateParser;
            this.contentResolver = contentResolver;
        }

        public BirthdayEntry queryContactsBirthday() {
            String selection = ContactsContract.Data.CONTACT_ID + " = " + contact.getContactID() + " AND " + Query.WHERE;
            Cursor cursor = contentResolver.query(Query.CONTENT_URI, Query.PROJECTION, selection, Query.WHERE_ARGS, ContactsContract.Data.CONTACT_ID + " LIMIT 1");
            if (cursor == null || cursor.isClosed()) {
                throw new RuntimeException("Invalid cursor when querying");
            }
            try {
                if (cursor.moveToFirst()) {
                    return getEntryFrom(cursor);
                }
            } finally {
                cursor.close();
            }
            return null;
        }

        private BirthdayEntry getEntryFrom(Cursor cursor) {
            long id = cursor.getLong(Query.ID);
            try {
                Date birthday = getBirthdayFrom(cursor);
                return new BirthdayEntry(id, birthday);
            } catch (DateParseException e) {
                ErrorTracker.track(e);
            }
            return null;
        }

        private Date getBirthdayFrom(Cursor cursor) throws DateParseException {
            String string = cursor.getString(Query.BIRTHDAY);
            return dateParser.parse(string);
        }

        public boolean replaceBirthdays(BirthdayEntry oldBirthday, Date birthday) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(ContactsContract.CommonDataKinds.Event.START_DATE, birthday.toShortDate());

            String where = ContactsContract.CommonDataKinds.Event._ID + " = " + oldBirthday.getEntryID();
            int update = contentResolver.update(ContactsContract.Data.CONTENT_URI, contentValues, where, null);

            return update == 1;
        }

        public boolean insertBirthday(Date birthday) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY);
            contentValues.put(ContactsContract.CommonDataKinds.Event.START_DATE, birthday.toString());

            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactID());
            contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE);

            return contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues) != null;
        }

        private int rawContactID() {
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID};
            String selection = ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = ?";
            String[] selectionArgs = new String[]{
                    String.valueOf(contact.getContactID())};
            Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null);
            if (cursor == null || cursor.isClosed()) {
                throw new DeveloperError("");
            }
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
            return 0;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static final class Query {
        /**
         * Queries the contacts tables for birthdays with the default settings.
         */
        public static Cursor query(ContentResolver cr) {
            return cr.query(CONTENT_URI, PROJECTION, WHERE, WHERE_ARGS, SORT_ORDER);
        }

        private static final Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;
        private static final String COL_DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;

        public static final String WHERE =
                "(" + ContactsContract.Data.MIMETYPE + " = ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE
                        + "="
                        + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + ")"
                        + " AND " + ContactsContract.Data.IN_VISIBLE_GROUP + " = 1";

        public static final String[] WHERE_ARGS = {
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };

        @SuppressLint("InlinedApi")
        public final static String SORT_ORDER = COL_DISPLAY_NAME;

        @SuppressLint("InlinedApi")
        public static final String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Event._ID,        //0
                ContactsContract.CommonDataKinds.Event.START_DATE, //1
        };
        public static final int ID = 0;
        public static final int BIRTHDAY = 1;

    }
}

package com.alexstyl.specialdates.events;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.DeviceContact;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.util.DateParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BirthdayDatabaseRefresher {

    @SuppressWarnings("unchecked")
    private static final List<Contact> NO_CONTACTS = Collections.EMPTY_LIST;

    private final ContentResolver contentResolver;
    private final DateParser dateParser;
    private final PeopleEventsPersister persister;
    private final BirthdayContentValuesMarshaller marshaller;

    static BirthdayDatabaseRefresher newInstance(Context context) {
        ContentResolver cr = context.getContentResolver();
        DateParser dateParser = new DateParser();
        PeopleEventsPersister persister = new PeopleEventsPersister(new EventSQLiteOpenHelper(context));
        BirthdayContentValuesMarshaller marshaller = new BirthdayContentValuesMarshaller();
        return new BirthdayDatabaseRefresher(cr, dateParser, persister, marshaller);
    }

    BirthdayDatabaseRefresher(ContentResolver contentResolver, DateParser dateParser, PeopleEventsPersister persister, BirthdayContentValuesMarshaller marshaller) {
        this.contentResolver = contentResolver;
        this.dateParser = dateParser;
        this.persister = persister;
        this.marshaller = marshaller;
    }

    public void refreshBirthdays() {
        clearAllBirthdays();
        List<Contact> contacts = loadBirtdaysFromDisk();
        storeContactsToProvider(contacts);
    }

    private void clearAllBirthdays() {
        persister.deleteAllBirthdays();
    }

    private List<Contact> loadBirtdaysFromDisk() {
        Cursor cursor = Query.query(contentResolver);
        if (isInvalid(cursor)) {
            return NO_CONTACTS;
        }
        List<Contact> contacts = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                String dateOfBirthText = cursor.getString(Query.BIRTHDAY);
                Birthday dateOfBirth;
                try {
                    dateOfBirth = birthdayOn(dateOfBirthText);
                } catch (DateParseException e) {
                    ErrorTracker.track(e);
                    continue;
                }
                long id = cursor.getLong(Query.ID);

                DisplayName display = getDisplayNameFrom(cursor);
                String lookup = cursor.getString(Query.LOOKUP_KEY);
                DeviceContact contact = new DeviceContact(id, display, lookup, dateOfBirth);
                contacts.add(contact);
            }
        } finally {
            cursor.close();
        }
        return contacts;
    }

    private DisplayName getDisplayNameFrom(Cursor cursor) {
        return DisplayName.from(cursor.getString(Query.DISPLAY_NAME));
    }

    private Birthday birthdayOn(String bday) throws DateParseException {
        DayDate parsedDate = dateParser.parse(bday);
        return Birthday.on(parsedDate);
    }

    private boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

    private void storeContactsToProvider(List<Contact> contacts) {
        ContentValues[] values = marshaller.marshall(contacts);
        persister.insertAnnualEvents(values);
    }

    /**
     * Contract that queries birthdays only
     */
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
                ContactsContract.Data.CONTACT_ID,     //0
                ContactsContract.Contacts.LOOKUP_KEY, //1
                COL_DISPLAY_NAME, //2
                ContactsContract.CommonDataKinds.Event.START_DATE, //3
        };
        public static final int ID = 0;
        public static final int LOOKUP_KEY = 1;
        public static final int DISPLAY_NAME = 2;

        public static final int BIRTHDAY = 3;

    }
}

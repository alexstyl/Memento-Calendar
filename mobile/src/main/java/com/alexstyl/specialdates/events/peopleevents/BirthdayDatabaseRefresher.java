package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.ContactProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BirthdayDatabaseRefresher {

    private static final List<ContactEvent> NO_CONTACTS = Collections.emptyList();
    private static final Optional<Contact> NO_CONTACT = Optional.absent();

    private final ContactProvider contactProvider;
    private final ContentResolver contentResolver;
    private final PeopleEventsPersister persister;
    private final ContactEventsMarshaller marshaller;

    static BirthdayDatabaseRefresher newInstance(Context context) {
        ContactProvider contactProvider = ContactProvider.get(context);
        PeopleEventsPersister persister = new PeopleEventsPersister(new EventSQLiteOpenHelper(context));
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller();
        return new BirthdayDatabaseRefresher(contactProvider, context.getContentResolver(), persister, marshaller);
    }

    BirthdayDatabaseRefresher(ContactProvider contactProvider,
                              ContentResolver contentResolver,
                              PeopleEventsPersister persister,
                              ContactEventsMarshaller marshaller) {
        this.contentResolver = contentResolver;
        this.persister = persister;
        this.marshaller = marshaller;
        this.contactProvider = contactProvider;
    }

    public void refreshBirthdays() {
        clearAllBirthdays();
        List<ContactEvent> contacts = loadBirtdaysFromDisk();
        storeContactsToProvider(contacts);
    }

    private void clearAllBirthdays() {
        persister.deleteAllBirthdays();
    }

    private List<ContactEvent> loadBirtdaysFromDisk() {
        Cursor cursor = BirthdayQuery.query(contentResolver);
        if (isInvalid(cursor)) {
            return NO_CONTACTS;
        }
        List<ContactEvent> contacts = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                int contactIdIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
                long contactId = cursor.getLong(contactIdIndex);
                Optional<Contact> optional = getDeviceContactWithId(contactId);
                if (!optional.isPresent()) {
                    continue;
                }
                Contact contact = optional.get();
                DayDate birthday = contact.getBirthday().asDayDate();
                contacts.add(new ContactEvent(EventType.BIRTHDAY, birthday, contact));
            }
        } finally {
            cursor.close();
        }
        return contacts;
    }

    private Optional<Contact> getDeviceContactWithId(long id) {
        try {
            Contact contact = contactProvider.getOrCreateContact(id);
            return new Optional<>(contact);
        } catch (ContactNotFoundException e) {
            return NO_CONTACT;
        }
    }

    private boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

    private void storeContactsToProvider(List<ContactEvent> contacts) {
        ContentValues[] values = marshaller.marshall(contacts);
        persister.insertAnnualEvents(values);
    }

    /**
     * Contract that queries birthdays only
     */
    private static final class BirthdayQuery {
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

        public static final String[] WHERE_ARGS = {ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};
        public final static String SORT_ORDER = COL_DISPLAY_NAME;
        public static final String[] PROJECTION = {ContactsContract.Data.CONTACT_ID};
        public static final int ID = 0;

    }
}

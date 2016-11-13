package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsQuery;
import com.alexstyl.specialdates.contact.DeviceContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DeviceContactsQuery {

    private static final String WHERE = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.Data.IN_VISIBLE_GROUP + "=1";
    private static final String[] WHERE_ARGS = {ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};

    private final ContentResolver resolver;

    public DeviceContactsQuery(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public List<Contact> getAllContacts() {
        Cursor cursor = resolver.query(
                ContactsQuery.CONTENT_URI,
                ContactsQuery.PROJECTION,
                WHERE,
                WHERE_ARGS,
                ContactsQuery.SORT_ORDER
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

    private Contact createContactFrom(Cursor cursor) {
        long contactID = getContactIdFrom(cursor);
        DisplayName displayName = getDisplayNameFrom(cursor);
        String lookupKey = getLookupKeyFrom(cursor);
        return new DeviceContact(contactID, displayName, lookupKey);
    }

    private static long getContactIdFrom(Cursor cursor) {
        return cursor.getLong(ContactsQuery.CONTACT_ID);
    }

    private static DisplayName getDisplayNameFrom(Cursor cursor) {
        return DisplayName.from(cursor.getString(ContactsQuery.DISPLAY_NAME));
    }

    private static String getLookupKeyFrom(Cursor cursor) {
        return cursor.getString(ContactsQuery.LOOKUP_KEY);
    }

}

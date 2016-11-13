package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

import com.alexstyl.specialdates.DisplayName;

import static com.alexstyl.specialdates.contact.ContactsQuery.SORT_ORDER;

class DeviceContactFactory {

    private static final String SELECTION_CONTACT_WITH_ID = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
    private final ContentResolver resolver;

    DeviceContactFactory(ContentResolver contentResolver) {
        resolver = contentResolver;
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
        String lookupKey = getLookupKeyFrom(cursor);
        return new DeviceContact(contactID, displayName, lookupKey);
    }

    private Cursor queryContactsWithContactId(long contactID) {
        return resolver.query(
                ContactsQuery.CONTENT_URI,
                ContactsQuery.PROJECTION,
                SELECTION_CONTACT_WITH_ID,
                new String[]{
                        String.valueOf(contactID),
                        StructuredName.CONTENT_ITEM_TYPE
                },
                SORT_ORDER + " LIMIT 1"
        );
    }

    private static boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
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

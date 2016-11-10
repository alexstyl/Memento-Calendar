package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.DisplayName;

class DeviceContactFactory {

    private final ContentResolver resolver;

    DeviceContactFactory(ContentResolver contentResolver) {
        resolver = contentResolver;
    }

    DeviceContact createContactWithId(long contactID) throws ContactNotFoundException {
        String selection = ContactsContract.Data.CONTACT_ID + " = " + contactID + " AND " + ContactsContract.Data.MIMETYPE + " = ?";

        Cursor cursor = resolver.query(ContactsQuery.CONTENT_URI, ContactsQuery.PROJECTION, selection, new String[]{
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        }, ContactsQuery.SORT_ORDER + " LIMIT 1");
        if (isInvalid(cursor)) {
            throw new RuntimeException("Cursor was invalid");
        }

        try {
            if (cursor.moveToFirst()) {
                DisplayName displayName = getDisplayNameFrom(cursor);
                String lookupKey = getLookupKeyFrom(cursor);
                return new DeviceContact(contactID, displayName, lookupKey);
            }
        } finally {
            cursor.close();
        }
        throw new ContactNotFoundException(contactID);
    }

    private static boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

    private static DisplayName getDisplayNameFrom(Cursor cursor) {
        return DisplayName.from(cursor.getString(ContactsQuery.DISPLAY_NAME));
    }

    private static String getLookupKeyFrom(Cursor cursor) {
        return cursor.getString(ContactsQuery.LOOKUP_KEY);
    }

}

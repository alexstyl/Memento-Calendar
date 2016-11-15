package com.alexstyl.specialdates.service;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.events.peopleevents.CustomEventType;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;

class CustomEventProvider {

    private static final Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;
    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Event.LABEL
    };
    private static final String SELECTION =
            "( " + ContactsContract.Data._ID + " = ?" +
                    " AND " + ContactsContract.Data.MIMETYPE + " = ? " +
                    " AND " + ContactsContract.Data.IN_VISIBLE_GROUP + " = 1" +
                    ")";

    private static final String SORT_ORDER = ContactsContract.CommonDataKinds.Event._ID + " LIMIT 1";

    private final ContentResolver resolver;

    CustomEventProvider(ContentResolver resolver) {
        this.resolver = resolver;
    }

    EventType getEventWithId(long deviceId) {
        Cursor cursor = resolver.query(CONTENT_URI, PROJECTION, SELECTION, new String[]{
                String.valueOf(deviceId),
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        }, SORT_ORDER);
        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL);
                String eventName = cursor.getString(columnIndex);
                return new CustomEventType(eventName);
            }
        } finally {
            cursor.close();
        }
        return StandardEventType.OTHER;
    }
}

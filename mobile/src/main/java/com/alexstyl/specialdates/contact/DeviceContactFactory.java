package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.util.DateParser;

class DeviceContactFactory {

    private final ContentResolver resolver;
    private final DateParser dateParser;

    DeviceContactFactory(ContentResolver contentResolver, DateParser dateParser) {
        resolver = contentResolver;
        this.dateParser = dateParser;
    }

    public DeviceContact createContactWithId(long contactID) throws ContactNotFoundException {
        String selection = ContactsContract.Data.CONTACT_ID + " = " + contactID;
        String birthdayRow = "(" + ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + ")";
        String nameRow = ContactsContract.Data.MIMETYPE + " = ?";
        String str = selection + " AND (" + birthdayRow + " OR " + nameRow + ")";

        Cursor cursor = resolver.query(ContactsQuery.CONTENT_URI, ContactsQuery.PROJECTION, str, new String[]{
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        }, ContactsQuery.SORT_ORDER);
        if (isInvalid(cursor)) {
            throw new RuntimeException("Cursor was invalid");
        }
        String lookupKey = null;
        DisplayName displayName = null;
        Birthday birthday = null;
        boolean found = false;
        while (cursor.moveToNext()) {
            if (ContactsQuery.isBirthdayRow(cursor)) {
                birthday = getBirthdayFrom(cursor);
                found = true;
            }

            if (displayName == null) {
                displayName = getDisplayNameFrom(cursor);
                lookupKey = getLookupKeyFrom(cursor);
                found = true;
            }
        }

        if (!found) {
            throw new ContactNotFoundException(contactID);
        }
        DeviceContact contact = new DeviceContact(contactID, displayName, lookupKey, birthday);
        cursor.close();
        return contact;
    }

    private boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

    private String getLookupKeyFrom(Cursor cursor) {
        return cursor.getString(ContactsQuery.LOOKUP_KEY);
    }

    private DisplayName getDisplayNameFrom(Cursor cursor) {
        return DisplayName.from(cursor.getString(ContactsQuery.DISPLAY_NAME));
    }

    private Birthday getBirthdayFrom(Cursor cursor) {
        String birthday = cursor.getString(ContactsQuery.BIRTHDAY);
        try {
            DayDate parse = dateParser.parse(birthday);
            return Birthday.on(parse);
        } catch (DateParseException e) {
            ErrorTracker.track(e);
        }
        return null;
    }

}

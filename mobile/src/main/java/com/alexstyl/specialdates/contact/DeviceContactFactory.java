package com.alexstyl.specialdates.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.ParsedDate;
import com.alexstyl.specialdates.util.ContactEventDateParser;

class DeviceContactFactory {

    private final ContentResolver resolver;
    private final ContactEventDateParser dateParser;

    DeviceContactFactory(ContentResolver contentResolver, ContactEventDateParser dateParser) {
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
        Optional<Birthday> birthday = Optional.absent();
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

    private Optional<Birthday> getBirthdayFrom(Cursor cursor) {
        String birthdayRaw = cursor.getString(ContactsQuery.BIRTHDAY);
        try {
            Birthday birthday = getBirthdayFrom(birthdayRaw);
            return new Optional<>(birthday);
        } catch (DateParseException e) {
            ErrorTracker.track(e);
            return Optional.absent();
        }
    }

    private Birthday getBirthdayFrom(String birthdayRaw) throws DateParseException {
        ParsedDate date = dateParser.parse(birthdayRaw);
        Birthday birthday;
        if (date.hasYear()) {
            birthday = Birthday.on(date.getDayOfMonth(), date.getMonth(), date.getYear());
        } else {
            birthday = Birthday.on(date.getDayOfMonth(), date.getMonth());
        }
        return birthday;
    }

}

package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactNotFoundException;
import com.alexstyl.specialdates.contact.AndroidContactsProvider;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.logger.simple.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alexstyl.specialdates.events.peopleevents.StandardEventType.*;

class PeopleEventsRepository {

    private static final Uri CONTENT_URI = ContactsContract.Data.CONTENT_URI;
    private static final String[] PROJECTION = {
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.CommonDataKinds.Event.TYPE,
            ContactsContract.CommonDataKinds.Event._ID,
            ContactsContract.CommonDataKinds.Event.START_DATE
    };
    private static final String SELECTION =
            "( " + ContactsContract.Data.MIMETYPE + " = ? "
                    + " AND " + ContactsContract.Data.IN_VISIBLE_GROUP + " = 1)";

    private static final String[] SELECT_ARGS = {ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};
    private static final String SORT_ORDER = null;

    private static final List<ContactEvent> NO_EVENTS = Collections.emptyList();

    private final ContentResolver contentResolver;
    private final AndroidContactsProvider contactsProvider;
    private final DateParser dateParser;

    PeopleEventsRepository(ContentResolver contentResolver, AndroidContactsProvider contactsProvider, DateParser dateParser) {
        this.contentResolver = contentResolver;
        this.contactsProvider = contactsProvider;
        this.dateParser = dateParser;
    }

    List<ContactEvent> fetchPeopleWithEvents() {
        Cursor cursor = contentResolver.query(CONTENT_URI, PROJECTION, SELECTION, SELECT_ARGS, SORT_ORDER);
        if (isInvalid(cursor)) {
            return NO_EVENTS;
        }
        List<ContactEvent> events = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                long contactId = getContactIdFrom(cursor);
                EventType eventType = getEventTypeFrom(cursor);
                try {
                    Date eventDate = getEventDateFrom(cursor);
                    long eventId = getEventIdFrom(cursor);
                    Contact contact = contactsProvider.getOrCreateContact(contactId);
                    events.add(new ContactEvent(new Optional<>(eventId), eventType, eventDate, contact));
                } catch (DateParseException e) {
                    ErrorTracker.track(e);
                } catch (ContactNotFoundException e) {
                    Log.e(e);
                }
            }
        } finally {
            cursor.close();
        }
        return events;
    }

    private static long getEventIdFrom(Cursor cursor) {
        int eventIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event._ID);
        return cursor.getLong(eventIdIndex);
    }

    private long getContactIdFrom(Cursor cursor) {
        int contactIdIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        return cursor.getLong(contactIdIndex);
    }

    private Date getEventDateFrom(Cursor cursor) throws DateParseException {
        int dateIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
        String dateRaw = cursor.getString(dateIndex);

        return dateParser.parse(dateRaw);
    }

    private EventType getEventTypeFrom(Cursor cursor) {
        int eventTypeIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE);
        int eventTypeRaw = cursor.getInt(eventTypeIndex);
        switch (eventTypeRaw) {
            case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY:
                return BIRTHDAY;
            case ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY:
                return ANNIVERSARY;
            case ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM:
                return CUSTOM;
        }
        return OTHER;
    }

    private boolean isInvalid(Cursor cursor) {
        return cursor == null || cursor.isClosed();
    }

}

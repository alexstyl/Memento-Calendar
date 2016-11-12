package com.alexstyl.specialdates.events.database;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.events.peopleevents.EventType;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;

public class PeopleEventsContract {

    public static final String AUTHORITY = "com.alexstyl.specialdates.peopleeventsprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class PeopleEvents implements BaseColumns, ContactColumns, EventColumns {

        private PeopleEvents() {
            // hide this
        }

        public static final String PATH = "people_events";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(PeopleEventsContract.CONTENT_URI, PATH);
        public static final String SORT_ORDER_DEFAULT = DATE + " ASC";

        public static Date getDateFrom(Cursor cursor) {
            int index = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.DATE);
            String text = cursor.getString(index);
            return from(text);
        }

        public static long getContactIdFrom(Cursor cursor) {
            int contactIdIndex = cursor.getColumnIndexOrThrow(PeopleEvents.CONTACT_ID);
            return cursor.getLong(contactIdIndex);
        }

        public static EventType getEventType(Cursor cursor) {
            int eventTypeIndex = cursor.getColumnIndexOrThrow(PeopleEvents.EVENT_TYPE);
            @EventTypeId int rawEventType = cursor.getInt(eventTypeIndex);
            return EventType.fromId(rawEventType);
        }

        public static Date from(String text) {
            try {
                return DateParser.INSTANCE.parse(text);
            } catch (DateParseException e) {
                e.printStackTrace();
                throw new DeveloperError("Invalid date stored to database. [" + text + "]");
            }
        }
    }

}

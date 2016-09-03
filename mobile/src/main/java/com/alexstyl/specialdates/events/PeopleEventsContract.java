package com.alexstyl.specialdates.events;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.alexstyl.specialdates.date.DateProvider;
import com.alexstyl.specialdates.date.DayDate;

public class PeopleEventsContract {

    public static final String AUTHORITY = "com.alexstyl.specialdates.peopleeventsprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class PeopleEvents implements BaseColumns, ContactColumns, EventColumns {

        private PeopleEvents() {
        }

        private static final DateProvider DATE_PROVIDER = new DateProvider();

        public static final String PATH = "people_events";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(PeopleEventsContract.CONTENT_URI, PATH);
        public static final String SORT_ORDER_DEFAULT = DATE + " ASC";

        public static DayDate getDateFrom(Cursor cursor) {
            int index = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.DATE);
            String text = cursor.getString(index);
            return DATE_PROVIDER.from(text);
        }

        public static long getContactIdFrom(Cursor cursor) {
            int contactIdIndex = cursor.getColumnIndexOrThrow(PeopleEvents.CONTACT_ID);
            return cursor.getLong(contactIdIndex);
        }

        public static EventType getEventType(Cursor cursor) {
            int eventTypeIndex = cursor.getColumnIndexOrThrow(PeopleEvents.EVENT_TYPE);
            int anInt = cursor.getInt(eventTypeIndex);
            return EventType.fromId(anInt);
        }
    }

}

package com.alexstyl.specialdates.events;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.alexstyl.specialdates.date.DayDate;

public class PeopleEventsContract {

    public static final String AUTHORITY = "com.alexstyl.specialdates.peopleeventsprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static class PeopleEvents implements BaseColumns, ContactColumns, EventColumns {

        private PeopleEvents() {
        }

        public static final String PATH = "people_events";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + PATH;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(PeopleEventsContract.CONTENT_URI, PATH);
        public static final String SORT_ORDER_DEFAULT = DATE + " ASC";

        public static DayDate getDateFrom(Cursor cursor) {
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

        private static final String SEPARATOR = "-";

        public static DayDate from(String text) {
            int dayToMonth = text.lastIndexOf(SEPARATOR);
            int monthToYear = text.lastIndexOf(SEPARATOR, dayToMonth - 1);

            int day = Integer.valueOf(text.substring(dayToMonth + 1, text.length()));
            int month = Integer.valueOf(text.substring(monthToYear + 1, dayToMonth));
            // need to check if we have year

            if (text.startsWith(SEPARATOR)) {
                return DayDate.newInstance(day, month);
            }

            int yearToMonth = text.indexOf(SEPARATOR);
            int year = Integer.valueOf(text.substring(0, yearToMonth));
            return DayDate.newInstance(day, month, year);
        }
    }

}

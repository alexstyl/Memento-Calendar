package com.alexstyl.specialdates.events.database;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
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

        public static Date getDateFrom(Cursor cursor) {
            int index = cursor.getColumnIndexOrThrow(PeopleEventsContract.PeopleEvents.DATE);
            String text = cursor.getString(index);
            return from(text);
        }

        public static long getContactIdFrom(Cursor cursor) {
            int contactIdIndex = cursor.getColumnIndexOrThrow(PeopleEvents.CONTACT_ID);
            return cursor.getLong(contactIdIndex);
        }

        public static StandardEventType getEventType(Cursor cursor) {
            int eventTypeIndex = cursor.getColumnIndexOrThrow(PeopleEvents.EVENT_TYPE);
            @EventTypeId int rawEventType = cursor.getInt(eventTypeIndex);
            if (rawEventType == EventColumns.TYPE_CUSTOM) {
                // TODO query custom event
                return StandardEventType.OTHER;
            }
            return StandardEventType.fromId(rawEventType);
        }

        public static Date from(String text) {
            try {
                return DateParser.INSTANCE.parse(text);
            } catch (DateParseException e) {
                e.printStackTrace();
                throw new DeveloperError("Invalid date stored to database. [" + text + "]");
            }
        }

        public static Optional<Long> getDeviceEventIdFrom(Cursor cursor) {
            int eventId = cursor.getColumnIndexOrThrow(PeopleEvents.DEVICE_EVENT_ID);
            long deviceEventId = cursor.getLong(eventId);
            if (isALegitEventId(deviceEventId)) {
                return Optional.absent();
            }
            return new Optional<>(deviceEventId);
        }

        private static boolean isALegitEventId(long deviceEventId) {
            return deviceEventId == -1;
        }
    }

}

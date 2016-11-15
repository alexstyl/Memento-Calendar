package com.alexstyl.specialdates.events.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

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
    }

}

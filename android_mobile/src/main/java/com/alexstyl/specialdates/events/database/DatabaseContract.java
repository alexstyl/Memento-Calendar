package com.alexstyl.specialdates.events.database;

import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final class AnnualEventsContract implements BaseColumns, ContactColumns, EventColumns {
        public static final String TABLE_NAME = "annual_events";
    }

    private DatabaseContract() {
        // hide this
    }
}

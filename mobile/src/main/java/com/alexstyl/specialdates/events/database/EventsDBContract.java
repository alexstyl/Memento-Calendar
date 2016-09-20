package com.alexstyl.specialdates.events.database;

import android.provider.BaseColumns;

public final class EventsDBContract {

    public static final class AnnualEvents implements BaseColumns, ContactColumns, EventColumns {

        public static final String TABLE_NAME = "annual_events";
    }

    public static final class DynamicEvents implements BaseColumns, ContactColumns, EventColumns {
        public static final String TABLE_NAME = "dynamic_events";
    }

}

package com.alexstyl.specialdates.events.database;

import android.provider.BaseColumns;

public final class EventsDBContract {

    public static final class AnnualEventsContract implements BaseColumns, ContactColumns, EventColumns {

        public static final String TABLE_NAME = "annual_events";
    }

    /**
     * @deprecated No need to store dynamic events anymore. we'll do that on the fly
     */
    public static final class DynamicEventsContract implements BaseColumns, ContactColumns, EventColumns {
        public static final String TABLE_NAME = "dynamic_events";
    }

}

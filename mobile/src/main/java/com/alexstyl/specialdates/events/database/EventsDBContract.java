package com.alexstyl.specialdates.events.database;

import android.provider.BaseColumns;

import com.alexstyl.specialdates.events.ContactColumns;
import com.alexstyl.specialdates.events.EventColumns;

public class EventsDBContract {

    public static class AnnualEvents implements BaseColumns, ContactColumns, EventColumns {

        public static final String TABLE_NAME = "annual_events";
    }

    public static class DynamicEvents implements BaseColumns, ContactColumns, EventColumns {
        public static final String TABLE_NAME = "dynamic_events";
    }

}

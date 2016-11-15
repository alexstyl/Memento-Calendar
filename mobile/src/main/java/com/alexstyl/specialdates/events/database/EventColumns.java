package com.alexstyl.specialdates.events.database;

public interface EventColumns {
    String EVENT_TYPE = "event_type";
    /**
     * Will be set to -1 if not available
     */
    String DEVICE_EVENT_ID = "device_event_id";

    int TYPE_BIRTHDAY = 0;
    int TYPE_NAMEDAY = 1;
    int TYPE_ANNIVERSARY = 2;
    int TYPE_OTHER = 3;
    int TYPE_CUSTOM = 4;

    String DATE = "date";
}

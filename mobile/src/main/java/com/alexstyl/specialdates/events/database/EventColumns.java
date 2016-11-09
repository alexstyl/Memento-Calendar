package com.alexstyl.specialdates.events.database;

interface EventColumns {
    String EVENT_TYPE = "event_type";
    int TYPE_BIRTHDAY = 0;
    int TYPE_NAMEDAY = 1;
    int TYPE_ANNIVERSARY = 2;
    int TYPE_OTHER = 3;
    int TYPE_CUSTOM = 4;

    String DATE = "date";
}

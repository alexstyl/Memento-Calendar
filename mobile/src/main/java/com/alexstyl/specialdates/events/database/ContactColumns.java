package com.alexstyl.specialdates.events.database;

public interface ContactColumns {
    String CONTACT_ID = "contact_id";
    String DISPLAY_NAME = "display_name";
    String SOURCE = "contact_source";

    /**
     * Events that exist within the device (such as Birthdays or Anniversaries)
     */
    int SOURCE_DEVICE = 0;
    /**
     * Events created by the app
     */
    int SOURCE_MEMENTO = 1;
}

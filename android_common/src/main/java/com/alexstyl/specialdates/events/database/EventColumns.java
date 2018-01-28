package com.alexstyl.specialdates.events.database;

public interface EventColumns {
    /**
     * A value of {@link EventTypeId} that indicates the type of event
     */
    String EVENT_TYPE = "event_type";

    /**
     * The id of the same event stored in the device's default database
     * <p>Will be set to -1 if not available</p>
     */
    String DEVICE_EVENT_ID = "device_event_id";

    String DATE = "date";
    String SOURCE = "source";

    /**
     * A value to indicate whether the user is interested in seeing this event.
     * <p>1 for visible, 0 for not-visible</p>
     */
    String VISIBLE = "visible";
}

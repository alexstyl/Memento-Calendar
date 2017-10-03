package com.alexstyl.specialdates.events.peopleevents;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.events.database.EventTypeId;

import java.util.HashMap;
import java.util.Map;

public enum StandardEventType implements EventType {
    BIRTHDAY(EventTypeId.TYPE_BIRTHDAY),
    NAMEDAY(EventTypeId.TYPE_NAMEDAY),
    ANNIVERSARY(EventTypeId.TYPE_ANNIVERSARY),
    OTHER(EventTypeId.TYPE_OTHER),
    CUSTOM(EventTypeId.TYPE_CUSTOM);

    @EventTypeId
    private final int eventTypeId;

    StandardEventType(@EventTypeId int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    private static final Map<Integer, StandardEventType> MAP;

    static {
        MAP = new HashMap<>();
        for (StandardEventType eventType : values()) {
            MAP.put(eventType.eventTypeId, eventType);
        }
    }

    public static StandardEventType fromId(@EventTypeId int eventTypeId) {
        if (MAP.containsKey(eventTypeId)) {
            return MAP.get(eventTypeId);
        }
        throw new IllegalArgumentException("No event type with eventTypeId " + eventTypeId);
    }

    @Override
    public String getEventName(@NonNull Strings strings) {
        return strings.nameOfEvent(this);
    }

    @EventTypeId
    public int getId() {
        return eventTypeId;
    }
}

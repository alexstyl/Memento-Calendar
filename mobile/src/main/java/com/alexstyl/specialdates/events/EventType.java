package com.alexstyl.specialdates.events;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.R;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    BIRTHDAY(EventColumns.TYPE_BIRTHDAY, R.string.birthday, R.color.birthday_red),
    NAMEDAY(EventColumns.TYPE_NAMEDAY, R.string.nameday, R.color.nameday_blue);

    private static final Map<Integer, EventType> map;

    static {
        map = new HashMap<>();
        for (EventType eventType : values()) {
            map.put(eventType.id, eventType);
        }
    }

    private final int id;
    private final int eventNameRes;
    private final int eventColorRes;

    EventType(@EventTypeId int id, @StringRes int nameResId, @ColorRes int colorResId) {
        this.id = id;
        this.eventNameRes = nameResId;
        this.eventColorRes = colorResId;
    }

    @StringRes
    public int nameRes() {
        return eventNameRes;
    }

    @ColorRes
    public int getColorRes() {
        return eventColorRes;
    }

    public static EventType fromId(@EventTypeId int eventTypeId) {
        if (map.containsKey(eventTypeId)) {
            return map.get(eventTypeId);
        }
        throw new IllegalArgumentException("No event type with id " + eventTypeId);
    }
}

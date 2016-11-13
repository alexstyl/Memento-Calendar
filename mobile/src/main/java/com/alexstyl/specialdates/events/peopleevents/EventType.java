package com.alexstyl.specialdates.events.peopleevents;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    BIRTHDAY(AnnualEventsContract.TYPE_BIRTHDAY, R.string.birthday, R.color.birthday_red),
    NAMEDAY(AnnualEventsContract.TYPE_NAMEDAY, R.string.nameday, R.color.nameday_blue),
    ANNIVERSARY(AnnualEventsContract.TYPE_ANNIVERSARY, R.string.Anniversary, R.color.anniversary_yellow),
    OTHER(AnnualEventsContract.TYPE_OTHER, R.string.Other, R.color.past_date_grey),
    CUSTOM(AnnualEventsContract.TYPE_CUSTOM, R.string.Custom, R.color.past_date_grey);

    @EventTypeId
    private final int eventTypeId;
    private final int eventNameRes;
    private final int eventColorRes;

    EventType(@EventTypeId int eventTypeId, @StringRes int nameResId, @ColorRes int colorResId) {
        this.eventTypeId = eventTypeId;
        this.eventNameRes = nameResId;
        this.eventColorRes = colorResId;
    }

    private static final Map<Integer, EventType> map;

    static {
        map = new HashMap<>();
        for (EventType eventType : values()) {
            map.put(eventType.eventTypeId, eventType);
        }
    }

    public static EventType fromId(@EventTypeId int eventTypeId) {
        if (map.containsKey(eventTypeId)) {
            return map.get(eventTypeId);
        }
        throw new IllegalArgumentException("No event type with eventTypeId " + eventTypeId);
    }

    @StringRes
    public int nameRes() {
        return eventNameRes;
    }

    @ColorRes
    public int getColorRes() {
        return eventColorRes;
    }

    @EventTypeId
    public int getId() {
        return eventTypeId;
    }
}

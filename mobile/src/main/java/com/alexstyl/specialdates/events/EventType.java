package com.alexstyl.specialdates.events;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.R;
import com.novoda.notils.exception.DeveloperError;

public enum EventType {
    BIRTHDAY(EventColumns.TYPE_BIRTHDAY, R.string.birthday, R.color.birthday_red),
    NAMEDAY(EventColumns.TYPE_NAMEDAY, R.string.nameday, R.color.nameday_blue);

    private final int id;
    private final int eventNameRes;
    private final int eventColorRes;

    EventType(int id, @StringRes int eventNameRes, @ColorRes int eventColorRes) {
        this.id = id;
        this.eventNameRes = eventNameRes;
        this.eventColorRes = eventColorRes;
    }

    @StringRes
    public int nameRes() {
        return eventNameRes;
    }

    @ColorRes
    public int getColorRes() {
        return eventColorRes;
    }

    public static EventType fromId(int eventTypeId) {
        for (EventType eventType : values()) {
            if (eventType.id == eventTypeId) {
                return eventType;
            }
        }
        throw new DeveloperError("No event type with id " + eventTypeId);
    }
}

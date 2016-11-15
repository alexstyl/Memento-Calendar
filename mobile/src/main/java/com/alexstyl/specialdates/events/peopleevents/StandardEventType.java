package com.alexstyl.specialdates.events.peopleevents;

import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.database.EventColumns;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.novoda.notils.exception.DeveloperError;

import java.util.HashMap;
import java.util.Map;

public enum StandardEventType implements EventType {
    BIRTHDAY(AnnualEventsContract.TYPE_BIRTHDAY, R.string.birthday, R.color.birthday_red),
    NAMEDAY(AnnualEventsContract.TYPE_NAMEDAY, R.string.nameday, R.color.nameday_blue),
    ANNIVERSARY(AnnualEventsContract.TYPE_ANNIVERSARY, R.string.Anniversary, R.color.anniversary_yellow),
    OTHER(AnnualEventsContract.TYPE_OTHER, R.string.Other, R.color.past_date_grey);

    @EventTypeId
    private final int eventTypeId;
    private final int eventNameRes;
    private final int eventColorRes;

    StandardEventType(@EventTypeId int eventTypeId, @StringRes int nameResId, @ColorRes int colorResId) {
        this.eventTypeId = eventTypeId;
        this.eventNameRes = nameResId;
        this.eventColorRes = colorResId;
    }

    private static final Map<Integer, StandardEventType> map;

    static {
        map = new HashMap<>();
        for (StandardEventType eventType : values()) {
            map.put(eventType.eventTypeId, eventType);
        }
    }

    public static StandardEventType fromId(@EventTypeId int eventTypeId) {
        if (eventTypeId == EventColumns.TYPE_CUSTOM) {
            throw new DeveloperError("Custom events need to be created separately. Use the CustomEventType class to create it.");
        }
        if (map.containsKey(eventTypeId)) {
            return map.get(eventTypeId);
        }
        throw new IllegalArgumentException("No event type with eventTypeId " + eventTypeId);
    }

    @Override
    public String getEventName(Resources resources) {
        return resources.getString(eventNameRes);
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

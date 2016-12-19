package com.alexstyl.specialdates.events.peopleevents;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.R;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventTypeId;

import java.util.HashMap;
import java.util.Map;

public enum StandardEventType implements EventType {
    BIRTHDAY(AnnualEventsContract.TYPE_BIRTHDAY, R.string.birthday, R.color.birthday_red, R.drawable.ic_cake),
    NAMEDAY(AnnualEventsContract.TYPE_NAMEDAY, R.string.nameday, R.color.nameday_blue, R.drawable.ic_face),
    ANNIVERSARY(AnnualEventsContract.TYPE_ANNIVERSARY, R.string.Anniversary, R.color.anniversary_yellow, R.drawable.ic_anniversary),
    OTHER(AnnualEventsContract.TYPE_OTHER, R.string.Other, R.color.past_date_grey, R.drawable.ic_other),
    CUSTOM(AnnualEventsContract.TYPE_CUSTOM, R.string.Custom, R.color.past_date_grey, R.drawable.ic_custom);

    @EventTypeId
    private final int eventTypeId;
    @StringRes
    private final int eventNameRes;
    @ColorRes
    private final int eventColorRes;
    @DrawableRes
    private int iconResId;

    StandardEventType(@EventTypeId int eventTypeId, @StringRes int nameResId, @ColorRes int colorResId, @DrawableRes int iconResId) {
        this.eventTypeId = eventTypeId;
        this.eventNameRes = nameResId;
        this.eventColorRes = colorResId;
        this.iconResId = iconResId;
    }

    private static final Map<Integer, StandardEventType> map;

    static {
        map = new HashMap<>();
        for (StandardEventType eventType : values()) {
            map.put(eventType.eventTypeId, eventType);
        }
    }

    public static StandardEventType fromId(@EventTypeId int eventTypeId) {
        if (map.containsKey(eventTypeId)) {
            return map.get(eventTypeId);
        }
        throw new IllegalArgumentException("No event type with eventTypeId " + eventTypeId);
    }

    @Override
    public String getEventName(StringResources stringResources) {
        return stringResources.getString(eventNameRes);
    }

    @ColorRes
    public int getColorRes() {
        return eventColorRes;
    }

    @EventTypeId
    public int getId() {
        return eventTypeId;
    }

    @DrawableRes
    @Override
    public int getIconResId() {
        return iconResId;
    }
}

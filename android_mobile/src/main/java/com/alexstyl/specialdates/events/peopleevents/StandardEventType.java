package com.alexstyl.specialdates.events.peopleevents;

import android.provider.ContactsContract.CommonDataKinds.Event;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.database.EventTypeId;

import java.util.HashMap;
import java.util.Map;

import static com.alexstyl.specialdates.events.database.EventTypeId.*;

public enum StandardEventType implements EventType {
    BIRTHDAY(TYPE_BIRTHDAY, R.string.birthday, R.color.birthday_red, R.drawable.ic_cake, Event.TYPE_BIRTHDAY),
    NAMEDAY(TYPE_NAMEDAY, R.string.nameday, R.color.nameday_blue, R.drawable.ic_face, -1) {
        @Override
        public int getAndroidType() {
            throw new UnsupportedOperationException("Namedays are not an Android supported event");
        }
    },
    ANNIVERSARY(TYPE_ANNIVERSARY, R.string.Anniversary, R.color.anniversary_yellow, R.drawable.ic_anniversary, Event.TYPE_ANNIVERSARY),
    OTHER(TYPE_OTHER, R.string.Other, R.color.past_date_grey, R.drawable.ic_other, Event.TYPE_OTHER),
    CUSTOM(TYPE_CUSTOM, R.string.Custom, R.color.past_date_grey, R.drawable.ic_custom, Event.TYPE_CUSTOM);

    @EventTypeId
    private final int eventTypeId;
    @StringRes
    private final int eventNameRes;
    @ColorRes
    private final int eventColorRes;
    @DrawableRes
    private int iconResId;

    private final int androidType;

    StandardEventType(@EventTypeId int eventTypeId, @StringRes int nameResId, @ColorRes int colorResId, @DrawableRes int iconResId, int androidType) {
        this.eventTypeId = eventTypeId;
        this.eventNameRes = nameResId;
        this.eventColorRes = colorResId;
        this.iconResId = iconResId;
        this.androidType = androidType;
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
    public String getEventName(Strings strings) {
        return strings.nameOfEvent(eventNameRes);
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

    @Override
    public int getAndroidType() {
        return androidType;
    }
}

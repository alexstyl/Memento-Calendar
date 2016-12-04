package com.alexstyl.specialdates.events.peopleevents;

import android.content.res.Resources;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.events.database.EventColumns;

public class CustomEventType implements EventType {

    private final String name;

    public CustomEventType(String name) {
        this.name = name;
    }

    @Override
    public String getEventName(Resources resources) {
        return name;
    }

    @Override
    public int getColorRes() {
        return R.color.purple_custom_event;
    }

    @Override
    public int getId() {
        return EventColumns.TYPE_CUSTOM;
    }
}

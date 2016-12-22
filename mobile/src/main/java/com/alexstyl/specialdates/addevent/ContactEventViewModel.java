package com.alexstyl.specialdates.addevent;

import android.support.annotation.DrawableRes;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;

final class ContactEventViewModel {

    private final String eventHint;
    private final EventType eventType;
    private final Optional<Date> dateOptional;

    ContactEventViewModel(String eventHint, EventType eventType, Optional<Date> dateOptional) {
        this.eventType = eventType;
        this.eventHint = eventHint;
        this.dateOptional = dateOptional;
    }

    @DrawableRes
    int getEventIconRes() {
        return getEventType().getIconResId();
    }

    String getHintText() {
        return eventHint;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Optional<Date> getDate() {
        return dateOptional;
    }
}

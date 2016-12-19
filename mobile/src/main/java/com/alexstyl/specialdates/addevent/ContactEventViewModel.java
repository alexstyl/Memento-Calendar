package com.alexstyl.specialdates.addevent;

import android.support.annotation.DrawableRes;

import com.alexstyl.specialdates.events.peopleevents.EventType;

final class ContactEventViewModel {

    private final String eventHint;
    private final EventType eventType;

    ContactEventViewModel(String eventHint, EventType eventType) {
        this.eventType = eventType;
        this.eventHint = eventHint;
    }

    @DrawableRes
    int getEventIconRes() {
        return eventType.getIconResId();
    }

    String getHintText() {
        return eventHint;
    }

    public EventType getEventType() {
        return eventType;
    }
}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactEventViewModel that = (ContactEventViewModel) o;

        if (!eventHint.equals(that.eventHint)) {
            return false;
        }
        if (!eventType.equals(that.eventType)) {
            return false;
        }
        return dateOptional.equals(that.dateOptional);

    }

    @Override
    public int hashCode() {
        int result = eventHint.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + dateOptional.hashCode();
        return result;
    }
}

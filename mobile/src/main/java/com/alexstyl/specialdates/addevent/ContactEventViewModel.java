package com.alexstyl.specialdates.addevent;

import android.support.annotation.DrawableRes;

import com.alexstyl.android.ViewVisibility;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;

final class ContactEventViewModel {

    private final String eventHint;
    private final EventType eventType;
    private final Optional<Date> dateOptional;
    private final int removeEventVisibility;

    ContactEventViewModel(String eventHint, EventType eventType, Optional<Date> dateOptional, @ViewVisibility int removeEventVisibility) {
        this.eventType = eventType;
        this.eventHint = eventHint;
        this.dateOptional = dateOptional;
        this.removeEventVisibility = removeEventVisibility;
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

    @ViewVisibility
    int getClearVisibility() {
        return removeEventVisibility;
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

        if (removeEventVisibility != that.removeEventVisibility) {
            return false;
        }
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
        result = 31 * result + removeEventVisibility;
        return result;
    }
}

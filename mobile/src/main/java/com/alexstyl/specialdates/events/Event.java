package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.peopleevents.EventType;

final public class Event {

    private final EventType eventType;
    private final Date date;

    public Event(EventType eventType, Date date) {
        this.eventType = eventType;
        this.date = date;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;

        if (!eventType.equals(event.eventType)) {
            return false;
        }
        return date.equals(event.date);

    }

    @Override
    public int hashCode() {
        int result = eventType.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}

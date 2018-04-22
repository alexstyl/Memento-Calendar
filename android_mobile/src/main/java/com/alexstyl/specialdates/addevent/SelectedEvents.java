package com.alexstyl.specialdates.addevent;

import android.annotation.SuppressLint;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.Event;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import java.util.Collection;
import java.util.HashMap;

public final class SelectedEvents {

    private final HashMap<Integer, Event> mappedEvents;

    @SuppressLint("UseSparseArrays")
    SelectedEvents() {
        mappedEvents = new HashMap<>();
    }

    public Collection<Event> getEvents() {
        return mappedEvents.values();
    }

    void replaceDate(EventType eventType, Date date) {
        int eventId = eventType.getId();
        Event event = new Event(eventType, date);
        mappedEvents.put(eventId, event);
    }

    public void remove(EventType eventType) {
        mappedEvents.remove(eventType.getId());
    }
}

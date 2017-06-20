package com.alexstyl.specialdates;

import java.util.ArrayList;
import java.util.List;

public class PeopleEventsMonitor {

    private final List<PeopleEventsUpdatedListener> listeners = new ArrayList<>();

    public void addListener(PeopleEventsUpdatedListener l) {
        synchronized (listeners) {
            this.listeners.add(l);
        }
    }

    public void removeListener(PeopleEventsUpdatedListener l) {
        synchronized (listeners) {
            this.listeners.remove(l);
        }
    }

    public interface PeopleEventsUpdatedListener {
        void onPeopleEventsUpdated();
    }
}

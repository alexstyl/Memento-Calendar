package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;

import com.alexstyl.specialdates.Monitor;

import java.util.Arrays;
import java.util.List;

class PeopleEventsMonitor implements Monitor {

    private final List<Monitor> monitors;

    public static PeopleEventsMonitor newInstance(Context context) {
        // contacts changed
        // events settings changed
        List<Monitor> contactsObservers = Arrays.asList((Monitor) new ContactsObserver(context.getContentResolver()));
        return new PeopleEventsMonitor(contactsObservers);
    }

    private PeopleEventsMonitor(List<Monitor> monitors) {
        this.monitors = monitors;
    }

    @Override
    public void startObserving(Callback callback) {
        for (Monitor monitor : monitors) {
            monitor.startObserving(callback);
        }
    }

    @Override
    public void stopObserving() {
        for (Monitor monitor : monitors) {
            monitor.stopObserving();
        }
    }
}

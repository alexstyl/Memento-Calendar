package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.Monitor;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Class that monitors whether the events need to be refreshed due to some user action.
 * <p>This action can be internal to the app (such as the user wants to see the namedays of a different locale)
 * or external (such as a change in the device database)</p>
 */
final class EventsRefreshRequestsMonitor implements Monitor {

    private final List<Monitor> monitors;

    public static EventsRefreshRequestsMonitor newInstance(Context context, Strings strings) {
        return new EventsRefreshRequestsMonitor(
                asList(
                        new ContactsObserver(context.getContentResolver()),
                        PeopleSettingsMonitor.newInstance(context, strings)
                ));
    }

    private EventsRefreshRequestsMonitor(List<Monitor> monitors) {
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

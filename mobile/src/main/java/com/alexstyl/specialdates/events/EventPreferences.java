package com.alexstyl.specialdates.events;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

public class EventPreferences {

    private final EasyPreferences preferences;

    public EventPreferences(Context context) {
        preferences = EasyPreferences.createForPrivatePreferences(context, R.string.pref_events);
    }

    public boolean hasBeenInitialised() {
        return preferences.getBoolean(R.string.key_events_are_initialised, false);
    }

    public void markEventsAsInitialised() {
        preferences.setBoolean(R.string.key_events_are_initialised, true);
    }
}

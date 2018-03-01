package com.alexstyl.specialdates.events.peopleevents

import android.content.Context

import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.R

class AndroidUpcomingEventSettings(context: Context) : UpcomingEventsSettings {

    private val preferences: EasyPreferences = EasyPreferences.createForPrivatePreferences(context, R.string.pref_events)

    override fun hasBeenInitialised(): Boolean {
        return preferences.getBoolean(R.string.key_events_are_initialised, false)
    }

    override fun markEventsAsInitialised() {
        preferences.setBoolean(R.string.key_events_are_initialised, true)
    }

    override fun reset() {
        preferences.setBoolean(R.string.key_events_are_initialised, false)
    }
}


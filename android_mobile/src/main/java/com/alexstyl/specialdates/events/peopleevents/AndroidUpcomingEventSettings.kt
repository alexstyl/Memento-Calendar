package com.alexstyl.specialdates.events.peopleevents

import android.content.Context

import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper

class AndroidUpcomingEventSettings(context: Context) : UpcomingEventsSettings {

    private val preferences: EasyPreferences = EasyPreferences.createForPrivatePreferences(context, R.string.pref_events)

    override fun hasBeenInitialised(): Boolean {
        return isUpdatedVersion() && preferences.getBoolean(R.string.key_events_are_initialised, false)
    }

    private fun isUpdatedVersion(): Boolean {
        val lastDatabaseVersion = preferences.getInt(R.string.key_database_version, -1)
        return lastDatabaseVersion >= EventSQLiteOpenHelper.DATABASE_VERSION
    }

    override fun markEventsAsInitialised() {
        preferences.setBoolean(R.string.key_events_are_initialised, true)
        preferences.setInteger(R.string.key_database_version, EventSQLiteOpenHelper.DATABASE_VERSION)
    }

    override fun reset() {
        preferences.setBoolean(R.string.key_events_are_initialised, false)
    }
}


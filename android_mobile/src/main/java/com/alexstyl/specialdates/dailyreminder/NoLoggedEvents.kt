package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.dailyreminder.log.LoggedEventsRepository

class NoLoggedEvents : LoggedEventsRepository {

    override fun writeEvents(events: String) {
        // Do nothing
    }

    override fun fetchAllEvents(): String {
        return ""
    }
}
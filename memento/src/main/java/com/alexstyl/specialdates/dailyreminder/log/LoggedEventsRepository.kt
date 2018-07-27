package com.alexstyl.specialdates.dailyreminder.log

interface LoggedEventsRepository {
    fun fetchAllEvents(): String
    fun writeEvents(events: String)
}

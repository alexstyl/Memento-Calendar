package com.alexstyl.specialdates.events.peopleevents

interface UpcomingEventsSettings {
    fun hasBeenInitialised(): Boolean
    fun markEventsAsInitialised()
    fun reset()
}

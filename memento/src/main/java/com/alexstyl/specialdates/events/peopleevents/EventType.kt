package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.events.database.EventTypeId

interface EventType {

    fun getEventName(strings: Strings): String

    @get:EventTypeId
    val id: Int
}

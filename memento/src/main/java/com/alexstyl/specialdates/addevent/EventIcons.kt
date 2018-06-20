package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.events.peopleevents.EventType

interface EventIcons {
    fun iconOf(eventType: EventType): Int
}

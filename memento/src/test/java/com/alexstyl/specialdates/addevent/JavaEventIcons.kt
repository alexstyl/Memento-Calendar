package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.events.peopleevents.EventType

class JavaEventIcons : EventIcons {
    override fun iconOf(eventType: EventType): Int = eventType.id

}

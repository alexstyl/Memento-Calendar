package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.events.database.EventTypeId
import com.alexstyl.specialdates.events.peopleevents.EventType

object AndroidEventIcons : EventIcons {
    override fun iconOf(eventType: EventType): Int = when (eventType.id) {
        EventTypeId.TYPE_BIRTHDAY -> R.drawable.ic_cake
        EventTypeId.TYPE_NAMEDAY -> R.drawable.ic_face
        EventTypeId.TYPE_ANNIVERSARY -> R.drawable.ic_anniversary
        EventTypeId.TYPE_OTHER -> R.drawable.ic_other
        EventTypeId.TYPE_CUSTOM -> R.drawable.ic_custom
        else -> {
            throw IllegalStateException("No icon for type $eventType")
        }
    }
}

package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.events.database.EventTypeId
import com.alexstyl.specialdates.events.peopleevents.EventType

object EventColors {
    fun colorOf(type: EventType): Int = when (type.id) {
        EventTypeId.TYPE_BIRTHDAY -> R.color.birthday_red
        EventTypeId.TYPE_NAMEDAY -> R.color.nameday_blue
        EventTypeId.TYPE_ANNIVERSARY -> R.color.anniversary_yellow
        EventTypeId.TYPE_CUSTOM -> R.color.purple_custom_event
        EventTypeId.TYPE_OTHER -> R.color.purple_custom_event
        else -> {
            throw IllegalStateException("No color matching for $type")
        }
    }

}

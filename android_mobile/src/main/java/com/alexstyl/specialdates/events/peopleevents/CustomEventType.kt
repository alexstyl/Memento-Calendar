package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.Strings

import com.alexstyl.specialdates.events.database.EventTypeId.TYPE_CUSTOM

class CustomEventType(private val name: String) : EventType {

    override fun getEventName(strings: Strings): String = name

    override val id: Int
        get() = TYPE_CUSTOM
}

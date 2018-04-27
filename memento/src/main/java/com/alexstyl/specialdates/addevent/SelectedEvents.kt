package com.alexstyl.specialdates.addevent


import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.Event
import com.alexstyl.specialdates.events.peopleevents.EventType

class SelectedEvents
internal constructor() {

    private val mappedEvents: HashMap<Int, Event>

    val events: Collection<Event>
        get() = mappedEvents.values

    init {
        mappedEvents = HashMap()
    }

    internal fun replaceDate(eventType: EventType, date: Date) {
        val eventId = eventType.id
        val event = Event(eventType, date)
        mappedEvents[eventId] = event
    }

    fun remove(eventType: EventType) {
        mappedEvents.remove(eventType.id)
    }
}

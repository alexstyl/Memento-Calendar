package com.alexstyl.specialdates.events

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType

data class Event(val eventType: EventType, val date: Date)

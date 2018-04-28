package com.alexstyl.specialdates.addevent.operations

import com.alexstyl.specialdates.addevent.operations.ContactOperation
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType

data class InsertEvent(val eventType: EventType, val date: Date) : ContactOperation

package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.EventType

class AddEventContactEventViewModelFactory(private val creator: DateLabelCreator) {

    fun createViewModel(contactEvent: ContactEvent): AddEventContactEventViewModel {
        return createViewModelWith(contactEvent.type, contactEvent.date)
    }

    fun createViewModelWith(eventType: EventType, date: Date): AddEventContactEventViewModel {
        val eventHint = creator.createWithYearPreferred(date)
        val dateOptional = Optional(date)
        return AddEventContactEventViewModel(eventHint, eventType, dateOptional, true, EventIcons.iconOf(eventType))
    }
}


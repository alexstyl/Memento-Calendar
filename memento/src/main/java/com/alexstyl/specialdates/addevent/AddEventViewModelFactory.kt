package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.EventType

class AddEventViewModelFactory(private val creator: DateLabelCreator,
                               private val strings: Strings,
                               private val eventIcons: EventIcons) {
    companion object {
        private val NO_DATE = Optional.absent<Date>()
    }

    fun createViewModelFor(contactEvent: ContactEvent): AddEventContactEventViewModel {
        return createViewModelFor(contactEvent.type, contactEvent.date)
    }

    fun createViewModelFor(eventType: EventType, date: Date): AddEventContactEventViewModel {
        val eventHint = creator.createWithYearPreferred(date)
        val dateOptional = Optional(date)
        val clearVisibility = true
        return AddEventContactEventViewModel(eventHint, eventType, dateOptional, clearVisibility, eventIcons.iconOf(eventType))
    }

    fun createViewModelFor(eventType: EventType): AddEventContactEventViewModel {
        val eventName = eventType.getEventName(strings)
        return AddEventContactEventViewModel(eventName, eventType, NO_DATE, false, eventIcons.iconOf(eventType))
    }
}


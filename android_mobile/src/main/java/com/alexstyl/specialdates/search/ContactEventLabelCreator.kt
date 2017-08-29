package com.alexstyl.specialdates.search

import com.alexstyl.resources.Strings
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator

class ContactEventLabelCreator(private val today: Date, private val strings: Strings, private val dateLabelCreator: DateLabelCreator) {

    fun createFor(event: ContactEvent): String {
        val eventLabel = event.getLabel(today, strings)
        val dateLabel = dateLabelCreator.createLabelFor(event.date)
        return strings.eventOnDate(eventLabel, dateLabel)
    }
}

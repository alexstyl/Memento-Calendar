package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.DateLabelCreator

class EventViewModelFactory(private val strings: Strings, private val dateLabelCreator: DateLabelCreator) {

    operator fun invoke(events: List<ContactEvent>): List<ContactEventViewModel> {
        val viewModels = ArrayList<ContactEventViewModel>(events.size)
        events.forEach {
            val eventName = it.type.getEventName(strings)
            val date = it.date
            val dateLabel = dateLabelCreator.createWithYearPreferred(date)
            viewModels.add(ContactEventViewModel(eventName, dateLabel, date))
        }
        return viewModels
    }
}

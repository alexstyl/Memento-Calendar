package com.alexstyl.specialdates.person

import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.DateLabelCreator

class EventViewModelFactory(private val stringResources: StringResources, private val dateLabelCreator: DateLabelCreator) {

    operator fun invoke(events: List<ContactEvent>): List<ContactEventViewModel> {
        val viewModels = ArrayList<ContactEventViewModel>(events.size)
        events.forEach {
            val eventName = it.type.getEventName(stringResources)
            val date = it.date
            val dateLabel = dateLabelCreator.createLabelWithYearPreferredFor(date)
            viewModels.add(ContactEventViewModel(eventName, dateLabel, date))
        }
        return viewModels
    }
}

package com.alexstyl.specialdates.person

import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.DateDisplayStringCreator

class EventViewModelFactory(val stringResources: StringResources) {

    operator fun invoke(events: List<ContactEvent>): List<ContactEventViewModel> {
        val viewModels = ArrayList<ContactEventViewModel>(events.size)
        events.forEach {
            val eventName = it.type.getEventName(stringResources)
            val date = it.date
            val dateLabel = DateDisplayStringCreator.INSTANCE.fullyFormattedDate(date)
            viewModels.add(ContactEventViewModel(eventName, dateLabel, date))
        }
        return viewModels
    }
}

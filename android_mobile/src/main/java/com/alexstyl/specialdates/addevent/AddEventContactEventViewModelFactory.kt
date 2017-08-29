package com.alexstyl.specialdates.addevent

import android.view.View
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.EventType
import java.util.*

internal class AddEventContactEventViewModelFactory(private val creator: DateLabelCreator) {

    fun createViewModel(contactEvents: List<ContactEvent>): List<AddEventContactEventViewModel> {
        val viewModels = ArrayList<AddEventContactEventViewModel>(contactEvents.size)

        for (contactEvent in contactEvents) {
            val viewModel = createViewModelWith(contactEvent.type, contactEvent.date)
            viewModels.add(viewModel)
        }
        return viewModels
    }

    fun createViewModelWith(eventType: EventType, date: Date): AddEventContactEventViewModel {
        val eventHint = creator.createLabelFor(date)
        val dateOptional = Optional(date)
        return AddEventContactEventViewModel(eventHint, eventType, dateOptional, View.VISIBLE, EventIcons.iconOf(eventType))
    }
}


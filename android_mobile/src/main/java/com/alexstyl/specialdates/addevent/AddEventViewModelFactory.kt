package com.alexstyl.specialdates.addevent

import android.view.View

import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.StandardEventType

internal class AddEventViewModelFactory(private val strings: Strings) {


    companion object {
        private val NO_DATE = Optional.absent<Date>()
    }

    fun createViewModelsForAllEventsBut(existingTypes: List<EventType>): List<AddEventContactEventViewModel> {
        val addEventViewModels = ArrayList<AddEventContactEventViewModel>()

        for (eventType in StandardEventType.values()) {
            if (existingTypes.contains(eventType)
                    || eventType == StandardEventType.NAMEDAY
                    || eventType == StandardEventType.CUSTOM) {
                continue
            }
            addEventViewModels.add(createAddEventViewModelsFor(eventType))
        }
        return addEventViewModels
    }

    fun createAddEventViewModelsFor(eventType: EventType): AddEventContactEventViewModel {
        val eventName = eventType.getEventName(strings)
        return AddEventContactEventViewModel(eventName, eventType, NO_DATE, View.GONE, EventIcons.iconOf(eventType))
    }

}

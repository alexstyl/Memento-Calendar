package com.alexstyl.specialdates.search

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.date.ContactEvent

class ContactEventViewModelFactory(private val eventLabelCreator: ContactEventLabelCreator,
                                   private val colors: Colors) {

    fun createViewModelFrom(contactEvents: List<ContactWithEvents>): List<ContactEventViewModel> {
        val models = ArrayList<ContactEventViewModel>()
        for (contactEvent in contactEvents) {

            val contact = contactEvent.contact

            contactEvent.events.forEach { event ->
                val eventLabel = eventLabelCreator.createFor(event)
                val variant = getVariationFor(event)
                models.add(ContactEventViewModel(
                        contact,
                        contact.displayName.toString(),
                        contact.imagePath,
                        eventLabel,
                        colors.getColorFor(event.type),
                        variant
                ))
            }
        }
        return models
    }

    private fun getVariationFor(event: ContactEvent): Int {
        return event.contact.contactID.toInt()
    }
}

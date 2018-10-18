package com.alexstyl.specialdates.search

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.date.ContactEvent

class SearchResultsViewModelFactory(private val eventLabelCreator: ContactEventLabelCreator,
                                    private val colors: Colors) {

    fun viewModelsFor(contactEvents: List<ContactWithEvents>): List<ContactEventViewModel> {
        val models = ArrayList<ContactEventViewModel>()
        for (contactEvent in contactEvents) {
            val contact = contactEvent.contact

            contactEvent.events.forEach { event ->
                models.add(ContactEventViewModel(
                        contact,
                        contact.displayName.toString(),
                        contact.imagePath,
                        eventLabelCreator.createFor(event),
                        colors.getColorFor(event.type),
                        event.backgroundVariant()
                ))
            }
        }
        return models.toList()
    }

    private fun ContactEvent.backgroundVariant() = this.contact.contactID.toInt()
}

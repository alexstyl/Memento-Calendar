package com.alexstyl.specialdates.search

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent

class SearchResultsViewModelFactory(private val eventLabelCreator: ContactEventLabelCreator,
                                    private val colors: Colors) {

    fun viewModelsFor(contactEvents: List<Contact>): List<ContactSearchResultViewModel> {
        val models = ArrayList<ContactSearchResultViewModel>()
        for (contactEvent in contactEvents) {
            TODO("Create ContactViewModel")
//            contactEvent.events.forEach { event ->
//                models.add(ContactSearchResultViewModel(
//                        contact,
//                        contact.displayName.toString(),
//                        contact.imagePath,
//                        eventLabelCreator.createFor(event),
//                        colors.getColorFor(event.type),
//                        event.backgroundVariant()
//                ))
//            }
        }
        return models.toList()
    }

    private fun ContactEvent.backgroundVariant() = this.contact.contactID.toInt()
}

package com.alexstyl.specialdates.upcoming

import com.alexstyl.resources.ColorResources
import com.alexstyl.resources.Strings
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date

class ContactViewModelFactory(private val colorResources: ColorResources, private val strings: Strings) {

    internal fun createViewModelFor(date: Date, contactEvent: ContactEvent): UpcomingContactEventViewModel {
        val contact = contactEvent.contact
        return UpcomingContactEventViewModel(
                contact,
                contact.displayName.toString(),
                contactEvent.getLabel(date, strings),
                colorResources.getColor(contactEvent.type.colorRes),
                contact.contactID.toInt(),
                contact.imagePath
        )
    }

}

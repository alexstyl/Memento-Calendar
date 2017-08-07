package com.alexstyl.specialdates.upcoming

import com.alexstyl.resources.ColorResources
import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date

class ContactViewModelFactory internal constructor(private val colorResources: ColorResources, private val stringResources: StringResources) {

    internal fun createViewModelFor(date: Date, contactEvent: ContactEvent): UpcomingContactEventViewModel {
        val contact = contactEvent.contact
        return UpcomingContactEventViewModel(
                contact,
                contact.displayName.toString(),
                contactEvent.getLabel(date, stringResources),
                colorResources.getColor(contactEvent.type.colorRes),
                contact.contactID.toInt(),
                contact.imagePath
        )
    }

}

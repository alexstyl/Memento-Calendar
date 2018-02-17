package com.alexstyl.specialdates.upcoming

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date

class ContactViewModelFactory(private val colors: Colors, private val strings: Strings) {

    internal fun createViewModelFor(date: Date, contactEvent: ContactEvent): UpcomingContactEventViewModel {
        val contact = contactEvent.contact
        return UpcomingContactEventViewModel(
                contact,
                contact.displayName.toString(),
                contactEvent.getLabel(date, strings),
                colors.getColorFor(contactEvent.type),
                contact.contactID.toInt(),
                contact.imagePath
        )
    }


}

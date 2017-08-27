package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.Contact

data class CelebratingContactViewModel(val contact: Contact, val letter: String, val colorVariant: Int) : NamedayScreenViewModel {
    override val viewType: Int
        get() = NamedayScreenViewType.CONTACT
    override val id: Long
        get() = contact.contactID
}

package com.alexstyl.specialdates.dailyreminder.actions

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.person.ContactActionViewModel

interface ContactActionsView {
    fun display(viewModels: List<ContactActionViewModel>)
    fun contact(): Contact
}

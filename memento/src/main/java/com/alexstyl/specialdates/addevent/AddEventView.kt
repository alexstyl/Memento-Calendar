package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.contact.Contact
import java.net.URI

interface AddEventView {
    fun display(uri: URI)

    fun replace(viewModel: AddEventContactEventViewModel)
    fun display(viewModels: List<AddEventContactEventViewModel>)

    fun removeAvatar()
    fun displayContact(contact: Contact)
}

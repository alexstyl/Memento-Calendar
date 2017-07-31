package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact

interface ContactActionsProvider {
    fun callActionsFor(contact: Contact): List<ContactActionViewModel>
    fun messagingActionsFor(contact: Contact): List<ContactActionViewModel>
}

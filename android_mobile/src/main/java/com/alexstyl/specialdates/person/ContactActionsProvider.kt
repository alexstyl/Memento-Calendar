package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact

interface ContactActionsProvider {
    fun callActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel>
    fun messagingActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel>
}

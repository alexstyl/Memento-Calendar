package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact

interface ContactActionsProvider {
    fun callActionsFor(contact: Contact, executor: ContactActions): List<ContactActionViewModel>
    fun messagingActionsFor(contact: Contact, executor: ContactActions): List<ContactActionViewModel>
}

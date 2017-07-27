package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact

interface ContactCallActionsProvider {
    fun callActionsFor(contact: Contact): List<ContactActionViewModel>
}

interface ContactMessagingActionsProvider {
    fun messagingActionsFor(contact: Contact): List<ContactActionViewModel>
}

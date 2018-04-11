package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact

class CompositeContactActionsProvider(private val providers: Map<Int, ContactActionsProvider>) : ContactActionsProvider {

    override fun callActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel> {
        if (providers[contact.source] != null) {
            return providers[contact.source]!!.callActionsFor(contact, actions)
        }
        return emptyList()
    }

    override fun messagingActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel> {
        if (providers[contact.source] != null) {
            return providers[contact.source]!!.messagingActionsFor(contact, actions)
        }
        return emptyList()
    }
}

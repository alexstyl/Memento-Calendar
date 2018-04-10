package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.contact.Contact

class CompositeContactActionsProvider(private val providers: ArrayList<ContactActionsProvider>) : ContactActionsProvider {

    override fun callActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel> {
        val empty = emptyList<ContactActionViewModel>()

        return providers.fold(empty, { contactEvents, provider ->
            contactEvents + provider.callActionsFor(contact, actions)
        })
    }

    override fun messagingActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel> {
        val empty = emptyList<ContactActionViewModel>()

        return providers.fold(empty, { contactEvents, provider ->
            contactEvents + provider.messagingActionsFor(contact, actions)
        })
    }
}

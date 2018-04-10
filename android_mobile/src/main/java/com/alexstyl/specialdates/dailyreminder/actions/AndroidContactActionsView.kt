package com.alexstyl.specialdates.dailyreminder.actions

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.person.ContactActionViewModel
import com.alexstyl.specialdates.person.ContactActionsAdapter

class AndroidContactActionsView(private val contact: Contact,
                                private val recyclerView: ContactActionsAdapter)
    : ContactActionsView {

    override fun display(viewModels: List<ContactActionViewModel>) {
        recyclerView.displayCallMethods(viewModels)
    }

    override fun contact(): Contact = contact
}

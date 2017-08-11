package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.ContactsProvider

class NamedaysViewModelFactory(val provide: ContactsProvider) {

    fun viewModelsFor(name: String): NamedaysViewModel {
        val contacts = provide.contactsCalled(name)
        // TODO populate with UI specific values
        return NamedaysViewModel(name, contacts)

    }

}


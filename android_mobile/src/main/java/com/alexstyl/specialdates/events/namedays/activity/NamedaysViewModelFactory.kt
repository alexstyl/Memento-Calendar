package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.Contact

class NamedaysViewModelFactory {

    fun viewModelsFor(name: String): NamedaysViewModel {
        return NamedaysViewModel(name)
    }

    fun viewModelsFor(contact: Contact): CelebratingContactViewModel {
        return CelebratingContactViewModel(contact)
    }

}


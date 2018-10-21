package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact

class SearchResultsViewModelFactory {

    fun viewModelsFor(contacts: List<Contact>) = contacts.map {
        ContactSearchResultViewModel(it, it.displayName.toString(), it.imagePath, it.backgroundVariant())
    }

    private fun Contact.backgroundVariant() = this.contactID.toInt()
}

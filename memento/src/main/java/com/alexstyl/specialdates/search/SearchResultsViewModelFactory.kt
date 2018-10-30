package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.namedays.NameCelebrations

class SearchResultsViewModelFactory(private val labelCreator: DateLabelCreator) {

    fun viewModelsFor(contacts: Collection<Contact>): List<SearchResultViewModel> = contacts.map {
        ContactSearchResultViewModel(it, it.displayName.toString(), it.imagePath, it.backgroundVariant())
    }

    private fun Contact.backgroundVariant() = this.contactID.toInt()

    fun contactPermissionViewModel(): SearchResultViewModel {
        return ContactReadPermissionRequestViewModel()
    }

    fun viewModelsFor(nameCelebrations: NameCelebrations): NamedaySearchResultViewModel {
        return NamedaySearchResultViewModel(
                nameCelebrations.name,
                nameCelebrations.dates
                        .map { date ->
                            NamedayDateViewModel(
                                    labelCreator.createLabelWithoutYear(date), date
                            )
                        })
    }
}

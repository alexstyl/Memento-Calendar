package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.search.SearchResultViewModel.NamedaySearchResultViewModel

class SearchResultsViewModelFactory(private val labelCreator: DateLabelCreator) {

    fun viewModelsFor(contacts: Collection<Contact>): List<SearchResultViewModel> = contacts.map {
        SearchResultViewModel.ContactSearchResultViewModel(it, it.displayName.toString(), it.imagePath, it.backgroundVariant())
    }

    private fun Contact.backgroundVariant() = this.contactID.toInt()

    fun contactPermissionViewModel() = SearchResultViewModel.ContactReadPermissionRequestViewModel


    fun viewModelsFor(nameCelebrations: NameCelebrations): NamedaySearchResultViewModel {
        return NamedaySearchResultViewModel(
                nameCelebrations.name,
                nameCelebrations.dates
                        .map { date ->
                            SearchResultViewModel.NamedayDateViewModel(
                                    labelCreator.createLabelWithoutYear(date), date
                            )
                        })
    }
}

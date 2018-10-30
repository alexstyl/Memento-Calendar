package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ImageURL
import com.alexstyl.specialdates.date.Date

sealed class SearchResultViewModel {

    object ContactReadPermissionRequestViewModel : SearchResultViewModel() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    data class ContactSearchResultViewModel(val contact: Contact,
                                            val displayName: String,
                                            val contactAvatarURI: ImageURL,
                                            val backgroundVariant: Int) : SearchResultViewModel()


    data class NamedaySearchResultViewModel(val nameday: String, val namedays: List<NamedayDateViewModel>) : SearchResultViewModel()

    data class NamedayDateViewModel(val dateLabel: String, val date: Date)

}

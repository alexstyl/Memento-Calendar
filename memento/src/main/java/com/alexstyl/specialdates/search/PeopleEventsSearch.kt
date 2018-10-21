package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider

class PeopleEventsSearch(private val peopleEventsProvider: ContactsProvider,
                         private val nameMatcher: NameMatcher) {

    fun searchForContacts(searchQuery: String): List<Contact> {
        return peopleEventsProvider
                .allContacts
                .filter {
                    nameMatcher.match(it.displayName, searchQuery)
                }
    }
}

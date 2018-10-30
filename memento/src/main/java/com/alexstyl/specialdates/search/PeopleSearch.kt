package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import io.reactivex.Observable

class PeopleSearch(private val peopleEventsProvider: PeopleEventsProvider,
                   private val nameMatcher: NameMatcher) {

    fun searchForContacts(searchQuery: String): Observable<Set<Contact>> {
        return Observable.fromCallable {
            peopleEventsProvider
                    .fetchAllEventsInAYear()
                    .asSequence()
                    .filter { nameMatcher.match(it.contact.displayName, searchQuery) }
                    .map { it.contact }
                    .toSet()
        }
    }
}

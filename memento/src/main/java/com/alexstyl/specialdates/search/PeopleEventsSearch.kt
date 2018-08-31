package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.util.HashMapList

class PeopleEventsSearch(private val peopleEventsProvider: PeopleEventsProvider,
                         private val nameMatcher: NameMatcher) {

    fun searchForContacts(searchQuery: String, counter: Int): List<ContactWithEvents> {
        if (counter <= 0) {
            return emptyList()
        }

        val normalisedQuery = searchQuery.trim { it <= ' ' }
        val events = HashMapList<Contact, ContactEvent>()

        peopleEventsProvider
                .fetchEventsBetween(TimePeriod.aYearFromNow())
                .forEach { contactEvent ->
                    val contact = contactEvent.contact
                    if (nameMatcher.match(contact.displayName, normalisedQuery)) {
                        events.addValue(contact, contactEvent)
                    }
                    if (events.keys().size >= counter) {
                        return@forEach
                    }
                }

        val contactWithEvents = ArrayList<ContactWithEvents>()
        for (contact in events.keys()) {
            val list = events[contact]
            contactWithEvents.add(ContactWithEvents(contact, list!!))
        }
        return contactWithEvents
    }
}

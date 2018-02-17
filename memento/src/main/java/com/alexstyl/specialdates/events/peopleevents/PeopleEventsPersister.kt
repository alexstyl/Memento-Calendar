package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.date.ContactEvent

interface PeopleEventsPersister {
    fun deleteAllNamedays()

    fun deleteAllEventsOfSource(@ContactSource source: Int)

    fun deleteAllDeviceEvents()

    fun markContactAsVisible(contact: Contact)

    fun markContactAsHidden(contact: Contact)

    fun getVisibilityFor(contact: Contact): Boolean
    
    fun insertAnnualEvents(events: List<ContactEvent>)
}

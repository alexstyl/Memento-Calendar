package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import java.util.*

data class ContactEventsOnADate(val date: Date, val events: List<ContactEvent>, val contacts: List<Contact>) {

    companion object {

        fun createFrom(date: Date, contactEvent: List<ContactEvent>): ContactEventsOnADate {
            val contacts = getContactsIn(contactEvent)
            return ContactEventsOnADate(date, contactEvent, contacts)
        }

        private fun getContactsIn(contactEvent: List<ContactEvent>): List<Contact> {
            val contacts = ArrayList<Contact>()
            for ((_, _, _, contact) in contactEvent) {
                if (!contacts.contains(contact)) {
                    contacts.add(contact)
                }
            }
            return Collections.unmodifiableList(contacts)
        }
    }
}

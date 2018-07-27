package com.alexstyl.specialdates

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.ANNIVERSARY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.BIRTHDAY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.NAMEDAY
import java.util.*

class TestContactEventsBuilder {
    private val contactEvents = ArrayList<ContactEvent>()

    fun addBirthdayFor(contact: Contact, date: Date): TestContactEventsBuilder {
        addEventFor(contact, BIRTHDAY, date)
        return this
    }

    fun addAnniversaryFor(contact: Contact, date: Date): TestContactEventsBuilder {
        addEventFor(contact, ANNIVERSARY, date)
        return this
    }

    fun addNamedayFor(contact: Contact, date: Date): TestContactEventsBuilder {
        addEventFor(contact, NAMEDAY, date)
        return this
    }

    private fun addEventFor(contact: Contact, eventType: EventType, date: Date) {
        contactEvents.add(ContactEvent(NO_DEVICE_CONTACT_ID, eventType, date, contact))
    }

    fun build(): List<ContactEvent> {
        return contactEvents.toList()
    }

    companion object {
        private val NO_DEVICE_CONTACT_ID = Optional.absent<Long>()
    }
}

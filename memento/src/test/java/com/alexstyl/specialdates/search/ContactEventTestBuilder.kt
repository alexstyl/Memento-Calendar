package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date

import com.alexstyl.specialdates.events.peopleevents.StandardEventType.ANNIVERSARY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.BIRTHDAY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.NAMEDAY

@Deprecated("Use the TestContactEventsBuilder instead")
class ContactEventTestBuilder(private val contact: Contact) {

    fun buildBirthday(date: Date): ContactEvent {
        return ContactEvent(NO_DEVICE_EVENT_ID, BIRTHDAY, date, contact)
    }

    fun buildNameday(date: Date): ContactEvent {
        return ContactEvent(NO_DEVICE_EVENT_ID, NAMEDAY, date, contact)
    }

    fun buildAnniversary(date: Date): ContactEvent {
        return ContactEvent(NO_DEVICE_EVENT_ID, ANNIVERSARY, date, contact)
    }

    companion object {

        private val NO_DEVICE_EVENT_ID = Optional.absent<Long>()
    }
}

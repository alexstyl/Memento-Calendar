package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod

interface PeopleEventsProvider {
    fun fetchEventsOn(date: Date): ContactEventsOnADate

    fun fetchEventsBetween(timePeriod: TimePeriod): List<ContactEvent>

    fun fetchEventsFor(contact: Contact): List<ContactEvent>

    fun findClosestEventDateOnOrAfter(date: Date): Date?
}

package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod

interface PeopleEventsProvider {
    fun fetchEventsOn(date: Date): ContactEventsOnADate

    fun fetchEventsBetween(timeDuration: TimePeriod): List<ContactEvent>

    fun fetchEventsFor(contact: Contact): List<ContactEvent>

    @Throws(NoEventsFoundException::class)
            /**
             * Returns the date of first event found on or after the passing date. Throws exception if no event exist after that date
             */
    fun findClosestEventDateOnOrAfter(date: Date): Date
}

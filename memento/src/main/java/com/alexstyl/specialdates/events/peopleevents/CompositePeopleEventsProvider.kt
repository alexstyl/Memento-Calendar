package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod


class CompositePeopleEventsProvider(private var providers: List<PeopleEventsProvider>)
    : PeopleEventsProvider {

    override fun fetchEventsOn(date: Date): ContactEventsOnADate {
        val empty = ContactEventsOnADate.createFrom(date, emptyList())

        return providers.fold(empty, { contactEvents, provider ->
            contactEvents + provider.fetchEventsOn(date)
        })
    }

    override fun fetchEventsBetween(timePeriod: TimePeriod): List<ContactEvent> =
            providers.fold(emptyList(), { list, provider ->
                list + provider.fetchEventsBetween(timePeriod)
            })

    override fun fetchEventsFor(contact: Contact): List<ContactEvent> =
            providers.fold(emptyList(), { list, provider ->
                list + provider.fetchEventsFor(contact)
            })


    override fun findClosestEventDateOnOrAfter(date: Date): Date? {
        date.ensureHasYear()

        return providers.mapNotNull {
            it.findClosestEventDateOnOrAfter(date)
        }.min()
    }

    private fun Date.ensureHasYear() {
        if (!this.hasYear()) {
            throw IllegalArgumentException("Date must contain year")
        }
    }
}


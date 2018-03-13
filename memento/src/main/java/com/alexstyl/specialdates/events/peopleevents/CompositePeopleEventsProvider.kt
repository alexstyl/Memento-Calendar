package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings


open class CompositePeopleEventsProvider(private val namedayPreferences: NamedayUserSettings,
                                    private val peopleNamedaysCalculator: PeopleNamedaysCalculator,
                                    private val deviceEvents: PeopleEventsProvider)
    : PeopleEventsProvider {

    private val closestEventsComparator = ClosestEventsComparator()

    override fun fetchEventsOn(date: Date): ContactEventsOnADate {
        val contactEvents = ArrayList<ContactEvent>()
        val (_, events) = deviceEvents.fetchEventsOn(date)
        contactEvents.addAll(events)
        if (namedayPreferences.isEnabled) {
            val namedaysContactEvents = peopleNamedaysCalculator.fetchEventsOn(date).events
            contactEvents.addAll(namedaysContactEvents)
        }

        return ContactEventsOnADate.createFrom(date, contactEvents)
    }

    override fun fetchEventsBetween(timePeriod: TimePeriod): List<ContactEvent> {
        val contactEvents = ArrayList<ContactEvent>()
        contactEvents.addAll(deviceEvents.fetchEventsBetween(timePeriod))

        if (namedayPreferences.isEnabled) {
            val namedaysContactEvents = peopleNamedaysCalculator.fetchEventsBetween(timePeriod)
            contactEvents.addAll(namedaysContactEvents)
        }
        return contactEvents.toList()
    }

    override fun fetchEventsFor(contact: Contact): List<ContactEvent> {
        val contactEvents = ArrayList<ContactEvent>()
        contactEvents.addAll(deviceEvents.fetchEventsFor(contact))
        if (namedayPreferences.isEnabled) {
            val namedaysContactEvents = peopleNamedaysCalculator.fetchEventsFor(contact)
            contactEvents.addAll(namedaysContactEvents)
        }
        return contactEvents.toList()
    }

    @Throws(NoEventsFoundException::class)
    override fun findClosestEventDateOnOrAfter(date: Date): Date {
        ensureDateHasYear(date)

        val deviceEvents = findNextDeviceEventsFor(date)
        val namedayEvents = findNextNamedayEventFor(date)
        val contactEventsOnADateOptional = returnClosestEventsOrMerge(deviceEvents, namedayEvents)
        if (contactEventsOnADateOptional.isPresent) {
            return contactEventsOnADateOptional.get().date
        }
        throw NoEventsFoundException("There are no events on or after $date")
    }

    private fun findNextDeviceEventsFor(date: Date): Optional<ContactEventsOnADate> {
        return try {
            val closestStaticDate = deviceEvents.findClosestEventDateOnOrAfter(date)
            Optional(deviceEvents.fetchEventsOn(closestStaticDate))
        } catch (e: NoEventsFoundException) {
            Optional.absent()
        }
    }

    private fun findNextNamedayEventFor(date: Date): Optional<ContactEventsOnADate> {
        if (!namedayPreferences.isEnabled) {
            return Optional.absent()
        }
        try {
            val closestEventDateOnOrAfter = peopleNamedaysCalculator.findClosestEventDateOnOrAfter(date)
            return Optional(peopleNamedaysCalculator.fetchEventsOn(closestEventDateOnOrAfter))
        } catch (e: NoEventsFoundException) {
            e.printStackTrace()
        }

        return Optional.absent()
    }

    private fun returnClosestEventsOrMerge(deviceEvents: Optional<ContactEventsOnADate>,
                                           namedayEvents: Optional<ContactEventsOnADate>)
            : Optional<ContactEventsOnADate> {
        if (!deviceEvents.isPresent && !namedayEvents.isPresent) {
            return Optional.absent()
        }

        val result = closestEventsComparator.compare(deviceEvents, namedayEvents)
        return when {
            result < 0 -> absentIfContainsNoEvent(deviceEvents)
            result > 0 -> absentIfContainsNoEvent(namedayEvents)
            else -> absentIfContainsNoEvent(createOptionalFor(deviceEvents, namedayEvents))
        }
    }

    private fun createOptionalFor(deviceEvents: Optional<ContactEventsOnADate>,
                                  namedayEvents: Optional<ContactEventsOnADate>): Optional<ContactEventsOnADate> {
        val combinedEvents = combine(namedayEvents.get().events, deviceEvents.get().events)
        return if (combinedEvents.isNotEmpty()) {
            Optional(
                    ContactEventsOnADate.createFrom(deviceEvents.get().date, combinedEvents))
        } else {
            Optional.absent()
        }
    }

    private fun absentIfContainsNoEvent(optional: Optional<ContactEventsOnADate>): Optional<ContactEventsOnADate> {
        if (!optional.isPresent) {
            return Optional.absent()
        }
        val events = optional.get().events
        return if (events.isNotEmpty()) {
            optional
        } else {
            Optional.absent()
        }

    }

    private fun <T> combine(listA: List<T>, listB: List<T>): List<T> {
        val contactEvents = ArrayList<T>()
        contactEvents.addAll(listA)
        contactEvents.addAll(listB)
        return contactEvents.toList()
    }

    private fun ensureDateHasYear(date: Date) {
        if (!date.hasYear()) {
            throw IllegalArgumentException("Date must contain year")
        }
    }
}

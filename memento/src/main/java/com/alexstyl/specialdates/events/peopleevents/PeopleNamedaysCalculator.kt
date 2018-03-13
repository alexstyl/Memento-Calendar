package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateComparator
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider

open class PeopleNamedaysCalculator(
        private val namedayPreferences: NamedayUserSettings,
        private val namedayCalendarProvider: NamedayCalendarProvider,
        private val contactsProvider: ContactsProvider) : PeopleEventsProvider {

    private val namedayCalendar: NamedayCalendar
        get() {
            val locale = namedayPreferences.selectedLanguage
            return namedayCalendarProvider.loadNamedayCalendarForLocale(locale, Date.CURRENT_YEAR)
        }

    override fun fetchEventsOn(date: Date): ContactEventsOnADate {
        val contactEvents = fetchEventsBetween(TimePeriod.between(date, date))
        return ContactEventsOnADate.createFrom(date, contactEvents)
    }

    override fun fetchEventsBetween(timePeriod: TimePeriod): List<ContactEvent> {
        val namedayEvents = ArrayList<ContactEvent>()
        for (contact in contactsProvider.allContacts) {
            for (firstName in contact.displayName.firstNames) {
                val nameDays = getSpecialNamedaysOf(firstName)
                if (nameDays.containsNoDate()) {
                    continue
                }

                val namedaysCount = nameDays.size()
                for (i in 0 until namedaysCount) {
                    val date = nameDays.getDate(i)
                    if (timePeriod.containsDate(date)) {
                        val nameday = ContactEvent(Optional(contact.contactID), StandardEventType.NAMEDAY, date, contact)
                        namedayEvents.add(nameday)
                    }
                }
            }
        }
        return namedayEvents.toList()
    }

    override fun fetchEventsFor(contact: Contact): List<ContactEvent> {
        val namedays = ArrayList<ContactEvent>()
        for (name in contact.displayName.allNames) {
            val (_, specialDates) = getSpecialNamedaysOf(name)
            for (i in 0 until specialDates.size()) {
                val date = specialDates.getDate(i)
                val deviceEventId = Optional(contact.contactID)
                val contactEvent = ContactEvent(deviceEventId, StandardEventType.NAMEDAY, date, contact)
                namedays.add(contactEvent)
            }
        }
        return namedays
    }

    @Throws(NoEventsFoundException::class)
    override fun findClosestEventDateOnOrAfter(date: Date): Date {
        val timePeriod = TimePeriod.between(date, Date.endOfYear(date.getYear()))
        val contactEvents = ArrayList(fetchEventsBetween(timePeriod))
        contactEvents.sortWith(Comparator { (_, _, date1), (_, _, date2) -> DATE_COMPARATOR.compare(date1, date2) })

        for ((_, _, contactEventDate) in contactEvents) {
            if (DATE_COMPARATOR.compare(contactEventDate, date) >= 0) {
                return contactEventDate
            }
        }
        throw NoEventsFoundException("No nameday events found on or after $date")
    }

    fun loadDeviceStaticNamedays(): List<ContactEvent> {
        val namedayEvents = ArrayList<ContactEvent>()
        val contacts = contactsProvider.allContacts
        for (contact in contacts) {
            val displayName = contact.displayName
            val namedays = HashSet<Date>()
            for (firstName in displayName.firstNames) {
                val nameDays = getNamedaysOf(firstName)
                if (nameDays.containsNoDate()) {
                    continue
                }
                val namedaysCount = nameDays.size()
                for (i in 0 until namedaysCount) {
                    val date = nameDays.getDate(i)
                    if (namedays.contains(date)) {
                        continue
                    }
                    val event = ContactEvent(Optional(contact.contactID), StandardEventType.NAMEDAY, date, contact)
                    namedayEvents.add(event)
                    namedays.add(date)
                }
            }
        }

        return namedayEvents
    }

    private fun getNamedaysOf(given: String): NameCelebrations {
        val namedayCalendar = namedayCalendar
        return namedayCalendar.getNormalNamedaysFor(given)
    }

    private fun getSpecialNamedaysOf(firstName: String): NameCelebrations {
        val namedayCalendar = namedayCalendar
        return namedayCalendar.getSpecialNamedaysFor(firstName)
    }

    companion object {

        private val DATE_COMPARATOR = DateComparator.INSTANCE
    }

}

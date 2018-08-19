package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateComparator
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.date.endOfYear
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider

open class PeopleDynamicNamedaysProvider(
        private val settings: NamedayUserSettings,
        private val namedayCalendarProvider: NamedayCalendarProvider,
        private val contactsProvider: ContactsProvider)
    : PeopleEventsProvider {

    override fun fetchEventsOn(date: Date): ContactEventsOnADate {
        val contactEvents = fetchEventsBetween(TimePeriod.between(date, date))
        return ContactEventsOnADate.createFrom(date, contactEvents)
    }

    override fun fetchEventsBetween(timePeriod: TimePeriod): List<ContactEvent> {
        if (!settings.isEnabled) {
            return emptyList()
        }
        val namedayEvents = mutableListOf<ContactEvent>()
        val calendar = namedayCalendarProvider.loadNamedayCalendarForLocale(settings.selectedLanguage, Date.CURRENT_YEAR)

        contactsProvider
                .allContacts
                .forEach { contact ->
                    for (firstName in contact.displayName.firstNames) {
                        calendar
                                .getSpecialNamedaysFor(firstName)
                                .dates
                                .forEach { date ->
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
        if (!settings.isEnabled) {
            return emptyList()
        }
        val locale = settings.selectedLanguage
        val calendar = namedayCalendarProvider.loadNamedayCalendarForLocale(locale, Date.CURRENT_YEAR)

        val namedays = ArrayList<ContactEvent>()
        for (name in contact.displayName.allNames) {
            val specialNamedays = calendar.getSpecialNamedaysFor(name)
            val specialDates = specialNamedays.dates

            for (i in 0 until specialDates.size) {
                val date = specialDates[i]
                val deviceEventId = Optional(contact.contactID)
                val contactEvent = ContactEvent(deviceEventId, StandardEventType.NAMEDAY, date, contact)
                namedays.add(contactEvent)
            }
        }
        return namedays
    }

    override fun findClosestEventDateOnOrAfter(date: Date): Date? {
        if (!settings.isEnabled) {
            return null
        }
        val timePeriod = TimePeriod.between(date, endOfYear(date.year!!))
        val contactEvents = ArrayList(fetchEventsBetween(timePeriod))
        contactEvents.sortWith(Comparator { (_, _, date1), (_, _, date2) -> DATE_COMPARATOR.compare(date1, date2) })

        for ((_, _, contactEventDate) in contactEvents) {
            if (DATE_COMPARATOR.compare(contactEventDate, date) >= 0) {
                return contactEventDate
            }
        }
        return null
    }

    fun loadAllStaticNamedays(): List<ContactEvent> {
        if (!settings.isEnabled) {
            return emptyList()
        }
        val namedayEvents = ArrayList<ContactEvent>()
        val contacts = contactsProvider.allContacts
        val locale = settings.selectedLanguage
        val calendar = namedayCalendarProvider.loadNamedayCalendarForLocale(locale, Date.CURRENT_YEAR)

        for (contact in contacts) {
            val displayName = contact.displayName
            val namedays = HashSet<Date>()
            for (firstName in displayName.firstNames) {
                val nameDays = calendar.getNormalNamedaysFor(firstName)
                if (nameDays.dates.isEmpty()) {
                    continue
                }
                val namedaysCount = nameDays.dates.size
                for (i in 0 until namedaysCount) {
                    val date = nameDays.dates[i]
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


    companion object {
        private val DATE_COMPARATOR = DateComparator.INSTANCE
    }

}

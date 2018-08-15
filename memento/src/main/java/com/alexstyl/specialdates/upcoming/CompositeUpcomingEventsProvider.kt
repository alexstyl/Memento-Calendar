package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.DateComparator
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysUserSettings
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider

class CompositeUpcomingEventsProvider(private val peopleEventsProvider: PeopleEventsProvider,
                                      private val namedayPreferences: NamedayUserSettings,
                                      private val namedayCalendarProvider: NamedayCalendarProvider,
                                      private val bankHolidaysUserSettings: BankHolidaysUserSettings,
                                      private val bankHolidayProvider: BankHolidayProvider,
                                      private val upcomingRowViewModelFactory: UpcomingEventRowViewModelFactory) : UpcomingEventsProvider {

    override fun calculateEventsBetween(timePeriod: TimePeriod): List<UpcomingRowViewModel> {
        val builder = UpcomingRowViewModelsBuilder(
                timePeriod,
                upcomingRowViewModelFactory
        )
        measure("contact events") {
            val contactEvents = peopleEventsProvider.fetchEventsBetween(timePeriod)
            builder.withContactEvents(contactEvents)
        }

        if (shouldLoadBankHolidays()) {
            val bankHolidays = bankHolidayProvider.calculateBankHolidaysBetween(timePeriod)
            builder.withBankHolidays(bankHolidays)
        }

        if (shouldLoadNamedays()) {
            val namedays = calculateNamedaysBetween(timePeriod)
            builder.withNamedays(namedays)
        }
        return builder.build()
    }

    private fun calculateNamedaysBetween(timeDuration: TimePeriod): List<NamesInADate> {
        // TODO break start to end year
        val selectedLanguage = namedayPreferences.selectedLanguage
        val namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(selectedLanguage, timeDuration.startingDate.year)

        var indexDate = timeDuration.startingDate
        val toDate = timeDuration.endingDate
        val namedays = ArrayList<NamesInADate>()

        while (COMPARATOR.compare(indexDate, toDate) < 0) {
            val allNamedayOn = namedayCalendar.getAllNamedaysOn(indexDate)
            if (allNamedayOn.names.isNotEmpty()) {
                namedays.add(allNamedayOn)
            }
            indexDate += 1
        }
        return namedays
    }

    private fun shouldLoadBankHolidays(): Boolean {
        return bankHolidaysUserSettings.isEnabled
    }

    private fun shouldLoadNamedays(): Boolean {
        return namedayPreferences.isEnabled && !namedayPreferences.isEnabledForContactsOnly
    }

    companion object {
        private val COMPARATOR = DateComparator.INSTANCE
    }
}

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
                                      private val namedayUserSettings: NamedayUserSettings,
                                      private val namedayCalendarProvider: NamedayCalendarProvider,
                                      private val bankHolidaysUserSettings: BankHolidaysUserSettings,
                                      private val bankHolidayProvider: BankHolidayProvider,
                                      private val upcomingRowViewModelFactory: UpcomingEventRowViewModelFactory) : UpcomingEventsProvider {

    override fun calculateEventsBetween(timePeriod: TimePeriod): List<UpcomingRowViewModel> {
        val builder = UpcomingRowViewModelsBuilder(
                timePeriod,
                upcomingRowViewModelFactory
        )

        val contactEvents = peopleEventsProvider.fetchEventsBetween(timePeriod)
        builder.withContactEvents(contactEvents)

        if (bankHolidaysUserSettings.isEnabled) {
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
        val selectedLanguage = namedayUserSettings.selectedLanguage
        val namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(selectedLanguage, timeDuration.startingDate.year!!)

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

    private fun shouldLoadNamedays(): Boolean {
        return namedayUserSettings.isEnabled && !namedayUserSettings.isEnabledForContactsOnly
    }

    companion object {
        private val COMPARATOR = DateComparator.INSTANCE
    }
}

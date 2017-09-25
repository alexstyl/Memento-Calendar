package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.DateComparator
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.service.PeopleEventsProvider

class UpcomingEventsProvider(private val peopleEventsProvider: PeopleEventsProvider,
                             private val namedayPreferences: NamedayUserSettings,
                             private val namedayCalendarProvider: NamedayCalendarProvider,
                             private val bankHolidaysPreferences: BankHolidaysPreferences,
                             private val bankHolidayProvider: BankHolidayProvider,
                             private val upcomingRowViewModelFactory: UpcomingEventRowViewModelFactory,
                             private val adRules: UpcomingEventsAdRules)
    : IUpcomingEventsProvider {

    override fun calculateEventsBetween(timePeriod: TimePeriod): List<UpcomingRowViewModel> {
        val contactEvents = peopleEventsProvider.getContactEventsFor(timePeriod)
        val upcomingRowViewModelsBuilder = UpcomingRowViewModelsBuilder(
                timePeriod,
                upcomingRowViewModelFactory,
                adRules
        )
                .withContactEvents(contactEvents)

        if (shouldLoadBankHolidays()) {
            val bankHolidays = bankHolidayProvider.calculateBankHolidaysBetween(timePeriod)
            upcomingRowViewModelsBuilder.withBankHolidays(bankHolidays)
        }

        if (shouldLoadNamedays()) {
            val namedays = calculateNamedaysBetween(timePeriod)
            upcomingRowViewModelsBuilder.withNamedays(namedays)
        }
        return upcomingRowViewModelsBuilder.build()
    }

    private fun calculateNamedaysBetween(timeDuration: TimePeriod): List<NamesInADate> {
        val selectedLanguage = namedayPreferences.selectedLanguage
        val namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(selectedLanguage, timeDuration.startingDate.getYear())

        var indexDate = timeDuration.startingDate
        val toDate = timeDuration.endingDate
        val namedays = ArrayList<NamesInADate>()

        while (COMPARATOR.compare(indexDate, toDate) < 0) {
            val allNamedayOn = namedayCalendar.getAllNamedaysOn(indexDate)
            if (allNamedayOn.nameCount() > 0) {
                namedays.add(allNamedayOn)
            }
            indexDate = indexDate.addDay(1)
        }
        return namedays
    }

    private fun shouldLoadBankHolidays(): Boolean {
        return bankHolidaysPreferences.isEnabled
    }

    private fun shouldLoadNamedays(): Boolean {
        return namedayPreferences.isEnabled && !namedayPreferences.isEnabledForContactsOnly
    }

    companion object {
        private val COMPARATOR = DateComparator.INSTANCE
    }

}

interface IUpcomingEventsProvider { // This is temporarily until we figure out how to mock kotlin classes with Mockito
    fun calculateEventsBetween(timePeriod: TimePeriod): List<UpcomingRowViewModel>
}

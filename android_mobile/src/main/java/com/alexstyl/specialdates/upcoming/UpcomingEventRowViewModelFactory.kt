package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate

class UpcomingEventRowViewModelFactory(private val today: Date,
                                       private val dateCreator: UpcomingDateStringCreator,
                                       private val contactViewModelFactory: ContactViewModelFactory) {

    fun createDateHeader(date: Date): UpcomingRowViewModel {
        val label = dateCreator.createLabelFor(date)
        return DateHeaderViewModel(label)
    }

    fun createViewModelFor(contactEvent: ContactEvent): UpcomingRowViewModel = contactViewModelFactory.createViewModelFor(today, contactEvent)

    fun createViewModelFor(bankHoliday: BankHoliday): UpcomingRowViewModel = BankHolidayViewModel(bankHoliday.holidayName)

    fun createViewModelFor(namedays: NamesInADate): UpcomingRowViewModel = NamedaysViewModel(namedays.names.joinToString(", "), namedays.date)

}

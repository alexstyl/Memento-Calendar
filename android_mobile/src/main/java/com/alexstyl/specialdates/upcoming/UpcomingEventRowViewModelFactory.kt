package com.alexstyl.specialdates.upcoming

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate

class UpcomingEventRowViewModelFactory(private val today: Date,
                                       private val colors: Colors,
                                       private val dateCreator: UpcomingDateStringCreator,
                                       private val contactViewModelFactory: ContactViewModelFactory) {

    fun createDateHeader(date: Date): UpcomingRowViewModel {
        val label = dateCreator.createLabelFor(date)
        return DateHeaderViewModel(label, colorFor(date))
    }

    private fun colorFor(date: Date): Int {
        return if (date == today) {
            colors.getTodayHeaderTextColor()
        } else {
            colors.getDateHeaderTextColor()
        }
    }

    fun createViewModelFor(contactEvent: ContactEvent): UpcomingRowViewModel = contactViewModelFactory.createViewModelFor(today, contactEvent)

    fun createViewModelFor(bankHoliday: BankHoliday): UpcomingRowViewModel = BankHolidayViewModel(bankHoliday.holidayName)

    fun createViewModelFor(namedays: NamesInADate): UpcomingRowViewModel = NamedaysViewModel(namedays.names.joinToString(", "), namedays.date)

}

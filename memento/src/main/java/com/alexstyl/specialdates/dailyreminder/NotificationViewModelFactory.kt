package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate

interface NotificationViewModelFactory {
    fun viewModelFor(contactEvent: ContactEvent): ContactEventNotificationViewModel
    fun summaryOf(viewModels: List<ContactEventNotificationViewModel>): SummaryNotificationViewModel
    fun namedaysViewModel(namedays: NamesInADate): NamedaysNotificationViewModel
    fun forBankHoliday(bankHoliday: BankHoliday): BankHolidayNotificationViewModel
}

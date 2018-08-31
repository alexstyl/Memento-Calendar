package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.bankholidays.BankHoliday
import com.alexstyl.specialdates.events.namedays.NamesInADate

interface DailyReminderViewModelFactory {
    fun viewModelFor(contact: Contact, events: List<ContactEvent>): ContactEventNotificationViewModel
    fun summaryOf(viewModels: List<ContactEventNotificationViewModel>): SummaryNotificationViewModel
    fun viewModelFor(namedays: NamesInADate): NamedaysNotificationViewModel
    fun viewModelFor(bankHoliday: BankHoliday): BankHolidayNotificationViewModel
}

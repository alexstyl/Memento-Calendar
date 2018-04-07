package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday

interface DailyReminderNotifier {
    fun forContacts(viewModels: List<ContactEventNotificationViewModel>)
    fun forNamedays(names: List<String>, date: Date)
    fun forBankholiday(bankHoliday: BankHoliday)

    fun cancelAllEvents()
}

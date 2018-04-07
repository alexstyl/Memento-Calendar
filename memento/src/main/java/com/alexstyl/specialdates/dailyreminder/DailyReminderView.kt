package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday

interface DailyReminderView {
    fun show(viewModels: List<ContactEventNotificationViewModel>)
    fun displayNamedays(names: List<String>, date: Date)
    fun displayBankholidays(bankHoliday: BankHoliday)

}

package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.bankholidays.BankHoliday

class NotificationDailyReminderView(private val notifier: DailyReminderNotifier)
    : DailyReminderView {

    override fun displayBankholidays(bankHoliday: BankHoliday) {
//        notifier.forBankholiday(bankHoliday)
        TODO()
    }

    override fun displayNamedays(names: List<String>, date: Date) {
//        notifier.forNamedays(names, date)
        TODO()
    }

    override fun show(viewModels: List<ContactEventNotificationViewModel>) {
        notifier.forContacts(viewModels)
    }
}


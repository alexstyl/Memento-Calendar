package com.alexstyl.specialdates.dailyreminder

interface DailyReminderNotifier {
    fun forContacts(viewModel: DailyReminderViewModel)
    fun cancelAllEvents()
}

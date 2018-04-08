package com.alexstyl.specialdates.dailyreminder

interface DailyReminderNotifier {
    fun notifyFor(viewModel: DailyReminderViewModel)
    fun cancelAllEvents()
}

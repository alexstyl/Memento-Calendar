package com.alexstyl.specialdates.dailyreminder

class NotificationDailyReminderView(private val notifier: DailyReminderNotifier)
    : DailyReminderView {

    override fun show(viewModel: DailyReminderViewModel) {
        notifier.forContacts(viewModel)
    }
}


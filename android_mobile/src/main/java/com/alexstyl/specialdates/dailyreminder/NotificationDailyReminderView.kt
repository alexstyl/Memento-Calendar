package com.alexstyl.specialdates.dailyreminder

class NotificationDailyReminderView(private val notifier: DailyReminderNotifier)
    : DailyReminderView {

    override fun show(viewModels: List<ContactEventNotificationViewModel>) {
        notifier.forContacts(viewModels)
    }
}


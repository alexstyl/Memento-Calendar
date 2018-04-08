package com.alexstyl.specialdates.dailyreminder

interface DailyReminderNotifier {
    fun forContacts(viewModels: List<ContactEventNotificationViewModel>)
    fun cancelAllEvents()
}

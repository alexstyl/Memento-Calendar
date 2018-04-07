package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.ContactEvent

interface NotificationViewModelFactory {
    fun contactEventsViewModel(contactEvent: ContactEvent): ContactEventNotificationViewModel
}

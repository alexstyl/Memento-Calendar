package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.ContactEvent

interface NotificationViewModelFactory {
    fun viewModelFor(contactEvent: ContactEvent): ContactEventNotificationViewModel
    fun summaryOf(viewModels: List<ContactEventNotificationViewModel>): SummaryNotificationViewModel
}

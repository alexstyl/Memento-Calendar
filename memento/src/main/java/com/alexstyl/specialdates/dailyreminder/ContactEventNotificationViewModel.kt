package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.ContactEvent

data class ContactEventNotificationViewModel(
        val notificationId: Int,
        val contactEvent: ContactEvent,
        val title: CharSequence,
        val label: CharSequence
)

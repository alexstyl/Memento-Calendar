package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent

data class ContactEventNotificationViewModel(
        val notificationId: Int,
        val contactEvent: ContactEvent,
        val title: CharSequence,
        val label: CharSequence,
        val actions: List<ContactActionViewModel>) {

    val contact: Contact
        get() =
            contactEvent.contact
}


package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.contact.Contact

data class ContactEventNotificationViewModel(
        val notificationId: Int,
        val contact: Contact,
        val title: CharSequence,
        val label: CharSequence,
        val actions: List<ContactActionViewModel>) 

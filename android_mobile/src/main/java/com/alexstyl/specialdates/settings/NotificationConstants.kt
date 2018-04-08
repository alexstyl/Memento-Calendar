package com.alexstyl.specialdates.settings

import com.alexstyl.specialdates.BuildConfig

object NotificationConstants {
    const val CHANNEL_ID_CONTACTS = BuildConfig.APPLICATION_ID + ".channel.contacts"
    const val CHANNEL_ID_NAMEDAYS = BuildConfig.APPLICATION_ID + ".channel.namedays"
    const val CHANNEL_ID_BANKHOLIDAY = BuildConfig.APPLICATION_ID + ".channel.bankholiday"
    
    const val NOTIFICATION_ID_CONTACTS_SUMMARY: Int = 492
    const val GROUP_DAILY_REMINDER = BuildConfig.APPLICATION_ID + ".group.daily_reminder"
}

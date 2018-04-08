package com.alexstyl.specialdates.settings

import com.alexstyl.specialdates.BuildConfig

object NotificationConstants {
    const val CHANNEL_ID_CONTACTS = BuildConfig.APPLICATION_ID + ".channel.contacts"
    const val CHANNEL_ID_NAMEDAYS = BuildConfig.APPLICATION_ID + ".channel.namedays"
    const val CHANNEL_ID_BANKHOLIDAY = BuildConfig.APPLICATION_ID + ".channel.bankholiday"

    const val NOTIFICATION_ID_CONTACTS_SUMMARY: Int = 4001
    const val NOTIFICATION_ID_NAMEDAYS = 4002
    const val NOTIFICATION_ID_BANK_HOLIDAY = 4003

    const val GROUP_DAILY_REMINDER = BuildConfig.APPLICATION_ID + ".group.daily_reminder"

}

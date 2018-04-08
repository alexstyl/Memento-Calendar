package com.alexstyl.specialdates.settings

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.graphics.Color
import com.alexstyl.android.Version
import com.alexstyl.specialdates.Strings

@SuppressLint("NewApi")
class NotificationChannelCreator(private val notificationManager: NotificationManager,
                                 private val strings: Strings) {


    fun createDailyReminderChannel() {
        if (!Version.hasOreo()) {
            return
        }
        val group = NotificationChannelGroup(NotificationConstants.GROUP_DAILY_REMINDER, strings.dailyReminder())
        notificationManager.createNotificationChannelGroup(group)

        createContactsChannel()
        createNamedayChannel()
        createBankHolidayChannel()
    }

    private fun createContactsChannel() {
        val contactsChannel = NotificationChannel(
                NotificationConstants.CHANNEL_ID_CONTACTS,
                strings.contacts(),
                NotificationManager.IMPORTANCE_DEFAULT)

        contactsChannel.group = NotificationConstants.GROUP_DAILY_REMINDER
        contactsChannel.enableLights(true)
        contactsChannel.lightColor = Color.RED
        contactsChannel.enableVibration(true)
        notificationManager.createNotificationChannel(contactsChannel)
    }

    private fun createNamedayChannel() {
        val namedaysChannel = NotificationChannel(
                NotificationConstants.CHANNEL_ID_NAMEDAYS,
                strings.namedays(),
                NotificationManager.IMPORTANCE_DEFAULT)

        namedaysChannel.group = NotificationConstants.GROUP_DAILY_REMINDER
        namedaysChannel.enableLights(false)
        namedaysChannel.enableVibration(false)
        notificationManager.createNotificationChannel(namedaysChannel)
    }

    private fun createBankHolidayChannel() {
        val bankHolidaysChannel = NotificationChannel(
                NotificationConstants.CHANNEL_ID_BANKHOLIDAY,
                strings.bankholidays(),
                NotificationManager.IMPORTANCE_DEFAULT)

        bankHolidaysChannel.group = NotificationConstants.GROUP_DAILY_REMINDER
        bankHolidaysChannel.enableLights(false)
        bankHolidaysChannel.enableVibration(false)
        notificationManager.createNotificationChannel(bankHolidaysChannel)

    }
}

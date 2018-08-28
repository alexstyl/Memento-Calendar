package com.alexstyl.specialdates.dailyreminder

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import com.alexstyl.Logger
import com.alexstyl.android.Version
import com.alexstyl.android.toUri
import com.alexstyl.specialdates.Strings
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

@TargetApi(Build.VERSION_CODES.O)
class DailyReminderOreoChannelCreator(private val notificationManager: NotificationManager,
                                      private val strings: Strings,
                                      private val dailyReminderPreferences: DailyReminderUserSettings,
                                      private val logger: Logger) {


    fun createDailyReminderChannel() {
        if (!Version.hasOreo()) {
            return
        }

        Observable.fromCallable {
            val group = NotificationChannelGroup(NotificationConstants.DAILY_REMINDER_GROUP_ID, strings.dailyReminder())
            if (notificationManager.notificationChannelGroups.contains(group)) {
                logger.warning("Already contains Group '${group.name}'. Won't create new channels [$group]")
                return@fromCallable
            }
            notificationManager.createNotificationChannelGroup(group)

            createContactsChannel()
            createNamedayChannel()
            createBankHolidayChannel()
        }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun createContactsChannel() {
        val contactsChannel = NotificationChannel(
                NotificationConstants.CHANNEL_ID_CONTACTS,
                strings.contacts(),
                NotificationManager.IMPORTANCE_DEFAULT)

        contactsChannel.group = NotificationConstants.DAILY_REMINDER_GROUP_ID
        contactsChannel.enableLights(true)
        contactsChannel.lightColor = Color.RED
        contactsChannel.enableVibration(dailyReminderPreferences.isVibrationEnabled())
        contactsChannel.setSound(dailyReminderPreferences.getRingtone().toUri(), AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build())

        notificationManager.createNotificationChannel(contactsChannel)
    }

    private fun createNamedayChannel() {
        val namedaysChannel = NotificationChannel(
                NotificationConstants.CHANNEL_ID_NAMEDAYS,
                strings.namedays(),
                NotificationManager.IMPORTANCE_LOW)

        namedaysChannel.group = NotificationConstants.DAILY_REMINDER_GROUP_ID
        namedaysChannel.enableLights(false)
        namedaysChannel.enableVibration(dailyReminderPreferences.isVibrationEnabled())
        namedaysChannel.setSound(dailyReminderPreferences.getRingtone().toUri(), AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build())
        notificationManager.createNotificationChannel(namedaysChannel)
    }

    private fun createBankHolidayChannel() {
        val bankHolidaysChannel = NotificationChannel(
                NotificationConstants.CHANNEL_ID_BANKHOLIDAY,
                strings.bankholidays(),
                NotificationManager.IMPORTANCE_LOW)

        bankHolidaysChannel.group = NotificationConstants.DAILY_REMINDER_GROUP_ID
        bankHolidaysChannel.enableLights(false)
        bankHolidaysChannel.enableVibration(dailyReminderPreferences.isVibrationEnabled())
        bankHolidaysChannel.setSound(dailyReminderPreferences.getRingtone().toUri(), AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build())
        notificationManager.createNotificationChannel(bankHolidaysChannel)

    }
}


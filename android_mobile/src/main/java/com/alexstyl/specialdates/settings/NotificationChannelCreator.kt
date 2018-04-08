package com.alexstyl.specialdates.settings

import android.annotation.SuppressLint
import android.app.NotificationChannel
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
        val notificationChannel = NotificationChannel(
                NotificationConstants.CHANNEL_ID_CONTACTS,
                strings.dailyReminder(),
                NotificationManager.IMPORTANCE_DEFAULT)

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED

        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

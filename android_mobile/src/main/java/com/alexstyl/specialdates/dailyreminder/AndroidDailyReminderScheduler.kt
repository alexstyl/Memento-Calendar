package com.alexstyl.specialdates.dailyreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.AlarmManagerCompat
import com.alexstyl.specialdates.TimeOfDay
import com.alexstyl.specialdates.TimeOfDay.Companion.now
import com.alexstyl.specialdates.date.todaysDate


class AndroidDailyReminderScheduler(private val context: Context,
                                    private val alarmManager: AlarmManager)
    : DailyReminderScheduler {

    override fun scheduleReminderFor(timeOfDay: TimeOfDay) {
        val timeSet = if (timeOfDay > now()) {
            todaysDate().toMillis() + timeOfDay.toMillis()
        } else {
            todaysDate().addDay(1).toMillis() + timeOfDay.toMillis()
        }

        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, timeSet, pendingIntent())
    }

    override fun cancelReminder() {
        alarmManager.cancel(pendingIntent())
    }

    private fun pendingIntent(): PendingIntent {
        val intent = Intent(context, DailyReminderReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }
}


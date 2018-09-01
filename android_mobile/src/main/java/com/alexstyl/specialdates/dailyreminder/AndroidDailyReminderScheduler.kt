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

    override fun isNextDailyReminderScheduled(): Boolean {
        val intent = Intent(context, DailyReminderReceiver::class.java)
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null
    }

    override fun scheduleReminderFor(timeOfDay: TimeOfDay) {
        val timeSet = nextOccurrenceOf(timeOfDay)

        val pendingIntent = buildPendingIntent()
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, timeSet, pendingIntent)
    }

    private fun buildPendingIntent(): PendingIntent {
        val intent = Intent(context, DailyReminderReceiver::class.java)
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun nextOccurrenceOf(timeOfDay: TimeOfDay): Long {
        return if (timeOfDay.isAfter(now())) {
            todaysDate().toMillis() + timeOfDay.toMillis()
        } else {
            todaysDate().addDay(1).toMillis() + timeOfDay.toMillis()
        }
    }

    override fun cancelReminder() {
        val intent = Intent(context, DailyReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private const val REQUEST_CODE = 300
    }
}



package com.alexstyl.specialdates.dailyreminder

import android.content.Context
import android.content.Intent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.putExtraDate

class AndroidDailyReminderDebugLauncher(private val context: Context) {

    fun launchForDate(date: Date) {
        val intent = Intent(context, DailyReminderReceiver::class.java)
        intent.putExtraDate(date)
        context.sendBroadcast(intent)
    }
}

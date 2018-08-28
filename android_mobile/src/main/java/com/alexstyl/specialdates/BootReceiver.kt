package com.alexstyl.specialdates

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler
import com.alexstyl.specialdates.dailyreminder.DailyReminderUserSettings
import javax.inject.Inject

class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var scheduler: DailyReminderScheduler
    @Inject lateinit var dailyReminderUserSettings: DailyReminderUserSettings

    override fun onReceive(context: Context, intent: Intent) {
        if (dailyReminderUserSettings.isEnabled()) {
            scheduler.scheduleReminderFor(dailyReminderUserSettings.getTimeSet())
        }
    }
}
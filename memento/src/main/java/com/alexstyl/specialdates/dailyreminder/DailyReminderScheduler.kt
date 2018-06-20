package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.TimeOfDay

interface DailyReminderScheduler {
    fun scheduleReminderFor(timeOfDay: TimeOfDay)
    fun cancelReminder()
}

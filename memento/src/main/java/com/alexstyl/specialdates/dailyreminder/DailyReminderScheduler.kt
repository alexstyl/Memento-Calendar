package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.TimeOfDay

interface DailyReminderScheduler {
    fun isNextDailyReminderScheduled(): Boolean
    fun scheduleReminderFor(timeOfDay: TimeOfDay)
    fun cancelReminder()
}

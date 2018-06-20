package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.TimeOfDay
import java.net.URI

interface DailyReminderUserSettings {
    fun isEnabled(): Boolean
    fun setEnabled(isEnabled: Boolean)
    fun getTimeSet(): TimeOfDay
    fun getRingtone(): URI
    fun isVibrationEnabled(): Boolean
    fun setDailyReminderTime(time: TimeOfDay)
}

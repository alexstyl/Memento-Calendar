package com.alexstyl.specialdates.dailyreminder

import android.media.RingtoneManager
import android.net.Uri
import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.TimeOfDay
import java.net.URI

class DailyReminderPreferences(private val preferences: EasyPreferences)
    : DailyReminderUserSettings {

    override fun setEnabled(isEnabled: Boolean) {
        preferences.setBoolean(R.string.key_daily_reminder, isEnabled)
    }

    override fun isEnabled(): Boolean = preferences.getBoolean(R.string.key_daily_reminder, true)

    override fun getTimeSet(): TimeOfDay {
        val time = preferences
                .getString(R.string.key_daily_reminder_time, DEFAULT_DAILY_REMINDER_TIME)
                .split(":")
        return TimeOfDay.at(time[0].toInt(), time[1].toInt())

    }

    override fun getRingtone(): URI {
        val selectedRingtone = preferences.getString(R.string.key_daily_reminder_ringtone, "")
        if (selectedRingtone.isEmpty()) {
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toURI()
        } else {
            return selectedRingtone.toURI()
        }
    }

    override fun isVibrationEnabled(): Boolean = preferences.getBoolean(R.string.key_daily_reminder_vibrate_enabled, false)

    override fun setDailyReminderTime(time: TimeOfDay) {
        preferences.setString(R.string.key_daily_reminder_time, time.toString())
    }


    private fun Uri.toURI(): URI = this.toString().toURI()
    private fun String.toURI(): URI = URI.create(this)

    companion object {
        private const val DEFAULT_DAILY_REMINDER_TIME = "08:00"
    }
}


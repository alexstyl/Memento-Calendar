package com.alexstyl.specialdates.dailyreminder

import android.content.Context

import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.MonthInt
import com.alexstyl.specialdates.date.Months

class DailyReminderDebugPreferences private constructor(private val preferences: EasyPreferences) {

    val selectedDate: Date
        get() {
            val dayOfMonth = preferences.getInt(R.string.key_debug_daily_reminder_date_fake_day, 1)
            @MonthInt val month = preferences.getInt(R.string.key_debug_daily_reminder_date_fake_month, Months.JANUARY)
            val year = preferences.getInt(R.string.key_debug_daily_reminder_date_fake_year, 2016)
            return Date.on(dayOfMonth, month, year)
        }

    internal val isFakeDateEnabled: Boolean
        get() = preferences.getBoolean(R.string.key_debug_daily_reminder_date_enable, false)

    fun setSelectedDate(dayOfMonth: Int, month: Int, year: Int) {
        val dayPair = Pair(R.string.key_debug_daily_reminder_date_fake_day, dayOfMonth)
        val monthPair = Pair(R.string.key_debug_daily_reminder_date_fake_month, month)
        val yearPair = Pair(R.string.key_debug_daily_reminder_date_fake_year, year)

        preferences.setIntegers(dayPair, monthPair, yearPair)
    }

    fun setEnabled(newValue: Boolean) {
        preferences.setBoolean(R.string.key_debug_daily_reminder_date_enable, newValue)
    }

    companion object {

        fun newInstance(context: Context): DailyReminderDebugPreferences {
            return DailyReminderDebugPreferences(EasyPreferences.createForPrivatePreferences(context, R.string.pref_dailyreminder_debug))
        }
    }
}

package com.alexstyl.specialdates.date

import com.alexstyl.specialdates.TimeOfDay

data class DateAndTime(private val date: Date, private val timeOfDay: TimeOfDay) {

    fun toMilis(): Long {
        return date.toMillis() + timeOfDay.toMillis()
    }

    fun addDay(i: Int): DateAndTime {
        return DateAndTime(date.addDay(i), timeOfDay)
    }
}

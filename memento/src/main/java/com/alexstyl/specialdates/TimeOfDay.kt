package com.alexstyl.specialdates

import org.joda.time.LocalTime

data class TimeOfDay(private val dateTime: LocalTime) {

    val hours: Int
        get() = dateTime.hourOfDay

    val minutes: Int
        get() = dateTime.minuteOfHour

    override fun toString(): String {
        val hour = hours
        val str = StringBuilder()
        if (isOneDigit(hour)) {
            str.append(ZERO)
        }
        str.append(hour)
                .append(SEPARATOR)
        val minute = minutes
        if (isOneDigit(minute)) {
            str.append(ZERO)
        }
        str.append(minute)
        return str.toString()
    }

    private fun isOneDigit(value: Int): Boolean {
        return value < 10
    }

    fun toMillis(): Long {
        return dateTime.millisOfDay.toLong()
    }

    fun addMinutes(minutes: Int) = TimeOfDay(dateTime.plusMinutes(minutes))

    companion object {
        fun at(hour: Int, minute: Int): TimeOfDay {
            return TimeOfDay(LocalTime(hour, minute))
        }

        private const val ZERO = "0"
        private const val SEPARATOR = ":"


    }
}

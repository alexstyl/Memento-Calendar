package com.alexstyl.specialdates

import android.support.annotation.IntRange
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

    fun isAfter(other: TimeOfDay) = compareTo(other) > 0

    private fun compareTo(other: TimeOfDay): Int {
        return if (hours == other.hours) {
            minutes - other.minutes
        } else {
            hours - other.hours
        }
    }

    companion object {
        fun at(@IntRange(from = 0, to = 23) hour: Int, @IntRange(from = 0, to = 59) minute: Int): TimeOfDay {
            return TimeOfDay(LocalTime(hour, minute))
        }

        fun now(): TimeOfDay {
            return TimeOfDay(LocalTime.now())
        }

        private const val ZERO = "0"
        private const val SEPARATOR = ":"


    }
}

@file:JvmName("DatesKt")

package com.alexstyl.specialdates.date

import com.alexstyl.specialdates.Optional
import org.joda.time.IllegalFieldValueException
import org.joda.time.LocalDate

fun todaysDate(): Date {
    val localDate = LocalDate.now()
    return Date(localDate, Optional(localDate.year))
}

fun dateOn(dayOfMonth: Int, @MonthInt month: Int): Date {
    val localDate = LocalDate(Months.NO_YEAR, month, dayOfMonth)
    return Date(localDate, Optional.absent())
}

fun dateOn(dayOfMonth: Int, @MonthInt month: Int, year: Int): Date {
    if (year <= 0) {
        throw IllegalArgumentException(
                "Do not call DayDate.on() if no year is present. Call the respective method without the year argument instead")
    }
    try {
        val localDate = LocalDate(year, month, dayOfMonth)
        return Date(localDate, Optional(year))
    } catch (a: IllegalFieldValueException) {
        throw IllegalArgumentException(String.format("%d/%d/%d is invalid", dayOfMonth, month, year))
    }
}


package com.alexstyl.specialdates.date

import org.joda.time.LocalDate


fun todaysDate(): Date {
    val localDate = LocalDate.now()
    return Date(localDate, localDate.year)
}

fun dateOn(dayOfMonth: Int, @MonthInt month: Int, year: Int? = null): Date {
    val localDate = if (year == null) {
        LocalDate(Months.NO_YEAR, month, dayOfMonth)
    } else {
        LocalDate(year, month, dayOfMonth)
    }
    return Date(localDate, year)
}

fun beggingOfYear(year: Int): Date = dateOn(1, Months.JANUARY, year)
fun endOfYear(currentYear: Int): Date = dateOn(31, Months.DECEMBER, currentYear)
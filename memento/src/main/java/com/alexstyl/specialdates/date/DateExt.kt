package com.alexstyl.specialdates.date

import org.joda.time.LocalDate

/**
 * A value that represents that no year has been specified.
 */
/*
 * The specific number (4) was used because it's the first positive year in JodaTime which contains the date 29 of February.
 * We currently not allow the user to use any value for a year less than 1900 anyhow. If year 4 is selected somehow through the device database,
 * then we are not going to treat it as a real year ¯\_(ツ)_/¯
 */

const val DEFAULT_YEAR = 4

fun todaysDate(): Date {
    val localDate = LocalDate.now()
    return Date(localDate, localDate.year)
}

fun dateOn(dayOfMonth: Int, @MonthInt month: Int, year: Int? = null): Date {
    val localDate = if (year == null) {
        LocalDate(DEFAULT_YEAR, month, dayOfMonth)
    } else {
        LocalDate(year, month, dayOfMonth)
    }
    return Date(localDate, year)
}

fun beggingOfYear(year: Int): Date = dateOn(1, Months.JANUARY, year)
fun endOfYear(currentYear: Int): Date = dateOn(31, Months.DECEMBER, currentYear)
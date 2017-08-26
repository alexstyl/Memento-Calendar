package com.alexstyl.specialdates.date

import com.alexstyl.specialdates.Optional
import org.joda.time.IllegalFieldValueException
import org.joda.time.LocalDate
import java.util.*

/**
 * A specific date on a specific year
 */
data class Date private constructor(private val localDate: LocalDate, private val year: Optional<Int>) : Comparable<Date> {

    fun addDay(i: Int): Date {
        val addedDate = localDate.plusDays(i)
        return Date(addedDate, Optional(addedDate.year))
    }

    val dayOfMonth: Int
        get() = localDate.dayOfMonth

    // JodaTime follows the same indexing as our project
    val month: Int
        @MonthInt
        get() = localDate.monthOfYear

    fun getYear(): Int = year.get()

    fun toMillis(): Long = localDate.toDate().time

    fun minusDay(days: Int): Date {
        val minusDays = localDate.minusDays(days)
        return Date(minusDays, Optional(minusDays.year))
    }

    val dayOfWeek: Int
        get() = localDate.dayOfWeek

    fun addWeek(weeks: Int): Date = addDay(7 * weeks)

    fun daysDifferenceTo(otherEvent: Date): Int {
        val dayOfYear = localDate.dayOfYear().get()
        val otherDayOfYear = otherEvent.localDate.dayOfYear().get()
        val daysOfYearsDifference = (getYear() - otherEvent.getYear()) * 365
        return otherDayOfYear - dayOfYear - daysOfYearsDifference
    }

    fun hasYear(): Boolean = year.isPresent

    fun hasNoYear(): Boolean = !year.isPresent

    override fun compareTo(right: Date): Int {
        if (this.hasYear() && right.hasYear()) {
            val yearOne = this.getYear()
            val yearTwo = right.getYear()
            if (yearOne > yearTwo) {
                return 1
            } else if (yearOne < yearTwo) {
                return -1
            }
        }
        if (this.month < right.month) {
            return -1
        } else if (this.month > right.month) {
            return 1
        }
        return this.dayOfMonth - right.dayOfMonth
    }

    companion object {

        var CURRENT_YEAR: Int = 0

        init {
            CURRENT_YEAR = LocalDate.now().year
        }

        fun today(): Date {
            val localDate = LocalDate.now()
            return Date(localDate, Optional(localDate.year))
        }

        fun on(dayOfMonth: Int, @MonthInt month: Int): Date {
            val localDate = LocalDate(Months.NO_YEAR, month, dayOfMonth)
            return Date(localDate, Optional.absent<Int>())
        }

        fun on(dayOfMonth: Int, @MonthInt month: Int, year: Int): Date {
            if (year <= 0) {
                throw IllegalArgumentException(
                        "Do not call DayDate.on() if no year is present. Call the respective method without the year argument instead")
            }
            try {
                val localDate = LocalDate(year, month, dayOfMonth)
                return Date(localDate, Optional(year))
            } catch (a: IllegalFieldValueException) {
                throw IllegalArgumentException(String.format(Locale.US, "%d/%d/%d is invalid", dayOfMonth, month, year))
            }

        }

        fun startOfTheYear(currentYear: Int): Date = Date.on(1, Months.JANUARY, currentYear)

        fun endOfYear(currentYear: Int): Date = Date.on(31, Months.DECEMBER, currentYear)
    }

}

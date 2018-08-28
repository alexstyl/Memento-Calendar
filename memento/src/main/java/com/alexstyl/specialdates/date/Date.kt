package com.alexstyl.specialdates.date

import org.joda.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.math.abs

data class Date(private val localDate: LocalDate, val year: Int?) : Comparable<Date> {

    fun addDay(i: Int): Date {
        val addedDate = localDate.plusDays(i)
        return Date(addedDate, addedDate.year)
    }

    operator fun plus(i: Int): Date = addDay(i)

    val dayOfMonth: Int get() = localDate.dayOfMonth

    val month: Int @MonthInt get() = localDate.monthOfYear


    fun toMillis(): Long = localDate.toDate().time

    fun minusDay(days: Int): Date {
        val minusDays = localDate.minusDays(days)
        return Date(minusDays, minusDays.year)
    }

    val dayOfWeek: Int
        get() = localDate.dayOfWeek

    fun addWeek(weeks: Int): Date = addDay(7 * weeks)

    fun daysDifferenceTo(otherEvent: Date): Int =
            abs(TimeUnit.MILLISECONDS.toDays(this.toMillis() - otherEvent.toMillis()).toInt())

    fun hasYear(): Boolean = year != null

    fun hasNoYear(): Boolean = !hasYear()

    val daysInCurrentMonth: Int
        get() = localDate.dayOfMonth().maximumValue

    override fun compareTo(other: Date): Int {
        return localDate.compareTo(other.localDate)
    }


    companion object {

        var CURRENT_YEAR: Int = 0

        init {
            CURRENT_YEAR = LocalDate.now().year
        }
    }

}

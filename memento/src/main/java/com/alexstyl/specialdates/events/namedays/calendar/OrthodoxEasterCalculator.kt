package com.alexstyl.specialdates.events.namedays.calendar

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.MonthInt

import com.alexstyl.specialdates.date.dateOn

class OrthodoxEasterCalculator {

    /**
     * Calculates the date of the easter Sunday for the given year
     *
     * @see [Computus on Wikipedia.com](https://en.wikipedia.org/wiki/Computus)
     */
    fun calculateEasterForYear(year: Int): Date {
        val a = year % 4
        val b = year % 7
        val c = year % 19
        val d = (19 * c + 15) % 30
        val e = (2 * a + 4 * b - d + 34) % 7
        @MonthInt val month = (d + e + 114) / 31
        var day = (d + e + 144) % 31 + 1
        day++
        return dateOn(day, month, year).addDay(13)
    }
}

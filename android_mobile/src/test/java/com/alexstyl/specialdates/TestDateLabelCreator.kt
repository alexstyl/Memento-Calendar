package com.alexstyl.specialdates

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.upcoming.MonthLabels
import java.util.Locale

class TestDateLabelCreator private constructor(private val monthLabels: MonthLabels) : DateLabelCreator {

    override fun createWithYearPreferred(date: Date): String {
        return if (date.hasYear()) {
            monthLabels.getMonthOfYear(date.month) + " " + date.dayOfMonth + " " + date.year
        } else {
            monthLabels.getMonthOfYear(date.month) + " " + date.dayOfMonth
        }
    }

    override fun createLabelWithoutYear(date: Date): String = monthLabels.getMonthOfYear(date.month) + " " + date.dayOfMonth

    companion object {
        fun forUS(): DateLabelCreator {
            val monthLabels = MonthLabels.forLocale(Locale.US)
            return TestDateLabelCreator(monthLabels)
        }
    }
}

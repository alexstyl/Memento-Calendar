package com.alexstyl.specialdates

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.upcoming.MonthLabels
import java.util.*

class TestDateLabelCreator private constructor(private val monthLabels: MonthLabels) : DateLabelCreator {

    override fun createLabelFor(date: Date): String {
        return if (date.hasYear()) {
            monthLabels.getMonthOfYear(date.month) + " " + date.dayOfMonth + " " + date.getYear()
        } else {
            monthLabels.getMonthOfYear(date.month) + " " + date.dayOfMonth
        }
    }

    companion object {
        fun forUS(): DateLabelCreator {
            val monthLabels = MonthLabels.forLocale(Locale.US)
            return TestDateLabelCreator(monthLabels)
        }
    }
}

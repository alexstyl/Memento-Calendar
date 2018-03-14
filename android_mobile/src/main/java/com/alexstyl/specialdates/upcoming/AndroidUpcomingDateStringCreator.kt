package com.alexstyl.specialdates.upcoming

import android.content.Context
import android.text.format.DateUtils

import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateComparator

class AndroidUpcomingDateStringCreator(
        private val strings: Strings,
        private val today: Date,
        private val context: Context) : UpcomingDateStringCreator {

    override fun createLabelFor(date: Date): String {
        var formatFlags = DateUtils.FORMAT_NO_NOON_MIDNIGHT or DateUtils.FORMAT_CAP_AMPM or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR

        val stringBuilder = StringBuilder()

        if (isToday(date)) {
            stringBuilder.append(strings.today()).append(DAY_OF_WEEK_SEPARATOR)
        } else if (isTomorrow(date)) {
            stringBuilder.append(strings.tomorrow()).append(DAY_OF_WEEK_SEPARATOR)
        } else {
            formatFlags = formatFlags or (DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY)
        }

        if (date.year != today.year) {
            formatFlags = formatFlags or DateUtils.FORMAT_SHOW_YEAR
        }
        stringBuilder.append(DateUtils.formatDateTime(context, date.toMillis(), formatFlags))
        return stringBuilder.toString()
    }

    private fun isToday(date: Date): Boolean {
        return DateComparator.INSTANCE.compare(date, today) == 0
    }

    private fun isTomorrow(date: Date): Boolean {
        return date.toMillis() - today.toMillis() == DateUtils.DAY_IN_MILLIS
    }

    companion object {
        private const val DAY_OF_WEEK_SEPARATOR = ", "
    }

}

package com.alexstyl.specialdates.date

import android.content.Intent

/**
 * Methods to hide the passing and retrieving of a Date from a [Intent]'s extras
 */
object DateBundleUtils {

    private val EXTRA_DAY_OF_MONTH = "extra:day_of_month"
    private val EXTRA_MONTH = "extra:month"
    private val EXTRA_YEAR = "extra:year"

    fun putDateAsExtraIntoIntent(date: Date, intent: Intent) {
        intent.putExtra(EXTRA_DAY_OF_MONTH, date.dayOfMonth)
        intent.putExtra(EXTRA_MONTH, date.month)
        intent.putExtra(EXTRA_YEAR, date.getYear())
    }

    fun extractDateFrom(intent: Intent): Date {
        val dayOfMonth = getExtraOrThrow(intent, EXTRA_DAY_OF_MONTH)
        @MonthInt val month = getExtraOrThrow(intent, EXTRA_MONTH)
        val year = getExtraOrThrow(intent, EXTRA_YEAR)
        return Date.on(dayOfMonth, month, year)
    }

    private fun getExtraOrThrow(intent: Intent, extra: String): Int {
        val intExtra = intent.getIntExtra(extra, -1)
        if (intExtra == -1) {
            throw IllegalArgumentException("Passing Intent did not include extra [$extra]")
        }
        return intExtra
    }
}

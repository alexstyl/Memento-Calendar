package com.alexstyl.specialdates.date

import android.content.Intent

val EXTRA_DAY_OF_MONTH = "extra:day_of_month"
val EXTRA_MONTH = "extra:month"
val EXTRA_YEAR = "extra:year"

fun Intent.putExtraDate(date: Date) {
    putExtra(EXTRA_DAY_OF_MONTH, date.dayOfMonth)
    putExtra(EXTRA_MONTH, date.month)
    putExtra(EXTRA_YEAR, date.getYear())
}

fun Intent.getDateExtraOrThrow(): Date {
    val dayOfMonth = getExtraOrThrow(this, EXTRA_DAY_OF_MONTH)
    @MonthInt val month = getExtraOrThrow(this, EXTRA_MONTH)
    val year = getExtraOrThrow(this, EXTRA_YEAR)
    return Date.on(dayOfMonth, month, year)
}

private fun getExtraOrThrow(intent: Intent, extra: String): Int {
    val intExtra = intent.getIntExtra(extra, -1)
    if (intExtra == -1) {
        throw IllegalArgumentException("Passing Intent did not include extra [$extra]")
    }
    return intExtra
}


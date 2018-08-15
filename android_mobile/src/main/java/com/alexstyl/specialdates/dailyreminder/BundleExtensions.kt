package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.date.Date
import com.evernote.android.job.util.support.PersistableBundleCompat


private const val EXTRA_DAY_OF_MONTH = "EXTRA_DAILY_REMINDER_DAY_OF_MONTH"
private const val EXTRA_MONTH = "EXTRA_DAILY_REMINDER_MONTH"
private const val EXTRA_YEAR = "EXTRA_DAILY_REMINDER_YEAR"

fun PersistableBundleCompat.getDate(): Date {
    val dayOfMonth = this.getInt(EXTRA_DAY_OF_MONTH, -1)
    val month = this.getInt(EXTRA_MONTH, -1)
    val year = this.getInt(EXTRA_YEAR, -1)
    assertValues(dayOfMonth, month, year)
    return dateOn(dayOfMonth, month, year)
}

private fun assertValues(dayOfMonth: Int, month: Int, year: Int) {
    if (dayOfMonth == -1 || month == -1 || year == -1) {
        throw IllegalArgumentException("Invalid date format. Use ${PersistableBundleCompat::javaClass.name}.addDate() method to pass a Date into the bundle")
    }
}

fun PersistableBundleCompat.putDate(date: Date): PersistableBundleCompat {
    this.putInt(EXTRA_DAY_OF_MONTH, date.dayOfMonth)
    this.putInt(EXTRA_MONTH, date.month)
    this.putInt(EXTRA_YEAR, date.year)
    return this
}

fun PersistableBundleCompat.containsDate(): Boolean {
    return this.containsKey(EXTRA_DAY_OF_MONTH)
            && this.containsKey(EXTRA_MONTH)
            && this.containsKey(EXTRA_YEAR)

}

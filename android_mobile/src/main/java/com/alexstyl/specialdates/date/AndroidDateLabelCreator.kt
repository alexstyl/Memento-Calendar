package com.alexstyl.specialdates.date

import android.content.Context
import android.text.format.DateUtils

class AndroidDateLabelCreator(private val context: Context) : DateLabelCreator {

    override fun createLabelFor(date: Date): String {
        return if (date.hasYear()) {
            val formatFlags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
            DateUtils.formatDateTime(context, date.toMillis(), formatFlags)
        } else {
            val formatFlags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR
            DateUtils.formatDateTime(context, date.toMillis(), formatFlags)
        }
    }
}

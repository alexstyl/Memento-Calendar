package com.alexstyl.specialdates.date

interface DateLabelCreator {
    /**
     * Returns the label of this date. If the Date does not include a year, this will fail silently, returning a label without any years
     */
    fun createWithYearPreferred(date: Date): String

    fun createLabelWithoutYear(date: Date): String
}

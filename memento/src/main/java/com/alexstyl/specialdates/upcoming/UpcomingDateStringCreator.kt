package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date

interface UpcomingDateStringCreator {
    fun createLabelFor(date: Date): String
}

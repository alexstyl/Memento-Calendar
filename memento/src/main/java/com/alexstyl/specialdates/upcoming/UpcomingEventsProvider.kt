package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.TimePeriod

interface UpcomingEventsProvider {
    fun calculateEventsBetween(timePeriod: TimePeriod): List<UpcomingRowViewModel>
}

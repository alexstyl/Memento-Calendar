package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.calendar.ImmutableNamesInADate

interface NamesInADate {
    val date: Date
    val names: List<String>

    operator fun plus(other: NamesInADate): NamesInADate {
        return ImmutableNamesInADate(date, this.names + other.names)
    }
}

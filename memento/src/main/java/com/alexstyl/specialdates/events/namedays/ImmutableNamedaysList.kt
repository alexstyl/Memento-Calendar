package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.dateOn

data class ImmutableNamedaysList(override val names: List<String>,
                                 val specificYearNamedays: Map<Date, MutableNamesInADate>,
                                 val recurringNamedays: Map<Date, MutableNamesInADate>
) : NamedaysList {

    override fun getNamedaysFor(date: Date): NamesInADate {
        assertHasYear(date)

        return (specificYearNamedays[date] ?: NoNamesInADate(date)) +
                (recurringNamedays[dateOn(date.dayOfMonth, date.month)] ?: NoNamesInADate(date))
    }

    private fun assertHasYear(date: Date) {
        if (date.hasNoYear()) {
            throw IllegalArgumentException("Must provide a date with a year to ask for Namedays")
        }
    }
}
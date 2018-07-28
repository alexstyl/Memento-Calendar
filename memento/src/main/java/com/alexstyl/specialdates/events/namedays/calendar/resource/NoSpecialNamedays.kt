package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.ImmutableNameCelebrations
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.NoNamesInADate

class NoSpecialNamedays : SpecialNamedays {

    override fun getNamedayOn(date: Date): NamesInADate {
        return NoNamesInADate(date)
    }

    override fun getNamedaysFor(name: String, year: Int): ImmutableNameCelebrations {
        return ImmutableNameCelebrations(name, emptyList())
    }

    override val allNames: List<String>
        get() = emptyList()
}

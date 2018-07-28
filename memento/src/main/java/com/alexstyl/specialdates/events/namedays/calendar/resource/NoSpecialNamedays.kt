package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NoNamesInADate

class NoSpecialNamedays : SpecialNamedays {

    override fun getNamedayOn(date: Date): NoNamesInADate {
        return NoNamesInADate(date)
    }

    override fun getNamedaysFor(name: String, year: Int): NameCelebrations {
        return NameCelebrations(name)
    }

    override val allNames: List<String>
        get() = emptyList()
}

package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.NoNameCelebrations
import com.alexstyl.specialdates.events.namedays.NoNamesInADate

object NoSpecialNamedays : SpecialNamedays {

    override fun getNamedayOn(date: Date): NamesInADate {
        return NoNamesInADate(date)
    }

    override fun getNamedaysFor(name: String, year: Int): NameCelebrations {
        return NoNameCelebrations(name)
    }

    override val allNames: List<String>
        get() = emptyList()

}

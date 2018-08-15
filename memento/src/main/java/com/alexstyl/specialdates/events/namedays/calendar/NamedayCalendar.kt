package com.alexstyl.specialdates.events.namedays.calendar

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.StaticNamedays
import com.alexstyl.specialdates.events.namedays.calendar.resource.SpecialNamedays

open class NamedayCalendar(val locale: NamedayLocale,
                           private val staticNamedays: StaticNamedays,
                           private val specialNamedays: SpecialNamedays,
                           val year: Int) {

    open val allNames: List<String> = staticNamedays.names + specialNamedays.allNames

    open fun getNormalNamedaysFor(name: String): NameCelebrations {
        return staticNamedays.getDatesFor(name)
    }

    open fun getSpecialNamedaysFor(firstName: String): NameCelebrations {
        return specialNamedays.getNamedaysFor(firstName, year)
    }

    open fun getAllNamedaysOn(date: Date): NamesInADate {
        return staticNamedays.getNamedaysFor(date) + specialNamedays.getNamedayOn(date)
    }

    open fun getAllNamedays(name: String): NameCelebrations {
        val names = staticNamedays.getDatesFor(name)
        val specialNames = specialNamedays.getNamedaysFor(name, year)

        return names + specialNames
    }
}

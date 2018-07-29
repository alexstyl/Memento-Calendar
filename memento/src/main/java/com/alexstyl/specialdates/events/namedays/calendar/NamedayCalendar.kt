package com.alexstyl.specialdates.events.namedays.calendar

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamedayBundle
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.resource.SpecialNamedays

class NamedayCalendar(val locale: NamedayLocale,
                      private val namedayBundle: NamedayBundle,
                      private val specialNamedays: SpecialNamedays,
                      val year: Int) {

    val allNames: List<String>
        get() {
            val names = namedayBundle.names
            names.addAll(specialNamedays.allNames)
            return names
        }

    fun getNormalNamedaysFor(name: String): NameCelebrations {
        return namedayBundle.getDatesFor(name)
    }

    fun getSpecialNamedaysFor(name: String): NameCelebrations {
        return specialNamedays.getNamedaysFor(name, year)
    }

    fun getAllNamedaysOn(date: Date): NamesInADate {
        return namedayBundle.getNamedaysFor(date) + specialNamedays.getNamedayOn(date)
    }

    fun getAllNamedays(name: String): NameCelebrations {
        val names = namedayBundle.getDatesFor(name)
        val specialNames = specialNamedays.getNamedaysFor(name, year)

        return names + specialNames
    }
}

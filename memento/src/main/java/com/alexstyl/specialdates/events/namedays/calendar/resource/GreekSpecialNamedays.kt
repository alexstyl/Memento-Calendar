package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator

internal class GreekSpecialNamedays private constructor(private val greekNamedays: GreekNamedays) : SpecialNamedays {

    override val allNames: List<String>
        get() = greekNamedays.names

    override fun getNamedayOn(date: Date): NamesInADate {
        return greekNamedays.getNamedayByDate(date)
    }

    override fun getNamedaysFor(name: String, year: Int): NameCelebrations {
        return greekNamedays.getNamedaysFor(name, year)
    }

    companion object {

        fun from(namedayJSON: NamedayJSON, easterCalculator: OrthodoxEasterCalculator): GreekSpecialNamedays {
            val greekNamedays = GreekNamedays.from(namedayJSON.special, easterCalculator)
            return GreekSpecialNamedays(greekNamedays)
        }
    }
}

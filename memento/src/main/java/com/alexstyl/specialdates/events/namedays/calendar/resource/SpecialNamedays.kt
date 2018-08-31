package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamesInADate

interface SpecialNamedays {

    val allNames: List<String>

    fun getNamedayOn(date: Date): NamesInADate

    fun getNamedaysFor(name: String, year: Int): NameCelebrations
}

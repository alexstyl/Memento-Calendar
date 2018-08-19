package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

interface NamedaysList {
    val names: MutableList<String>
    fun getNamedaysFor(date: Date): NamesInADate
}

interface MutableNamedaysList : NamedaysList {
    fun addRecurringNameday(date: Date, name: String)
    fun addSpecificYearNameday(date: Date, name: String)
}
package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.calendar.resource.Node

data class Namedays(private val namesToDate: Node, private val namedaysList: NamedaysList) {

    val names = namedaysList.names

    fun getDatesFor(name: String): NameCelebrations {
        return namesToDate.getDates(name) ?: NoCelebrations(name)
    }

    fun getNamedaysFor(date: Date): NamesInADate {
        return namedaysList.getNamedaysFor(date)
    }
}

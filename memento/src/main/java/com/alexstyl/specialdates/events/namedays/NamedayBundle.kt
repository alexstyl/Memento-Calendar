package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.calendar.resource.Node

class NamedayBundle(private val namesToDate: Node, private val namedaysList: NamedaysList) {

    val names: MutableList<String>
        get() = namedaysList.getNames()

    fun getDatesFor(name: String): NameCelebrations {
        return namesToDate.getDates(name)
    }

    fun getNamedaysFor(date: Date): NamesInADate {
        return namedaysList.getNamedaysFor(date)
    }
}

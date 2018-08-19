package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations

interface Node {
    fun addDate(name: String, date: Date)
    fun getDates(name: String): NameCelebrations?
}

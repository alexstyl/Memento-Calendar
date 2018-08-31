package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

interface NamedaysList {
    val names: List<String>
    fun getNamedaysFor(date: Date): NamesInADate
}
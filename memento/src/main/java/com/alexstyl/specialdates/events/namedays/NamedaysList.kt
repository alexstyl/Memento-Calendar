package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

interface NamedaysList {
    val names: MutableList<String>
    fun getNamedaysFor(date: Date): NamesInADate
    fun addNameday(date: Date, name: String)
}
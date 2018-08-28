package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

interface MutableNamedaysList : NamedaysList {
    fun addRecurringNameday(date: Date, name: String)
    fun addSpecificYearNameday(date: Date, name: String)
}
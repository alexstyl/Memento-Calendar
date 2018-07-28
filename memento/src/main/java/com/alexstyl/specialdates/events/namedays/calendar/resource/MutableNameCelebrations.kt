package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations

interface MutableNameCelebrations : NameCelebrations {
    fun addDate(date: Date)
}

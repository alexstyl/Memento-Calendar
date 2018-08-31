package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

interface MutableNameCelebrations : NameCelebrations {
    fun addDate(date: Date)
}

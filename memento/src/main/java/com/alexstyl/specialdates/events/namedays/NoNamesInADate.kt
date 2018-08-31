package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

data class NoNamesInADate(override val date: Date) : NamesInADate {
    override val names: List<String>
        get() = emptyList()
}

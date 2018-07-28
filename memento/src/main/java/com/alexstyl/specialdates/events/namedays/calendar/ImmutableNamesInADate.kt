package com.alexstyl.specialdates.events.namedays.calendar

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NamesInADate

data class ImmutableNamesInADate(override val date: Date, override val names: List<String>)
    : NamesInADate